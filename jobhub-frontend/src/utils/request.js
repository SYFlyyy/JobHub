import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const baseURL = 'http://localhost:8080/jobhub'

const instance = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 10000,
  withCredentials: true,
})

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    // TODO 2. 携带token
    return config
  },
  (err) => Promise.reject(err),
)

// 响应拦截器
instance.interceptors.response.use(
  (res) => {
    // TODO 3. 处理业务失败
    // TODO 4. 摘取核心响应数据
    if (res.data.code === 0) {
      return res
    }
    ElMessage.error(res.data.message || '服务异常')
    return Promise.reject(res.data)
  },
  (err) => {
    // TODO 5. 处理401错误
    if (err.response && err.response.status === 401) {
      const userStore = useUserStore()
      userStore.clearLoginUser()
      ElMessage.error('请重新登录')
      window.location.href = '/login'
    }
    // 错误默认情况
    ElMessage.error(res.response.data.message || '服务异常')
    return Promise.reject(err)
  },
)

export default instance
export { baseURL }
