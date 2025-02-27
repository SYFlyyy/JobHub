import request from '@/utils/request'

// 获取职位列表
export const getJobListService = (data) => {
  return request.post('/job/list/page', data)
}

// 获取职位列表（含企业信息）
export const getJobWithCompanyService = (data) => {
  return request.post('/job/list/page/vo', data)
}

// 职位审核
export const reviewJobService = ({id}) => {
  return request.post('/job/review', {
    id
  })
}

// 收藏职位
export const jobCollectService = ({id}) => {
  return request.post('/job/collect', {
    id
  })
}

// 取消收藏
export const jobCancelCollectService = ({id}) => {
  return request.post('/job/cancelCollect', {
    id
  })
}

// 获取收藏职位列表
export const getJobCollectListService = (data) => {
  return request.post('/job/collectedJob', data)
}

// 根据id获取职位详情
export const getJobDetailByIdService = ({id}) => {
  return request.post('/job/getById', {
    id
  })
}

// 上线职位
export const onlineJobService = ({id}) => {
  return request.post('/job/online', {
    id
  })
}

// 下线职位
export const offlineJobService = ({id}) => {
  return request.post('/job/offline', {
    id
  })
}

// 新增职位
export const addJobService = (data) => {
  return request.post('/job/add', data)
}
