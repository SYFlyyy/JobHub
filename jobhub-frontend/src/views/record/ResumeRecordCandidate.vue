<script setup>
import { ref } from 'vue';
import PageContainer from '@/views/layout/PageContainer.vue';
import { getCandidateResumeRecordService } from '@/api/resumeRecord';
import { getJobDetailByIdService } from '@/api/job';

const loading = ref(false)
const resumeRecordList = ref([])
const total = ref(0)
const params = ref({
  status: '',
  pageSize: 5,
  current: 1
})

const getResumeRecordList = async () => {
  loading.value = true
  const res = await getCandidateResumeRecordService(params.value)
  resumeRecordList.value = res.data.data.records
  total.value = parseInt(res.data.data.total)
  loading.value = false
}
// 初始加载投递记录数据
getResumeRecordList()

const handleSizeChange = (size) => {
  params.value.current = 1
  params.value.pageSize = size
  getResumeRecordList()
}

const handleCurrentChange = (page) => {
  params.value.current = page
  getResumeRecordList()
}

const dialogVisible = ref(false)
const jobDetail = ref({})
const form = ref({
  id: ''
})

const getJobById = async (id) => {
  console.log(id)
  form.value.id = id
  const res = await getJobDetailByIdService(form.value)
  jobDetail.value = res.data.data
  dialogVisible.value = true
  form.value.id = ''
}

const handleDialogClose = () => {
  jobDetail.value = {}
  dialogVisible.value = false
}

const typeStatus = (row, column, cellValue) => {
  // 0-已投递、1-面试流程中、2-面试通过、3-录用意向、4-已录用、5-流程结束
  switch (cellValue) {
    case 0:
      return '已投递'
    case 1:
      return '面试流程中'
    case 2:
      return '面试通过'
    case 3:
      return '录用意向'
    case 4:
      return '已录用'
    case 5:
      return '流程结束'
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

</script>

<template>
  <PageContainer>
    <template #title>
      投递记录
    </template>
    <!-- 根据状态搜索 -->
    <el-form>

    </el-form>
    <el-table v-loading="loading" :data="resumeRecordList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column prop="jobName" label="职位名称" align="center"></el-table-column>
      <el-table-column prop="salary" label="薪资" align="center"></el-table-column>
      <el-table-column prop="companyName" label="所属企业" align="center"></el-table-column>
      <el-table-column prop="status" label="招聘状态" :formatter="typeStatus" align="center"></el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="info"
            size="small"
            plain
            @click="getJobById(row.jobId)"
          >详情</el-button>
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
  </PageContainer>

</template>

<style>

</style>
