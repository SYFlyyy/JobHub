<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { updatePasswordService } from '@/api/user'
import PageContainer from '../layout/PageContainer.vue'

const pwdForm = ref({
  oldPassword: '',
  newPassword: '',
  checkPassword: '',
})
const formRef = ref()
const router = useRouter()
const userStore = useUserStore()

const checkOldSame = (rule, value, cb) => {
  if (value === pwdForm.value.oldPassword) {
    cb(new Error('原密码和新密码不能一样!'))
  } else {
    cb()
  }
}

const checkNewSame = (rule, value, cb) => {
  if (value !== pwdForm.value.newPassword) {
    cb(new Error('新密码和确认新密码不一样!'))
  } else {
    cb()
  }
}
const rules = {
  // 原密码
  oldPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{8,15}$/,
      message: '密码长度必须是8-15位的非空字符串',
      trigger: 'blur',
    },
  ],
  // 新密码
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    {
      pattern: /^\S{8,15}$/,
      message: '密码长度必须是8-15位的非空字符串',
      trigger: 'blur',
    },
    { validator: checkOldSame, trigger: 'blur' },
  ],
  // 确认新密码
  checkPassword: [
    { required: true, message: '请再次确认新密码', trigger: 'blur' },
    {
      pattern: /^\S{8,15}$/,
      message: '密码长度必须是8-15位的非空字符串',
      trigger: 'blur',
    },
    { validator: checkNewSame, trigger: 'blur' },
  ],
}

const onSubmit = async () => {
  const valid = await formRef.value.validate()
  if (valid) {
    await updatePasswordService(pwdForm.value)
    ElMessage({ type: 'success', message: '更换密码成功' })
    userStore.clearLoginUser()
    router.push('/login')
  }
}

const onReset = () => {
  formRef.value.resetFields()
}
</script>
<template>
  <page-container>
    <template #title>
      <div>修改密码</div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-form :model="pwdForm" :rules="rules" ref="formRef" label-width="100px" size="large">
          <el-form-item label="原密码" prop="oldPassword">
            <el-input v-model="pwdForm.oldPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="pwdForm.newPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="checkPassword">
            <el-input v-model="pwdForm.checkPassword" type="password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="onSubmit" type="primary">修改密码</el-button>
            <el-button @click="onReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </page-container>
</template>
