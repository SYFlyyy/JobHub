<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { addNoticeService, getNoticeListService, updateNoticeService, onlineNoticeService, offlineNoticeService, getNoticeByIdService } from '@/api/notice'

const noticeList = ref([])
const loading = ref(false)
const params = ref({
  id: '',
  pageSize: 5,
  current: 1
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

const formatStatus = (row, column, cellValue) => {
  switch (cellValue) {
    case 0:
      return '上线'   // 对应数字 0 显示 '上线'
    case 1:
      return '下线'   // 对应数字 1 显示 '下线'
    default:
      return '未知'   // 如果是其他数字，显示 '未知'
  }
}

const onlineNotice = async (id) => {
  await ElMessageBox.confirm('确认上线该公告吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  params.value.id = id
  await onlineNoticeService(params.value)
  params.value.id = ''
  ElMessage.success('上线成功')
  getNoticeList()
}
const offlineNotice = async (id) => {
  await ElMessageBox.confirm('确认下线该公告吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  params.value.id = id
  await offlineNoticeService(params.value)
  params.value.id = ''
  ElMessage.success('下线成功')
  getNoticeList()
}

const handleSizeChange = (size) => {
  params.value.current = 1
  params.value.pageSize = size
  getNoticeList()
}

const handleCurrentChange = (page) => {
  params.value.current = page
  getNoticeList()
}

const addNoticeForm = ref({
  title: '',
  content: '',
})

const addDialogVisible = ref(false)
const addNotice = () => {
  addDialogVisible.value = true
}
const handleAddNotice = async () => {
  await ElMessageBox.confirm('确认新增公告吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  const res = await addNoticeService(addNoticeForm.value)
  if (res.data.data) {
    ElMessage.success('新增成功')
    addDialogVisible.value = false
    addNoticeForm.value = {
      title: '',
      content: '',
    }
    getNoticeList()
  } else {
    ElMessage.error('新增失败')
  }
}
const handleAddDialogClose = () => {
  addNoticeForm.value = {
      title: '',
      content: '',
    }
  addDialogVisible.value = false
}
</script>

<template>
  <page-container>
    <template #title>
      <div>公告管理</div>
    </template>
    <template #extra>
      <el-button type="primary" @click="addNotice">新增公告</el-button>
    </template>

    <el-table v-loading="loading" :data="noticeList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column label="序号" width="200" type="index" align="center"></el-table-column>
      <el-table-column prop="title" label="公告标题" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" :formatter="formatStatus" align="center"></el-table-column>
      <el-table-column label="操作" width="260" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="info"
            size="small"
            plain
            @click="getNoticeById(row.id)"
          >详情</el-button>
          <el-button
            round
            type="primary"
            size="small"
            plain
            @click="onlineNotice(row.id)"
          >编辑</el-button>
          <el-button
            round
            type="success"
            size="small"
            plain
            @click="onlineNotice(row.id)"
          >上线</el-button>
          <el-button
            round
            type="danger"
            size="small"
            plain
            @click="offlineNotice(row.id)"
          >下线</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增公告弹窗 -->
    <el-dialog
      v-model="addDialogVisible"
      title="新增公告"
      width="40%"
      :before-close="handleAddDialogClose"
    >
      <template #default>
        <el-form :model="addNoticeForm" label-width="100px" size="large" class="job-form">
          <el-form-item label="公告标题">
            <el-input v-model="addNoticeForm.title" placeholder="请输入公告标题" clearable></el-input>
          </el-form-item>
          <el-form-item label="公告内容">
            <el-input v-model="addNoticeForm.content" type="textarea" placeholder="请输入职位详情" clearable class="textarea-input"></el-input>
          </el-form-item>
          <div class="dialog-footer">
            <el-button @click="handleAddDialogClose">取消</el-button>
            <el-button type="primary" @click="handleAddNotice">新增</el-button>
          </div>
        </el-form>
      </template>
    </el-dialog>
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
