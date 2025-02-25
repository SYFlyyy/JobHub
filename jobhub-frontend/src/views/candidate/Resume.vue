<template>
  <el-card style="width: 300px; max-height: 400px;">
    <template #header>
      <div class="card-header">
        简历附件
      </div>
    </template>
    <div class="resume-section">
      <div class="resume-info">
        <el-icon><Document /></el-icon>
        <el-link v-if="resumePath" :href="'http://localhost:8080/jobhub/' + resumePath" target="_blank" type="primary">
          查看简历
        </el-link>
      </div>
    </div>
    <div class="resume-form">
      <el-upload
        ref="uploadRef"
        class="resume-upload"
        :auto-upload="false"
        :on-change="handleFileChange"
        accept=".jpg,.png,.pdf"
      >
        <template #tip>
          <div class="upload-tip">
            支持格式：JPG/PNG/PDF，大小不超过10MB
          </div>
        </template>
      </el-upload>
    </div>
    <div class="action-buttons">
      <el-button
        type="primary"
        :icon="Plus"
        @click="uploadRef?.$el.querySelector('input').click()"
      >
        选择简历附件
      </el-button>
      <el-button
        type="success"
        :icon="Upload"
        :disabled="!selectedFile"
        @click="handleUpload"
      >
        上传简历
      </el-button>
    </div>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import { uploadResumeService, getResumePathService } from '@/api/resume'
import { ElMessage } from 'element-plus'
import { Document, Plus, Upload } from '@element-plus/icons-vue'

const uploadRef = ref()
const selectedFile = ref(null)
const resumePath = ref('')

const getResume = async () => {
  const res = await getResumePathService()
  resumePath.value = res.data.data
}
getResume()

const handleFileChange = (file, fileList) => {
  if (fileList.length > 1) {
    fileList.splice(0, 1) // 保留第一个文件
  }
  const uploadFile = fileList[0]
  const validTypes = ['image/jpeg', 'image/png', 'application/pdf']
  if (!validTypes.includes(uploadFile.raw.type)) {
    ElMessage.error('仅支持 JPG/PNG/PDF 格式的文件')
    return
  }
  if (uploadFile.raw.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过 10MB')
    return
  }
  selectedFile.value = uploadFile.raw
}

const handleUpload = async () => {
  if (!selectedFile.value) {
    ElMessage.error('请先选择需要上传的文件')
    return
  }
  const formData = new FormData()
  formData.append('file', selectedFile.value)
  try {
    await uploadResumeService(formData)
    const res = await getResumePathService()
    resumePath.value = res.data.data
    ElMessage.success('简历上传成功')
  } catch (error) {
    ElMessage.error('简历上传失败')
  } finally {
    selectedFile.value = null
    uploadRef.value.clearFiles() // 清空已选择的文件
  }
}

</script>

<style lang="scss" scoped>
.el-card {
  flex-direction: column;  /* 设置卡片内容为竖直排列 */
  align-items: center;  /* 使内容居中 */
  padding: 20px;  /* 为卡片添加一些内边距 */
  gap: 15px;  /* 在每个部分之间添加间隔 */
}

.resume-section {
  flex-direction: column;  /* 让 resume-info 垂直排列 */
  width: 240px;
  height: 50px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  gap: 10px;  /* 增加内部分隔 */
  display: flex;
  justify-content: center;
  align-items: center;
  .resume-info {
    display: flex;
    align-items: center;
    gap: 10px;  /* 在图标和链接之间增加间隔 */
  }
}

.resume-form {
  margin-top: 20px;  /* 为表单增加顶部间隔 */
}

.upload-tip {
  color: #909399;
  font-size: 12px;
  max-height: 30px;
}

.action-buttons {
  margin-top: 20px;  /* 为按钮区域增加顶部间隔 */
  gap: 10px;  /* 在按钮之间增加间隔 */
}

</style>
