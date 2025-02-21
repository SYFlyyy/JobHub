import { defineStore } from 'pinia'
import { ref } from 'vue'

// 用户模块
export const useUserStore = defineStore('user', () => {
  const loginUser = ref()

  const setLoginUser = (obj) => {
    loginUser.value = obj
  }

  const clearLoginUser = () => {
    loginUser.value = null
  }

  return {
    loginUser,
    setLoginUser,
    clearLoginUser
  }
},{
  persist: true
})
