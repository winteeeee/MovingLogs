<template>
  <div class="post-write-container">
    <h1 class="write-title">{{ isEditing ? '게시글 수정' : '새 게시글 작성' }}</h1>

    <div class="form-container">

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
        <label for="post-title">머리말</label>
        <textarea
          type="text"
          id="post-description"
          v-model="postData.description"
          class="form-control"
          placeholder="머리말을 입력하세요"
        />
      </div>

      <div class="form-group">
        <label for="post-content">내용</label>
        <CKEditor :value="initContent" @change="onEditorChange" @ready="onEditorReady" />
      </div>

      <TripostWriteWaypointsCard
        :waypoints="postData.waypoints"
      />


      <TripostWriteRouteImageUploader
        :waypoints="postData.waypoints"
        @image-added="handleRouteImageAdded"
        @image-removed="handleRouteImageRemoved"
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
import { useRouter } from 'vue-router';
import TripostWriteWaypointsCard from '@/components/tripost-write/TripostWriteWaypointsCard.vue'
import TripostWriteRouteImageUploader from '@/components/tripost-write/TripostWriteRouteImageUploader.vue'
import CKEditor from '@/components/common/CKEditor.vue'
import api from '@/api/axios.js'

const router = useRouter();

const plan = ref({})
const isEditing = ref(false)
const isAdmin = ref(false) // 실제 구현 시 사용자 권한에 따라 설정
const initContent = ref("")

const postData = reactive({
  id: '',
  title: '',
  description: '',
  content: '',
  waypoints: [],
})


const props = defineProps({
  planId: {
    type: String,
    default: null,
  },
  tripostId: {
    type: String,
    default: null,
  },
})

onMounted(async () => {
  console.log(props);

  if (props.planId) {
    await loadPlan(props.planId);
  } else if (props.tripostId) {
    await loadTripost(props.tripostId)
  } else {

  }
});

async function loadPlan(planId) {
  // 기존 여행 정보 가져오기
  const response = await api.get(`/api/v1/plans/${planId}`);
  plan.value = response.data;

  postData.waypoints = plan.value.waypointList.map((waypoint) => {
    return {
      attractionId: waypoint.id.id,
      attractionName: waypoint.title,
      addr1: waypoint.addr1,
      contentTypeName: waypoint.contentTypeName,
      latitude: waypoint.latitude,
      longitude: waypoint.longitude,
      images: []
    }
  });

  console.log(plan.value)
  console.log(postData.waypoints)
}

async function loadTripost(tripostId) {
  // 기존 게시글 가져오기
  const response = await api.get(`/api/v1/triposts/${tripostId}`);
  Object.assign(postData, response.data.tripost);
  postData.waypoints.forEach(waypoint => {
    if (!waypoint.images) {
      waypoint.images = [];
    }
  });

  initContent.value = postData.content;
  isEditing.value = true;
}

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
      id: '',
      title: '',
      description: '',
      content: '',
      waypoints: [],
    }

    Object.assign(postData, dummyPost)
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

function handleRouteImageAdded({ waypointIndex, image }) {
  postData.waypoints[waypointIndex].images.push(image)
  console.log(`이미지 추가됨: ${waypointIndex}`, image)
}

function handleRouteImageRemoved({ waypointIndex, index, image }) {
  // 이미지 제거 이벤트 처리
  postData.waypoints[waypointIndex].images.splice(index, 1);
  console.log(`이미지 제거됨: ${waypointIndex}`, image)
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

async function submitPost() {
  // 유효성 검사
  if (!postData.title.trim()) {
    alert('제목을 입력해주세요.')
    return
  }

  if (!postData.description.trim()) {
    alert('머리말을 입력해주세요.')
    return
  }

  if (!postData.content.trim()) {
    alert('내용을 입력해주세요.')
    return
  }

  const waypoints = postData.waypoints.map((waypoint, index) => {
    const images = waypoint.images.map((image, index) => {
      return {
        imageId: image.imageId,
        seq: index
      }
    })
    return {
      id: waypoint.id,
      attractionId: waypoint.attractionId,
      latitude: waypoint.latitude,
      longitude: waypoint.longitude,
      images: images,
      seq: index,
    }
  })

  const data = {
    title: postData.title,
    description: postData.description,
    content: postData.content,
    waypoints: waypoints,
  }

  if (isEditing.value) {
    try {
      const response = await api.put(`/api/v1/triposts/${postData.id}`, data);
      console.log(response.data)
      alert('게시글이 수정되었습니다.');
      router.push({ name: 'TripostBoardPage' });
    } catch (e) {
      console.error('Error:', e);
      alert('게시글 등록 중 오류가 발생했습니다.');
    }
  } else {
    try {
      const response = await api.post(`/api/v1/triposts`, data);
      console.log(response.data)
      alert('게시글이 등록되었습니다.');
      router.push({ name: 'TripostBoardPage' });
    } catch (e) {
      console.error('Error:', e);
      alert('게시글 등록 중 오류가 발생했습니다.');
    }
  }
}
</script>

<style scoped>
.post-write-container {
  max-width: 1200px;
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
