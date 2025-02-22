<script setup>
import {
  User,
  CaretBottom,
  Crop,
  EditPen,
  SwitchButton,
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'
import AdminAside from './AdminAside.vue';
import CandidateAside from './CandidateAside.vue';
import RecruiterAside from './RecruiterAside.vue';
import { useUserStore } from '@/stores/user';
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';

const userStore = useUserStore();
const router = useRouter();
// const loginUser = ref()
// onBeforeMount(() => {
//   loginUser.value = userStore.loginUser;
// })
// 从store中获取登录用户信息
onMounted(() => {
  userStore.getLoginUser()
})

const handleCommand = async (command) => {
  if (command === 'logout') {
    // 退出操作
    await ElMessageBox.confirm('确认退出登录吗？', '温馨提示', {
      type: 'warning',
      confirmButtonText: '确认',
      cancelButtonText: '取消',
    })
    userStore.clearLoginUser();
    router.push('/login')
  } else {
    router.push(`/user/${command}`);
  }
}
</script>

<template>
  <el-container class="layout-container">
    <AdminAside v-if="userStore.loginUser.role === 0"/>
    <CandidateAside v-else-if="userStore.loginUser.role === 1"/>
    <RecruiterAside v-else-if="userStore.loginUser.role === 2"/>
    <el-container>
      <el-header>
        <div v-if="userStore.loginUser.role === 0"><strong>{{ userStore.loginUser.username ? userStore.loginUser.username : '用户' }}</strong></div>
        <div v-else-if="userStore.loginUser.role === 1">求职者：<strong>{{ userStore.loginUser.username ? userStore.loginUser.username : '用户' }}</strong></div>
        <div v-else-if="userStore.loginUser.role === 2">招聘者：<strong>{{ userStore.loginUser.username ? userStore.loginUser.username : '用户' }}</strong></div>
        <el-dropdown placement="bottom-end" @command="handleCommand">
          <span class="el-dropdown__box">
            <el-avatar :src="'http://localhost:8080/jobhub/' + userStore.loginUser.avatar" />
            <el-icon><CaretBottom /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="information" :icon="User"
                >基本资料</el-dropdown-item
              >
              <!-- <el-dropdown-item command="avatar" :icon="Crop"
                >更换头像</el-dropdown-item
              > -->
              <el-dropdown-item command="password" :icon="EditPen"
                >重置密码</el-dropdown-item
              >
              <el-dropdown-item command="logout" :icon="SwitchButton"
                >退出登录</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
      <el-footer>艾府招聘 ©2025 Created by 烧鸭饭</el-footer>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
  .el-aside {
    background-color: #FAFAFA;
    // &__logo {
    //   height: 120px;
    //   background: url('@/assets/logo.svg') no-repeat center / 45px auto;
    // }
    &__logo {
      height: 120px;
      display: flex;          // 新增flex布局
      align-items: center;    // 垂直居中
      padding-left: 30px;     // 左侧留出间距
      background: url('@/assets/logo.svg') no-repeat left center / 45px auto;  // 调整背景位置
      font-size: 20px;        // 文字大小
      font-weight: 600;       // 文字加粗
      color: black;         // 文字颜色
      margin-left: 40px;
      // 添加伪元素显示文字
      &::after {
        content: "艾府招聘";
        margin-left: 30px;    // 文字距离logo的间距
      }
    }
    .el-menu {
      border-right: none;
    }
  }
  .el-header {
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .el-dropdown__box {
      display: flex;
      align-items: center;
      .el-icon {
        color: #999;
        margin-left: 10px;
      }

      &:active,
      &:focus {
        outline: none;
      }
    }
  }
  .el-footer {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: #666;
  }
}
</style>
