import request from '@/utils/request'

// 新增面试邀请
export const addInvitationService = (data) => {
  return request.post('/invitation/add', data)
}

// 接受面试邀请
export const acceptInvitationService = (data) => {
  return request.post('/invitation/accept', data)
}

// 拒绝面试邀请
export const rejectInvitationService = (data) => {
  return request.post('/invitation/reject', data)
}

// 获取面试邀请列表
export const getInvitationListService = (data) => {
  return request.post('/invitation/list/page', data)
}
