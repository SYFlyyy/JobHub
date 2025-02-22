import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', component: () => import('@/views/login/Login.vue') },
    {
      path: '/',
      component: () => import('@/views/layout/LayoutContainer.vue'),
      redirect: '/user/information',
      children: [
        {
          path: '/admin/userManage',
          component: () => import('@/views/admin/UserManage.vue'),
        },
        {
          path: '/admin/companyManage',
          component: () => import('@/views/admin/CompanyManage.vue'),
        },
        {
          path: '/admin/jobManage',
          component: () => import('@/views/admin/JobManage.vue'),
        },
        {
          path: '/data/platformData',
          component: () => import('@/views/data/PlatformData.vue'),
        },
        {
          path: '/data/resumeData',
          component: () => import('@/views/data/ResumeData.vue'),
        },
        {
          path: '/notice/admin',
          component: () => import('@/views/notice/NoticeAdmin.vue'),
        },
        {
          path: '/notice/user',
          component: () => import('@/views/notice/NoticeUser.vue'),
        },
        {
          path: '/feedback/admin',
          component: () => import('@/views/feedback/FeedbackAdmin.vue'),
        },
        {
          path: '/feedback/user',
          component: () => import('@/views/feedback/FeedbackUser.vue'),
        },
        {
          path: '/job/page',
          component: () => import('@/views/job/JobPage.vue'),
        },
        {
          path: '/job/collect',
          component: () => import('@/views/job/JobCollect.vue'),
        },
        {
          path: '/job/jobManage',
          component: () => import('@/views/job/JobManage.vue'),
        },
        {
          path: '/record/candidate',
          component: () => import('@/views/record/ResumeRecordCandidate.vue'),
        },
        {
          path: '/candidate/application',
          component: () => import('@/views/candidate/Application.vue'),
        },
        {
          path: '/recruiter/companyManage',
          component: () => import('@/views/recruiter/CompanyManage.vue'),
        },
        {
          path: '/record/recruiter',
          component: () => import('@/views/record/ResumeRecordRecruiter.vue'),
        },
        // {
        //   path: '/user/avatar',
        //   component: () => import('@/views/user/UserAvatar.vue'),
        // },
        {
          path: '/user/information',
          component: () => import('@/views/user/UserInfo.vue'),
        },
        {
          path: '/user/password',
          component: () => import('@/views/user/UserPassword.vue'),
        }
      ],
    },
  ],
})

router.beforeEach((to) => {
  const userStore = useUserStore()
  if (!userStore.loginUser && to.path !== '/login') {
    return '/login'
  }
})

export default router
