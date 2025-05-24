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

// 폼 데이터
const formData = reactive({
  title: '',
  startDate: '',
  endDate: '',
  description: '',
})

// props 변경 감지
watch(
  () => props.plan,
  (newPlan) => {
    if (newPlan) {
      formData.title = newPlan.title || ''
      formData.startDate = newPlan.startDate || ''
      formData.endDate = newPlan.endDate || ''
      formData.description = newPlan.description || ''
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
    description: formData.description,
  })
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

/* 반응형 디자인 */
@media (max-width: 768px) {
  .form-row {
    flex-direction: column;
    gap: 16px;
  }
}
</style>
