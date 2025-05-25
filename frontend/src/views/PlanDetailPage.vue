<template>
  <div class="plan-detail-page">
    <!-- 헤더 영역 -->
    <div class="plan-detail-header">
      <div class="header-content">
        <div class="header-title">
          <h1 class="title">{{ plan?.title || '여행 계획' }}</h1>
          <div class="plan-meta">
            <span class="plan-dates">
              {{ formatDate(plan?.startDate) }} ~ {{ formatDate(plan?.endDate) }}
            </span>
          </div>
          <span class="last-updated" v-if="plan?.updatedAt">
            마지막 업데이트: {{ formatDateTime(plan.updatedAt) }}
          </span>
        </div>

        <div class="header-actions">
          <button
            class="button button-secondary"
            @click="goToEdit"
          >
            <span class="icon-edit"></span> 수정
          </button>

          <button
            class="button button-danger"
            @click="confirmDelete"
          >
            <span class="icon-trash"></span> 삭제
          </button>

          <button
            class="button button-secondary"
            @click="goToMyPlans"
          >
            <span class="icon-back"></span> 돌아가기
          </button>
        </div>
      </div>
    </div>

    <!-- 메인 콘텐츠 -->
    <div class="plan-detail-content" v-if="plan">
      <!-- 기본 정보 카드 -->
      <div class="info-card">
        <h2 class="card-title">여행 정보</h2>
        <div class="info-grid">
          <div class="info-item">
            <label class="info-label">여행 제목</label>
            <div class="info-value">{{ plan.title }}</div>
          </div>
          <div class="info-item">
            <label class="info-label">여행 기간</label>
            <div class="info-value">
              {{ formatDate(plan.startDate) }} ~ {{ formatDate(plan.endDate) }}
              <span class="duration">({{ calculateDuration(plan.startDate, plan.endDate) }}일)</span>
            </div>
          </div>
        </div>

        <div class="info-item full-width" v-if="plan.description">
          <label class="info-label">여행 설명</label>
          <div class="info-value description">{{ plan.description }}</div>
        </div>
      </div>

      <!-- 여행 장소 목록 -->
      <div class="waypoints-card">
        <div class="card-header">
          <h2 class="card-title">
            여행 장소
            <span class="waypoint-count">{{ plan.waypointList?.length || 0 }}개 장소</span>
          </h2>
        </div>

        <div v-if="!plan.waypointList || plan.waypointList.length === 0" class="empty-waypoints">
          <div class="empty-content">
            <span class="icon-map-pin"></span>
            <h3>등록된 장소가 없습니다</h3>
            <p>여행 계획을 수정하여 장소를 추가해보세요.</p>
          </div>
        </div>

        <div v-else class="waypoints-timeline">
          <div
            v-for="(waypoint, index) in plan.waypointList"
            :key="waypoint.id"
            class="timeline-item"
          >
            <div class="timeline-marker">
              <div class="marker-number">{{ index + 1 }}</div>
              <div class="timeline-line" v-if="index < plan.waypointList.length - 1"></div>
            </div>

            <div class="waypoint-card">
              <div class="waypoint-header">
                <div class="waypoint-name">{{ waypoint.title }}</div>
                <div class="waypoint-category">{{ waypoint.contentTypeName }}</div>
              </div>

              <div class="waypoint-details">
                <div class="waypoint-address">
                  <span class="icon-location"></span>
                  {{ waypoint.addr1 }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 로딩 상태 -->
    <div v-else class="loading-container">
      <div class="loading-spinner"></div>
      <p>여행 계획을 불러오는 중...</p>
    </div>

    <!-- 삭제 확인 모달 -->
    <div v-if="showDeleteModal" class="modal-overlay" @click="closeDeleteModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>여행 계획 삭제</h3>
          <button class="modal-close" @click="closeDeleteModal">×</button>
        </div>
        <div class="modal-body">
          <p>이 여행 계획을 영구적으로 삭제하시겠습니까?</p>
          <p class="warning-text">이 작업은 되돌릴 수 없습니다.</p>
        </div>
        <div class="modal-footer">
          <button class="button button-secondary" @click="closeDeleteModal">
            취소
          </button>
          <button class="button button-danger" @click="deletePlan">
            삭제
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useRouter } from 'vue-router'
import api from '@/api/axios.js'

// 상태 관리
const serverUrl = import.meta.env.VITE_API_SERVER_URL
const route = useRoute();
const router = useRouter();
const plan = ref(null)
const showDeleteModal = ref(false)

// 여행 계획 데이터 로드
async function loadPlanData() {
   try {
     const response = await api.get(`${serverUrl}/api/v1/plans/${route.params.id}`);
     console.log(response.data)
     plan.value = response.data
  } catch (error) {
    console.error('데이터 로드 실패:', error)
    alert('여행 계획을 불러오는 중 오류가 발생했습니다.')
  }
}

// 수정 페이지로 이동
function goToEdit() {
  console.log('수정 페이지로 이동')
  router.push(`/plan/update/${plan.value.id}`)
}

function goToMyPlans() {
  router.push('/my-plans')
}

// 삭제 확인
function confirmDelete() {
  showDeleteModal.value = true
}

// 삭제 모달 닫기
function closeDeleteModal() {
  showDeleteModal.value = false
}

// 여행 계획 삭제
async function deletePlan() {
  try {
    await api.delete(`${serverUrl}/api/v1/plans/${plan.value.id}`)
    alert('여행 계획이 삭제되었습니다.')
    showDeleteModal.value = false
    router.back();
    console.log('목록 페이지로 이동')
  } catch (error) {
    console.error('삭제 실패:', error)
    alert('삭제 중 오류가 발생했습니다.')
  }
}

