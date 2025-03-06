<script setup>
import { ref } from 'vue'
import PageContainer from '@/views/layout/PageContainer.vue'
import { getPlatformDataService } from '@/api/data'
import { ElMessage } from 'element-plus'

const platformData = ref({})
const loading = ref(false)

const getPlatformData = async () => {
  loading.value = true
  const res = await getPlatformDataService()
  platformData.value = res.data.data
  console.log(platformData.value)
  loading.value = false
  ElMessage.success('数据更新成功')
}

getPlatformData()

</script>

<template>
  <page-container>
    <template #title>
      <div>平台数据</div>
    </template>
    <!-- <template #extra>
      <el-button type="primary" @click="getPlatformData">更新数据</el-button>
    </template> -->

    <div class="card-list">
      <el-card>
        <div class="card-item">
          <div class="card-item-title">求职者总数</div>
          <div class="card-item-content">{{ platformData.candidateCount }}</div>
        </div>
      </el-card>
      <el-card>
        <div class="card-item">
          <div class="card-item-title">招聘者总数</div>
          <div class="card-item-content">{{ platformData.recruiterCount }}</div>
        </div>
      </el-card>
      <el-card>
        <div class="card-item">
          <div class="card-item-title">企业总数</div>
          <div class="card-item-content">{{ platformData.companyCount }}</div>
        </div>
      </el-card>
      <el-card>
        <div class="card-item">
          <div class="card-item-title">职位总数</div>
          <div class="card-item-content">{{ platformData.jobCount }}</div>
        </div>
      </el-card>
    </div>
  </page-container>
</template>

<style lang="scss" scoped>
.page-container {
  --primary-color: #409eff;
  --success-color: #67c23a;
  --warning-color: #e6a23c;
  --danger-color: #f56c6c;
}

.card-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2rem;
  padding: 1.5rem;

  .el-card {
    transition: all 0.3s ease;
    border-radius: 12px;
    background: linear-gradient(145deg, #ffffff 0%, #f8f9fa 100%);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    min-width: 400px;
    min-height: 200px;
    margin: 0 auto;
    margin-bottom: 3rem;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 15px rgba(var(--primary-color), 0.15);
    }

    &:nth-child(1) .card-item-content {
      color: var(--primary-color);
    }
    &:nth-child(2) .card-item-content {
      color: var(--success-color);
    }
    &:nth-child(3) .card-item-content {
      color: var(--warning-color);
    }
    &:nth-child(4) .card-item-content {
      color: var(--danger-color);
    }
  }
}

.card-item {
  padding: 1.5rem;
  text-align: center;

  &-title {
    font-size: 1.1rem;
    color: #6c757d;
    margin-bottom: 1rem;
    font-weight: 500;
    letter-spacing: 0.5px;
  }

  &-content {
    font-size: 2.2rem;
    font-weight: 700;
    line-height: 1.2;
    transition: font-size 0.3s ease;
    position: relative;

    &::after {
      content: '';
      position: absolute;
      bottom: -8px;
      left: 50%;
      width: 40px;
      height: 3px;
      background: currentColor;
      transform: translateX(-50%);
      border-radius: 2px;
    }
  }
}

/* 响应式设计 */
@media (max-width: 992px) {
  .card-list {
    grid-template-columns: 1fr;
    gap: 1.5rem;
    padding: 1rem;
  }

  .card-item {
    padding: 2rem; // 增加内边距
    text-align: center;

    &-title {
    font-size: 1.2rem; // 稍大字号
    color: #6c757d;
    margin-bottom: 1.2rem;
    }

    &-content {
      font-size: 2.4rem; // 增大数字
    }
  }
}

/* 加载状态 */
.el-card {
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 50%;
    height: 100%;
    background: linear-gradient(
      to right,
      transparent 0%,
      rgba(255, 255, 255, 0.6) 50%,
      transparent 100%
    );
    animation: shimmer 1.5s infinite;
  }
}

@keyframes shimmer {
  100% {
    left: 200%;
  }
}
</style>
