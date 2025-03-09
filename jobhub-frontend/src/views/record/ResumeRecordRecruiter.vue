<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getResumeRecordService, updateResumeRecordService } from '@/api/resumeRecord'
import { getApplicationInfoService } from '@/api/applicationInfo'
import { getResumePathServie } from '@/api/resume'
import dayjs from 'dayjs'

const resumeRecordList = ref([])
const loading = ref(false)
const params = ref({
  pageSize: 5,
  current: 1
})

const total = ref(0)

const getResumeRecordList = async () => {
  loading.value = true
  const res = await getResumeRecordService(params.value)
  resumeRecordList.value = res.data.data.records
  total.value = parseInt(res.data.data.total)
  loading.value = false
}
// 初始加载职位数据
getResumeRecordList()

const formatStatus = (row, column, cellValue) => {
  switch (cellValue) {
    case 0:
      return '已投递'   // 对应数字 0 显示 '已投递'
    case 1:
      return '面试流程中'   // 对应数字 1 显示 '面试流程中'
    case 2:
      return '面试通过' // 对应数字 2 显示 '待审核'
    case 3:
      return '录用意向' // 对应数字 3 显示 '录用意向'
    case 4:
      return '已录用' // 对应数字 3 显示 '已录用'
    case 5:
      return '流程结束' // 对应数字 3 显示 '流程结束'
    default:
      return '未知'   // 如果是其他数字，显示 '未知'
  }
}

const handleSizeChange = (size) => {
  params.value.current = 1
  params.value.pageSize = size
  getResumeRecordList()
}

const handleCurrentChange = (page) => {
  params.value.current = page
  getResumeRecordList()
}

const applicationInfoDialogVisible = ref(false)
const applicationInfo = ref({})
const applicationInfoFrom = ref({
  id: ''
})
const handleGetApplicationInfo = async (id) => {
  applicationInfoFrom.value.id = id
  const res = await getApplicationInfoService(applicationInfoFrom.value)
  applicationInfo.value = res.data.data
  applicationInfoDialogVisible.value = true
  applicationInfoFrom.value.id = ''
}

const formatDate = (date) => {
  return date ? dayjs(date).format('YYYY年MM月DD日') : ''
}

const resumePath = ref('')
const resumePathFrom = ref({
  id: ''
})
const handleGetResume = async (id) => {
  resumePathFrom.value.id = id
  const res = await getResumePathServie(resumePathFrom.value)
  resumePath.value = res.data.data
  window.open('http://localhost:8080/jobhub/' + resumePath.value, '_blank')
}

const statusDialogVisible = ref(false)
const statusForm = ref({
  id: '',
  status: ''
})
const handleUpdateStatus = (id) => {
  statusDialogVisible.value = true
  statusForm.value.id = id
}

const handleStatusDialogClose = () => {
  statusForm.value.id = ''
  statusForm.value.status = ''
  statusDialogVisible.value = false
}

const updateStatus = async () => {
  await ElMessageBox.confirm('确认更新当前的招聘状态吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  const res = await updateResumeRecordService(statusForm.value)
  if (res.data.data) {
    ElMessage.success('更新状态成功')
    statusForm.value.id = ''
    statusForm.value.status = ''
    statusDialogVisible.value = false
    getResumeRecordList()
  }
}
</script>

<template>
  <page-container>
    <template #title>
      <div>招聘记录</div>
    </template>

    <el-table v-loading="loading" :data="resumeRecordList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column label="序号" width="100" type="index" align="center"></el-table-column>
      <el-table-column prop="jobName" label="职位名称" align="center"></el-table-column>
      <el-table-column prop="userName" label="求职者" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" :formatter="formatStatus" align="center"></el-table-column>
      <el-table-column label="操作" width="300" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="primary"
            size="small"
            plain
            @click="handleGetApplicationInfo(row.userId)"
          >在线简历</el-button>
          <el-button
            round
            type="success"
            size="small"
            plain
            @click="handleGetResume(row.resumeId)"
          >简历附件</el-button>
          <el-button
            round
            type="warning"
            size="small"
            plain
            @click="handleUpdateStatus(row.id)"
          >更新状态</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      v-model="applicationInfoDialogVisible"
      title="在线简历"
      width="40%"
    >
      <template #default>
        <el-form :model="applicationInfo" label-width="120px" size="large" class="applicationInfo-form">
          <el-form-item label="姓名">
            <div>{{ applicationInfo.name }}</div>
          </el-form-item>
          <el-form-item label="性别">
            <div>{{ applicationInfo.gender === 0 ? '男' : '女' }}</div>
          </el-form-item>
          <el-form-item label="出生日期">
            <div>{{ formatDate(applicationInfo.birthDate) }}</div>
          </el-form-item>
          <el-form-item label="手机" prop="phone">
            <div>{{ applicationInfo.phone }}</div>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <div>{{ applicationInfo.email }}</div>
          </el-form-item>
          <el-form-item label="地址">
            <div>{{ applicationInfo.address }}</div>
          </el-form-item>
          <el-form-item label="学校">
            <div>{{ applicationInfo.school }}</div>
          </el-form-item>
          <el-form-item label="专业">
            <div>{{ applicationInfo.major }}</div>
          </el-form-item>
          <el-form-item label="毕业时间">
            <div>{{ formatDate(applicationInfo.graduation) }}</div>
          </el-form-item>
          <el-form-item label="荣誉证书">
            <div>{{ applicationInfo.honor }}</div>
          </el-form-item>
          <el-form-item label="专业技能">
            <div>{{ applicationInfo.skill }}</div>
          </el-form-item>
          <el-form-item label="项目经历">
            <div>{{ applicationInfo.projectExperience }}</div>
          </el-form-item>
          <el-form-item label="工作/实习经历">
            <div>{{ applicationInfo.workExperience }}</div>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>
    <el-dialog
      v-model="statusDialogVisible"
      :before-close="handleStatusDialogClose"
      title="更新招聘状态"
      width="30%"
    >
      <template #default>
        <el-form :model="statusForm" label-width="100px" size="large" class="status-form">
          <el-form-item label="招聘状态">
            <el-select v-model="statusForm.status" placeholder="请选择招聘状态" style="width: 500px" clearable>
              <el-option label="面试通过" value="2"></el-option>
              <el-option label="录用意向" value="3"></el-option>
              <el-option label="已录用" value="4"></el-option>
              <el-option label="流程结束" value="5"></el-option>
            </el-select>
          </el-form-item>
          <div class="dialog-footer">
            <el-button @click="handleStatusDialogClose">取消</el-button>
            <el-button type="primary" @click="updateStatus">更新状态</el-button>
          </div>
        </el-form>
      </template>
    </el-dialog>
    <el-pagination
      v-model:current-page="params.current"
      v-model:page-size="params.pageSize"
      :page-sizes="[3, 5, 10]"
      layout="sizes, prev, pager, next"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top: 20px; justify-content: flex-end"
    />
  </page-container>
</template>

<style lang="scss" scoped>
.long-text {
  max-height: 100px; /* 设置最大高度 */
  overflow-y: auto; /* 当内容超过最大高度时添加垂直滚动条 */
  word-break: break-all;
  white-space: normal;
}

.job-form {
  width: 100%;
  max-width: 710px;
  padding-right: 30px;
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

.applicationInfo-form {
  width: 100%;
  max-width: 800px;
}

.status-form {
  width: 100%;
  max-width: 380px;
}
</style>
