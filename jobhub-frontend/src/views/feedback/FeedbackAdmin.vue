<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getFeedbackListService, getFeedbackDetailService } from '@/api/feedback'

const feedbackList = ref([])
const loading = ref(false)
const params = ref({
  id: '',
  userId: '',
  contact: '',
  content: '',
  current: 1,
  pageSize: 5,
})

const total = ref(0)

const getFeedbackList = async () => {
  loading.value = true
  const res = await getFeedbackListService(params.value)
  feedbackList.value = res.data.data.records
  total.value = parseInt(res.data.data.total)
  loading.value = false
}

const detailDialogVisible = ref(false)
const feedbackDetail = ref({})
const getFeedbackById = async (id) => {
  params.value.id = id
  const res = await getFeedbackDetailService(params.value)
  feedbackDetail.value = res.data.data
  detailDialogVisible.value = true
  params.value.id = ''
}
const handleDetailDialogClose = () => {
  feedbackDetail.value = {}
  detailDialogVisible.value = false
}

// 初始加载职位数据
getFeedbackList()

const handleSizeChange = (size) => {
  params.value.current = 1
  params.value.pageSize = size
  getFeedbackList()
}

const handleCurrentChange = (page) => {
  params.value.current = page
  getFeedbackList()
}
</script>

<template>
  <page-container>
    <template #title>
      <div>用户反馈</div>
    </template>

    <el-table v-loading="loading" :data="feedbackList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column label="序号" type="index" align="center" width="300"></el-table-column>
      <el-table-column prop="updateTime" label="反馈时间" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="info"
            size="small"
            plain
            @click="getFeedbackById(row.id)"
          >反馈详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 反馈详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      :before-close="handleDetailDialogClose"
    >
      <template #default>
        <el-descriptions
          title="反馈详情"
          direction="vertical"
          :column="2"
          border
        >
          <el-descriptions-item label-align="center" align="center" label="联系方式">{{ feedbackDetail.contact }}</el-descriptions-item>
          <el-descriptions-item label-align="center" align="center" label="反馈时间">{{ feedbackDetail.updateTime }}</el-descriptions-item>
          <el-descriptions-item label-align="center" label="反馈内容" :span="4">
            <div class="long-text">{{ feedbackDetail.content }}</div>
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
