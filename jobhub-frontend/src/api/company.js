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

// 企业注册
export const registerCompanyService = (data) => {
  return request.post('/company/add', data)
}

// 上传logo
export const uploadLogoService = (data) => {
  return request.post('/company/upload/logo', data)
}

// 获取企业信息
export const getCompanyInfoService = () => {
  return request.get('/company/get')
}
