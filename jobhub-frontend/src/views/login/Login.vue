<script setup>
import { userRegisterService, userLoginService } from '@/api/user';
import { User, Lock } from '@element-plus/icons-vue'
import { ref, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const isRegister = ref(false)
const form = ref()
const userStore = useUserStore()
const router = useRouter()
// 用于提交的form数据对象
const formModel = ref({
  account: '',
  password: '',
  checkPassword: '',
  role: ''
})
// 表单校验规则
const rules = {
  account: [
    { required: true, message: '请输入用户名', trigger: 'blur'},
    { min: 4, max: 10, message: '用户名必须在4到10个字符', trigger: 'blur'}
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur'},
    // 正则校验 \S 表示非空字符
    { pattern: /^\S{8,15}$/, message: '密码必须是8到15位非空字符', trigger: 'blur'}
  ],
  checkPassword: [
    { required: true, message: '请输入确认密码', trigger: 'blur'},
    {
      // 自定义校验
      validator: (rule, value, callback) => {
        if (value !== formModel.value.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          // 成功也需要调用 callback
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}
const register = async () => {
  await form.value.validate()
  await userRegisterService(formModel.value)
  ElMessage.success('注册成功')
  // 注册成功后跳转到登录页
  isRegister.value = false
}
const login = async () => {
  await form.value.validate()
  const res = await userLoginService(formModel.value)
  ElMessage.success('登录成功')
  userStore.setLoginUser(res.data.data)
  router.push('/')
}
watch(isRegister, () => {
  formModel.value = {
    account: '',
    password: '',
    checkPassword: '',
    role: ''
  }
})
</script>

<template>
  <div class="container">
    <div class="header">
      <img src="@/assets/logo.svg" class="logo" alt="logo">
      <span class="title">艾府招聘</span>
    </div>
    <el-row class="login-page">
      <el-col class="form">
        <!-- 注册表单 -->
        <el-form
          :model="formModel"
          :rules="rules"
          style="width: 300px"
          ref="form"
          size="large"
          autocomplete="off"
          v-if="isRegister"
        >
          <el-form-item>
            <h1>注册</h1>
            </el-form-item>
            <el-form-item prop="account">
              <el-input
              v-model="formModel.account"
              :prefix-icon="User"
              placeholder="请输入用户名"
            ></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="formModel.password"
                :prefix-icon="Lock"
                type="password"
                placeholder="请输入密码"
              ></el-input>
            </el-form-item>
            <el-form-item prop="checkPassword">
              <el-input
                v-model="formModel.checkPassword"
                :prefix-icon="Lock"
                type="password"
                placeholder="请输入确认密码"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-radio-group v-model="formModel.role">
                <el-radio value="1">求职者</el-radio>
                <el-radio value="2">招聘者</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button @click="register" class="button" type="primary" auto-insert-space>注册</el-button>
            </el-form-item>
            <el-form-item class="flex">
              <el-link type="info" :underline="false" @click="isRegister = false">
                ← 返回
              </el-link>
            </el-form-item>
          </el-form>

          <!-- 登录表单 -->
          <el-form
          :model="formModel"
          :rules="rules"
          ref="form"
          style="width: 300px"
          size="large"
          autocomplete="off" v-else>
            <el-form-item>
              <h1>登录</h1>
            </el-form-item>
            <el-form-item>
              <el-input
              v-model="formModel.account"
              :prefix-icon="User"
              placeholder="请输入用户名"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="formModel.password"
                name="password"
                :prefix-icon="Lock"
                type="password"
                placeholder="请输入密码"
              ></el-input>
            </el-form-item>
            <el-form-item class="flex">
              <div class="flex">
                <el-checkbox>记住我</el-checkbox>
                <el-link type="primary" :underline="false">忘记密码？</el-link>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button @click="login" class="button" type="primary" auto-insert-space>登录</el-button>
            </el-form-item>
            <el-form-item class="flex">
              <el-link type="info" :underline="false" @click="isRegister = true">
                注册 →
              </el-link>
            </el-form-item>
          </el-form>
        </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped>
.container {
  background: #f0f2f5 url('@/assets/background.svg') no-repeat center center;
  background-size: 100%;
  height: 100vh; // 确保背景图覆盖整个页面
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  .header {
    text-align: center;
    height: 50px;
    line-height: 44px;
    padding: 0px 0 4px; // 调整 padding 以减少间距
    // margin-bottom: 0px; // 增加 header 和 login-page 之间的距离
    .logo {
      height: 55px;
      vertical-align: top;
      margin-right: 16px;
      border-style: none;
    }
    .title {
      color: rgba(0, 0, 0, .85);
      font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
      font-weight: 600;
      position: relative;
      top: 4px;
      font-size: 40px;
    }
  }
  .login-page {
    // display: flex;
    // justify-content: center;
    // align-items: center; // 使表单垂直居中
    display: flex;
    justify-content: center;
    align-items: center;
    /* 新增：固定表单容器高度 */
    height: 458px;  // 根据实际内容调整这个值
    margin-top: 0;  // 保持与header的固定间距
    .form {
      // display: flex;
      // flex-direction: column;
      // justify-content: center;
      // align-items: center;
      // width: 100%;
      // max-width: 400px; // 恢复最大宽度
      // padding: 20px;
      // background-color: transparent;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      width: 100%;
      max-width: 400px;
      /* 新增：固定表单区域高度 */
      padding: 17px;
      .title {
        text-align: center;
      }
      .button {
        width: 100%;
        margin-top: 20px;
      }
      .flex {
        width: 100%;
        display: flex;
        justify-content: space-between;
      }
    }
  }
  h1 {
    font-size: 24px;
    text-align: center;
    width: 100%
  }
}
</style>
