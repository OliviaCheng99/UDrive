<template>
  <div class="login-body">
    <div class="bg"></div>
    <div class="login-panel">
      <el-form
        class="login-register"
        :model="formData"
        :rules="rules"
        ref="formDataRef"
      >
        <div class="login-title">UDrive</div>
        <!--input-->
        <el-form-item prop="email">
          <el-input
            size="large"
            clearable
            placeholder="email"
            v-model.trim="formData.email"
            maxLength="150"
          >
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!--password-->
        <el-form-item prop="password" v-if="opType == 1">
          <el-input
            type="password"
            size="large"
            placeholder="password"
            v-model.trim="formData.password"
            show-password
          >
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <!--register-->
        <div v-if="opType == 0 || opType == 2">
          <el-form-item prop="emailCode">
            <div class="send-emali-panel">
              <el-input
                size="large"
                placeholder="Please enter email code."
                v-model.trim="formData.emailCode"
              >
                <template #prefix>
                  <span class="iconfont icon-checkcode"></span>
                </template>
              </el-input>
              <el-button
                class="send-mail-btn"
                type="primary"
                size="large"
                @click="getEmailCode"
                >Get email code.</el-button
              >
            </div>
            <el-popover placement="left" :width="500" trigger="click">
              <div>
                <p>1、在垃圾箱中查找邮箱验证码</p>
                <p>2、在邮箱中头像->设置->反垃圾->白名单->设置邮件地址白名单</p>
                <p>
                  3、将邮箱【laoluo@wuhancoder.com】添加到白名单不知道怎么设置？
                </p>
              </div>
              <template #reference>
                <span class="a-link" :style="{ 'font-size': '14px' }"
                  >Didn't receive email code？</span
                >
              </template>
            </el-popover>
          </el-form-item>
          <el-form-item prop="nickName" v-if="opType == 0">
            <el-input
              size="large"
              clearable
              placeholder="Please enter your nickname"
              v-model.trim="formData.nickName"
              maxLength="20"
            >
              <template #prefix>
                <span class="iconfont icon-account"></span>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="registerPassword">
            <el-input
              type="password"
              size="large"
              placeholder="Please enter your password."
              v-model.trim="formData.registerPassword"
              show-password
            >
              <template #prefix>
                <span class="iconfont icon-password"></span>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="reRegisterPassword">
            <el-input
              type="password"
              size="large"
              placeholder="Enter your password again."
              v-model.trim="formData.reRegisterPassword"
              show-password
            >
              <template #prefix>
                <span class="iconfont icon-password"></span>
              </template>
            </el-input>
          </el-form-item>
        </div>
        <el-form-item prop="checkCode">
          <div class="check-code-panel">
            <el-input
              size="large"
              placeholder="check code"
              v-model.trim="formData.checkCode"
              @keyup.enter="doSubmit"
            >
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img
              :src="checkCodeUrl"
              class="check-code"
              @click="changeCheckCode(0)"
            />
          </div>
        </el-form-item>
        <el-form-item v-if="opType == 1">
          <div class="rememberme-panel">
            <el-checkbox v-model="formData.rememberMe">Remember me</el-checkbox>
          </div>
          <div class="no-account">
            <a href="javascript:void(0)" class="a-link" @click="showPanel(2)"
              >Forgot password？</a
            >
            <a href="javascript:void(0)" class="a-link" @click="showPanel(0)"
              >Create account</a
            >
          </div>
        </el-form-item>
        <el-form-item v-if="opType == 0">
          <a href="javascript:void(0)" class="a-link" @click="showPanel(1)"
            >Log in</a
          >
        </el-form-item>
        <el-form-item v-if="opType == 2">
          <a href="javascript:void(0)" class="a-link" @click="showPanel(1)"
            >Log in?</a
          >
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            class="op-btn"
            @click="doSubmit"
            size="large"
          >
            <span v-if="opType == 0">Register</span>
            <span v-if="opType == 1">Sign in</span>
            <span v-if="opType == 2">Reset password</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--send email code-->
    <Dialog
      :show="dialogConfig4SendMailCode.show"
      :title="dialogConfig4SendMailCode.title"
      :buttons="dialogConfig4SendMailCode.buttons"
      width="500px"
      :showCancel="false"
      @close="dialogConfig4SendMailCode.show = false"
    >
      <el-form
        :model="formData4SendMailCode"
        :rules="rules"
        ref="formData4SendMailCodeRef"
        label-width="80px"
        @submit.prevent
      >
        <el-form-item label="email">
          {{ formData.email }}
        </el-form-item>
        <el-form-item label="checkcode" prop="checkCode">
          <div class="check-code-panel">
            <el-input
              size="large"
              placeholder="Please enter the check code."
              v-model.trim="formData4SendMailCode.checkCode"
            >
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img
              :src="checkCodeUrl4SendMailCode"
              class="check-code"
              @click="changeCheckCode(1)"
            />
          </div>
        </el-form-item>
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import md5 from "js-md5";

const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const api = {
  checkCode: "/api/checkCode",
  sendMailCode: "/sendEmailCode",
  register: "/register",
  login: "/login",
  resetPwd: "/resetPwd",
};

// 0:register 1:log in 2:reset password
const opType = ref(1);
const showPanel = (type) => {
  opType.value = type;
  resetForm();
};

onMounted(() => {
  showPanel(1);
});

