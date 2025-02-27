import request from '@/utils/request'

// 新增在线简历
export const addApplicationInfoService = (data) => {
  return request.post('/applicationInfo/add', data)
}

// 更新在线简历
export const updateApplicationInfoService = (data) => {
  return request.post('/applicationInfo/update', data)
}

// 查看自己在线简历
export const getOwnApplicationInfoService = () => {
  return request.post('/applicationInfo/getOwn')
}

// 查看是否有在线简历
export const hasApplicationInfoService = () => {
  return request.get('/applicationInfo/has')
}

// 查看求职者在线简历
export const getApplicationInfoService = (data) => {
  return request.post('/applicationInfo/getUser', data)
}
