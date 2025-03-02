<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getNoticeListService, getNoticeByIdService } from '@/api/notice'

const noticeList = ref([])
const loading = ref(false)
const params = ref({
  status: 0, // 只能查看上线的公告
})

const total = ref(0)

const getNoticeList = async () => {
  loading.value = true
  const res = await getNoticeListService(params.value)
  noticeList.value = res.data.data.records
  total.value = parseInt(res.data.data.total)
  loading.value = false
}

const detailDialogVisible = ref(false)
const noticeDetail = ref({})
const getNoticeById = async (id) => {
  params.value.id = id
  const res = await getNoticeByIdService(params.value)
  noticeDetail.value = res.data.data
  detailDialogVisible.value = true
  params.value.id = ''
}
const handleDetailDialogClose = () => {
  noticeDetail.value = {}
  detailDialogVisible.value = false
}

// 初始加载数据
getNoticeList()

</script>

<template>
  <page-container>
    <template #title>
      <div>公告</div>
    </template>

    <el-table v-loading="loading" :data="noticeList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column label="序号" width="200" type="index" align="center"></el-table-column>
      <el-table-column prop="title" label="公告标题" align="center"></el-table-column>
      <el-table-column label="操作" width="260" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="info"
            size="small"
            plain
            @click="getNoticeById(row.id)"
          >查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 公告详情弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      :before-close="handleDetailDialogClose"
    >
      <template #default>
        <el-descriptions
          title="公告详情"
          direction="vertical"
          :column="1"
          border
        >
          <el-descriptions-item label-align="center" align="center" label="公告标题">{{ noticeDetail.title }}</el-descriptions-item>
          <el-descriptions-item label-align="center" label="公告内容" :span="4">
            <div class="long-text">{{ noticeDetail.content }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </template>
    </el-dialog>
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
