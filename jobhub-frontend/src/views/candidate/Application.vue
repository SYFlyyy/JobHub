<script setup>
import { ref } from 'vue'
import dayjs from 'dayjs'
import PageContainer from '@/views/layout/PageContainer.vue'
import { addApplicationInfoService, updateApplicationInfoService, hasApplicationInfoService, getOwnApplicationInfoService } from '@/api/applicationInfo'
import { useUserStore } from '@/stores/user'
import Resume from './Resume.vue'

const applicationInfo = ref({
  name: '',
  gender: null,
  birthDate: null,
  phone: '',
  email: '',
  address: '',
  school: '',
  major: '',
  graduation: null,
  honor: '',
  skill: '',
  projectExperience: '',
  workExperience: ''
})

const isEditing = ref(false)

const rules = {
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号码格式不正确', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '邮箱地址格式不正确', trigger: 'blur' }
  ]
}

const formatDate = (date) => {
  return date ? dayjs(date).format('YYYY年MM月DD日') : ''
}

const getApplicationInfo = async () => {
  const res = await getOwnApplicationInfoService()
  if (res.data) {
    applicationInfo.value = res.data.data
  }
}
getApplicationInfo()

const userStore = useUserStore()
const submitApplicationInfo = async () => {
  const valid = await applicationForm.value.validate()
  if (!valid) return
  const res = await hasApplicationInfoService()
  console.log(res.data.data)
  if (!res.data.data) {
    await updateApplicationInfoService(applicationInfo.value)
  } else {
    await addApplicationInfoService(applicationInfo.value)
  }
  getApplicationInfo()
  userStore.getLoginUser()
  ElMessage.success('提交成功')
  isEditing.value = false
}

const applicationForm = ref(null)
</script>

<template>
  <page-container>
    <template #title>
      <div>个人简历</div>
    </template>
    <el-row :gutter="30" class="form-container">
      <el-col :span="16">
        <el-form :model="applicationInfo" :rules="rules" ref="applicationForm" label-width="120px" size="large" class="applicationInfo-form">
          <el-form-item label="姓名">
            <div v-if="!isEditing">{{ applicationInfo.name }}</div>
            <el-input v-else v-model="applicationInfo.name"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <div v-if="!isEditing && applicationInfo.gender !== null">{{ applicationInfo.gender === 0 ? '男' : '女' }}</div>
            <el-radio-group v-else-if="isEditing" v-model="applicationInfo.gender">
              <el-radio :label="0">男</el-radio>
              <el-radio :label="1">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="出生日期">
            <div v-if="!isEditing">{{ formatDate(applicationInfo.birthDate) }}</div>
            <el-date-picker v-else v-model="applicationInfo.birthDate" type="date" placeholder="选择日期"></el-date-picker>
          </el-form-item>
          <el-form-item label="手机" prop="phone">
            <div v-if="!isEditing">{{ applicationInfo.phone }}</div>
            <el-input v-else v-model="applicationInfo.phone"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <div v-if="!isEditing">{{ applicationInfo.email }}</div>
            <el-input v-else v-model="applicationInfo.email"></el-input>
          </el-form-item>
          <el-form-item label="地址">
            <div v-if="!isEditing">{{ applicationInfo.address }}</div>
            <el-input v-else v-model="applicationInfo.address"></el-input>
          </el-form-item>
          <el-form-item label="学校">
            <div v-if="!isEditing">{{ applicationInfo.school }}</div>
            <el-input v-else v-model="applicationInfo.school"></el-input>
          </el-form-item>
          <el-form-item label="专业">
            <div v-if="!isEditing">{{ applicationInfo.major }}</div>
            <el-input v-else v-model="applicationInfo.major"></el-input>
          </el-form-item>
          <el-form-item label="毕业时间">
            <div v-if="!isEditing">{{ formatDate(applicationInfo.graduation) }}</div>
            <el-date-picker v-else v-model="applicationInfo.graduation" type="date" placeholder="选择日期"></el-date-picker>
          </el-form-item>
          <el-form-item label="荣誉证书">
            <div v-if="!isEditing">{{ applicationInfo.honor }}</div>
            <el-input type="textarea" v-else v-model="applicationInfo.honor" class="textarea-input"></el-input>
          </el-form-item>
          <el-form-item label="专业技能">
            <div v-if="!isEditing">{{ applicationInfo.skill }}</div>
            <el-input type="textarea" v-else v-model="applicationInfo.skill" class="textarea-input"></el-input>
          </el-form-item>
          <el-form-item label="项目经历">
            <div v-if="!isEditing">{{ applicationInfo.projectExperience }}</div>
            <el-input type="textarea" v-else v-model="applicationInfo.projectExperience" class="textarea-input"></el-input>
          </el-form-item>
          <el-form-item label="工作/实习经历">
            <div v-if="!isEditing">{{ applicationInfo.workExperience }}</div>
            <el-input type="textarea" v-else v-model="applicationInfo.workExperience" class="textarea-input"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="isEditing ? submitApplicationInfo() : isEditing = true">
              {{ isEditing ? '提交' : '编辑' }}
            </el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="8" class="resume">
        <Resume></Resume>
      </el-col>
    </el-row>
  </page-container>
</template>

<style lang="scss" scoped>
.applicationInfo-form {
  width: 100%;
  max-width: 800px;
  padding-right: 30px;
  border-right
  : 1px solid #ebeef5;
}

.page-container {
  padding: 20px;
}

.form-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;

  .el-col {
    padding: 0 15px;
  }

  /* 调整右侧列的对齐方式 */
  .el-col:last-child {
    align-self: flex-start;
  }
}

.applicationInfo-form {
  width: 700px; /* 调整表单的宽度 */
}

.textarea-input {
  word-break: break-all; /* 确保长单词换行 */
  white-space: pre-wrap; /* 保留空白符并换行 */
}

.el-textarea {
  height: 160px;
  :deep(.el-textarea__inner) {
    height: 140px;
  }
}
</style>
