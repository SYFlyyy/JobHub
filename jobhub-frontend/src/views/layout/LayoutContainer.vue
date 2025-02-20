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
</script>

<template>
  <el-container class="layout-container">
    <AdminAside />
    <CandidateAside />
    <RecruiterAside />
    <el-container>
      <el-header>
        <div>管理员：<strong>admin</strong></div>
        <el-dropdown placement="bottom-end">
          <span class="el-dropdown__box">
            <el-avatar :src="avatar" />
            <el-icon><CaretBottom /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile" :icon="User"
                >基本资料</el-dropdown-item
              >
              <el-dropdown-item command="avatar" :icon="Crop"
                >更换头像</el-dropdown-item
              >
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