// 날짜 포맷팅
function formatDate(dateString) {
  if (!dateString) return '-'

  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')

  return `${year}.${month}.${day}`
}

// 날짜시간 포맷팅
function formatDateTime(dateString) {
  if (!dateString) return '-'

  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')

  return `${year}.${month}.${day} ${hours}:${minutes}`
}

// 여행 기간 계산
function calculateDuration(startDate, endDate) {
  if (!startDate || !endDate) return 0

  const start = new Date(startDate)
  const end = new Date(endDate)
  const diffTime = Math.abs(end - start)
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1

  return diffDays
}

// 컴포넌트 마운트 시 데이터 로드
onMounted(() => {
  console.log('PlanDetailPage - onMounted')
  loadPlanData()
})
</script>

<style scoped>
/* 기본 스타일 및 리셋 */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

/* 페이지 레이아웃 */
.plan-detail-page {
  padding: 24px;
  max-width: 1000px;
  margin: 0 auto;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  color: #333;
  background-color: #f8f9fa;
  min-height: 100vh;
}

/* 헤더 스타일 */
.plan-detail-header {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #333;
  margin: 0 0 8px;
}

.plan-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.plan-dates {
  font-weight: 600;
  color: #4a6bdf;
}

.last-updated {
  font-size: 0.85rem;
  color: #6c757d;
}

.header-actions {
  display: flex;
  gap: 12px;
}

/* 콘텐츠 레이아웃 */
.plan-detail-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 카드 공통 스타일 */
.info-card,
.waypoints-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

.card-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e9ecef;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e9ecef;
}

.waypoint-count {
  font-size: 0.9rem;
  font-weight: normal;
  color: #6c757d;
  margin-left: 8px;
}

/* 정보 그리드 */
.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-item.full-width {
  grid-column: 1 / -1;
}

.info-label {
  font-size: 0.9rem;
  font-weight: 500;
  color: #495057;
}

.info-value {
  font-size: 1rem;
  color: #333;
}

.info-value.description {
  line-height: 1.6;
  color: #495057;
}

.duration {
  color: #6c757d;
  font-size: 0.9rem;
}

/* 타임라인 스타일 */
.waypoints-timeline {
  position: relative;
}

.timeline-item {
  display: flex;
  margin-bottom: 24px;
  position: relative;
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-marker {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 20px;
  position: relative;
}

.marker-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #4a6bdf;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.9rem;
  z-index: 2;
}

.timeline-line {
  width: 2px;
  height: 60px;
  background-color: #e9ecef;
  margin-top: 8px;
}

.waypoint-card {
  flex: 1;
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  border-left: 4px solid #4a6bdf;
}

.waypoint-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.waypoint-name {
  font-weight: 600;
  font-size: 1.1rem;
  color: #333;
}

.waypoint-category {
  background-color: #e9ecef;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  color: #495057;
}

.waypoint-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.waypoint-address {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #6c757d;
  font-size: 0.9rem;
}

.duration {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.85rem;
  color: #495057;
}

/* 빈 상태 */
.empty-waypoints {
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px dashed #dee2e6;
  padding: 40px 24px;
  text-align: center;
}

.empty-content h3 {
  font-size: 1.1rem;
  font-weight: 600;
  color: #495057;
  margin: 12px 0 8px;
}

.empty-content p {
  color: #6c757d;
  margin: 0;
}

/* 로딩 상태 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 24px;
  text-align: center;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #4a6bdf;
  border-radius: 50%;
  margin-bottom: 16px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 버튼 스타일 */
.button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  outline: none;
}

.button-secondary {
  background-color: #f0f0f0;
  color: #333;
}

.button-secondary:hover {
  background-color: #e0e0e0;
}

.button-danger {
  background-color: #f8d7da;
  color: #dc3545;
}

.button-danger:hover {
  background-color: #f5c6cb;
}

/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  max-width: 400px;
  width: 100%;
  margin: 20px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px 16px;
  border-bottom: 1px solid #e9ecef;
}

.modal-header h3 {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
}

.modal-close {
  background: transparent;
  border: none;
  font-size: 1.5rem;
  font-weight: 700;
  line-height: 1;
  color: #000;
  opacity: 0.5;
  cursor: pointer;
}

.modal-close:hover {
  opacity: 0.75;
}

.modal-body {
  padding: 20px 24px;
}

.modal-body p {
  margin: 0 0 8px;
}

.warning-text {
  color: #dc3545;
  font-size: 0.9rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px 20px;
  border-top: 1px solid #e9ecef;
}

/* 아이콘 스타일 */
.icon-edit::before {
  content: '✏️';
  margin-right: 6px;
}

.icon-back::before {
  content: '⬅️';
  margin-right: 6px;
}

.icon-trash::before {
  content: '🗑️';
  margin-right: 6px;
}

.icon-map-pin::before {
  content: '📍';
  font-size: 2rem;
}

.icon-location::before {
  content: '📍';
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .plan-detail-page {
    padding: 16px;
  }

  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .header-actions {
    width: 100%;
  }

  .button {
    flex: 1;
  }

  .plan-meta {
    flex-wrap: wrap;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .timeline-item {
    flex-direction: column;
  }

  .timeline-marker {
    flex-direction: row;
    margin-right: 0;
    margin-bottom: 12px;
  }

  .timeline-line {
    width: 60px;
    height: 2px;
    margin-top: 0;
    margin-left: 8px;
  }
}
</style>
