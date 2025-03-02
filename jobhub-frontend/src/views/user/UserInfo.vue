<script setup>
import { useUserStore } from '@/stores/user';
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { updateUserInfoService } from '@/api/user'
import avatar from '@/assets/default.png'
const {
  loginUser: { account, username, email, phone },
  getLoginUser
} = useUserStore()
const userStore = useUserStore()
const userInfo = ref({ account, username, email, phone })

const rules = {
  username: [
    { required: true, message: '请输入您的姓名', trigger: 'blur' },
    {
      pattern: /^\S{2,10}$/,
      message: '昵称必须是2-10位的非空字符串',
      trigger: 'blur'
    }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    {
      pattern: /^1[3-9]\d{9}$/,
      message: '手机号格式不正确',
      trigger: 'blur'
    }
  ]
}

const formRef = ref()
const onSubmit = async () => {
  const valid = await formRef.value.validate()
  if (valid) {
    await updateUserInfoService(userInfo.value)
    getLoginUser()
    ElMessage.success('修改成功')
  }
}

import { Upload, Plus } from '@element-plus/icons-vue'
import { uploadAvatarService } from '@/api/user'
import { ElMessage } from 'element-plus'

const uploadRef = ref()
const loading = ref(false)
const imgUrl = ref(getFullAvatarUrl(userStore.loginUser?.avatar))
const selectedFile = ref(null)

// 获取完整头像地址（带时间戳避免缓存）
function getFullAvatarUrl(path) {
  if (!path) return avatar
  return `http://localhost:8080/jobhub/${path}?t=${Date.now()}`
}

// 处理文件选择变化
const handleFileChange = (uploadFile) => {
  // 验证文件类型
  const validTypes = ['image/jpeg', 'image/png']
  if (!validTypes.includes(uploadFile.raw.type)) {
    ElMessage.error('仅支持 JPG/PNG 格式图片')
    return
  }

  // 验证文件大小（2MB）
  const isLt2M = uploadFile.raw.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB')
    return
  }

  // 生成预览图
  const reader = new FileReader()
  reader.onload = (e) => {
    imgUrl.value = e.target.result
  }
  reader.readAsDataURL(uploadFile.raw)
  selectedFile.value = uploadFile.raw
}

// 执行上传操作
const handleUpload = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择要上传的图片')
    return
  }

  try {
    loading.value = true
    const formData = new FormData()
    formData.append('file', selectedFile.value)

    const res = await uploadAvatarService(formData)

    // 更新本地存储的用户信息
    userStore.loginUser.avatar = res.data.data
    // 更新显示（添加时间戳强制刷新）
    imgUrl.value = getFullAvatarUrl(res.data.data)
    ElMessage.success('头像更新成功')
  } catch (error) {
    ElMessage.error('头像上传失败')
  } finally {
    loading.value = false
    selectedFile.value = null
  }
}
</script>

<template>
  <page-container>
    <template #title>
      <div>基本资料</div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-form
          :model="userInfo"
          :rules="rules"
          ref="formRef"
          label-width="100px"
          size="large"
        >
          <el-form-item label="用户名">
            <el-input v-model="userInfo.account" disabled></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="username">
            <el-input v-model="userInfo.username"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="userInfo.email"></el-input>
          </el-form-item>
          <el-form-item label="手机" prop="phone">
            <el-input v-model="userInfo.phone"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">提交修改</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="12">
        <div class="avatar-container">
          <!-- 头像预览 -->
          <div class="avatar-wrapper">
            <img
              :src="imgUrl"
              class="avatar-image"
            />
            <div class="avatar-mask">
              <span>点击更换头像</span>
            </div>

            <!-- 隐藏的上传组件 -->
            <el-upload
              ref="uploadRef"
              class="avatar-uploader"
              :auto-upload="false"
              :show-file-list="false"
              :on-change="handleFileChange"
              accept=".jpg,.jpeg,.png"
            >
              <div class="upload-trigger"></div>
            </el-upload>
          </div>

          <!-- 操作按钮 -->
          <div class="action-buttons">
            <el-button
              type="primary"
              :icon="Plus"
              @click="uploadRef?.$el.querySelector('input').click()"
            >
              选择新头像
            </el-button>
            <el-button
              type="success"
              :icon="Upload"
              :loading="loading"
              @click="handleUpload"
            >
              {{ loading ? '上传中...' : '确认上传' }}
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </page-container>
</template>

<style lang="scss" scoped>
.avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
}

.avatar-wrapper {
  position: relative;
  width: 200px;
  height: 200px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s ease;


  &:hover {
    transform: scale(1.05);

    .avatar-mask {
      opacity: 1;
    }
  }
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  span {
    color: white;
    font-size: 14px;
  }
}

.upload-trigger {
  position: absolute;
  width: 100%;
  height: 100%;
  opacity: 0;
}

.action-buttons {
  display: flex;
  gap: 20px;
}
</style>
