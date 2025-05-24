<template>
  <div class="post-write-container">
    <h1 class="write-title">{{ isEditing ? '게시글 수정' : '새 게시글 작성' }}</h1>

    <div class="form-container">
      <div class="form-group">
        <label for="post-category">카테고리</label>
        <select id="post-category" v-model="postData.type" class="form-control">
          <option value="free">자유게시판</option>
          <option value="tips">여행 팁</option>
          <option value="food">현지 맛집</option>
          <option value="accommodation">숙소 추천</option>
          <option value="notice" v-if="isAdmin">공지사항</option>
        </select>
      </div>

      <div class="form-group">
        <label for="post-title">제목</label>
        <input
          type="text"
          id="post-title"
          v-model="postData.title"
          class="form-control"
          placeholder="제목을 입력하세요"
        />
      </div>

      <div class="form-group">
        <label for="post-content">내용</label>
        <CKEditor v-model="postData.content" @change="onEditorChange" @ready="onEditorReady" />
      </div>

      <PostWriteTagForm :tags="postData.tags" @add-tag="addTag" @remove-tag="removeTag" />

      <PostWriteRouteEditor
        :route-data="postData.route"
        v-model:include-route="includeRoute"
        @route-change="updateRoute"
        @update:coordinates="updateCoordinates"
      />

      <PostWriteRouteImageUploader
        v-if="includeRoute"
        :include-route="includeRoute"
        :route-data="postData.route"
        :route-images="routeImages"
        @image-added="handleRouteImageAdded"
        @image-removed="handleRouteImageRemoved"
      />

      <PostWriteImageUploader
        v-if="!includeRoute"
        :images="previewImages"
        @image-added="handleImageAdded"
        @image-removed="handleImageRemoved"
      />

      <div class="form-actions">
        <button type="button" class="cancel-btn" @click="cancelPost">취소</button>
        <button type="button" class="submit-btn" @click="submitPost">
          {{ isEditing ? '수정하기' : '작성하기' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineProps } from 'vue'
import PostWriteTagForm from '@/components/post-write/PostWriteTagForm.vue'
import PostWriteRouteEditor from '@/components/post-write/PostWriteRouteEditor.vue'
import PostWriteRouteImageUploader from '@/components/post-write/PostWriteRouteImageUploader.vue'
import PostWriteImageUploader from '@/components/post-write/PostWriteImageUploader.vue'
import CKEditor from '@/components/common/CKEditor.vue'



const props = defineProps({
  tripId: {
    type: String,
    default: null,
  },
  editPostId: {
    type: Number,
    default: null,
  },
})

onMounted(() => {
  if (props.tripId) {

  } else if (props.editPostId) {

  } else {

  }
});

const isEditing = ref(false)
const isAdmin = ref(false) // 실제 구현 시 사용자 권한에 따라 설정
const includeRoute = ref(false)
const postData = reactive({
  type: 'free',
  title: '',
  content: '',
  route: {
    departure: '',
    destination: '',
    waypoints: [''],
    coordinates: [],
  },
  tags: [],
})
const previewImages = ref([])
const routeImages = reactive({
  departure: [],
  destination: [],
  // waypoint-0, waypoint-1 등은 동적으로 추가됨
})
const editor = ref(null)

onMounted(() => {
  if (props.editPostId) {
    isEditing.value = true
    fetchPostData()
  }
})

function fetchPostData() {
  // API 호출 대신 더미 데이터 사용
  setTimeout(() => {
    // 실제 구현 시 API에서 게시글 데이터를 가져옴
    const dummyPost = {
      id: props.editPostId,
      type: 'free',
      title: '서울에서 부산까지 3박 4일 여행 후기',
      content:
        '<p>서울에서 출발해 부산까지 3박 4일 동안 여행했습니다. 중간에 대전과 경주를 들렀는데, 특히 경주의 역사 유적지가 인상적이었습니다.</p><p>불국사와 석굴암은 꼭 방문해볼 만한 곳입니다. 경주 시내에서는 첨성대와 안압지도 볼 수 있었습니다.</p>',
      route: {
        departure: '서울',
        destination: '부산',
        waypoints: ['대전', '경주'],
        coordinates: [
          { lat: 37.5665, lng: 126.978 }, // 서울
          { lat: 36.3504, lng: 127.3845 }, // 대전
          { lat: 35.8562, lng: 129.2247 }, // 경주
          { lat: 35.1796, lng: 129.0756 }, // 부산
        ],
      },
      tags: ['서울', '부산', '경주', '로드트립', '여행후기'],
    }

    Object.assign(postData, dummyPost)
    includeRoute.value = true
  }, 500)
}

function onEditorChange(content) {
  // 에디터 내용 변경 이벤트 처리
  // console.log(content)
  postData.content = content
}

function onEditorReady(editorInstance) {
  // 에디터 인스턴스 저장
  editor.value = editorInstance
}

function addTag(tag) {
  if (!postData.tags.includes(tag)) {
    postData.tags.push(tag)
  }
}

function removeTag(index) {
  postData.tags.splice(index, 1)
}

function updateRoute(routeData) {
  Object.assign(postData.route, routeData)
}

function updateCoordinates(coordinates) {
  postData.route.coordinates = coordinates
}

function handleRouteImageAdded({ pointType, image }) {
  // 이미지 추가 이벤트 처리
  console.log(`이미지 추가됨: ${pointType}`, image)
}

function handleRouteImageRemoved({ pointType, index, image }) {
  // 이미지 제거 이벤트 처리
  console.log(`이미지 제거됨: ${pointType}`, image)
}

function handleImageAdded(image) {
  // 일반 이미지 추가 이벤트 처리
  console.log('이미지 추가됨:', image)
}

function handleImageRemoved({ index, image }) {
  // 일반 이미지 제거 이벤트 처리
  console.log('이미지 제거됨:', image)
}

function cancelPost() {
  if (confirm('작성 중인 내용이 저장되지 않습니다. 취소하시겠습니까?')) {
    goToBoard()
  }
}

function goToBoard() {
  // 게시판 목록으로 이동
  console.log('게시판으로 이동')
  // router.push({ name: 'BoardMainPage' });
}

function submitPost() {
  // 유효성 검사
  if (!postData.title.trim()) {
    alert('제목을 입력해주세요.')
    return
  }

  if (!postData.content.trim()) {
    alert('내용을 입력해주세요.')
    return
  }

  // 경로 정보 포함 시 유효성 검사
  if (includeRoute.value) {
    if (!postData.route.departure.trim()) {
      alert('출발지를 입력해주세요.')
      return
    }

    if (!postData.route.destination.trim()) {
      alert('도착지를 입력해주세요.')
      return
    }
  }

  // 이미지 업로드 처리
  const formData = new FormData()

  // 게시글 데이터
  formData.append('title', postData.title)
  formData.append('content', postData.content)
  formData.append('type', postData.type)
  formData.append('tags', JSON.stringify(postData.tags))

  // 경로 정보
  if (includeRoute.value) {
    formData.append('route', JSON.stringify(postData.route))

    // 경로별 이미지 처리
    Object.keys(routeImages).forEach((pointType) => {
      routeImages[pointType].forEach((image, index) => {
        formData.append(`routeImages[${pointType}][${index}]`, image.file)
        formData.append(`routeImagesAlt[${pointType}][${index}]`, image.alt || '')
        if (image.location) {
          formData.append(
            `routeImagesLocation[${pointType}][${index}]`,
            JSON.stringify(image.location),
          )
        }
      })
    })
  } else {
    // 일반 이미지 처리
    previewImages.value.forEach((image, index) => {
      formData.append(`images[${index}]`, image.file)
      formData.append(`imagesAlt[${index}]`, image.alt || '')
      if (image.location) {
        formData.append(`imagesLocation[${index}]`, JSON.stringify(image.location))
      }
    })
  }

  // API 호출
  console.log('게시글 제출:', postData)
  console.log('이미지 데이터:', includeRoute.value ? routeImages : previewImages.value)

  // 실제 구현 시 API 호출
  // const url = isEditing.value ? `/api/posts/${props.editPostId}` : '/api/posts';
  // const method = isEditing.value ? 'PUT' : 'POST';

  // fetch(url, {
  //   method: method,
  //   body: formData
  // })
  // .then(response => response.json())
  // .then(data => {
  //   alert(isEditing.value ? '게시글이 수정되었습니다.' : '게시글이 등록되었습니다.');
  //   router.push({ name: 'PostDetailPage', params: { id: data.id } });
  // })
  // .catch(error => {
  //   console.error('Error:', error);
  //   alert('게시글 등록 중 오류가 발생했습니다.');
  // });

  // 임시 처리
  setTimeout(() => {
    alert(isEditing.value ? '게시글이 수정되었습니다.' : '게시글이 등록되었습니다.')
    goToBoard()
  }, 1000)
}
</script>

<style scoped>
.post-write-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Noto Sans KR', sans-serif;
}

.write-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 30px;
  color: #333;
}

.form-container {
  background-color: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 25px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #495057;
}

.form-control {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 16px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 30px;
}

.cancel-btn {
  padding: 10px 20px;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.submit-btn {
  padding: 10px 20px;
  background-color: #fd7e14;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #e8590c;
}

.cancel-btn:hover {
  background-color: #5a6268;
}

@media (max-width: 768px) {
  .post-write-container {
    padding: 15px;
  }

  .form-container {
    padding: 20px;
  }

  .write-title {
    font-size: 24px;
  }
}
</style>
