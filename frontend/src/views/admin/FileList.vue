<template>
  <div>
    <div class="top">
      <div class="top-op">
        <div class="search-panel">
          <el-input clearable placeholder="search files by name" v-model="fileNameFuzzy" @keyup.enter="search">
            <template #suffix>
              <i class="iconfont icon-search" @click="search"></i>
            </template>
          </el-input>
        </div>
        <div class="iconfont icon-refresh" @click="loadDataList"></div>
        <el-button :style="{ 'margin-left': '10px' }" type="danger" :disabled="selectFileIdList.length == 0"
          @click="delFileBatch">
          <span class="iconfont icon-del"></span>
          Batch delete
        </el-button>
      </div>
      <!--nav-->
      <Navigation ref="navigationRef" @navChange="navChange" :adminShow="true"></Navigation>
    </div>
    <div class="file-list">
      <Table :columns="columns" :showPagination="true" :dataSource="tableData" :fetch="loadDataList" :initFetch="false"
        :options="tableOptions" @rowSelected="rowSelected">
        <template #fileName="{ index, row }">
          <div class="file-item" @mouseenter="showOp(row)" @mouseleave="cancelShowOp(row)">
            <template v-if="(row.fileType == 3 || row.fileType == 1) && row.status == 2">
              <icon :cover="row.fileCover" :width="32"></icon>
            </template>
            <template v-else>
              <icon v-if="row.folderType == 0" :fileType="row.fileType"></icon>
              <icon v-if="row.folderType == 1" :fileType="0"></icon>
            </template>
            <span class="file-name" v-if="!row.showEdit" :title="row.fileName">
              <span @click="preview(row)">{{ row.fileName }}</span>
              <span v-if="row.status == 0" class="transfer-status">transcoding</span>
              <span v-if="row.status == 1" class="transfer-status transfer-fail">transcoding failed</span>
            </span>
            <div class="edit-panel" v-if="row.showEdit">
              <el-input v-model.trim="row.fileNameReal" :maxLength="190" @keyup.enter="saveNameEdit(index)">
                <template #suffix>{{ row.fileSuffix }}</template>
              </el-input>
              <span :class="[
                'iconfont icon-right1',
                row.fileNameReal ? '' : 'not-allow',
              ]" @click="saveNameEdit(index)"></span>
              <span class="iconfont icon-error" @click="cancelNameEdit(index)"></span>
            </div>
            <span class="op">
              <template v-if="row.showOp && row.fileId">
                <span class="iconfont icon-download" @click="download(row)" v-if="row.folderType == 0">download</span>
                <span class="iconfont icon-del" @click="delFile(row)">delete</span>
              </template>
            </span>
          </div>
        </template>
        <template #fileSize="{ index, row }">
          <span v-if="row.fileSize">
            {{ proxy.Utils.size2Str(row.fileSize) }}</span>
        </template>
      </Table>
    </div>
    <!--preview-->
    <Preview ref="previewRef"> </Preview>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, computed } from "vue";
const { proxy } = getCurrentInstance();

const api = {
  loadDataList: "/admin/loadFileList",
  delFile: "/admin/delFile",
  createDownloadUrl: "/admin/createDownloadUrl",
  download: "/api/admin/download",
};

//List
const columns = [
  {
    label: "File Name",
    prop: "fileName",
    scopedSlots: "fileName",
  },
  {
    label: "publisher",
    prop: "nickName",
    width: 250,
  },
  {
    label: "last modification",
    prop: "lastUpdateTime",
    width: 200,
  },
  {
    label: "size",
    prop: "fileSize",
    scopedSlots: "fileSize",
    width: 200,
  },
];
//search
const search = () => {
  showLoading.value = true;
  loadDataList();
};
//List
const tableData = ref({});
const tableOptions = {
  extHeight: 50,
  selectType: "checkbox",
};

//Batch select
const selectFileIdList = ref([]);
const rowSelected = (rows) => {
  selectFileIdList.value = [];
  rows.forEach((item) => {
    selectFileIdList.value.push(item.userId + "_" + item.fileId);
  });
  console.log(selectFileIdList);
};

const fileNameFuzzy = ref();
const showLoading = ref(true);

const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
    fileNameFuzzy: fileNameFuzzy.value,
    filePid: currentFolder.value.fileId,
  };
  let result = await proxy.Request({
    url: api.loadDataList,
    showLoading: showLoading,
    params,
  });
  if (!result) {
    return;
  }
  tableData.value = result.data;
};

//Display operation buttons
const showOp = (row) => {
  tableData.value.list.forEach((element) => {
    element.showOp = false;
  });
  row.showOp = true;
};

const cancelShowOp = (row) => {
  row.showOp = false;
};

const previewRef = ref();
const navigationRef = ref();
const preview = (data) => {
  if (data.folderType == 1) {
    navigationRef.value.openFolder(data);
    return;
  }
  if (data.status != 2) {
    proxy.Message.warning("The file is being transcoded and cannot be previewed");
    return;
  }
  previewRef.value.showPreview(data, 1);
};

//List
const currentFolder = ref({ fileId: 0 });
const navChange = (data) => {
  const { curFolder } = data;
  currentFolder.value = curFolder;
  showLoading.value = true;
  loadDataList();
};

//delete files
const delFile = (row) => {
  proxy.Confirm(
    `Are you sure to delete【${row.fileName}】? Files deleted can be restored from the recycle bin within 10 days`,
    async () => {
      let result = await proxy.Request({
        url: api.delFile,
        params: {
          fileIdAndUserIds: row.userId + "_" + row.fileId,
        },
      });
      if (!result) {
        return;
      }
      loadDataList();
    }
  );
};
//Batch delete
const delFileBatch = () => {
  if (selectFileIdList.value.length == 0) {
    return;
  }
  proxy.Confirm(
    `Are you sure to delete the file? Files deleted can be restored from the recycle bin within 10 days`,
    async () => {
      let result = await proxy.Request({
        url: api.delFile,
        params: {
          fileIdAndUserIds: selectFileIdList.value.join(","),
        },
      });
      if (!result) {
        return;
      }
      loadDataList();
    }
  );
};

//download files
const download = async (row) => {
  let result = await proxy.Request({
    url: api.createDownloadUrl + "/" + row.userId + "/" + row.fileId,
  });
  if (!result) {
    return;
  }
  window.location.href = api.download + "/" + result.data;
};
</script>

<style lang="scss" scoped>
@import "@/assets/file.list.scss";

.search-panel {
  margin-left: 0px !important;
}

.file-list {
  margin-top: 10px;

  .file-item {
    .op {
      width: 140px;
    }
  }
}
</style>