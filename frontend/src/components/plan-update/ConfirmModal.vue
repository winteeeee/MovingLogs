<template>
  <div class="confirm-modal">
    <div class="modal-backdrop" @click="$emit('close')"></div>
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{ title }}</h5>
          <button type="button" class="modal-close" @click="$emit('close')">×</button>
        </div>
        <div class="modal-body">
          <p>{{ message }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="button button-secondary" @click="$emit('close')">
            {{ cancelText }}
          </button>
          <button
            type="button"
            :class="['button', confirmType === 'danger' ? 'button-danger' : 'button-primary']"
            @click="$emit('confirm')"
          >
            {{ confirmText }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount } from 'vue'

defineProps({
  title: {
    type: String,
    default: '확인',
  },
  message: {
    type: String,
    required: true,
  },
  confirmText: {
    type: String,
    default: '확인',
  },
  cancelText: {
    type: String,
    default: '취소',
  },
  confirmType: {
    type: String,
    default: 'primary', // 'primary' 또는 'danger'
  },
})

defineEmits(['close', 'confirm'])

// 모달 표시 시 body 스크롤 방지
onMounted(() => {
  document.body.style.overflow = 'hidden'
})

// 모달 닫을 때 body 스크롤 복원
onBeforeUnmount(() => {
  document.body.style.overflow = ''
})
</script>

<style scoped>
/* 모달 스타일 */
.confirm-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: -1;
}

.modal-dialog {
  width: 100%;
  max-width: 500px;
  margin: 1.75rem auto;
  position: relative;
}

.modal-content {
  position: relative;
  display: flex;
  flex-direction: column;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  outline: 0;
  animation: modal-show 0.3s ease;
}

@keyframes modal-show {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  border-bottom: 1px solid #e9ecef;
}

.modal-title {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
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
  position: relative;
  flex: 1 1 auto;
  padding: 16px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  padding: 16px;
  border-top: 1px solid #e9ecef;
  gap: 8px;
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

.button-primary {
  background-color: #4a6bdf;
  color: white;
}

.button-primary:hover {
  background-color: #3a5bc9;
}

.button-secondary {
  background-color: #f0f0f0;
  color: #333;
}

.button-secondary:hover {
  background-color: #e0e0e0;
}

.button-danger {
  background-color: #dc3545;
  color: white;
}

.button-danger:hover {
  background-color: #c82333;
}

/* 반응형 디자인 */
@media (max-width: 576px) {
  .modal-dialog {
    margin: 1rem;
  }
}
</style>
