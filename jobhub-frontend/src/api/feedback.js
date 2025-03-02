import request from '@/utils/request'

// 获取反馈列表
export const getFeedbackListService = (data) => {
  return request.post('/feedback/list/page', data)
}

// 新增反馈
export const addFeedbackService = (data) => {
  return request.post('/feedback/add', data)
}

// 根据id获取反馈详情
export const getFeedbackDetailService = ({id}) => {
  return request.post('/feedback/get', {
    id
  })
}
