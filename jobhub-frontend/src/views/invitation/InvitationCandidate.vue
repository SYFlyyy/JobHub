<script setup>
import { ref, computed } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getInvitationListService, acceptInvitationService, rejectInvitationService  } from '@/api/invitation'
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
      return '待确认'
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

const handleAccept = async () => {
  await ElMessageBox.confirm('确认接受本次面试邀请吗？', '操作确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  const { id } = invitationDetail.value
  const res = await acceptInvitationService({ id})
  ElMessage.success('已确认接受面试')
  // 刷新列表和关闭弹窗
  getInvitationList()
  invitationDetailDialogVisible.value = false
}

const handleReject = async () => {
  await ElMessageBox.confirm('确认拒绝本次面试邀请吗？', '操作确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  const { id } = invitationDetail.value
  const res = await rejectInvitationService({ id })
  ElMessage.success('已确认拒绝面试')
  // 刷新列表和关闭弹窗
  getInvitationList()
  invitationDetailDialogVisible.value = false
}

const showActions = computed(() => {
  return invitationDetail.value.status === 0 // 0表示待确认状态
})
</script>

<template>
  <page-container>
    <template #title>
      <div>面试通知</div>
    </template>

    <el-table v-loading="loading" :data="invitationList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column label="序号" width="100" type="index" align="center"></el-table-column>
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
      <template #footer>
        <div class="dialog-footer">
          <!-- 显示操作按钮的条件 -->
          <template v-if="showActions">
            <el-button
              type="success"
              @click="handleAccept"
              v-loading="loading"
            >接受面试</el-button>
            <el-button
              type="danger"
              @click="handleReject"
              v-loading="loading"
            >拒绝面试</el-button>
          </template>
          <el-button v-else @click="invitationDetailDialogVisible = false">关闭</el-button>
        </div>
      </template>
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
.dialog-footer {
  display: flex;
  justify-content: center;
}
</style>
