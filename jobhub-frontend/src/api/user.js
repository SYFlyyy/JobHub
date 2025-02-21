import request from '@/utils/request'

// 用户注册
export const userRegisterService = ({account, password, checkPassword, role}) => {
  return request.post('/user/register', {
    account,
    password,
    checkPassword,
    role
  })
}

// 用户登录
export const userLoginService = ({account, password}) => {
  return request.post('/user/login', {
    account,
    password
  })
}

// 获取用户列表
export const getUserListService = (data) => {
  return request.post('/user/list/page', data)
}

// 审核用户
export const reviewUserService = ({id}) => {
  return request.post('/user/review', {
    id
  })
}

// 冻结用户
export const freezeUserService = ({id}) => {
  return request.post('/user/freeze', {
    id
  })
}
