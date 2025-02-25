import request from '@/utils/request'

// 新增投递记录
export const addResumeRecordServcie = ({id}) => {
  return request.post('/resumeRecord/add', {
    id
  })
}

// 获取求职者投递记录
export const getCandidateResumeRecordService = (data) => {
  return request.post('/resumeRecord/list/page/my', data)
}
