import { defineStore } from 'pinia'
import { ref } from 'vue'

// 用户模块
export const useUserStore = defineStore('user', () => {
  const loginUser = ref()
  const setLoginUser = (user) => {
    loginUser.value = user
  }
  const removeLoginUser = () => {
    loginUser.value = null
  }
  return {
    loginUser,
    setLoginUser,
    removeLoginUser,
  }
})
