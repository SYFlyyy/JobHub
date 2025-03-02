<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getJobListService, addJobService, onlineJobService, offlineJobService } from '@/api/job'

const jobList = ref([])
const loading = ref(false)
const params = ref({
  id: '',
  companyId: '',
  companyName: '',
  name: '',
  type: '',
  salary: '',
  intro: '',
  status: '', // 默认查询待审核
  pageSize: 5,
  current: 1
})

const total = ref(0)

const getJobList = async () => {
  loading.value = true
  const res = await getJobListService(params.value)
  jobList.value = res.data.data.records
  total.value = parseInt(res.data.data.total)
  loading.value = false
}

const detailDialogVisible = ref(false)
const jobDetail = ref({})
const getJobById = async (id) => {
  params.value.id = id
  const res = await getJobListService(params.value)
  jobDetail.value = res.data.data.records[0]
  detailDialogVisible.value = true
  params.value.id = ''
}
const handleDetailDialogClose = () => {
  jobDetail.value = {}
  detailDialogVisible.value = false
}

// 初始加载数据
getJobList()

const formatStatus = (row, column, cellValue) => {
  switch (cellValue) {
    case 0:
      return '上线'   // 对应数字 0 显示 '正常'
    case 1:
      return '下线'   // 对应数字 1 显示 '禁用'
    case 2:
      return '待审核' // 对应数字 2 显示 '待审核'
    case 3:
      return '审核通过' // 对应数字 3 显示 '审核通过'
    default:
      return '未知'   // 如果是其他数字，显示 '未知'
  }
}
const typeStatus = (row, column, cellValue) => {
  switch (cellValue) {
    case 0:
      return '全职'
    case 1:
      return '实习'
    case 2:
      return '兼职'
    default:
      return '未知'   // 如果是其他数字，显示 '未知'
  }
}
const jobTypeStatus = (type) => {
  switch (type) {
    case 0:
      return '全职'
    case 1:
      return '实习'
    case 2:
      return '兼职'
    default:
      return '未知'   // 如果是其他数字，显示 '未知'
  }
}
const onlineJob = async (id) => {
  await ElMessageBox.confirm('确认上线该职位吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  params.value.id = id
  await onlineJobService(params.value)
  params.value.id = ''
  ElMessage.success('上线成功')
  getJobList()
}
const offlineJob = async (id) => {
  await ElMessageBox.confirm('确认下线该职位吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  params.value.id = id
  await offlineJobService(params.value)
  params.value.id = ''
  ElMessage.success('下线成功')
  getJobList()
}

const handleSizeChange = (size) => {
  params.value.current = 1
  params.value.pageSize = size
  getJobList()
}

const handleCurrentChange = (page) => {
  params.value.current = page
  getJobList()
}

const addJobForm = ref({
  name: '',
  type: '',
  salary: '',
  intro: ''
})

const addDialogVisible = ref(false)
const addJob = () => {
  addDialogVisible.value = true
}
const handleAddJob = async () => {
  await ElMessageBox.confirm('确认新增职位吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  const res = await addJobService(addJobForm.value)
  if (res.data.data) {
    ElMessage.success('新增成功')
    addDialogVisible.value = false
    addJobForm.value = {
      name: '',
      type: '',
      salary: '',
      intro: ''
    }
    getJobList()
  } else {
    ElMessage.error('新增失败')
  }
}
const handleAddDialogClose = () => {
  addJobForm.value = {
    name: '',
    type: '',
    salary: '',
    intro: ''
  }
  addDialogVisible.value = false
}
</script>

<template>
  <page-container>
    <template #title>
      <div>职位管理</div>
    </template>
    <template #extra>
      <el-button type="primary" @click="addJob">新增职位</el-button>
    </template>

    <el-table v-loading="loading" :data="jobList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column label="序号" width="100" type="index" align="center"></el-table-column>
      <el-table-column prop="name" label="职位名称" align="center"></el-table-column>
      <el-table-column prop="type" label="职位类型" :formatter="typeStatus" align="center"></el-table-column>
      <el-table-column prop="salary" label="薪资" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" :formatter="formatStatus" align="center"></el-table-column>
      <el-table-column label="操作" width="260" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="info"
            size="small"
            plain
            @click="getJobById(row.id)"
          >详情</el-button>
          <el-button
            round
            type="primary"
            size="small"
            plain
            @click="onlineJob(row.id)"
          >编辑</el-button>
          <el-button
            round
            type="success"
            size="small"
            plain
            @click="onlineJob(row.id)"
          >上线</el-button>
          <el-button
            round
            type="danger"
            size="small"
            plain
            @click="offlineJob(row.id)"
          >下线</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增职位弹窗 -->
    <el-dialog
      v-model="addDialogVisible"
      title="新增职位"
      width="40%"
      :before-close="handleAddDialogClose"
    >
      <template #default>
        <el-form :model="addJobForm" label-width="100px" size="large" class="job-form">
          <el-form-item label="职位名称">
            <el-input v-model="addJobForm.name" placeholder="请输入职位名称" clearable></el-input>
          </el-form-item>
          <el-form-item label="职位详情">
            <el-input v-model="addJobForm.intro" type="textarea" placeholder="请输入职位详情" clearable class="textarea-input"></el-input>
          </el-form-item>
          <el-form-item label="职位薪资">
            <el-input v-model="addJobForm.salary" placeholder="请输入职位薪资" clearable></el-input>
          </el-form-item>
          <el-form-item label="职位类型">
            <el-select
              v-model="addJobForm.type"
              placeholder="请选择职位类型"
              style="width: 600px"
              clearable
            >
              <el-option label="全职" value="0"></el-option>
              <el-option label="实习" value="1"></el-option>
              <el-option label="兼职" value="2"></el-option>
            </el-select>
          </el-form-item>
          <div class="dialog-footer">
            <el-button @click="handleAddDialogClose">取消</el-button>
            <el-button type="primary" @click="handleAddJob">新增</el-button>
          </div>
        </el-form>
      </template>
    </el-dialog>
    <!-- 职位详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      :before-close="handleDetailDialogClose"
    >
      <template #default>
        <el-descriptions
          title="职位详情"
          direction="vertical"
          :column="3"
          border
        >
          <el-descriptions-item label-align="center" align="center" label="职位名称">{{ jobDetail.name }}</el-descriptions-item>
          <el-descriptions-item label-align="center" align="center" label="职位薪资">{{ jobDetail.salary }}</el-descriptions-item>
          <el-descriptions-item label-align="center" align="center" label="职位类型" :span="1">{{ jobTypeStatus(jobDetail.type) }}</el-descriptions-item>
          <el-descriptions-item label-align="center" label="职位详情" :span="4">
            <div class="long-text">{{ jobDetail.intro }}</div>
          </el-descriptions-item>
        </el-descriptions>
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
  max-width: 580px;
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
