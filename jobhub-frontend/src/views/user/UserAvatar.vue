<!-- <script setup>
import { ref } from 'vue'
import { Plus, Upload } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const uploadRef = ref()
const imgUrl = ref(userStore.loginUser.avatar)

// 执行上传
const handleUpload = async () => {

}
</script>

<template>
  <page-container title="更换头像">
    <el-row>
      <el-col :span="12">
        <el-upload
          ref="uploadRef"
          class="avatar-uploader"
          :auto-upload="false"
          :show-file-list="false"
        >
          <img v-if="imgUrl" :src="'http://localhost:8080/jobhub/' + imgUrl" class="avatar" />
          <img v-else src="@/assets/avatar.jpg" width="278" />
        </el-upload>
        <br />
        <el-button type="primary" :icon="Plus" size="large">
          选择图片
        </el-button>
        <el-button type="success" :icon="Upload" size="large" @click="handleUpload">
          上传头像
        </el-button>
      </el-col>
    </el-row>
  </page-container>
</template>

<style lang="scss" scoped>
.avatar-uploader {
  :deep() {
    .avatar {
      width: 278px;
      height: 278px;
      display: block;
    }
    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }
    .el-upload:hover {
      border-color: var(--el-color-primary);
    }
    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 278px;
      height: 278px;
      text-align: center;
    }
  }
}
</style> -->


<script setup>
import { ref } from 'vue'
import { Upload, Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { uploadAvatarService } from '@/api/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()

const uploadRef = ref()
const loading = ref(false)
const imgUrl = ref(getFullAvatarUrl(userStore.loginUser?.avatar))
const selectedFile = ref(null)

// 获取完整头像地址（带时间戳避免缓存）
function getFullAvatarUrl(path) {
  if (!path) return '@/assets/avatar.jpg'
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
  <page-container title="更换头像">
    <el-row justify="center">
      <el-col :span="12">
        <div class="avatar-container">
          <!-- 头像预览 -->
          <div class="avatar-wrapper">
            <img
              :src="imgUrl"
              class="avatar-image"
              alt="用户头像"
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
