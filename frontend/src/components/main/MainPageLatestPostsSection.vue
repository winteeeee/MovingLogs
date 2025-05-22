<template>
  <section class="latest-posts-section">
    <div class="container">
      <div class="section-header">
        <h2 class="section-title">최신 여행 이야기</h2>
        <router-link to="/board" class="view-all-link">
          전체보기 <i class="bi bi-arrow-right"></i>
        </router-link>
      </div>

      <div class="row">
        <div class="col-md-6 col-lg-3 mb-4" v-for="post in latestPosts" :key="post.id">
          <div class="post-card">
            <div class="post-thumbnail">
              <img :src="post.thumbnail" :alt="post.title" class="post-img" />
              <div class="post-category">{{ getCategoryName(post.type) }}</div>
            </div>
            <div class="post-content">
              <h3 class="post-title">{{ post.title }}</h3>
              <p class="post-excerpt">{{ truncateContent(post.content) }}</p>
              <div class="post-meta">
                <span class="post-date">{{ formatDate(post.createdAt) }}</span>
                <div class="post-stats">
                  <span class="stat-item"> <i class="bi bi-eye"></i> {{ post.views }} </span>
                  <span class="stat-item">
                      <i class="bi bi-chat"></i> {{ post.comments.length }}
                    </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

// 최신 게시글 데이터
const latestPosts = ref([
  {
    id: 1,
    type: 'tips',
    title: '혼자 떠나는 여행, 준비물 체크리스트',
    content:
      '혼자 여행을 준비하는 분들을 위한 필수 준비물 체크리스트입니다. 여행 전 꼭 확인하세요!',
    thumbnail: 'https://via.placeholder.com/300x200?text=Travel+Tips',
    createdAt: new Date('2023-05-20'),
    views: 342,
    comments: [
      { id: 1, content: '정말 유용한 정보네요!' },
      { id: 2, content: '체크리스트 덕분에 잘 준비했어요.' },
    ],
  },
  {
    id: 2,
    type: 'free',
    title: '강원도 속초 2박 3일 여행 후기',
    content:
      '지난 주말 속초로 2박 3일 여행을 다녀왔습니다. 날씨도 좋고 해산물도 맛있어서 정말 좋았어요. 특히 속초 해변에서 본 일출은 정말 환상적이었습니다.',
    thumbnail: 'https://via.placeholder.com/300x200?text=Sokcho',
    createdAt: new Date('2023-05-18'),
    views: 256,
    comments: [{ id: 3, content: '저도 다음 주에 속초 가는데 참고할게요!' }],
  },
  {
    id: 3,
    type: 'food',
    title: '전주 한옥마을 맛집 베스트 5',
    content:
      '전주 한옥마을에서 꼭 가봐야 할 맛집 5곳을 소개합니다. 전통 한식부터 현대적인 퓨전 요리까지 다양하게 즐길 수 있는 맛집들이에요.',
    thumbnail: 'https://via.placeholder.com/300x200?text=Jeonju+Food',
    createdAt: new Date('2023-05-15'),
    views: 421,
    comments: [
      { id: 4, content: '비빔밥 맛집 추천해주셔서 감사합니다!' },
      { id: 5, content: '3번 맛집은 줄이 너무 길던데요...' },
      { id: 6, content: '사진만 봐도 침이 고이네요.' },
    ],
  },
  {
    id: 4,
    type: 'accommodation',
    title: '경주 한옥 스테이 추천',
    content:
      '경주 여행에서 특별한 경험을 원하신다면 한옥 스테이를 추천합니다. 전통적인 분위기에서 현대적인 편의시설까지 갖춘 최고의 한옥 숙소들을 소개합니다.',
    thumbnail: 'https://via.placeholder.com/300x200?text=Gyeongju+Hanok',
    createdAt: new Date('2023-05-12'),
    views: 198,
    comments: [
      { id: 7, content: '가격대가 어떻게 되나요?' },
      { id: 8, content: '예약은 어디서 하나요?' },
    ],
  },
])

// 유틸리티 함수
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

function truncateContent(content) {
  if (!content) return ''
  if (content.length <= 60) return content
  return content.substring(0, 60) + '...'
}

function formatDate(date) {
  if (!date) return ''

  const d = new Date(date)
  const now = new Date()
  const diff = Math.floor((now - d) / 1000) // 초 단위 차이

  if (diff < 60) {
    return '방금 전'
  } else if (diff < 3600) {
    return `${Math.floor(diff / 60)}분 전`
  } else if (diff < 86400) {
    return `${Math.floor(diff / 3600)}시간 전`
  } else if (diff < 604800) {
    return `${Math.floor(diff / 86400)}일 전`
  } else {
    return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, '0')}.${String(d.getDate()).padStart(2, '0')}`
  }
}
</script>

<style scoped>
/* 최신 게시글 섹션 */
.latest-posts-section {
  background-color: #f8f9fa;
}

.post-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.post-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.post-thumbnail {
  position: relative;
  height: 160px;
  overflow: hidden;
}

.post-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.post-card:hover .post-img {
  transform: scale(1.05);
}

.post-category {
  position: absolute;
  bottom: 12px;
  left: 12px;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.post-content {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.post-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 10px;
  color: #333;
  line-height: 1.3;
}

.post-excerpt {
  font-size: 14px;
  color: #6c757d;
  line-height: 1.5;
  margin-bottom: 15px;
  flex: 1;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #6c757d;
}

.post-stats {
  display: flex;
  gap: 10px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

section {
  padding: 60px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.section-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: #333;
  position: relative;
  padding-left: 16px;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 8px;
  height: 24px;
  width: 4px;
  background-color: var(--primary-color);
  border-radius: 2px;
}

.view-all-link {
  display: flex;
  align-items: center;
  color: var(--primary-color);
  font-weight: 500;
  text-decoration: none;
  transition: color 0.2s;
}

.view-all-link:hover {
  color: var(--primary-hover);
}

.view-all-link i {
  margin-left: 6px;
}

@media (max-width: 767.98px) {
  section {
    padding: 40px 0;
  }

  .section-title {
    font-size: 1.5rem;
  }
}
</style>
