<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getCompanyListService, reviewCompanyService, freezeCompanyService } from '@/api/company'

const companyList = ref([])
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
})

const getCompanyList = async () => {
  loading.value = true
  const res = await getCompanyListService(params.value)
  companyList.value = res.data.data.records
  loading.value = false
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
</script>

<template>
  <page-container>
    <template #title>
      <div>企业管理</div>
    </template>
    <!-- <template #extra>
      <el-button type="primary">添加分类</el-button>
    </template> -->

    <el-table v-loading="loading" :data="companyList" style="width: 100%" :row-style="{ height: '60px' }">
          <el-table-column label="序号" width="100" type="index" align="center"></el-table-column>
          <el-table-column prop="name" label="企业名称" align="center"></el-table-column>
          <el-table-column prop="intro" label="企业介绍" align="center"></el-table-column>
          <el-table-column prop="address" label="地址" align="center"></el-table-column>
          <el-table-column prop="type" label="企业类型" align="center"></el-table-column>
          <el-table-column prop="size" label="企业规模" align="center"></el-table-column>
          <el-table-column prop="logo" label="logo" align="center"></el-table-column>
          <el-table-column prop="status" label="状态" :formatter="formatStatus" align="center"></el-table-column>
          <el-table-column label="操作" width="150" align="center">
            <template #default="{ row }">
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
  </page-container>
</template>

<style lang="scss" scoped>
</style>
