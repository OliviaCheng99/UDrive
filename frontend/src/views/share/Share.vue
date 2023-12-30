<template>
  <div>
    <div class="top">
      <el-button
        type="primary"
        :disabled="selectIdList.length == 0"
        @click="cancelShareBatch"
      >
        <span class="iconfont icon-cancel"></span>cancel sharing
      </el-button>
    </div>
    <div class="file-list">
      <Table
        :columns="columns"
        :showPagination="true"
        :dataSource="tableData"
        :fetch="loadDataList"
        :options="tableOptions"
        @rowSelected="rowSelected"
      >
        <template #fileName="{ index, row }">
          <div
            class="file-item"
            @mouseenter="showOp(row)"
            @mouseleave="cancelShowOp(row)"
          >
            <template
              v-if="
                (row.fileType == 3 || row.fileType == 1) && row.status !== 0
              "
            >
              <icon :cover="row.fileCover"></icon>
            </template>
            <template v-else>
              <icon v-if="row.folderType == 0" :fileType="row.fileType"></icon>
              <icon v-if="row.folderType == 1" :fileType="0"></icon>
            </template>
            <span
              class="file-name"
              v-if="!row.showRename"
              :title="row.fileName"
            >
              <span>{{ row.fileName }}</span>
            </span>
            <span class="op">
              <template v-if="row.showOp && row.fileId">
                <span class="iconfont icon-link" @click="copy(row)"
                  >复制链接</span
                >
                <span class="iconfont icon-cancel" @click="cancelShare(row)"
                  >取消分享</span
                >
              </template>
            </span>
          </div>
        </template>
        <template #expireTime="{ index, row }">
          {{ row.validType == 3 ? "永久" : row.expireTime }}
        </template>
      </Table>
    </div>
  </div>
</template>

<script setup>
import useClipboard from "vue-clipboard3";
const { toClipboard } = useClipboard();
import { ref, reactive, getCurrentInstance, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const api = {
  loadDataList: "/share/loadShareList",
  cancelShare: "/share/cancelShare",
};

const shareUrl = ref(document.location.origin + "/share/");

//list
const columns = [
  {
    label: "fileName",
    prop: "fileName",
    scopedSlots: "fileName",
  },
  {
    label: "shareTime",
    prop: "shareTime",
    width: 200,
  },
  {
    label: "expireTime",
    prop: "expireTime",
    scopedSlots: "expireTime",
    width: 200,
  },
  {
    label: "viewTime",
    prop: "showCount",
    width: 200,
  },
];
//search
const search = () => {
  showLoading.value = true;
  loadDataList();
};
//list
const tableData = ref({});
const tableOptions = {
  extHeight: 20,
  selectType: "checkbox",
};

const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
  };
  if (params.category !== "all") {
    delete params.filePid;
  }
  let result = await proxy.Request({
    url: api.loadDataList,
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

//copy link
const copy = async (data) => {
  await toClipboard(
    `链接:${shareUrl.value}${data.shareId} 提取码: ${data.code}`
  );
  proxy.Message.success("复制成功");
};

//Batch select
const selectIdList = ref([]);
const rowSelected = (rows) => {
  selectIdList.value = [];
  rows.forEach((item) => {
    selectIdList.value.push(item.shareId);
  });
};

//cancel sharing
const cancelShareIdList = ref([]);
const cancelShareBatch = () => {
  if (selectIdList.value.length == 0) {
    return;
  }
  cancelShareIdList.value = selectIdList.value;
  cancelShareDone();
};

const cancelShare = (row) => {
  cancelShareIdList.value = [row.shareId];
  cancelShareDone();
};

const cancelShareDone = async () => {
  proxy.Confirm(`你确定要取消分享吗？`, async () => {
    let result = await proxy.Request({
      url: api.cancelShare,
      params: {
        shareIds: cancelShareIdList.value.join(","),
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("取消分享成功");
    loadDataList();
  });
};
</script>

<style lang="scss" scoped>
@import "@/assets/file.list.scss";
.file-list {
  margin-top: 10px;
  .file-item {
    .file-name {
      span {
        &:hover {
          color: #494944;
        }
      }
    }
    .op {
      width: 170px;
    }
  }
}
</style>