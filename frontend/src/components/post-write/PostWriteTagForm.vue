<template>
  <div class="form-section">
    <h3>태그</h3>
    <div class="tags-input-container">
      <div class="tags-input">
        <div v-for="(tag, index) in tags" :key="index" class="tag-item">
          <span class="tag-text">#{{ tag }}</span>
          <button type="button" class="remove-tag-btn" @click="removeTag(index)">
            <i class="bi bi-x"></i>
          </button>
        </div>
        <input
          type="text"
          v-model="tagInput"
          @keydown.enter.prevent="addTag"
          @keydown.space.prevent="addTag"
          @keydown.comma.prevent="addTag"
          placeholder="태그 입력 후 Enter"
          class="tag-input"
        />
      </div>
      <div class="tag-suggestions" v-if="tagSuggestions.length > 0">
        <div
          v-for="suggestion in tagSuggestions"
          :key="suggestion"
          class="tag-suggestion"
          @click="selectTagSuggestion(suggestion)"
        >
          #{{ suggestion }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue'

const props = defineProps({
  tags: {
    type: Array,
    required: true,
  },
  popularTags: {
    type: Array,
    default: () => [
      '여행',
      '맛집',
      '숙소',
      '교통',
      '액티비티',
      '힐링',
      '로드트립',
      '백패킹',
      '가족여행',
      '혼자여행',
      '커플여행',
    ],
  },
})

const emit = defineEmits(['add-tag', 'remove-tag'])

const tagInput = ref('')
const tagSuggestions = ref([])

watch(
  () => tagInput.value,
  (val) => {
    if (val.trim()) {
      tagSuggestions.value = props.popularTags
        .filter((tag) => tag.toLowerCase().includes(val.toLowerCase()) && !props.tags.includes(tag))
        .slice(0, 5)
    } else {
      tagSuggestions.value = []
    }
  },
)

function addTag() {
  const tag = tagInput.value.trim()
  if (tag && !props.tags.includes(tag)) {
    emit('add-tag', tag)
  }
  tagInput.value = ''
}

function removeTag(index) {
  emit('remove-tag', index)
}

function selectTagSuggestion(tag) {
  if (!props.tags.includes(tag)) {
    emit('add-tag', tag)
  }
  tagInput.value = ''
}
</script>

<style scoped>
.form-section {
  margin-bottom: 30px;
  border-top: 1px solid #e9ecef;
  padding-top: 20px;
}

.form-section h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  color: #333;
}

.tags-input-container {
  position: relative;
}

.tags-input {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 10px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  min-height: 50px;
}

.tag-item {
  display: flex;
  align-items: center;
  background-color: #e9ecef;
  border-radius: 4px;
  padding: 5px 10px;
  font-size: 14px;
}

.tag-text {
  margin-right: 5px;
}

.remove-tag-btn {
  background: none;
  border: none;
  color: #6c757d;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tag-input {
  flex: 1;
  min-width: 100px;
  border: none;
  outline: none;
  font-size: 14px;
  padding: 5px;
}

.tag-suggestions {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background-color: white;
  border: 1px solid #ced4da;
  border-top: none;
  border-radius: 0 0 4px 4px;
  z-index: 10;
  max-height: 150px;
  overflow-y: auto;
}

.tag-suggestion {
  padding: 8px 12px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.tag-suggestion:hover {
  background-color: #f8f9fa;
}
</style>
