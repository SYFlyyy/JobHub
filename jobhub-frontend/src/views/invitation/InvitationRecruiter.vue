<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getInvitationListService } from '@/api/invitation'
import { getApplicationInfoService } from '@/api/applicationInfo'
import { getJobListService } from '@/api/job'

const invitationList = ref([])
const loading = ref(false)
const params = ref({
  pageSize: 5,
  current: 1
})

const total = ref(0)

const getInvitationList = async () => {
  loading.value = true
  const res = await getInvitationListService(params.value)
  invitationList.value = res.data.data.records
  total.value = parseInt(res.data.data.total)
  loading.value = false
}

// 初始加载数据
getInvitationList()

const typeStatus = (row, column, cellValue) => {
  switch (cellValue) {
    case 0:
      return '已发送'
    case 1:
      return '已接受面试邀请'
    case 2:
      return '已拒绝面试邀请'
    default:
      return '未知'   // 如果是其他数字，显示 '未知'
  }
}

const handleSizeChange = (size) => {
  params.value.current = 1
  params.value.pageSize = size
  getInvitationList()
}

const handleCurrentChange = (page) => {
  params.value.current = page
  getInvitationList()
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

const jobForm = ref({
  id: ''
})
const detailDialogVisible = ref(false)
const jobDetail = ref({})
const getJobById = async (id) => {
  jobForm.value.id = id
  const res = await getJobListService(jobForm.value)
  jobDetail.value = res.data.data.records[0]
  detailDialogVisible.value = true
  jobForm.value.id = ''
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
const handleDetailDialogClose = () => {
  jobDetail.value = {}
  detailDialogVisible.value = false
}

const invitationDetailDialogVisible = ref(false)
const invitationDetail = ref({})
const invitationForm = ref({
  id: ''
})
const getInvitationDetail = async (id) => {
  invitationForm.value.id = id
  const res = await getInvitationListService(invitationForm.value)
  invitationDetail.value = res.data.data.records[0]
  invitationDetailDialogVisible.value = true
  invitationForm.value.id = ''
}
const handleInvitationDetailClose = () => {
  invitationDetailDialogVisible.value = false
  invitationDetail.value = {}
}
</script>

<template>
  <page-container>
    <template #title>
      <div>面试邀请</div>
    </template>

    <el-table v-loading="loading" :data="invitationList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column label="序号" width="100" type="index" align="center"></el-table-column>
      <el-table-column label="求职者信息" width="260" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="primary"
            size="small"
            plain
            @click="handleGetApplicationInfo(row.canId)"
          >在线简历</el-button>
        </template>
      </el-table-column>
      <el-table-column label="职位" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="success"
            size="small"
            plain
            @click="getJobById(row.jobId)"
          >职位信息</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" :formatter="typeStatus" align="center"></el-table-column>
      <el-table-column label="操作" width="260" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="info"
            size="small"
            plain
            @click="getInvitationDetail(row.id)"
          >详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      v-model="invitationDetailDialogVisible"
      title="面试邀请详情"
      width="50%"
      @closed="handleInvitationDetailClose"
    >
      <el-descriptions
        :column="1"
        border
        label-align="center"
        align="center"
      >
        <el-descriptions-item label="内容" :span="1" label-width="100px" label-align="center">
          <div class="invitation-content">
            {{ invitationDetail.message }}
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
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

<style>
.invitation-content {
  white-space: pre-wrap;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
}
</style>
