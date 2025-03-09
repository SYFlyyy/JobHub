import requst from '@/utils/request'

// 获取平台数据
export const getPlatformDataService = () => {
  return requst.get('/data/platform')
}

// 获取招聘数据
export const getRecruitDataService = (data) => {
  return requst.post('/data/hiring', data)
}
