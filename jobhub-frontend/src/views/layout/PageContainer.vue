<script setup>
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>
          <slot name="title"></slot>
        </span>
        <div class="extra">
          <slot name="extra"></slot>
        </div>
      </div>
    </template>
    <slot></slot>
  </el-card>
</template>

<style lang="scss" scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}
</style>
