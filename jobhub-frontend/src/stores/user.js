import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getLoginUserService } from '@/api/user'

// 用户模块
export const useUserStore = defineStore('user', () => {
  const loginUser = ref()

  const setLoginUser = (obj) => {
    loginUser.value = obj
  }

  const clearLoginUser = () => {
    loginUser.value = null
  }

  const getLoginUser = async () => {
    const res = await getLoginUserService()
    setLoginUser(res.data.data)
  }

  return {
    loginUser,
    setLoginUser,
    clearLoginUser,
    getLoginUser
  }
},{
  persist: true
})
