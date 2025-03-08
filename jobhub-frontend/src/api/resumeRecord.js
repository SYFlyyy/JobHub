import request from '@/utils/request'

// 新增投递记录
export const addResumeRecordServcie = (data) => {
  return request.post('/resumeRecord/add', data)
}

// 获取求职者投递记录
export const getCandidateResumeRecordService = (data) => {
  return request.post('/resumeRecord/list/page/my', data)
}

// 获取招聘记录
export const getResumeRecordService = (data) => {
  return request.post('/resumeRecord/list/page', data)
}

// 更新投递记录状态
export const updateResumeRecordService = (data) => {
  return request.post('/resumeRecord/update/status', data)
}
