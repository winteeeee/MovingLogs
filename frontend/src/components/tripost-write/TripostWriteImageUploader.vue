<template>
  <div class="image-uploader">
    <div class="image-preview-container">
      <div v-for="(image, index) in images" :key="index" class="image-preview-item">
        <img :src="serverUrl + image.imageUrl" :alt="image.alt || '이미지 미리보기'" class="preview-image" />
        <div class="image-actions">
          <button type="button" class="remove-image-btn" @click="removeImage(index, image)">
            <i class="bi bi-trash"></i>
          </button>
        </div>
        <div class="image-info">
          <div v-if="image.alt" class="image-alt">{{ image.alt }}</div>
          <div v-if="image.location" class="image-location">
            <i class="bi bi-geo-alt"></i> {{ getLocationName(image.location) }}
          </div>
        </div>
      </div>

      <div class="image-upload-box" @click="triggerFileInput">
        <input
          type="file"
          ref="fileInput"
          accept="image/*"
          multiple
          @change="handleFileChange"
          class="file-input"
        />
        <div class="upload-icon">
          <i class="bi bi-plus-lg"></i>
        </div>
        <div class="upload-text">이미지 추가</div>
      </div>
    </div>

    <div v-if="showImageForm" class="image-form">
      <div class="form-group">
        <label for="image-alt">이미지 설명</label>
        <input
          type="text"
          id="image-alt"
          v-model="imageForm.alt"
          placeholder="이미지에 대한 설명을 입력하세요"
          class="form-control"
        />
      </div>

      <div class="form-group">
        <label for="image-location">위치 정보</label>
        <div class="location-input">
          <input
            type="text"
            id="image-location"
            v-model="imageForm.locationText"
            placeholder="위치 정보를 입력하세요"
            class="form-control"
          />
          <button type="button" class="map-btn" @click="openLocationMap">
            <i class="bi bi-map"></i>
          </button>
        </div>
      </div>

      <div class="form-actions">
        <button type="button" class="cancel-btn" @click="cancelImageForm">취소</button>
        <button type="button" class="save-btn" @click="saveImageForm">저장</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, defineProps, defineEmits } from 'vue'
import api from '@/api/axios.js'

const serverUrl = import.meta.env.VITE_API_SERVER_URL

const props = defineProps({
  images: {
    type: Array,
    default: () => [],
  },
  maxImages: {
    type: Number,
    default: 10,
  },
})

const emit = defineEmits(['image-added', 'image-removed'])

const fileInput = ref(null)
const showImageForm = ref(false)
const editingImageIndex = ref(-1)
const imageForm = reactive({
  alt: '',
  locationText: '',
  location: null,
})

function triggerFileInput() {
  if (props.images.length >= props.maxImages) {
    alert(`최대 ${props.maxImages}개의 이미지만 업로드할 수 있습니다.`)
    return
  }

  fileInput.value.click()
}

async function handleFileChange(event) {
  const files = event.target.files

  if (!files || files.length === 0) return

  // 최대 이미지 개수 제한
  const remainingSlots = props.maxImages - props.images.length
  const filesToProcess = Math.min(files.length, remainingSlots)

  if (files.length > remainingSlots) {
    alert(
      `최대 ${props.maxImages}개의 이미지만 업로드할 수 있습니다. 처음 ${remainingSlots}개만 처리됩니다.`,
    )
  }

  const formData = new FormData();

  for (let i = 0; i < filesToProcess; i++) {
    const file = files[i]

    // 이미지 파일 유효성 검사
    if (!file.type.startsWith('image/')) {
      alert('이미지 파일만 업로드할 수 있습니다.')
      continue
    }

    // 파일 크기 제한 (5MB)
    if (file.size > 5 * 1024 * 1024) {
      alert('파일 크기는 5MB 이하여야 합니다.')
      continue
    }

    formData.append('files', file);
  }

  try {
    const response = await api.post(`/api/v1/images`, formData);

    // TODO 주석 삭제
    console.log(response);

    const uploadedImages = await response.data;

    for (const img of uploadedImages) {
      emit('image-added', {
        imageId: img.imageId,
        imageUrl: img.imageUrl,
        thumbnailUrl: img.thumbnailUrl,
      });
    }
  } catch (error) {
    console.error(error);
    alert('이미지 업로드 중 오류가 발생했습니다.');
  }

  // 파일 입력 초기화
  event.target.value = ''
}

