import { createRouter, createWebHistory } from 'vue-router'
import MainPage from '@/views/MainPage.vue'
import BoardMainPage from '@/views/TripostBoardPage.vue'
import TripostWritePage from '@/views/TripostWritePage.vue'
// import PlanningPage from '@/views/PlanningPage.vue'
import PlanningPage from '@/views/PlanningPage.vue'
import TripostDetailPage from '@/views/TripostDetailPage.vue'
import PlanUpdatePage from '@/views/PlanUpdatePage.vue'
import LoginSuccessPage from '@/views/LoginSuccessPage.vue'
import MyTripPlanPage from '@/views/MyTripPlanPage.vue'
import PlanDetailPage from '@/views/PlanDetailPage.vue'
import ServiceIntroPage from '@/views/ServiceIntroPage.vue'

const routes = [
  { path: '/', component: MainPage },
  { path: '/service-intro', name: 'ServiceIntroPage', component: ServiceIntroPage },
  { path: '/tripost/board', name: 'TripostBoardPage', component: BoardMainPage},
  { path: '/tripost/write/:planId', name: 'TripostWritePage', component: TripostWritePage, props: true },
  { path: '/tripost/:tripostId', name: 'TripostDetailPage', component: TripostDetailPage, props: true },
  { path: '/planning', name: 'PlanningPage', component: PlanningPage },
  // { path: '/trip/plan', component: () => import('@/domains/plan/pages/MyTravelPlanPage.vue') },
  { path: '/login/success', name: 'LoginSuccessPage' , component: LoginSuccessPage },
  { path: '/my-plans', name: 'MyTripPlanPage', component: MyTripPlanPage },
  { path: '/plan/detail/:id', name: 'PlanDetailPage', component: PlanDetailPage },
  { path: '/plan/update/:id', name: 'PlanUpdatePage', component: PlanUpdatePage },
  // 기타 라우트 정의
]

export const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior:(to, from, savedPosition) => {
    return {top: 0};
  }
})
