<template>
  <div class="post-detail-container">
    <div v-if="loading" class="loading-container">
      <div class="spinner"></div>
      <p>게시글을 불러오는 중입니다...</p>
    </div>

    <div v-else-if="!post" class="error-container">
      <i class="bi bi-exclamation-triangle"></i>
      <p>게시글을 찾을 수 없습니다.</p>
      <button class="back-btn" @click="goToBoard">게시판으로 돌아가기</button>
    </div>

    <div v-else class="post-content">
      <PostDetailHeader :post="post" @tag-click="goToTagSearch" />

      <div class="post-actions">
        <button class="action-btn" @click="goToBoard"><i class="bi bi-list"></i> 목록</button>
        <div v-if="isAuthor" class="author-actions">
          <button class="action-btn" @click="editPost"><i class="bi bi-pencil"></i> 수정</button>
          <button class="action-btn delete-btn" @click="deletePost">
            <i class="bi bi-trash"></i> 삭제
          </button>
        </div>
      </div>

      <PostDetailRouteInfo
        v-if="post.hasRoute"
        :route="post.route"
        :map-visible="mapVisible"
        @toggle-map="toggleMap"
      />

      <div class="post-body" v-html="post.content"></div>

      <PostDetailImageGallery
        v-if="post.images && post.images.length > 0"
        :images="post.images"
        title="이미지 갤러리"
      />

      <!-- TODO 좋아요, 공유 아이콘으로 변경 -->
      <div class="post-reactions">
        <button class="reaction-btn" :class="{ active: liked }" @click="toggleLike">
          <i class="bi" :class="liked ? 'bi-heart-fill' : 'bi-heart'"></i>
          <span class="reaction-count">{{ post.likes }}</span>
        </button>
        <button class="reaction-btn" @click="scrollToComments">
          <i class="bi bi-chat"></i>
          <span class="reaction-count">{{ post.comments ? post.comments.length : 0 }}</span>
        </button>
        <button class="reaction-btn" @click="sharePost">
          <i class="bi bi-share"></i>
          <span class="reaction-text">공유</span>
        </button>
      </div>

      <div class="author-info">
        <div class="author-avatar">
          <img
            :src="post.author.avatar || 'https://via.placeholder.com/60'"
            :alt="post.author.name"
          />
        </div>
        <div class="author-details">
          <div class="author-name">{{ post.author.name }}</div>
          <div class="author-bio">{{ post.author.bio || '소개가 없습니다.' }}</div>
        </div>
      </div>

      <div ref="commentsSection">
        <PostDetailCommentSection
          :post-id="post.id"
          :comments="post.comments || []"
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
import PostDetailHeader from '../components/post-detail/PostDetailHeader.vue'
import PostDetailRouteInfo from '../components/post-detail/PostDetailRouteInfo.vue'
import PostDetailImageGallery from '../components/post-detail/PostDetailImageGallery.vue'
import PostDetailCommentSection from '../components/post-detail/PostDetailCommentSection.vue'

// 상태 정의
const loading = ref(true)
const post = ref(null)
const relatedPosts = ref([])
const liked = ref(false)
const mapVisible = ref(false)
const commentsSection = ref(null)
const isAuthor = ref(false) // 실제 구현 시 로그인 사용자와 게시글 작성자 비교

// 라이프사이클 훅
onMounted(() => {
  fetchPostData()
})