const checkRePassword = (rule, value, callback) => {
  if (value !== formData.value.registerPassword) {
    callback(new Error(rule.message));
  } else {
    callback();
  }
};
const formData = ref({});
const formDataRef = ref();
const rules = {
  email: [
    { required: true, message: "Please enter your email." },
    { validator: proxy.Verify.email, message: "Please enter the right email." },
  ],
  password: [{ required: true, message: "Please enter your password." }],
  emailCode: [{ required: true, message: "Please enter the email code" }],
  nickName: [{ required: true, message: "Please enter your nickname" }],
  registerPassword: [
    { required: true, message: "Please enter the right email." },
    {
      validator: proxy.Verify.password,
      message: "Password can only contain 8-18 numbers, letters or special characters.",
    },
  ],
  reRegisterPassword: [
    { required: true, message: "Please enter your password again." },
    {
      validator: checkRePassword,
      message: "Password mismatch.",
    },
  ],
  checkCode: [{ required: true, message: "Please enter the check code on the image." }],
};
//check code
const checkCodeUrl = ref();
const checkCodeUrl4SendMailCode = ref();
const changeCheckCode = (type) => {
  if (type == 0) {
    checkCodeUrl.value =
      api.checkCode + "?type=" + type + "&time=" + new Date().getTime();
  } else {
    checkCodeUrl4SendMailCode.value =
      api.checkCode + "?type=" + type + "&time=" + new Date().getTime();
  }
};

//email code pop-up
const formData4SendMailCode = ref({});
const formData4SendMailCodeRef = ref();
const dialogConfig4SendMailCode = reactive({
  show: false,
  title: "send email code",
  buttons: [
    {
      type: "primary",
      text: "send email code",
      click: () => {
        sendEmailCode();
      },
    },
  ],
});
//get email code
const getEmailCode = () => {
  formDataRef.value.validateField("email", (valid) => {
    if (!valid) {
      return;
    }
    dialogConfig4SendMailCode.show = true;

    nextTick(() => {
      changeCheckCode(1);
      formData4SendMailCodeRef.value.resetFields();
      formData4SendMailCode.value = {
        email: formData.value.email,
      };
    });
  });
};
//send email
const sendEmailCode = () => {
  formData4SendMailCodeRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    const params = Object.assign({}, formData4SendMailCode.value);
    params.type = opType.value == 0 ? 0 : 1;
    let result = await proxy.Request({
      url: api.sendMailCode,
      params: params,
      errorCallback: () => {
        changeCheckCode(1);
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("Send successfully! Please check your email.");
    dialogConfig4SendMailCode.show = false;
  });
};

//reset form
const resetForm = () => {
  nextTick(() => {
    changeCheckCode(0);
    formDataRef.value.resetFields();
    formData.value = {};

    //log in
    if (opType.value == 1) {
      const cookieLoginInfo = proxy.VueCookies.get("loginInfo");
      if (cookieLoginInfo) {
        formData.value = cookieLoginInfo;
      }
    }
  });
};

//log in, register, reset password, submit form
const doSubmit = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {};
    Object.assign(params, formData.value);
    //register
    if (opType.value == 0 || opType.value == 2) {
      params.password = params.registerPassword;
      delete params.registerPassword;
      delete params.reRegisterPassword;
    }
    //log in
    if (opType.value == 1) {
      let cookieLoginInfo = proxy.VueCookies.get("loginInfo");
      let cookiePassword =
        cookieLoginInfo == null ? null : cookieLoginInfo.password;
      if (params.password !== cookiePassword) {
        params.password = md5(params.password);
      }
    }
    let url = null;
    if (opType.value == 0) {
      url = api.register;
    } else if (opType.value == 1) {
      url = api.login;
    } else if (opType.value == 2) {
      url = api.resetPwd;
    }
    let result = await proxy.Request({
      url: url,
      params: params,
      errorCallback: () => {
        changeCheckCode(0);
      },
    });
    if (!result) {
      return;
    }
    
    if (opType.value == 0) {
      proxy.Message.success("Register successfully! Please log in.");
      showPanel(1);
    } else if (opType.value == 1) {
      //log in
      if (params.rememberMe) {
        const loginInfo = {
          email: params.email,
          password: params.password,
          rememberMe: params.rememberMe,
        };
        proxy.VueCookies.set("loginInfo", loginInfo, "7d");
      } else {
        proxy.VueCookies.remove("loginInfo");
      }
      proxy.Message.success("Log in successfully!");
      //store cookie
      proxy.VueCookies.set("userInfo", result.data, 0);
      //redirect to original page
      const redirectUrl = route.query.redirectUrl || "/";
      router.push(redirectUrl);
    } else if (opType.value == 2) {
      //reset password
      proxy.Message.success("Password reset! Please log in.");
      showPanel(1);
    }
  });
};

</script>

<style lang="scss" scoped>
.login-body {
  height: calc(100vh);
  background-size: cover;
  display: flex;
  background-color: #408FC8;
  position: relative;
  .bg {
    flex: 1;
    background-size: cover;
    background-position: center;
    background-size: 700px;
    background-repeat: no-repeat;
    background-image: url("../assets/login_img.png");
    
  }
  .login-panel {
    width: 430px;
    margin-right: 15%;
    margin-top: calc((100vh - 500px) / 2);
    .login-register {
      padding: 25px;
      background: #fff;
      border-radius: 5px;
      .login-title {
        text-align: center;
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 20px;
      }
      .send-emali-panel {
        display: flex;
        width: 100%;
        justify-content: space-between;
        .send-mail-btn {
          margin-left: 5px;
        }
      }
      .rememberme-panel {
        width: 100%;
      }
      .no-account {
        width: 100%;
        display: flex;
        justify-content: space-between;
      }
      .op-btn {
        width: 100%;
      }
    }
  }

  .check-code-panel {
    width: 100%;
    display: flex;
    .check-code {
      margin-left: 5px;
      cursor: pointer;
    }
  }
}
</style>