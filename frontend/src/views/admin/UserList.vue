<template>
  <div>
    <div class="top-panel">
      <el-form :model="searchFormData" label-width="80px" @submit.prevent>
        <el-row>
          <el-col :span="4">
            <!--input输入-->
            <el-form-item label="user nickname">
              <el-input
                clearable
                placeholder="support fuzzy search"
                v-model.trim="searchFormData.nickNameFuzzy"
                @keyup.native="loadDataList"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <!-- 下拉框 -->
            <el-form-item label="status">
              <el-select
                clearable
                placeholder="select status"
                v-model="searchFormData.status"
              >
                <el-option :value="1" label="activate"></el-option>
                <el-option :value="0" label="deactivate"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4" :style="{ 'padding-left': '10px' }">
            <el-button type="primary" @click="loadDataList"> search </el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="file-list">
      <Table
        :columns="columns"
        :showPagination="true"
        :dataSource="tableData"
        :fetch="loadDataList"
        :options="tableOptions"
      >
        <template #avatar="{ index, row }">
          <div class="avatar">
            <Avatar :userId="row.userId" :avatar="row.qqAvatar"></Avatar>
          </div>
        </template>

        <template #space="{ index, row }">
          {{ proxy.Utils.size2Str(row.useSpace) }}/{{
            proxy.Utils.size2Str(row.totalSpace)
          }}
        </template>

        <template #status="{ index, row }">
          <span v-if="row.status == 1" style="color: #529b2e">activate</span>
          <span v-if="row.status == 0" style="color: #f56c6c">deactivate</span>
        </template>
        <template #op="{ index, row }">
          <span class="a-link" @click="updateSpace(row)">allocate space</span>
          <el-divider direction="vertical" />
          <span class="a-link" @click="updateUserStatus(row)">{{
            row.status == 0 ? "activate" : "deactivate"
          }}</span>
        </template>
      </Table>
    </div>
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="400px"
      :showCancel="false"
      @close="dialogConfig.show = false"
    >
      <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        label-width="80px"
        @submit.prevent
      >
        <!--input输入-->
        <el-form-item label="nickname">
          {{ formData.nickName }}
        </el-form-item>
        <el-form-item label="space" prop="changeSpace">
          <el-input
            clearable
            placeholder="please enter space"
            v-model="formData.changeSpace"
          >
            <template #suffix>MB</template>
          </el-input>
        </el-form-item>
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from "vue";
import { useRouter, useRoute } from "vue-router";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const api = {
  loadDataList: "/admin/loadUserList",
  updateUserStatus: "/admin/updateUserStatus",
  updateUserSpace: "/admin/updateUserSpace",
};

//列表
const columns = [
  {
    label: "avatar",
    prop: "avatar",
    width: 80,
    scopedSlots: "avatar",
  },
  {
    label: "nickName",
    prop: "nickName",
    width: 90
  },
  {
    label: "email",
    prop: "email",
  },
  {
    label: "space",
    prop: "space",
    scopedSlots: "space",
    width: 120,
  },
  {
    label: "joinTime",
    prop: "joinTime",
    width: 160,
  },
  {
    label: "lastLoginTime",
    prop: "lastLoginTime",
    width: 160,
  },
  {
    label: "status",
    prop: "status",
    scopedSlots: "status",
    width: 90,
  },
  {
    label: "operation",
    prop: "op",
    scopedSlots: "op",
  },
];
const searchFormData = ref({});

//列表
const tableData = ref({});
const tableOptions = {
  extHeight: 20,
};
const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
  };
  Object.assign(params, searchFormData.value);
  let result = await proxy.Request({
    url: api.loadDataList,
    params,
  });
  if (!result) {
    return;
  }
  tableData.value = result.data;
};

//修改状态
const updateUserStatus = (row) => {
  proxy.Confirm(
    `Are you sure to【${row.status == 0 ? "activate" : "deactivate"}】?`,
    async () => {
      let result = await proxy.Request({
        url: api.updateUserStatus,
        params: {
          userId: row.userId,
          status: row.status == 0 ? 1 : 0,
        },
      });
      if (!result) {
        return;
      }
      loadDataList();
    }
  );
};

//分配空间大小
const dialogConfig = ref({
  show: false,
  title: "allocate space",
  buttons: [
    {
      type: "primary",
      text: "confirm",
      click: (e) => {
        submitForm();
      },
    },
  ],
});

const formData = ref({});
const formDataRef = ref();
const rules = {
  changeSpace: [{ required: true, message: "please enter the space" }],
};

const updateSpace = (data) => {
  dialogConfig.value.show = true;
  nextTick(() => {
    formDataRef.value.resetFields();
    formData.value = Object.assign({}, data);
  });
};

const submitForm = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {};
    Object.assign(params, formData.value);
    let result = await proxy.Request({
      url: api.updateUserSpace,
      params: params,
    });
    if (!result) {
      return;
    }
    dialogConfig.value.show = false;
    proxy.Message.success("success");
    loadDataList();
  });
};
</script>
<style lang="scss" scoped>
.top-panel {
  margin-top: 10px;
}
.avatar {
  width: 50px;
  height: 50px;
  border-radius: 25px;
  overflow: hidden;
  img {
    width: 100%;
    height: 100;
  }
}
.el-table .cell {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>