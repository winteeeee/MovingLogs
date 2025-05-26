<template>
  <div class="post-detail-container">
    <div v-if="loading" class="loading-container">
      <div class="spinner"></div>
      <p>게시글을 불러오는 중입니다...</p>
    </div>

    <div v-else-if="!tripost" class="error-container">
      <i class="bi bi-exclamation-triangle"></i>
      <p>게시글을 찾을 수 없습니다.</p>
      <button class="back-btn" @click="goToBoard">게시판으로 돌아가기</button>
    </div>

    <div v-else class="post-content">
      <PostDetailHeader :tripost="tripost" @tag-click="goToTagSearch" />

      <PostDetailRouteInfo
        :waypoints="tripost.waypoints"
        :map-visible="mapVisible"
        @toggle-map="toggleMap"
      />

      <div class="post-body" v-html="tripost.content"></div>

      <PostDetailImageGallery
        v-for="(waypoint, index) in tripost.waypoints"
        :key="waypoint.id"
        :images="waypoint.images"
        :title="`${index+1}. ${waypoint.attractionName}`"
      />


      <div class="post-actions">
        <button class="action-btn" @click="goToBoard"><i class="bi bi-list"></i> 목록</button>
        <div v-if="isAuthor" class="author-actions">
          <button class="action-btn" @click="editPost"><i class="bi bi-pencil"></i> 수정</button>
          <button class="action-btn delete-btn" @click="deletePost">
            <i class="bi bi-trash"></i> 삭제
          </button>
        </div>
      </div>


      <div class="post-reactions">
        <button class="reaction-btn" :class="{ active: liked }" @click="toggleLike">
          <i class="bi" :class="liked ? 'bi-heart-fill' : 'bi-heart'"></i>
          <span class="reaction-count">{{ tripost.likeCount }}</span>
        </button>
        <button class="reaction-btn" @click="scrollToComments">
          <i class="bi bi-chat"></i>
          <span class="reaction-count">{{ tripost.commentCount }}</span>
        </button>
        <button class="reaction-btn" @click="sharePost">
          <i class="bi bi-share"></i>
          <span class="reaction-text">공유</span>
        </button>
      </div>


      <div ref="commentsSection">
        <PostDetailCommentSection
          :tripostId="tripost.id"
          :comments="comments"
          @add-comment="addComment"
          @add-reply="addReply"
          @delete-comment="deleteComment"
          @delete-reply="deleteReply"
        />
      </div>

      <div class="related-posts" v-if="relatedPosts.length > 0">
        <h3 class="related-title">관련 게시글</h3>
        <div class="related-list">
          <div
            v-for="relatedPost in relatedPosts"
            :key="relatedPost.id"
            class="related-item"
            @click="goToPost(relatedPost.id)"
          >
            <div v-if="relatedPost.thumbnail" class="related-thumbnail">
              <img :src="relatedPost.thumbnail" :alt="relatedPost.title" />
            </div>
            <div class="related-info">
              <div class="related-category">{{ getCategoryName(relatedPost.type) }}</div>
              <div class="related-post-title">{{ relatedPost.title }}</div>
              <div class="related-meta">
                <span class="related-author">{{ relatedPost.author.name }}</span>
                <span class="related-date">{{ formatDate(relatedPost.createdAt) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import PostDetailHeader from '../components/post-detail/PostDetailHeader.vue'
import PostDetailRouteInfo from '../components/post-detail/PostDetailRouteInfo.vue'
import PostDetailImageGallery from '../components/post-detail/PostDetailImageGallery.vue'
import PostDetailCommentSection from '../components/post-detail/PostDetailCommentSection.vue'
import api from '@/api/axios.js'

const router = useRouter()

// 상태 정의
const loading = ref(true)
const tripost = reactive({})
const comments = ref([])
const relatedPosts = ref([])
const liked = ref(false)
const mapVisible = ref(false)
const commentsSection = ref(null)
const isAuthor = ref(false) // 실제 구현 시 로그인 사용자와 게시글 작성자 비교

const props = defineProps({
  tripostId: {
    type: String,
    required: true,
  }
})

// 라이프사이클 훅
onMounted(() => {
  fetchTripostData()
  fetchCommentData()
  checkLiked()
})

// 메서드
async function fetchTripostData() {
  const response = await api.get(`/api/v1/triposts/${props.tripostId}`)
  Object.assign(tripost, response.data.tripost)
  isAuthor.value = response.data.author;

  loading.value = false
}

async function fetchCommentData() {
  const response = await api.get(`/api/v1/triposts/${props.tripostId}/comments`, {
    params: {
      page: 1,
      size: 100
    }
  });
  comments.value = response.data.result.content
  console.log(response.data)
}

async function checkLiked() {
  const response = await api.get(`/api/v1/triposts/${props.tripostId}/likes`)
  liked.value = response.data.liked
}

function goToBoard() {
  console.log('게시판으로 이동')
  router.push({ name: 'TripostBoardPage' });
}

function goToPost(postId) {
  console.log(`게시글 ${postId}로 이동`)
  // router.push({ name: 'PostDetailPage', params: { id: postId } });
}

function goToTagSearch(tag) {
  console.log(`태그 ${tag}로 검색`)
  // router.push({ name: 'BoardMainPage', query: { tags: tag } });
}

function editPost() {
  if (tripost) {
    console.log(`게시글 ${tripost.id} 수정`)
    router.push({
      name: 'TripostWritePage',
      query: {tripostId: tripost.id},
    });
  }
}

async function deletePost() {
  if (!tripost) return

  if (confirm('게시글을 삭제하시겠습니까?')) {
    try {
      const response = await api.delete(`/api/v1/triposts/${tripost.id}`);
      console.log(response.data)

      alert('게시글이 삭제되었습니다.')
      goToBoard()
    } catch (e) {
      console.error("삭제 실패 :" + e)
    }
  }
}

async function toggleLike() {
  if (!tripost) return

  try {
    if (liked.value) {
      const response = await api.delete(`/api/v1/triposts/${tripost.id}/likes`);
      console.log(response.data)
      tripost.likeCount--
    } else {
      const response = await api.post(`/api/v1/triposts/${tripost.id}/likes`);
      console.log(response.data)
      tripost.likeCount++
    }
    liked.value = !liked.value
  } catch (error) {
    alert("좋아요 처리 실패");
    console.log(error);
  }
}

function toggleMap() {
  mapVisible.value = !mapVisible.value
}

function scrollToComments() {
  if (commentsSection.value) {
    commentsSection.value.scrollIntoView({ behavior: 'smooth' })
  }
}

function sharePost() {
  if (!tripost.value) return

  // 공유 기능 구현
  if (navigator.share) {
    navigator
      .share({
        title: tripost.value.title,
        text: `${tripost.value.title} - ${tripost.value.author.name}`,
        url: window.location.href,
      })
      .catch((error) => console.log('공유 실패:', error))
  } else {
    // 클립보드에 URL 복사
    navigator.clipboard
      .writeText(window.location.href)
      .then(() => alert('URL이 클립보드에 복사되었습니다.'))
      .catch((error) => console.error('클립보드 복사 실패:', error))
  }
}

async function addComment(newComment) {
  if (!tripost) return

  console.log(newComment)
  comments.value.push(newComment)
  tripost.commentCount++;
}

function addReply({ commentId, reply }) {
  if (!tripost) return

  const comment = tripost.value.comments.find((c) => c.id === commentId)
  if (comment) {
    if (!comment.replies) {
      comment.replies = []
    }

    // 실제 구현 시 API 호출
    // fetch(`/api/comments/${commentId}/replies`, {
    //   method: 'POST',
    //   headers: {
    //     'Content-Type': 'application/json'
    //   },
    //   body: JSON.stringify(reply)
    // })
    // .then(response => response.json())
    // .then(data => {
    //   comment.replies.push(data);
    // })
    // .catch(error => {
    //   console.error('Error:', error);
    //   alert('답글 등록 중 오류가 발생했습니다.');
    // });

    // 임시 처리
    comment.replies.push(reply)
  }
}

async function deleteComment(commentId) {
  if (!tripost) return

  tripost.commentCount--;

  const index = comments.value.findIndex((c) => c.id === commentId)
  if (index !== -1) {
    comments.value.splice(index, 1)
  }
}

function deleteReply({ commentId, replyId }) {
  if (!tripost.value) return

  const comment = tripost.value.comments.find((c) => c.id === commentId)
  if (comment && comment.replies) {
    // 실제 구현 시 API 호출
    // fetch(`/api/replies/${replyId}`, {
    //   method: 'DELETE'
    // })
    // .then(response => response.json())
    // .then(data => {
    //   const index = comment.replies.findIndex(r => r.id === replyId);
    //   if (index !== -1) {
    //     comment.replies.splice(index, 1);
    //   }
    // })
    // .catch(error => {
    //   console.error('Error:', error);
    //   alert('답글 삭제 중 오류가 발생했습니다.');
    // });

    // 임시 처리
    const index = comment.replies.findIndex((r) => r.id === replyId)
    if (index !== -1) {
      comment.replies.splice(index, 1)
    }
  }
}

function getCategoryName(type) {
  const categories = {
    free: '자유게시판',
    notice: '공지사항',
    tips: '여행 팁',
    food: '현지 맛집',
    accommodation: '숙소 추천',
  }
  return categories[type] || '기타'
}

function formatDate(date) {
  if (!date) return ''

  const d = new Date(date)
  return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, '0')}.${String(d.getDate()).padStart(2, '0')}`
}
</script>

<style scoped>
.post-detail-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Noto Sans KR', sans-serif;
}

.loading-container,
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #fd7e14;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.error-container i {
  font-size: 48px;
  color: #dc3545;
  margin-bottom: 15px;
}

.error-container p {
  margin-bottom: 20px;
  color: #6c757d;
}

.back-btn {
  padding: 10px 20px;
  background-color: #fd7e14;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}

.post-content {
  background-color: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.post-actions {
  display: flex;
  justify-content: end;
  margin-bottom: 10px;
  gap: 10px;
}

.author-actions {
  display: flex;
  gap: 10px;
}

.action-btn {
  padding: 8px 15px;
  background-color: #f8f9fa;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 14px;
  color: #495057;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.action-btn:hover {
  background-color: #e9ecef;
}

.delete-btn {
  color: #dc3545;
}

.delete-btn:hover {
  background-color: #f8d7da;
  border-color: #f5c2c7;
}

.post-body {
  margin: 30px 0;
  line-height: 1.8;
  color: #333;
  font-size: 16px;
}

.post-reactions {
  display: flex;
  gap: 15px;
  margin: 30px 0;
  justify-content: center;
}

.reaction-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 10px 15px;
  background-color: #f8f9fa;
  border: 1px solid #ced4da;
  border-radius: 30px;
  font-size: 16px;
  color: #495057;
  cursor: pointer;
}

.reaction-btn:hover {
  background-color: #e9ecef;
}

.reaction-btn.active {
  background-color: #fff5f5;
  border-color: #ffc9c9;
  color: #e03131;
}

.reaction-count {
  font-weight: 500;
}

.reaction-text {
  font-size: 14px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 15px;
  margin: 30px 0;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.author-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
}

.author-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.author-details {
  flex: 1;
}

.author-name {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 5px;
  color: #333;
}

.author-bio {
  font-size: 14px;
  color: #6c757d;
  line-height: 1.5;
}

.related-posts {
  margin-top: 50px;
}

.related-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 15px;
  color: #333;
}

.related-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 15px;
}

.related-item {
  display: flex;
  gap: 10px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
}

.related-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.related-thumbnail {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
}

.related-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.related-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.related-category {
  font-size: 12px;
  color: #6c757d;
  margin-bottom: 5px;
}

.related-post-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 5px;
  line-height: 1.3;
}

.related-meta {
  font-size: 12px;
  color: #6c757d;
  display: flex;
  justify-content: space-between;
  margin-top: auto;
}

@media (max-width: 768px) {
  .post-detail-container {
    padding: 15px;
  }

  .post-content {
    padding: 20px;
  }

  .post-body {
    font-size: 15px;
  }

  .related-list {
    grid-template-columns: 1fr;
  }
}
</style>
