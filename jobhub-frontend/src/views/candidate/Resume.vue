<!-- <script setup>
import { ref } from 'vue'
import { uploadResumeService, getUserResumePathService } from '@/api/resume'
import { Document, Plus, Upload } from '@element-plus/icons-vue'

const uploadRef = ref()
const selectedFile = ref(null)
const resumePath = ref('')

const getResume = async () => {
  const res = await getUserResumePathService()
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
    const res = await getUserResumePathService()
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

</style> -->

<script setup>
import { ref, reactive } from 'vue'
import { uploadResumeService, getUserResumePathService } from '@/api/resume'
import { Document, Plus, Upload } from '@element-plus/icons-vue'

const slots = [1, 2, 3] // 三个简历槽位
const uploadRefs = ref([])
const selectedFiles = reactive({ 1: null, 2: null, 3: null })
const resumes = reactive({
  1: { fileName: '', filePath: '' },
  2: { fileName: '', filePath: '' },
  3: { fileName: '', filePath: '' }
})

// 获取用户所有简历
const getResumes = async () => {
  const res = await getUserResumePathService()
  if (!res.data.data) return
  res.data.data.forEach(item => {
    resumes[item.slot] = {
      fileName: item.fileName,
      filePath: item.filePath
    }
  })
}
getResumes()

// 文件选择处理
const handleFileChange = (file, fileList, slot) => {
  if (fileList.length > 1) {
    fileList.splice(0, 1)
  }
  const uploadFile = fileList[0]

  // 文件验证
  const validTypes = ['image/jpeg', 'image/png', 'application/pdf']
  if (!validTypes.includes(uploadFile.raw?.type)) {
    ElMessage.error('仅支持 JPG/PNG/PDF 格式的文件')
    return false
  }
  if (uploadFile.raw?.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过 10MB')
    return false
  }

  selectedFiles[slot] = uploadFile.raw
}

// 上传处理
const handleUpload = async (slot) => {
  if (!selectedFiles[slot]) {
    ElMessage.error('请先选择需要上传的文件')
    return
  }

  const formData = new FormData()
  formData.append('file', selectedFiles[slot])
  formData.append('slot', slot.toString())

  try {
    await uploadResumeService(formData)
    await getResumes() // 刷新简历列表
    ElMessage.success(`简历附件 ${slot} 上传成功`)
    selectedFiles[slot] = null
    uploadRefs.value[slot-1].clearFiles()
  } catch (error) {
    ElMessage.error(`简历附件 ${slot} 上传失败`)
  }
}
</script>

<template>
  <el-card class="resume-container">
    <template #header>
      <div class="card-header">
        简历附件
      </div>
    </template>

    <div v-for="slot in slots" :key="slot" class="resume-slot">
      <div class="slot-header">
        <span class="slot-title">简历附件 {{ slot }}</span>
        <el-tag v-if="resumes[slot].filePath" type="success">已上传</el-tag>
        <el-tag v-else type="info">暂未简历</el-tag>
      </div>

      <div class="resume-info">
        <el-icon><Document /></el-icon>
        <el-link
          v-if="resumes[slot].filePath"
          :href="'http://localhost:8080/jobhub/' + resumes[slot].filePath"
          target="_blank"
        >
          {{ resumes[slot].fileName || '我的简历' }}
        </el-link>
        <span v-else class="empty-tip">暂无简历</span>
      </div>

      <el-upload
        :ref="el => uploadRefs[slot-1] = el"
        class="resume-upload"
        :auto-upload="false"
        :on-change="(file, fileList) => handleFileChange(file, fileList, slot)"
        accept=".jpg,.png,.pdf"
      >
        <template #trigger>
          <el-button
            type="primary"
            :icon="Plus"
            size="small"
          >
            选择文件
          </el-button>
        </template>
        <template #tip>
          <div class="upload-tip">
            已选文件: {{ selectedFiles[slot]?.name || '未选择' }}
          </div>
        </template>
      </el-upload>

      <el-button
        type="success"
        :icon="Upload"
        size="small"
        :disabled="!selectedFiles[slot]"
        @click="handleUpload(slot)"
      >
        上传简历附件
      </el-button>
    </div>
  </el-card>
</template>

<style lang="scss" scoped>
.resume-container {
  max-width: 800px;
  margin: 20px auto;

  .resume-slot {
    padding: 16px;
    margin-bottom: 20px;
    border: 1px solid #ebeef5;
    border-radius: 4px;

    .slot-header {
      display: flex;
      align-items: center;
      margin-bottom: 12px;

      .slot-title {
        margin-right: 12px;
        font-weight: 500;
      }
    }

    .resume-info {
      display: flex;
      align-items: center;
      margin-bottom: 12px;
      gap: 8px;

      .empty-tip {
        color: #909399;
      }
    }

    .resume-upload {
      margin-bottom: 12px;
    }

    .upload-tip {
      color: #909399;
      font-size: 12px;
      margin-top: 8px;
    }
  }

  .el-button + .el-button {
    margin-left: 12px;
  }
}
</style>