function removeImage(index, image) {
  emit('image-removed', { index, image })
}

function openLocationMap() {
  // 실제 구현 시 지도 모달 열기
  alert('지도에서 위치를 선택할 수 있는 기능은 실제 구현 시 추가됩니다.')
}

function cancelImageForm() {
  showImageForm.value = false
  editingImageIndex.value = -1
  resetImageForm()
}

function saveImageForm() {
  if (editingImageIndex.value >= 0 && editingImageIndex.value < props.images.length) {
    const updatedImage = { ...props.images[editingImageIndex.value] }

    updatedImage.alt = imageForm.alt

    // 위치 정보 처리
    if (imageForm.location) {
      updatedImage.location = imageForm.location
    } else if (imageForm.locationText) {
      // 텍스트로 입력된 위치 정보 처리
      updatedImage.location = imageForm.locationText
    } else {
      updatedImage.location = null
    }

    // 이미지 업데이트 이벤트 발생
    emit('image-removed', {
      index: editingImageIndex.value,
      image: props.images[editingImageIndex.value],
    })
    emit('image-added', updatedImage)
  }

  showImageForm.value = false
  editingImageIndex.value = -1
  resetImageForm()
}

function resetImageForm() {
  imageForm.alt = ''
  imageForm.locationText = ''
  imageForm.location = null
}

function getLocationName(location) {
  if (typeof location === 'string') {
    return location
  } else if (location && location.lat && location.lng) {
    return `${location.lat.toFixed(4)}, ${location.lng.toFixed(4)}`
  }
  return ''
}
</script>

<style scoped>
.image-uploader {
  margin-bottom: 20px;
}

.image-preview-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 10px;
  margin-bottom: 15px;
}

.image-preview-item {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  aspect-ratio: 1/1;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  position: absolute;
  top: 5px;
  right: 5px;
  display: flex;
  gap: 5px;
}

.edit-image-btn,
.remove-image-btn {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.8);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
}

.edit-image-btn:hover,
.remove-image-btn:hover {
  background-color: white;
}

.edit-image-btn {
  color: #495057;
}

.remove-image-btn {
  color: #dc3545;
}

.image-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  padding: 8px;
  color: white;
  font-size: 12px;
}

.image-alt {
  margin-bottom: 3px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.image-location {
  display: flex;
  align-items: center;
  gap: 3px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  opacity: 0.8;
}

.image-upload-box {
  border: 2px dashed #ced4da;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  aspect-ratio: 1/1;
  background-color: #f8f9fa;
  transition: background-color 0.2s;
}

.image-upload-box:hover {
  background-color: #e9ecef;
}

.file-input {
  display: none;
}

.upload-icon {
  font-size: 24px;
  color: #6c757d;
  margin-bottom: 5px;
}

.upload-text {
  font-size: 14px;
  color: #6c757d;
}

.image-form {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 15px;
  margin-top: 15px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #495057;
  font-size: 14px;
}

.form-control {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 14px;
}

.location-input {
  display: flex;
  gap: 10px;
}

.location-input .form-control {
  flex: 1;
}

.map-btn {
  padding: 0 12px;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.cancel-btn {
  padding: 8px 15px;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.save-btn {
  padding: 8px 15px;
  background-color: #fd7e14;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.cancel-btn:hover {
  background-color: #5a6268;
}

.save-btn:hover {
  background-color: #e8590c;
}

@media (max-width: 768px) {
  .image-preview-container {
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }
}
</style>
