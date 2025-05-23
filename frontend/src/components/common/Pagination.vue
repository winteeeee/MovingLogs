<template>
  <div class="pagination">
    <button @click="changePage(current - 1)" :disabled="current === 1">이전</button>

    <button
      v-for="page in total"
      :key="page"
      @click="changePage(page)"
      :class="{ active: page === current }"
    >
      {{ page }}
    </button>

    <button @click="changePage(current + 1)" :disabled="current === total">다음</button>
  </div>
</template>

<script setup>
const props = defineProps({
  total: Number,
  current: Number,
})

const emit = defineEmits(['page-change'])

function changePage(page) {
  if (page >= 1 && page <= props.total) {
    emit('page-change', page)
  }
}
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 3%;
  gap: 8px;
}

.pagination button {
  padding: 8px 12px;
  border: none;
  background-color: #f1f1f1;
  color: #333;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.pagination button:hover {
  background-color: #e0e0e0;
}

.pagination button.active {
  background-color: #4a6bdf;
  color: white;
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