// 메서드
function fetchPostData() {
  // API 호출 대신 더미 데이터 사용
  setTimeout(() => {
    // 실제 구현 시 API에서 게시글 데이터를 가져옴
    post.value = {
      id: 1,
      type: 'free',
      title: '서울에서 부산까지 3박 4일 여행 후기',
      content: `
        <p>서울에서 출발해 부산까지 3박 4일 동안 여행했습니다. 중간에 대전과 경주를 들렀는데, 특히 경주의 역사 유적지가 인상적이었습니다.</p>
        <p>첫째 날은 서울에서 출발해 대전까지 이동했습니다. 대전에서는 엑스포 과학공원을 방문했는데, 다양한 과학 전시물을 볼 수 있어서 좋았습니다.</p>
        <p>둘째 날은 대전에서 경주로 이동했습니다. 경주에서는 불국사와 석굴암을 방문했습니다. 불국사의 웅장한 건축물과 석굴암의 정교한 불상은 정말 인상적이었습니다.</p>
        <p>셋째 날은 경주에서 부산으로 이동했습니다. 부산에서는 해운대 해수욕장과 광안리 해변을 방문했습니다. 저녁에는 광안대교의 야경을 감상했는데, 정말 아름다웠습니다.</p>
        <p>넷째 날은 부산에서 자갈치 시장과 국제시장을 방문했습니다. 신선한 해산물과 다양한 먹거리를 즐길 수 있었습니다.</p>
        <p>이번 여행은 대한민국의 다양한 모습을 볼 수 있는 좋은 기회였습니다. 다음에는 더 많은 시간을 가지고 각 도시를 더 자세히 탐험해보고 싶습니다.</p>
      `,
      author: {
        id: 1,
        name: '여행자',
        avatar: 'https://via.placeholder.com/60?text=User',
        bio: '국내 여행을 좋아하는 여행 블로거입니다. 주로 로드트립 위주의 여행을 즐깁니다.',
      },
      createdAt: new Date('2023-05-15'),
      views: 120,
      likes: 15,
      comments: [
        {
          id: 1,
          content:
            '좋은 정보 감사합니다! 저도 다음 달에 같은 코스로 여행 예정인데, 경주에서 추천하는 숙소가 있을까요?',
          author: {
            id: 2,
            name: '여행준비중',
            avatar: 'https://via.placeholder.com/30?text=User2',
          },
          createdAt: new Date('2023-05-16'),
          replies: [
            {
              id: 3,
              content:
                '경주 한옥마을 근처에 있는 게스트하우스를 이용했는데 접근성도 좋고 가격도 합리적이었어요!',
              author: {
                id: 1,
                name: '여행자',
                avatar: 'https://via.placeholder.com/30?text=User',
              },
              createdAt: new Date('2023-05-16'),
            },
          ],
        },
        {
          id: 2,
          content:
            '부산 자갈치 시장은 정말 볼거리가 많죠. 저도 얼마 전에 다녀왔는데 회를 먹었더니 정말 신선하고 맛있었어요!',
          author: {
            id: 3,
            name: '맛집탐험가',
            avatar: 'https://via.placeholder.com/30?text=User3',
          },
          createdAt: new Date('2023-05-17'),
          replies: [],
        },
      ],
      tags: ['서울', '부산', '경주', '로드트립', '여행후기'],
      hasRoute: true,
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
      images: [
        {
          url: 'https://via.placeholder.com/800x600?text=Seoul',
          alt: '서울 출발',
          location: { lat: 37.5665, lng: 126.978 },
        },
        {
          url: 'https://via.placeholder.com/800x600?text=Daejeon',
          alt: '대전 엑스포 과학공원',
          location: { lat: 36.3504, lng: 127.3845 },
        },
        {
          url: 'https://via.placeholder.com/800x600?text=Gyeongju',
          alt: '경주 불국사',
          location: { lat: 35.8562, lng: 129.2247 },
        },
        {
          url: 'https://via.placeholder.com/800x600?text=Busan+Beach',
          alt: '부산 해운대 해수욕장',
          location: { lat: 35.1796, lng: 129.0756 },
        },
        {
          url: 'https://via.placeholder.com/800x600?text=Busan+Bridge',
          alt: '부산 광안대교 야경',
          location: { lat: 35.1545, lng: 129.1295 },
        },
        {
          url: 'https://via.placeholder.com/800x600?text=Busan+Market',
          alt: '부산 자갈치 시장',
          location: { lat: 35.0971, lng: 129.0302 },
        },
      ],
    }

    // 관련 게시글 데이터
    relatedPosts.value = [
      {
        id: 2,
        type: 'tips',
        title: '경주 여행 필수 코스 5곳',
        author: {
          id: 4,
          name: '역사여행가',
          avatar: 'https://via.placeholder.com/30?text=User4',
        },
        createdAt: new Date('2023-05-10'),
        thumbnail: 'https://via.placeholder.com/300x200?text=Gyeongju+Tips',
      },
      {
        id: 3,
        type: 'food',
        title: '부산 맛집 베스트 5',
        author: {
          id: 3,
          name: '맛집탐험가',
          avatar: 'https://via.placeholder.com/30?text=User3',
        },
        createdAt: new Date('2023-05-05'),
        thumbnail: 'https://via.placeholder.com/300x200?text=Busan+Food',
      },
      {
        id: 4,
        type: 'free',
        title: '서울에서 부산까지 기차 여행 코스',
        author: {
          id: 5,
          name: '기차여행자',
          avatar: 'https://via.placeholder.com/30?text=User5',
        },
        createdAt: new Date('2023-04-28'),
        thumbnail: 'https://via.placeholder.com/300x200?text=Train+Travel',
      },
    ]

    // 작성자 여부 확인 (실제 구현 시 로그인 사용자 정보와 비교)
    isAuthor.value = true // 테스트용으로 true 설정

    loading.value = false
  }, 1000)
}

