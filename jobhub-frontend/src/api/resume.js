import request from '@/utils/request'

// 上传简历简历附件
export const uploadResumeService = (data) => {
  return request.post('/resume/upload', data, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取简历附件路径
export const getResumePathService = () => {
  return request.get('/resume/path')
}
