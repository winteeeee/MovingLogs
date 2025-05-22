<template>
  <div class="plan-basic-info">
    <h2 class="section-title">기본 정보</h2>

    <div class="form-container">
      <div class="form-group">
        <label for="plan-title" class="form-label">여행 제목</label>
        <input
          type="text"
          id="plan-title"
          class="form-input"
          v-model="formData.title"
          placeholder="여행 제목을 입력하세요"
          @input="updatePlan"
        />
      </div>

      <div class="form-row">
        <div class="form-group">
          <label for="plan-start-date" class="form-label">시작일</label>
          <input
            type="date"
            id="plan-start-date"
            class="form-input"
            v-model="formData.startDate"
            @change="updatePlan"
          />
        </div>

        <div class="form-group">
          <label for="plan-end-date" class="form-label">종료일</label>
          <input
            type="date"
            id="plan-end-date"
            class="form-input"
            v-model="formData.endDate"
            @change="updatePlan"
          />
        </div>
      </div>

      <div class="form-group">
        <label for="plan-destination" class="form-label">여행지</label>
        <input
          type="text"
          id="plan-destination"
          class="form-input"
          v-model="formData.destination"
          placeholder="주요 여행지를 입력하세요"
          @input="updatePlan"
        />
      </div>

      <div class="form-group">
        <label for="plan-description" class="form-label">여행 설명</label>
        <textarea
          id="plan-description"
          class="form-textarea"
          v-model="formData.description"
          placeholder="여행에 대한 간단한 설명을 입력하세요"
          rows="3"
          @input="updatePlan"
        ></textarea>
      </div>

      <div class="form-group">
        <label for="plan-thumbnail" class="form-label">대표 이미지</label>
        <div class="thumbnail-container">
          <img
            v-if="formData.thumbnail"
            :src="formData.thumbnail"
            alt="여행 대표 이미지"
            class="thumbnail-preview"
          />
          <div v-else class="thumbnail-placeholder">
            <span class="icon-image"></span>
            <span>이미지 없음</span>
          </div>

          <div class="thumbnail-actions">
            <button class="button button-secondary button-sm" @click="selectImage">
              <span class="icon-upload"></span> 이미지 변경
            </button>
            <button
              v-if="formData.thumbnail"
              class="button button-danger button-sm"
              @click="removeImage"
            >
              <span class="icon-trash"></span> 삭제
            </button>
          </div>

          <!-- 실제 파일 입력은 숨김 처리 -->
          <input
            type="file"
            ref="fileInput"
            class="hidden-file-input"
            accept="image/*"
            @change="handleFileChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'

const props = defineProps({
  plan: {
    type: Object,
    default: null,
  },
})

const emit = defineEmits(['update:plan'])

// 파일 입력 참조
const fileInput = ref(null)

// 폼 데이터
const formData = reactive({
  title: '',
  startDate: '',
  endDate: '',
  destination: '',
  description: '',
  thumbnail: '',
})

// props 변경 감지
watch(
  () => props.plan,
  (newPlan) => {
    if (newPlan) {
      formData.title = newPlan.title || ''
      formData.startDate = newPlan.startDate || ''
      formData.endDate = newPlan.endDate || ''
      formData.destination = newPlan.destination || ''
      formData.description = newPlan.description || ''
      formData.thumbnail = newPlan.thumbnail || ''
    }
  },
  { immediate: true, deep: true },
)

// 계획 업데이트
function updatePlan() {
  emit('update:plan', {
    title: formData.title,
    startDate: formData.startDate,
    endDate: formData.endDate,
    destination: formData.destination,
    description: formData.description,
    thumbnail: formData.thumbnail,
  })
}

// 이미지 선택 버튼 클릭
function selectImage() {
  fileInput.value.click()
}

// 파일 변경 처리
function handleFileChange(event) {
  const file = event.target.files[0]
  if (!file) return

  // 이미지 파일 유효성 검사
  if (!file.type.startsWith('image/')) {
    alert('이미지 파일만 업로드 가능합니다.')
    return
  }

  // 파일 크기 제한 (5MB)
  if (file.size > 5 * 1024 * 1024) {
    alert('파일 크기는 5MB 이하여야 합니다.')
    return
  }

  // 실제 구현: 파일 업로드 API 호출
  // 여기서는 로컬 URL 생성으로 시뮬레이션
  const reader = new FileReader()
  reader.onload = (e) => {
    formData.thumbnail = e.target.result
    updatePlan()
  }
  reader.readAsDataURL(file)

  // 파일 입력 초기화
  event.target.value = ''
}

// 이미지 삭제
function removeImage() {
  formData.thumbnail = ''
  updatePlan()
}
</script>

<style scoped>
/* 섹션 스타일 */
.plan-basic-info {
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

/* 폼 스타일 */
.form-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-row {
  display: flex;
  gap: 16px;
}

.form-group {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-label {
  font-size: 0.9rem;
  font-weight: 500;
  color: #495057;
}

.form-input,
.form-textarea {
  padding: 10px 12px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 0.95rem;
  transition:
    border-color 0.2s,
    box-shadow 0.2s;
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: #4a6bdf;
  box-shadow: 0 0 0 2px rgba(74, 107, 223, 0.25);
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

/* 썸네일 스타일 */
.thumbnail-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.thumbnail-preview {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #e9ecef;
}

.thumbnail-placeholder {
  width: 100%;
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
  border: 1px dashed #ced4da;
  border-radius: 4px;
  color: #6c757d;
  gap: 8px;
}

.thumbnail-actions {
  display: flex;
  gap: 8px;
}

.hidden-file-input {
  display: none;
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

.button-sm {
  padding: 6px 12px;
  font-size: 0.85rem;
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
  color: #721c24;
}

.button-danger:hover {
  background-color: #f5c6cb;
}

/* 아이콘 스타일 */
.icon-image::before {
  content: '🖼️';
  font-size: 2rem;
}

.icon-upload::before {
  content: '📤';
  margin-right: 6px;
}

.icon-trash::before {
  content: '🗑️';
  margin-right: 6px;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .form-row {
    flex-direction: column;
    gap: 16px;
  }
}
</style>
