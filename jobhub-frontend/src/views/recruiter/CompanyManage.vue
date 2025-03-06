<script setup>
import { ref, watch } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getCompanyInfoService, registerCompanyService, uploadLogoService, updateCompanyInfoService } from '@/api/company'
import { Upload, Plus } from '@element-plus/icons-vue'
import { pcaTextArr } from "element-china-area-data";
import avatar from '@/assets/default.png'

const uploadRef = ref()
const loading = ref(false)
const selectedFile = ref(null)

const companyInfo = ref({
  id: '',
  name: '',
  intro: '',
  address: '',
  type: '',
  size: '',
  logo: '',
  status: ''
})

const getCompanyInfo = async () => {
  const res = await getCompanyInfoService()
  if (res.data.data) {
    companyInfo.value = res.data.data
  }
}
getCompanyInfo()

const imgUrl = ref(getFullAvatarUrl(companyInfo.value?.logo))

// 获取完整头像地址（带时间戳避免缓存）
function getFullAvatarUrl(path) {
  if (!path) return avatar
  return `http://localhost:8080/jobhub/${path}?t=${Date.now()}`
}

// 监听 companyInfo 的变化，并更新 imgUrl
watch(companyInfo, (newVal) => {
  imgUrl.value = getFullAvatarUrl(newVal.logo)
})

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
    await uploadLogoService(formData)
    getCompanyInfo()
    ElMessage.success('头像更新成功')
  } catch (error) {
    ElMessage.error('头像上传失败')
  } finally {
    loading.value = false
    selectedFile.value = null
  }
}

const addressObj = ref([])
const formattedAddress = () => {
  if (addressObj.value.length === 0) {
    return
  }
  const specialCities = ['北京市', '上海市', '天津市', '重庆市'];
  let selectedValues;
  if (specialCities.includes(addressObj.value[0])) {
    // 如果下标 0 的值是特殊城市，取 0 和 2 的值
    selectedValues = [addressObj.value[0], addressObj.value[2]];
  } else {
    // 否则取 1 和 2 的值
    selectedValues = [addressObj.value[1], addressObj.value[2]];
  }
  // 过滤掉可能存在的 undefined 值
  const validValues = selectedValues.filter(value => value!== undefined);
  // 将过滤后的数组元素拼接成字符串
  return validValues.join('');
}

const registerDialogVisible = ref(false)
const registerForm = ref({
  name: '',
  intro: '',
  address: '',
  type: '',
  size: ''
})
const register = () => {
  registerDialogVisible.value = true
}
const handleRegisterDialogClose = () => {
  registerForm.value = {
    name: '',
    intro: '',
    address: '',
    type: '',
    size: ''
  }
  addressObj.value = []
  registerDialogVisible.value = false
}
const handleRegister = async () => {
  await ElMessageBox.confirm('确认注册企业吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  // 注册逻辑
  registerForm.value.address = formattedAddress()
  const res = await registerCompanyService(registerForm.value)
  if (res.data.data) {
    ElMessage.success('注册成功')
    registerForm.value = {
      name: '',
      intro: '',
      address: '',
      type: '',
      size: ''
    }
    addressObj.value = []
    getCompanyInfo()
    registerDialogVisible.value = false
  } else {
    ElMessage.error('注册失败')
  }
}

const editDialogVisible = ref(false)
const editForm = ref({
  id: '',
  name: '',
  intro: '',
  address: '',
  type: '',
  size: ''
})

// 打开编辑弹窗
const openEditDialog = () => {
  if (!companyInfo.value.id) {
    ElMessage.warning('请先完成企业注册')
    return
  }

  // 填充表单数据
  editForm.value = { ...companyInfo.value }

  editDialogVisible.value = true
}

// 提交编辑
const handleEditSubmit = async () => {
  try {
    await ElMessageBox.confirm('确认修改企业信息吗？', '温馨提示', {
      type: 'warning',
      confirmButtonText: '确认',
      cancelButtonText: '取消',
    })

    // 处理地址转换
    editForm.value.address = formattedAddress()

    // 调用更新接口
    await updateCompanyInfoService(editForm.value)

    // 刷新数据
    await getCompanyInfo()
    ElMessage.success('修改成功，等待管理员审核')
    editDialogVisible.value = false
    addressObj.value = []
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('修改失败')
    }
  }
}

// 关闭编辑弹窗
const handleEditDialogClose = () => {
  editForm.value = {
    id: '',
    name: '',
    intro: '',
    address: '',
    type: '',
    size: ''
  }
  addressObj.value = []
  editDialogVisible.value = false
}
</script>

