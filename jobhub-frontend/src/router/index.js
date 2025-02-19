import { createRouter, createWebHistory } from 'vue-router'

const userRouter = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', component: () => import('@/views/login/Login.vue') },
    {
      path: '/',
      component: () => import('@/views/layout/LayoutContainer.vue'),
      redirect: '/admin/userManage',
      children: [
        {
          path: '/admin/userManage',
          component: () => import('@/views/admin/UserManage.vue'),
        },
        {
          path: '/candidate/application',
          component: () => import('@/views/candidate/Application.vue'),
        },
        {
          path: '/candidate/resume',
          component: () => import('@/views/candidate/Resume.vue'),
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
          path: '/feedback',
          component: () => import('@/views/feedback/Feedback.vue'),
        },
        {
          path: '/job',
          component: () => import('@/views/job/Job.vue'),
        },
        {
          path: '/notice',
          component: () => import('@/views/notice/Notice.vue'),
        },
        {
          path: '/recruiter/company',
          component: () => import('@/views/recruiter/Company.vue'),
        },
        {
          path: '/user/userAvatar',
          component: () => import('@/views/user/UserAvatar.vue'),
        },
        {
          path: '/user/userInfo',
          component: () => import('@/views/user/UserInfo.vue'),
        },
        {
          path: '/user/userPassword',
          component: () => import('@/views/user/UserPassword.vue'),
        }
      ],
    },
  ],
})

export default userRouter
