import request from '@/utils/request'

// 获取企业列表
export const getCompanyListService = (data) => {
  return request.post('/company/list/page', data)
}

// 企业审核
export const reviewCompanyService = ({id}) => {
  return request.post('/company/review', {
    id
  })
}

// 冻结企业
export const freezeCompanyService = ({id}) => {
  return request.post('/company/freeze', {
    id
  })
}
