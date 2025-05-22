<template>
  <div class="plan-settings">
    <h2 class="section-title">설정</h2>

    <div class="settings-container">
      <div class="setting-item">
        <div class="setting-info">
          <h3 class="setting-title">공개 여부</h3>
          <p class="setting-description">여행 계획을 다른 사용자에게 공개할지 설정합니다.</p>
        </div>

        <div class="setting-control">
          <label class="toggle">
            <input type="checkbox" v-model="isPublic" @change="updateSettings" />
            <span class="toggle-slider"></span>
          </label>
        </div>
      </div>

      <div class="setting-divider"></div>

      <div class="setting-item">
        <div class="setting-info">
          <h3 class="setting-title">알림 설정</h3>
          <p class="setting-description">여행 일정 알림을 받을지 설정합니다.</p>
        </div>

        <div class="setting-control">
          <label class="toggle">
            <input type="checkbox" v-model="notifications" @change="updateSettings" />
            <span class="toggle-slider"></span>
          </label>
        </div>
      </div>

      <div class="setting-divider"></div>

      <div class="setting-item">
        <div class="setting-info">
          <h3 class="setting-title">여행 태그</h3>
          <p class="setting-description">여행 계획에 태그를 추가하여 분류합니다.</p>
        </div>

        <div class="tags-container">
          <div class="tags-list">
            <div v-for="(tag, index) in tags" :key="index" class="tag-item">
              <span class="tag-text">{{ tag }}</span>
              <button class="tag-remove" @click="removeTag(index)">×</button>
            </div>
          </div>

          <div class="tag-input-container">
            <input
              type="text"
              class="tag-input"
              v-model="newTag"
              placeholder="태그 추가..."
              @keydown.enter.prevent="addTag"
            />
            <button class="tag-add-button" @click="addTag" :disabled="!newTag.trim()">추가</button>
          </div>
        </div>
      </div>

      <div class="setting-divider"></div>

      <div class="setting-item">
        <div class="setting-info">
          <h3 class="setting-title danger">여행 계획 삭제</h3>
          <p class="setting-description">
            이 여행 계획을 영구적으로 삭제합니다. 이 작업은 되돌릴 수 없습니다.
          </p>
        </div>

        <div class="setting-control">
          <button class="button button-danger" @click="confirmDelete">삭제</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  plan: {
    type: Object,
    default: null,
  },
})

const emit = defineEmits(['update:plan'])

// 설정 상태
const isPublic = ref(false)
const notifications = ref(false)
const tags = ref([])
const newTag = ref('')

// props 변경 감지
watch(
  () => props.plan,
  (newPlan) => {
    if (newPlan) {
      isPublic.value = newPlan.isPublic || false
      notifications.value = newPlan.notifications || false
      tags.value = newPlan.tags || []
    }
  },
  { immediate: true, deep: true },
)

// 설정 업데이트
function updateSettings() {
  emit('update:plan', {
    isPublic: isPublic.value,
    notifications: notifications.value,
    tags: [...tags.value],
  })
}

// 태그 추가
function addTag() {
  if (!newTag.value.trim()) return

  // 중복 태그 방지
  if (!tags.value.includes(newTag.value.trim())) {
    tags.value.push(newTag.value.trim())
    updateSettings()
  }

  newTag.value = ''
}

// 태그 삭제
function removeTag(index) {
  tags.value.splice(index, 1)
  updateSettings()
}

// 삭제 확인
function confirmDelete() {
  if (confirm('정말 이 여행 계획을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
    console.log('여행 계획 삭제')
    // 실제 구현: API 호출로 삭제 후 목록 페이지로 이동
    // router.push('/travel-plans')
  }
}
</script>

<style scoped>
/* 섹션 스타일 */
.plan-settings {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e9ecef;
}

/* 설정 컨테이너 */
.settings-container {
  display: flex;
  flex-direction: column;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 16px 0;
}

.setting-divider {
  height: 1px;
  background-color: #e9ecef;
  margin: 0;
}

/* 설정 정보 */
.setting-info {
  flex: 1;
  padding-right: 16px;
}

.setting-title {
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 4px;
}

.setting-title.danger {
  color: #dc3545;
}

.setting-description {
  font-size: 0.85rem;
  color: #6c757d;
}

/* 토글 스위치 */
.toggle {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;
}

.toggle input {
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: 0.4s;
  border-radius: 24px;
}

.toggle-slider:before {
  position: absolute;
  content: '';
  height: 18px;
  width: 18px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: 0.4s;
  border-radius: 50%;
}

input:checked + .toggle-slider {
  background-color: #4a6bdf;
}

input:focus + .toggle-slider {
  box-shadow: 0 0 1px #4a6bdf;
}

input:checked + .toggle-slider:before {
  transform: translateX(26px);
}

/* 태그 스타일 */
.tags-container {
  width: 100%;
  margin-top: 8px;
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}

.tag-item {
  display: inline-flex;
  align-items: center;
  background-color: #e9ecef;
  border-radius: 16px;
  padding: 4px 12px;
  font-size: 0.85rem;
}

.tag-text {
  margin-right: 6px;
}

.tag-remove {
  background: none;
  border: none;
  color: #6c757d;
  cursor: pointer;
  font-size: 1rem;
  line-height: 1;
  padding: 0;
}

.tag-remove:hover {
  color: #dc3545;
}

.tag-input-container {
  display: flex;
  gap: 8px;
}

.tag-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 0.9rem;
}

.tag-input:focus {
  outline: none;
  border-color: #4a6bdf;
  box-shadow: 0 0 0 2px rgba(74, 107, 223, 0.25);
}

.tag-add-button {
  padding: 8px 12px;
  background-color: #f0f0f0;
  border: 1px solid #ced4da;
  border-radius: 4px;
  color: #495057;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.2s;
}

.tag-add-button:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.tag-add-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
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

.button-danger {
  background-color: #f8d7da;
  color: #dc3545;
}

.button-danger:hover {
  background-color: #f5c6cb;
}
</style>
