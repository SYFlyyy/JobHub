import requst from '@/utils/request'

// 获取平台数据
export const getPlatformDataService = () => {
  return requst.get('/data/platform')
}
