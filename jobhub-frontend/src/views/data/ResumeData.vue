<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getJobListService } from '@/api/job'
import { getRecruitDataService } from '@/api/data'

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

// 初始加载数据
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

const handleSizeChange = (size) => {
  params.value.current = 1
  params.value.pageSize = size
  getJobList()
}

const handleCurrentChange = (page) => {
  params.value.current = page
  getJobList()
}

const dialogVisible = ref(false)
const hiringData = ref({})
const loadingHiringData = ref(false)

// 获取招聘数据
const handleGetRecruitData = async (jobId) => {
  try {
    loadingHiringData.value = true
    const res = await getRecruitDataService({ id: jobId })
    hiringData.value = res.data.data
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取数据失败: ' + error.message)
  } finally {
    loadingHiringData.value = false
  }
}
</script>

<template>
  <page-container>
    <template #title>
      <div>招聘数据</div>
    </template>

    <el-table v-loading="loading" :data="jobList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column label="序号" width="100" type="index" align="center"></el-table-column>
      <el-table-column prop="name" label="职位名称" align="center"></el-table-column>
      <el-table-column prop="type" label="职位类型" :formatter="typeStatus" align="center"></el-table-column>
      <el-table-column prop="salary" label="薪资" align="center"></el-table-column>
      <el-table-column label="操作" width="260" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="primary"
            size="small"
            plain
            @click="handleGetRecruitData(row.id)"
            :loading="loadingHiringData"
          >招聘数据</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      v-model="dialogVisible"
      title="招聘数据分析"
      width="800px"
    >
      <div class="data-container">
        <!-- 基础数据 -->
        <el-row :gutter="20" class="data-section">
          <el-col :span="6">
            <div class="data-item">
              <div class="label">职位收藏量</div>
              <div class="value">{{ hiringData.favoriteCount || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="data-item">
              <div class="label">投递总量</div>
              <div class="value">{{ hiringData.applicationCount || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="data-item">
              <div class="label">面试邀请</div>
              <div class="value">{{ hiringData.applicationPassCount || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="data-item">
              <div class="label">最终录用</div>
              <div class="value">{{ hiringData.hiredCount || 0 }}</div>
            </div>
          </el-col>
        </el-row>

        <!-- 转化率数据 -->
        <el-row :gutter="20" class="data-section rate-section">
          <el-col :span="8">
            <div class="rate-item">
              <div class="label">初筛通过率</div>
              <div class="value success">{{ hiringData.applicationPassRate || '0.00%' }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="rate-item">
              <div class="label">面试通过率</div>
              <div class="value warning">{{ hiringData.interviewPassRate || '0.00%' }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="rate-item">
              <div class="label">全流程转化率</div>
              <div class="value primary">{{ hiringData.totalConversionRate || '0.00%' }}</div>
            </div>
          </el-col>
        </el-row>

        <!-- 详细数据 -->
        <el-descriptions
          title="详细数据"
          :column="2"
          border
          class="detail-section"
        >
          <el-descriptions-item label="面试候选人" label-align="center" align="center">{{ hiringData.interviewCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="面试通过" label-align="center" align="center">{{ hiringData.interviewPassCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="录用意向" label-align="center" align="center">{{ hiringData.intentionCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="最终录用率" label-align="center" align="center">{{ hiringData.hiredPassRate || '0.00%' }}</el-descriptions-item>
        </el-descriptions>
      </div>
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
.data-container {
  padding: 20px;
}

.data-section {
  margin-bottom: 30px;
}

.data-item {
  text-align: center;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
  .label {
    color: #909399;
    font-size: 14px;
    margin-bottom: 8px;
  }
  .value {
    font-size: 20px;
    font-weight: 600;
    color: #303133;
  }
}

.rate-section {
  .rate-item {
    text-align: center;
    padding: 15px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);
    .label {
      color: #909399;
      font-size: 13px;
      margin-bottom: 6px;
    }
    .value {
      font-size: 20px;
      font-weight: bold;
      &.success { color: #67c23a; }
      &.warning { color: #e6a23c; }
      &.primary { color: #409eff; }
    }
  }
}

:deep(.el-descriptions__body .el-descriptions__table) {
  table-layout: fixed !important;
}

:deep(.el-descriptions__body .el-descriptions__table tbody td) {
  width: 50%;
  min-width: 50%;
}

:deep(.el-descriptions-item__label),
:deep(.el-descriptions-item__content) {
  flex: 1;
  min-width: 150px;
  justify-content: center;
}
</style>