function goToBoard() {
  console.log('게시판으로 이동')
  // router.push({ name: 'BoardMainPage' });
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
  if (post.value) {
    console.log(`게시글 ${post.value.id} 수정`)
    // router.push({ name: 'PostWritePage', params: { id: post-write.value.id } });
  }
}

function deletePost() {
  if (!post.value) return

  if (confirm('게시글을 삭제하시겠습니까?')) {
    console.log(`게시글 ${post.value.id} 삭제`)
    // 실제 구현 시 API 호출
    // fetch(`/api/posts/${post-write.value.id}`, {
    //   method: 'DELETE'
    // })
    // .then(response => response.json())
    // .then(data => {
    //   alert('게시글이 삭제되었습니다.');
    //   router.push({ name: 'BoardMainPage' });
    // })
    // .catch(error => {
    //   console.error('Error:', error);
    //   alert('게시글 삭제 중 오류가 발생했습니다.');
    // });

    // 임시 처리
    setTimeout(() => {
      alert('게시글이 삭제되었습니다.')
      goToBoard()
    }, 500)
  }
}

function toggleMap() {
  mapVisible.value = !mapVisible.value
}

function toggleLike() {
  if (!post.value) return

  liked.value = !liked.value

  if (liked.value) {
    post.value.likes++
  } else {
    post.value.likes--
  }

  // 실제 구현 시 API 호출
  // fetch(`/api/posts/${post-write.value.id}/like`, {
  //   method: 'POST',
  //   headers: {
  //     'Content-Type': 'application/json'
  //   },
  //   body: JSON.stringify({ liked: liked.value })
  // });
}

function scrollToComments() {
  if (commentsSection.value) {
    commentsSection.value.scrollIntoView({ behavior: 'smooth' })
  }
}

function sharePost() {
  if (!post.value) return

  // 공유 기능 구현
  if (navigator.share) {
    navigator
      .share({
        title: post.value.title,
        text: `${post.value.title} - ${post.value.author.name}`,
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

function addComment(newComment) {
  if (!post.value) return

  // 실제 구현 시 API 호출
  // fetch(`/api/posts/${post-write.value.id}/comments`, {
  //   method: 'POST',
  //   headers: {
  //     'Content-Type': 'application/json'
  //   },
  //   body: JSON.stringify(newComment)
  // })
  // .then(response => response.json())
  // .then(data => {
  //   post-write.value.comments.push(data);
  // })
  // .catch(error => {
  //   console.error('Error:', error);
  //   alert('댓글 등록 중 오류가 발생했습니다.');
  // });

  // 임시 처리
  post.value.comments.push(newComment)
}

function addReply({ commentId, reply }) {
  if (!post.value) return

  const comment = post.value.comments.find((c) => c.id === commentId)
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

function deleteComment(commentId) {
  if (!post.value) return

  // 실제 구현 시 API 호출
  // fetch(`/api/comments/${commentId}`, {
  //   method: 'DELETE'
  // })
  // .then(response => response.json())
  // .then(data => {
  //   const index = post-write.value.comments.findIndex(c => c.id === commentId);
  //   if (index !== -1) {
  //     post-write.value.comments.splice(index, 1);
  //   }
  // })
  // .catch(error => {
  //   console.error('Error:', error);
  //   alert('댓글 삭제 중 오류가 발생했습니다.');
  // });

  // 임시 처리
  const index = post.value.comments.findIndex((c) => c.id === commentId)
  if (index !== -1) {
    post.value.comments.splice(index, 1)
  }
}

function deleteReply({ commentId, replyId }) {
  if (!post.value) return

  const comment = post.value.comments.find((c) => c.id === commentId)
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
  justify-content: space-between;
  margin-bottom: 20px;
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
