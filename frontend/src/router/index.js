import { createRouter, createWebHistory } from 'vue-router'
import MainPage from '@/views/MainPage.vue'
import BoardMainPage from '@/views/BoardMainPage.vue'
import PostWritePage from '@/views/PostWritePage.vue'
// import PlanningPage from '@/views/PlanningPage.vue'
import PlanningPage from '@/views/TempPlanningPage.vue'
import PostDetailPage from '@/views/PostDetailPage.vue'
import MyTravelPlanPage from '@/views/MyTripPlanPage.vue'
import PlanUpdatePage from '@/views/PlanUpdatePage.vue'
import LoginSuccessPage from '@/views/LoginSuccessPage.vue'

const routes = [
  { path: '/', component: MainPage },
  { path: '/board', component: BoardMainPage},
  { path: '/tripost/write', name: 'PostWritePage', component: PostWritePage },
  { path: '/planning', name: 'PlanningPage', component: PlanningPage },
  // { path: '/post/:id', component: () => import('@/domains/post/pages/PostDetailPage.vue') },
  // { path: '/trip/plan', component: () => import('@/domains/plan/pages/MyTravelPlanPage.vue') },
  { path: '/login/success', name: 'LoginSuccessPage' , component: LoginSuccessPage }
  // 기타 라우트 정의
]

export const router = createRouter({
  history: createWebHistory(),
  routes,
})
