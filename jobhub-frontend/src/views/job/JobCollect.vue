<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getJobCollectListService, jobCancelCollectService, getJobWithCompanyService, getJobDetailByIdService } from '@/api/job'
import { addResumeRecordServcie } from '@/api/resumeRecord';
import { getUserResumePathService } from '@/api/resume';
import { hasApplicationInfoService } from '@/api/applicationInfo'

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
  status: '',
  pageSize: 5,
  current: 1
})

const total = ref(0)

const getJobList = async () => {
  loading.value = true
  const res = await getJobCollectListService(params.value)
  jobList.value = res.data.data.records
  total.value = parseInt(res.data.data.total)
  loading.value = false
}

const dialogVisible = ref(false)
const jobDetail = ref({})
const getJobById = async (id) => {
  params.value.id = id
  const res = await getJobDetailByIdService(params.value)
  jobDetail.value = res.data.data
  dialogVisible.value = true
  params.value.id = ''
}
const handleDialogClose = () => {
  jobDetail.value = {}
  dialogVisible.value = false
}

// 初始加载职位数据
getJobList()

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

const handleSizeChange = (size) => {
  params.value.current = 1
  params.value.pageSize = size
  getJobList()
}

const handleCurrentChange = (page) => {
  params.value.current = page
  getJobList()
}

const cancelCollectForm = ref({})
const cancelCollect = async (id) => {
  await ElMessageBox.confirm('确认收藏该职位吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  cancelCollectForm.value.id = id
  const res = await jobCancelCollectService(cancelCollectForm.value)
  ElMessage.success('操作成功')
  await getJobList()
}

const deliverDialogVisible = ref(false)
const resumes = ref([])
const selectedJobId = ref('')
const selectedSlot = ref('')

const openDeliverDialog = async (jobId) => {
  selectedJobId.value = jobId
  const has = await hasApplicationInfoService()
  if (!has.data.data) {
    ElMessage.warning('请先完善个人在线简历')
    return
  }
  const res = await getUserResumePathService()
  if (!res.data.data) {
    ElMessage.warning('请先上传至少一份简历附件')
    return
  }
  resumes.value = res.data.data
  deliverDialogVisible.value = true
}

const submitDeliver = async () => {
  if (!selectedSlot.value) {
    ElMessage.warning('请选择要投递的简历')
    return
  }
  await ElMessageBox.confirm('确认投递该职位吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })

  await addResumeRecordServcie({
    jobId: selectedJobId.value,
    slot: selectedSlot.value
  })

  ElMessage.success('投递成功')
  deliverDialogVisible.value = false
  selectedSlot.value = ''
  selectedJobId.value = ''
}
</script>

<template>
  <page-container>
    <template #title>
      <div>职位管理</div>
    </template>
    <!-- <template #extra>
      <el-button type="primary">添加分类</el-button>
    </template> -->

    <el-table v-loading="loading" :data="jobList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column prop="name" label="职位名称" align="center"></el-table-column>
      <el-table-column prop="salary" label="薪资" align="center"></el-table-column>
      <el-table-column prop="address" label="地址" align="center"></el-table-column>
      <el-table-column prop="type" label="职位类型" :formatter="typeStatus" align="center"></el-table-column>
      <el-table-column label="操作" width="180" align="center">
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
            @click="cancelCollect(row.id)"
          >取消收藏</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      v-model="dialogVisible"
      :before-close="handleDialogClose"
    >
      <template #default>
        <el-descriptions
          title="职位详情"
          direction="vertical"
          :column="4"
          border
        >
          <el-descriptions-item label-align="center" align="center" label="职位名称">{{ jobDetail.name }}</el-descriptions-item>
          <el-descriptions-item label-align="center" align="center" label="职位薪资">{{ jobDetail.salary }}</el-descriptions-item>
          <el-descriptions-item label-align="center" align="center" label="职位类型" :span="1">{{ jobTypeStatus(jobDetail.type) }}</el-descriptions-item>
          <el-descriptions-item label-align="center" align="center" label="所属企业">{{ jobDetail.companyName }}</el-descriptions-item>
          <el-descriptions-item label-align="center" label="职位详情" :span="4">
            <div class="long-text">{{ jobDetail.intro }}</div>
          </el-descriptions-item>
        </el-descriptions>
        <div class="dialog-footer">
          <el-button type="success" plain @click="openDeliverDialog(jobDetail.id)">投递</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
      v-model="deliverDialogVisible"
      title="选择简历附件"
      width="30%"
      @closed="selectedSlot = ''"
    >
      <el-select v-model="selectedSlot" placeholder="请选择简历" style="width: 100%">
        <el-option
          v-for="resume in resumes"
          :key="resume.slot"
          :label="resume.fileName"
          :value="resume.slot"
        />
      </el-select>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deliverDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitDeliver">确定</el-button>
        </span>
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
.dialog-footer {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  margin-top: 20px; /* 可以根据需要调整顶部间距 */
}
</style>
