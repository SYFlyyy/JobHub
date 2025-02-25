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
      <!-- 职位 -->
      <el-sub-menu index="/job">
        <template #title>
          <el-icon><Suitcase /></el-icon>
          <span>职位</span>
        </template>
        <el-menu-item index="/job/page">
          <el-icon><View /></el-icon>
          <span>浏览职位</span>
        </el-menu-item>
        <el-menu-item index="/job/collect">
          <el-icon><CollectionTag /></el-icon>
          <span>职位收藏</span>
        </el-menu-item>
      </el-sub-menu>
      <!-- 投递记录 -->
      <el-menu-item index="/record/candidate">
        <el-icon><Message /></el-icon>
        <span>投递记录</span>
      </el-menu-item>
      <!-- 个人简历 -->
      <el-menu-item index="/candidate/application">
        <el-icon><Tickets /></el-icon>
        <span>个人简历</span>
      </el-menu-item>
      <!-- 公告 -->
      <el-menu-item index="/notice/user">
        <el-icon><Bell /></el-icon>
        <span>公告</span>
      </el-menu-item>
      <!-- 用户反馈 -->
      <el-menu-item index="/feedback/user">
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

<script setup>
import {
  User,
  Suitcase,
  View,
  CollectionTag,
  Message,
  ChatLineSquare,
  Crop,
  EditPen,
  Tickets,
  MessageBox,
  Bell
} from '@element-plus/icons-vue'
</script>

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
