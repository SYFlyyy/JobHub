import request from '@/utils/request'

// 新增公告
export const addNoticeService = (data) => {
  return request.post('/notice/add', data)
}

// 获取公告列表
export const getNoticeListService = (data) => {
  return request.post('/notice/list/page', data)
}

// 根据id获取公告
export const getNoticeByIdService = ({id}) => {
  return request.post('/notice/get', {
    id
  })
}

// 编辑公告
export const updateNoticeService = (data) => {
  return request.post('/notice/update', data)
}

// 上线公告
export const onlineNoticeService = (data) => {
  return request.post('/notice/online', data)
}

// 下线公告
export const offlineNoticeService = (data) => {
  return request.post('/notice/offline', data)
}