<template>
  <page-container>
    <template #title>
      <div>企业管理</div>
    </template>
    <el-row>
      <el-col :span="14">
        <el-card>
          <template #header>
            <div class="card-header">
              <el-button type="primary" @click="register">注册企业</el-button>
              <el-button type="info" @click="openEditDialog">编辑企业信息</el-button>
              <el-button type="success">绑定企业</el-button>
            </div>
          </template>
          <el-form :model="companyInfo" label-width="120px" size="large" class="companyInfo-form">
            <el-form-item label="企业名称">
              <div>{{ companyInfo.name }}</div>
            </el-form-item>
            <el-form-item label="企业介绍">
              <div class="textarea">{{ companyInfo.intro }}</div>
            </el-form-item>
            <el-form-item label="企业地址">
              <div>{{ companyInfo.address }}</div>
            </el-form-item>
            <el-form-item label="企业类型">
              <div>{{ companyInfo.type }}</div>
            </el-form-item>
            <el-form-item label="企业规模">
              <div>{{ companyInfo.size }}</div>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <!-- 注册弹窗 -->
      <el-dialog
        v-model="registerDialogVisible"
        :before-close="handleRegisterDialogClose"
        width="40%"
        title="注册企业"
      >
        <template #default>
          <el-form :model="registerForm" label-width="120px" size="large" class="companyInfo-form">
            <el-form-item label="企业名称">
              <el-input v-model="registerForm.name" placeholder="请输入企业名称" clearable></el-input>
            </el-form-item>
            <el-form-item label="企业介绍">
              <el-input v-model="registerForm.intro" type="textarea" placeholder="请输入企业介绍" clearable class="textarea-input"></el-input>
            </el-form-item>
            <el-form-item label="企业地址">
              <el-cascader
                placeholder="请选择地区"
                :options="pcaTextArr"
                v-model="addressObj"
                style="width: 600px"
              >
              </el-cascader>
            </el-form-item>
            <el-form-item label="企业类型">
              <el-input v-model="registerForm.type" placeholder="请输入企业类型" clearable></el-input>
            </el-form-item>
            <el-form-item label="企业规模">
              <el-select
                v-model="registerForm.size"
                placeholder="请选择企业规模"
                style="width: 600px"
                clearable
              >
                <el-option label="0-20人" value="0-20人"></el-option>
                <el-option label="21-99人" value="21-99人"></el-option>
                <el-option label="100-499人" value="100-499人"></el-option>
                <el-option label="500-999人" value="500-999人"></el-option>
                <el-option label="1000-9999人" value="1000-9999人"></el-option>
                <el-option label="10000人以上" value="10000人以上"></el-option>
              </el-select>
            </el-form-item>
            <div class="dialog-footer">
              <el-button @click="handleRegisterDialogClose">取消</el-button>
              <el-button type="primary" @click="handleRegister">注册</el-button>
            </div>
          </el-form>
        </template>
      </el-dialog>
      <!-- 编辑企业信息弹窗 -->
      <el-dialog
        v-model="editDialogVisible"
        title="编辑企业信息"
        width="40%"
        :before-close="handleEditDialogClose"
      >
        <template #default>
          <el-form :model="editForm" label-width="120px" size="large" class="companyInfo-form">
            <el-form-item label="企业名称">
              <el-input v-model="editForm.name" placeholder="请输入企业名称" clearable></el-input>
            </el-form-item>
            <el-form-item label="企业介绍">
              <el-input
                v-model="editForm.intro"
                type="textarea"
                placeholder="请输入企业介绍"
                clearable
                class="textarea-input"
              ></el-input>
            </el-form-item>
            <el-form-item label="企业地址">
              <el-cascader
                placeholder="请选择地区"
                :options="pcaTextArr"
                v-model="addressObj"
                style="width: 600px"
              ></el-cascader>
            </el-form-item>
            <el-form-item label="企业类型">
              <el-input v-model="editForm.type" placeholder="请输入企业类型" clearable></el-input>
            </el-form-item>
            <el-form-item label="企业规模">
              <el-select
                v-model="editForm.size"
                placeholder="请选择企业规模"
                style="width: 600px"
                clearable
              >
                <el-option label="0-20人" value="0-20人"></el-option>
                <el-option label="21-99人" value="21-99人"></el-option>
                <el-option label="100-499人" value="100-499人"></el-option>
                <el-option label="500-999人" value="500-999人"></el-option>
                <el-option label="1000-9999人" value="1000-9999人"></el-option>
                <el-option label="10000人以上" value="10000人以上"></el-option>
              </el-select>
            </el-form-item>
            <div class="dialog-footer">
              <el-button @click="handleEditDialogClose">取消</el-button>
              <el-button type="primary" @click="handleEditSubmit">提交</el-button>
            </div>
          </el-form>
        </template>
      </el-dialog>
      <el-col :span="10">
        <div class="avatar-container">
          <!-- logo预览 -->
          <div class="avatar-wrapper">
            <img
              :src="imgUrl"
              class="avatar-image"
            />
            <div class="avatar-mask">
              <span>点击更换logo</span>
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
              选择logo
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
.card-header {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.companyInfo-form {
  width: 100%;
  max-width: 550px;
}

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

.dialog-footer {
  display: flex;
  justify-content: center; /* 水平居中 */
  margin-top: 20px; /* 可以根据需要调整顶部间距 */
}
</style>
