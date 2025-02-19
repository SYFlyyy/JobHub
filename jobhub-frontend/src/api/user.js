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
