<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getJobWithCompanyService, jobCollectService, getJobDetailByIdService } from '@/api/job'
import { getCompanyListService } from '@/api/company'
import { pcaTextArr } from "element-china-area-data";
import { addResumeRecordServcie } from '@/api/resumeRecord';

const jobList = ref([])
const loading = ref(false)
const params = ref({
  searchKey: '',
  id: '',
  companyId: '',
  companyName: '',
  name: '',
  type: 0,
  salary: '',
  intro: '',
  pageSize: 9,
  current: 1
})

const companyParams = ref({
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

const total = ref(0)

const addressObj = ref([])
const formattedAddress = () => {
  if (addressObj.value.length === 0) {
    return
  }
  const specialCities = ['北京市', '上海市', '天津市', '重庆市'];
  let selectedValues;
  if (specialCities.includes(addressObj.value[0])) {
    // 如果下标 0 的值是特殊城市，取 0 和 2 的值
    selectedValues = [addressObj.value[0], addressObj.value[2]];
  } else {
    // 否则取 1 和 2 的值
    selectedValues = [addressObj.value[1], addressObj.value[2]];
  }
  // 过滤掉可能存在的 undefined 值
  const validValues = selectedValues.filter(value => value!== undefined);
  // 将过滤后的数组元素拼接成字符串
  return validValues.join('');
}

const getJobList = async () => {
  loading.value = true
  params.value.address = formattedAddress()
  const res = await getJobWithCompanyService(params.value)
  jobList.value = res.data.data.records
  total.value = parseInt(res.data.data.total)
  loading.value = false
}

const jobDialogVisible = ref(false)
const companyDialogVisible = ref(false)
const jobDetail = ref({})
const getJobById = async (id) => {
  params.value.id = id
  const res = await getJobDetailByIdService(params.value)
  jobDetail.value = res.data.data
  jobDialogVisible.value = true
  params.value.id = ''
}

const companyDetail = ref({})
const getCompanyById = async (id) => {
  companyParams.value.id = id
  const res = await getCompanyListService(companyParams.value)
  companyDetail.value = res.data.data.records[0]
  companyDialogVisible.value = true
  companyParams.value.id = ''
}
const handleJobDialogClose = () => {
  jobDetail.value = {}
  jobDialogVisible.value = false
}
const handleCompanyDialogClose = () => {
  companyDetail.value = {}
  companyDialogVisible.value = false
}

// 初始加载职位数据
getJobList()

const typeStatus = (type) => {
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

const handleCurrentChange = (page) => {
  params.value.current = page
  getJobList()
}

const resetParams = () => {
  params.value.searchKey = ''
  params.value.address = ''
  addressObj.value = []
  getJobList()
}

const activeName = ref('fullTime')
const handleClick = () => {
  if (activeName.value === 'fullTime') {
    params.value.type = '0'
  } else if (activeName.value === 'intern') {
    params.value.type = '1'
  } else if (activeName.value === 'partTime') {
    params.value.type = '2'
  }
  getJobList()
}

const collectForm = ref({})
const jobCollect = async (id) => {
  await ElMessageBox.confirm('确认收藏该职位吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  collectForm.value.id = id
  await jobCollectService(collectForm.value)
  ElMessage.success('收藏成功')
  getJobList()
}

const deliverForm = ref({})
const deliver = async (id) => {
  await ElMessageBox.confirm('确认投递该职位吗？', '温馨提示', {
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消',
  })
  deliverForm.value.id = id
  await addResumeRecordServcie(deliverForm.value)
  ElMessage.success('投递成功')
}
</script>

<template>
  <page-container>
    <template #title>
      <div>浏览职位</div>
    </template>
    <!-- <template #extra>
      <el-button type="primary">添加分类</el-button>
    </template> -->

    <el-tabs v-model="activeName" class="tabs" @tab-click="handleClick">
      <el-tab-pane label="全职" name="fullTime"></el-tab-pane>
      <el-tab-pane label="实习" name="intern"></el-tab-pane>
      <el-tab-pane label="兼职" name="partTime"></el-tab-pane>
    </el-tabs>

    <el-form :model="params" inline>
      <el-form-item >
        <el-input v-model="params.searchKey" placeholder="请输入" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-cascader
          placeholder="请选择地区"
          :options="pcaTextArr"
          v-model="addressObj">
        </el-cascader>
      <el-form-item style="margin-left: 30px;">
        <el-button type="primary" @click="getJobList">查询</el-button>
        <el-button @click="resetParams">重置</el-button>
      </el-form-item>
      </el-form-item>
    </el-form>

    <div class="card-list">
      <el-card v-for="item in jobList" :key="item.id" class="card-item" @click="getJobById(item.id)" style="margin-top: 13px">
        <template #header>
          <div class="card-header">
            <div>{{ item.name }}</div>
            <div>{{ item.salary }}</div>
          </div>
        </template>
        <template #default>
          <div class="card-content">
            <div class="left-content">
              <div>{{ item.companyName }}</div>
              <div>{{ item.size }}</div>
            </div>
            <div class="right-content">
              <div>{{ item.address }}</div>
            </div>
          </div>
        </template>
      </el-card>
    </div>
    <el-dialog
      v-model="jobDialogVisible"
      :before-close="handleJobDialogClose"
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
          <el-descriptions-item label-align="center" align="center" label="所属企业">
            <el-link type="primary" @click="getCompanyById(jobDetail.companyId)">{{ jobDetail.companyName }}</el-link>
          </el-descriptions-item>
          <el-descriptions-item label-align="center" label="职位详情" :span="4">
            <div class="long-text">{{ jobDetail.intro }}</div>
          </el-descriptions-item>
        </el-descriptions>
        <div class="dialog-footer">
          <el-button type="primary" plain @click="jobCollect(jobDetail.id)">收藏</el-button>
          <el-button type="success" plain @click="deliver(jobDetail.id)">投递</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
      v-model="companyDialogVisible"
      :before-close="handleCompanyDialogClose"
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
          <el-descriptions-item label-align="center" align="center" label="企业名称">{{ companyDetail.name }}</el-descriptions-item>
          <el-descriptions-item label-align="center" align="center" label="企业地址">{{ companyDetail.address }}</el-descriptions-item>
          <el-descriptions-item label-align="center" align="center" label="企业类型">{{ companyDetail.type }}</el-descriptions-item>
          <el-descriptions-item label-align="center" align="center" label="企业规模">{{ companyDetail.size }}</el-descriptions-item>
          <el-descriptions-item label-align="center" label="企业介绍">
            <div class="long-text">{{ companyDetail.intro }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </template>
    </el-dialog>
    <el-pagination
      v-model:current-page="params.current"
      v-model:page-size="params.pageSize"
      layout="prev, pager, next"
      :total="total"
      @current-change="handleCurrentChange"
      style="margin-top: 20px; justify-content: flex-end"
    />
  </page-container>
</template>

<style lang="scss" scoped>
.card-list {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  margin-bottom: 8px;
}

.card-item {
  height: 115px;
  width: 395px;
}

.card-header {
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: space-between; /* 让子元素在主轴上两端对齐 */
  align-items: center; /* 让子元素在交叉轴上居中对齐 */
  font-weight: bold;
}

.card-content {
  display: flex; /* 使用 Flexbox 布局 */
  justify-content: space-between; /* 让左右内容分别在两端 */
  align-items: center; /* 垂直居中对齐 */
}

.left-content {
  display: flex; /* 让 companyName 和 size 在同一行 */
  gap: 10px; /* 设置 companyName 和 size 之间的间距 */
}

.dialog-footer {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  margin-top: 20px; /* 可以根据需要调整顶部间距 */
}

.long-text {
  max-height: 100px; /* 设置最大高度 */
  overflow-y: auto; /* 当内容超过最大高度时添加垂直滚动条 */
  word-break: break-all;
  white-space: normal;
}
</style>
