<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { freezeUserService, getUserListService, reviewUserService } from '@/api/user'

const userList = ref([])
const activeName = ref('candidate')
const loading = ref(false)
const params = ref({
  id: '',
  account: '',
  username: '',
  email: '',
  phone: '',
  role: 1, // 默认查询求职者
  status: '',
})

const getUserList = async () => {
  loading.value = true
  const res = await getUserListService(params.value)
  userList.value = res.data.data.records
  loading.value = false
}

const handleClick = (tab) => {
  if (activeName.value === 'candidate') {
    params.value.role = '1'
  } else if (activeName.value === 'recruiter') {
    params.value.role = '2'
  }
  getUserList()
}
// 初始加载求职者数据
getUserList()

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
const reviewUser = async (id) => {
  await ElMessageBox.confirm('确认审核通过该用户吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  params.value.id = id
  await reviewUserService(params.value)
  params.value.id = ''
  ElMessage.success('操作成功')
  getUserList()
}

const freezeUser = async (id) => {
  await ElMessageBox.confirm('确认冻结通过该用户吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  params.value.id = id
  await freezeUserService(params.value)
  params.value.id = ''
  ElMessage.success('操作成功')
  getUserList()
}
</script>

<template>
  <page-container>
    <template #title>
      <div>用户管理</div>
    </template>
    <!-- <template #extra>
      <el-button type="primary">添加分类</el-button>
    </template> -->

    <el-tabs v-model="activeName" class="tabs" @tab-click="handleClick">
      <el-tab-pane label="求职者管理" name="candidate">
        <el-table v-loading="loading" :data="userList" style="width: 100%" :row-style="{ height: '60px' }">
          <el-table-column label="序号" width="100" type="index" align="center"></el-table-column>
          <el-table-column prop="account" label="账号" align="center"></el-table-column>
          <el-table-column prop="username" label="姓名" align="center"></el-table-column>
          <el-table-column prop="avatar" label="头像" align="center">
            <template #default="{ row }">
              <el-avatar :src="'http://localhost:8080/jobhub/' + row.avatar" />
            </template>
          </el-table-column>
          <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
          <el-table-column prop="phone" label="手机" align="center"></el-table-column>
          <el-table-column prop="status" label="状态" :formatter="formatStatus" align="center"></el-table-column>
          <el-table-column label="操作" width="150" align="center">
            <template #default="{ row }">
              <el-button
                round
                type="primary"
                size="small"
                plain
                @click="reviewUser(row.id)"
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
      </el-tab-pane>
      <el-tab-pane label="招聘者管理" name="recruiter">
        <el-table v-loading="loading" :data="userList" style="width: 100%" :row-style="{ height: '60px' }">
          <el-table-column label="序号" width="100" type="index" align="center"></el-table-column>
          <el-table-column prop="account" label="账号" align="center"></el-table-column>
          <el-table-column prop="username" label="姓名" align="center"></el-table-column>
          <el-table-column prop="avatar" label="头像" align="center">
            <template #default="{ row }">
              <el-avatar :src="'http://localhost:8080/jobhub/' + row.avatar" />
            </template>
          </el-table-column>
          <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
          <el-table-column prop="phone" label="手机" align="center"></el-table-column>
          <el-table-column prop="status" label="状态" :formatter="formatStatus" align="center"></el-table-column>
          <el-table-column label="操作" width="150" align="center">
            <template #default="{ row }">
              <el-button
                round
                type="primary"
                size="small"
                plain
                @click="reviewUser(row.id)"
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
      </el-tab-pane>
    </el-tabs>
  </page-container>
</template>

<style lang="scss" scoped>
</style>
