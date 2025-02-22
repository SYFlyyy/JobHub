<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getCompanyListService, reviewCompanyService, freezeCompanyService } from '@/api/company'

const companyList = ref([])

const total = ref(0)


const loading = ref(false)
const params = ref({
  id: '',
  name: '',
  intro: '',
  address: '',
  type: '',
  size: '',
  logo: '',
  status: '',
  pageSize: 5,
  current: 1
})

const getCompanyList = async () => {
  loading.value = true
  const res = await getCompanyListService(params.value)
  companyList.value = res.data.data.records
  total.value = parseInt(res.data.data.total)
  loading.value = false
}

const dialogVisible = ref(false)
const companyDetail = ref({})
const getCompanyById = async (id) => {
  params.value.id = id
  const res = await getCompanyListService(params.value)
  companyDetail.value = res.data.data.records[0]
  dialogVisible.value = true
  params.value.id = ''
}
const handleDialogClose = () => {
  companyDetail.value = {}
  dialogVisible.value = false
}

// 初始加载企业数据
getCompanyList()

const formatStatus = (row, column, cellValue) => {
  switch (cellValue) {
    case 0:
      return '正常'   // 对应数字 0 显示 '正常'
    case 1:
      return '冻结'   // 对应数字 1 显示 '禁用'
    case 2:
      return '待审核' // 对应数字 2 显示 '待审核'
    default:
      return '未知'   // 如果是其他数字，显示 '未知'
  }
}
const reviewCompany = async (id) => {
  await ElMessageBox.confirm('确认审核通过该企业吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  params.value.id = id
  await reviewCompanyService(params.value)
  params.value.id = ''
  ElMessage.success('操作成功')
  getCompanyList()
}

const freezeUser = async (id) => {
  await ElMessageBox.confirm('确认冻结通过该企业吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  params.value.id = id
  await freezeCompanyService(params.value)
  params.value.id = ''
  ElMessage.success('操作成功')
  getCompanyList()
}

const handleSizeChange = (size) => {
  params.value.current = 1
  params.value.pageSize = size
  getCompanyList()
}

const handleCurrentChange = (page) => {
  params.value.current = page
  getCompanyList()
}

const resetParams = () => {
  params.value.name = ''
  params.value.address = ''
  params.value.type = ''
  params.value.size = ''
  params.value.status = ''
}
</script>

<template>
  <page-container>
    <template #title>
      <div>企业管理</div>
    </template>
    <!-- <template #extra>
      <el-button type="primary">添加分类</el-button>
    </template> -->
    <el-form inline>
      <el-form-item label="名称：">
        <el-input v-model="params.name" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item label="地址：">
        <el-input v-model="params.address" placeholder="请输入" clearable />
      </el-form-item>
        <el-form-item label="类型：">
      <el-input v-model="params.type" placeholder="请输入" clearable />
        </el-form-item>
      <el-form-item label="规模：">
        <el-select
          v-model="params.size"
          placeholder="请选择"
          style="width: 192px"
          clearable
        >
          <el-option label="0-20人" value="0-20人"></el-option>
          <el-option label="21-99人" value="21-99人"></el-option>
          <el-option label="100-499人" value="100-499人"></el-option>
          <el-option label="500-999人" value="500-999人"></el-option>
          <el-option label="1000-9999人" value="v"></el-option>
          <el-option label="10000+人" value="10000+人"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态：">
        <el-select
          v-model="params.status"
          placeholder="请选择"
          style="width: 192px"
          clearable
        >
          <el-option label="正常" value="0"></el-option>
          <el-option label="冻结" value="1"></el-option>
          <el-option label="待审核" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getCompanyList">搜索</el-button>
        <el-button @click="resetParams">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="companyList" style="width: 100%" :row-style="{ height: '60px' }">
      <el-table-column label="序号" width="100" type="index" align="center"></el-table-column>
      <el-table-column prop="name" label="企业名称" align="center"></el-table-column>
      <el-table-column prop="logo" label="企业logo" align="center">
        <template #default="{ row }">
          <el-avatar :src="'http://localhost:8080/jobhub/' + row.logo" />
        </template>
      </el-table-column>
      <el-table-column prop="address" label="地址" align="center"></el-table-column>
      <el-table-column prop="type" label="企业类型" align="center"></el-table-column>
      <el-table-column prop="size" label="企业规模" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" :formatter="formatStatus" align="center"></el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template #default="{ row }">
          <el-button
            round
            type="info"
            size="small"
            plain
            @click="getCompanyById(row.id)"
          >详情</el-button>
          <el-button
            round
            type="primary"
            size="small"
            plain
            @click="reviewCompany(row.id)"
          >审核</el-button>
          <el-button
            round
            type="danger"
            size="small"
            plain
            @click="freezeUser(row.id)"
          >冻结</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      v-model="dialogVisible"
      :before-close="handleDialogClose"
    >
      <template #default>
        <el-descriptions
          title="企业详情"
          direction="vertical"
          border
          style="margin-top: 20px"
        >
          <el-descriptions-item
            :rowspan="2"
            :width="140"
            label="企业logo"
            align="center"
          >
            <el-image
              style="width: 100px; height: 100px"
              :src="'http://localhost:8080/jobhub/' + companyDetail.logo"
            />
          </el-descriptions-item>
          <el-descriptions-item label="企业名称">{{ companyDetail.name }}</el-descriptions-item>
          <el-descriptions-item label="企业地址">{{ companyDetail.address }}</el-descriptions-item>
          <el-descriptions-item label="企业类型">{{ companyDetail.type }}</el-descriptions-item>
          <el-descriptions-item label="企业规模">{{ companyDetail.size }}</el-descriptions-item>
          <el-descriptions-item label="企业介绍">
            <div class="long-text">{{ companyDetail.intro }}</div>
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
</style>
