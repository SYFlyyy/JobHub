<script setup>
import {
  Management,
  User,
  OfficeBuilding,
  Suitcase,
  ChatLineSquare,
  Crop,
  EditPen,
  DataAnalysis,
  MessageBox,
  Bell
} from '@element-plus/icons-vue'
</script>

<template>
  <el-aside width="220px">
    <div class="el-aside__logo"></div>
    <el-menu
      active-text-color="rgb(121.3, 187.1, 255)"
      background-color="#FAFAFA"
      :default-active="$route.path"
      text-color="#606266"
      router
    >
      <!-- 管理 -->
      <el-sub-menu index="/admin">
        <template #title>
          <el-icon><Management /></el-icon>
          <span>管理</span>
        </template>
        <el-menu-item index="/admin/userManage">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/companyManage">
          <el-icon><OfficeBuilding /></el-icon>
          <span>企业管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/jobManage">
          <el-icon><Suitcase /></el-icon>
          <span>职位管理</span>
        </el-menu-item>
      </el-sub-menu>
      <!-- 平台数据 -->
      <el-menu-item index="/data/platformData">
        <el-icon><DataAnalysis /></el-icon>
        <span>平台数据</span>
      </el-menu-item>
      <!-- 公告 -->
      <el-menu-item index="/notice/admin">
        <el-icon><Bell /></el-icon>
        <span>公告管理</span>
      </el-menu-item>
      <!-- 用户反馈 -->
      <el-menu-item index="/feedback/admin">
        <el-icon><MessageBox /></el-icon>
        <span>用户反馈</span>
      </el-menu-item>
      <!-- 个人中心 -->
      <el-sub-menu index="/user">
        <template #title>
          <el-icon><User /></el-icon>
          <span>个人中心</span>
        </template>
        <el-menu-item index="/user/information">
          <el-icon><ChatLineSquare /></el-icon>
          <span>基本资料</span>
        </el-menu-item>
        <!-- <el-menu-item index="/user/avatar">
          <el-icon><Crop /></el-icon>
          <span>更换头像</span>
        </el-menu-item> -->
        <el-menu-item index="/user/password">
          <el-icon><EditPen /></el-icon>
          <span>修改密码</span>
        </el-menu-item>
      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<style lang="scss" scoped>
.el-aside {
    background-color: #FAFAFA;
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
</style>
