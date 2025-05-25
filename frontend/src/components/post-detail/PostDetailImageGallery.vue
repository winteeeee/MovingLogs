<!-- TODO 모달 닫기, 이전, 다음 버튼 가시성 개선 -->
<template>
  <div class="image-gallery" v-if="images && images.length > 0">
    <h3 v-if="title" class="gallery-title">{{ title }}</h3>

    <div class="gallery-grid" :class="{ 'single-image': images.length === 1 }">
      <div
        v-for="(image, index) in displayImages"
        :key="index"
        class="gallery-item"
        @click="openImageViewer(index)"
      >
        <img :src="serverUrl + image.thumbnailUrl" class="gallery-image" />
        <div class="image-overlay">
        </div>
      </div>

      <div
        v-if="images.length > maxDisplay"
        class="more-images"
        @click="openImageViewer(maxDisplay)"
      >
        <div class="more-count">+{{ images.length - maxDisplay }}</div>
      </div>
    </div>

    <!-- 이미지 뷰어 모달 -->
    <div class="image-viewer" v-if="showViewer">
      <div class="viewer-overlay" @click="closeImageViewer"></div>
      <div class="viewer-container">
        <!-- 개선된 X 버튼 (모달 닫기) -->
        <button class="close-viewer" @click="closeImageViewer" aria-label="모달 닫기">
          <i class="bi bi-x-lg"></i>
        </button>

        <div class="viewer-content">
          <button
            v-if="images.length > 1"
            class="nav-btn prev-btn"
            @click.stop="prevImage"
            :disabled="currentImageIndex === 0"
          >
            <i class="bi bi-chevron-left"></i>
          </button>

          <div class="viewer-image-container">
            <img :src="serverUrl + currentImage.imageUrl" :alt="currentImage.alt || '이미지'" class="viewer-image" />
          </div>

          <button
            v-if="images.length > 1"
            class="nav-btn next-btn"
            @click.stop="nextImage"
            :disabled="currentImageIndex === images.length - 1"
          >
            <i class="bi bi-chevron-right"></i>
          </button>
        </div>

        <div class="viewer-info">
          <div class="viewer-location" v-if="currentImage.location">
            <i class="bi bi-geo-alt"></i> {{ getLocationName(currentImage.location) }}
          </div>
          <div class="viewer-counter">{{ currentImageIndex + 1 }} / {{ images.length }}</div>
        </div>

        <div class="viewer-thumbnails" v-if="images.length > 1">
          <div
            v-for="(image, index) in images"
            :key="index"
            class="thumbnail-item"
            :class="{ active: index === currentImageIndex }"
            @click.stop="setCurrentImage(index)"
          >
            <img :src="serverUrl + image.thumbnailUrl" class="thumbnail-image" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, defineProps } from 'vue'
const serverUrl = import.meta.env.VITE_API_SERVER_URL

const props = defineProps({
  images: {
    type: Array,
    required: true,
  },
  title: {
    type: String,
    default: '',
  },
  maxDisplay: {
    type: Number,
    default: 5,
  },
})

const showViewer = ref(false)
const currentImageIndex = ref(0)

const displayImages = computed(() => {
  return props.images.slice(0, props.maxDisplay)
})

const currentImage = computed(() => {
  return props.images[currentImageIndex.value] || {}
})

function openImageViewer(index) {
  currentImageIndex.value = index
  showViewer.value = true
  document.body.style.overflow = 'hidden' // 스크롤 방지
}

function closeImageViewer() {
  showViewer.value = false
  document.body.style.overflow = '' // 스크롤 복원
}

function prevImage() {
  if (currentImageIndex.value > 0) {
    currentImageIndex.value--
  }
}

function nextImage() {
  if (currentImageIndex.value < props.images.length - 1) {
    currentImageIndex.value++
  }
}

function setCurrentImage(index) {
  currentImageIndex.value = index
}

function getLocationName(location) {
  // 실제 구현 시 좌표를 주소로 변환하는 로직 추가
  // 여기서는 간단히 좌표만 표시
  if (typeof location === 'string') {
    return location
  } else if (location && location.lat && location.lng) {
    return `${location.lat.toFixed(4)}, ${location.lng.toFixed(4)}`
  }
  return '위치 정보 없음'
}

function handleKeyDown(e) {
  if (!showViewer.value) return

  if (e.key === 'Escape') {
    closeImageViewer()
  } else if (e.key === 'ArrowLeft') {
    prevImage()
  } else if (e.key === 'ArrowRight') {
    nextImage()
  }
}

onMounted(() => {
  // 키보드 이벤트 리스너 등록
  window.addEventListener('keydown', handleKeyDown)
})

onBeforeUnmount(() => {
  // 키보드 이벤트 리스너 제거
  window.removeEventListener('keydown', handleKeyDown)
})
</script>

<style scoped>
.image-gallery {
  margin: 10px 0;
  background-color: #f8f9fa;
  padding: 0.8rem 1.4rem;
  border-radius: 0.7rem;
  border-left: 4px solid #fd7e14;
}

.gallery-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  color: #333;
}

.gallery-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 10px;
}

.gallery-item {
  position: relative;
  border-radius: 4px;
  overflow: hidden;
  aspect-ratio: 4/3;
  cursor: pointer;
}

.gallery-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.gallery-item:hover .gallery-image {
  transform: scale(1.05);
}

.image-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  padding: 10px;
  color: white;
}

.image-alt {
  font-size: 14px;
  margin-bottom: 5px;
}

.image-location {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 5px;
  opacity: 0.8;
}

.more-images {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  aspect-ratio: 4/3;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.more-count {
  font-size: 24px;
  font-weight: 700;
  color: white;
}

/* 이미지 뷰어 스타일 */
.image-viewer {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.viewer-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.9);
}

.viewer-container {
  position: relative;
  width: 90%;
  max-width: 1200px;
  height: 90%;
  z-index: 1001;
  display: flex;
  flex-direction: column;
}

/* 개선된 닫기 버튼 스타일 */
.close-viewer {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  font-size: 24px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1002;
  transition:
    background-color 0.2s,
    transform 0.2s;
}

.close-viewer:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

.viewer-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.viewer-image-container {
  height: 100%;
  max-height: 1000px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.viewer-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.nav-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.nav-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.nav-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.prev-btn {
  left: 15px;
}

.next-btn {
  right: 15px;
}

.viewer-info {
  padding: 15px;
  color: white;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.viewer-caption {
  flex: 1;
  font-size: 16px;
}

.viewer-location {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
  opacity: 0.8;
}

.viewer-counter {
  font-size: 14px;
  opacity: 0.8;
}

.viewer-thumbnails {
  display: flex;
  gap: 10px;
  padding: 15px;
  overflow-x: auto;
}

.thumbnail-item {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  opacity: 0.6;
  transition: opacity 0.2s;
}

.thumbnail-item.active {
  opacity: 1;
  border: 2px solid #fd7e14;
}

.thumbnail-item:hover {
  opacity: 0.8;
}

.thumbnail-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

@media (max-width: 768px) {
  .gallery-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }

  .nav-btn {
    width: 30px;
    height: 30px;
    font-size: 16px;
  }

  .viewer-caption {
    font-size: 14px;
  }

  .thumbnail-item {
    width: 50px;
    height: 50px;
  }
}
</style>
