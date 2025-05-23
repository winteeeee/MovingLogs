<template>
  <div class="container-fluid travel-board-container">
    <BoardHeader :activeTab="activeTab" @update:activeTab="activeTab = $event" />

    <BoardSearchFilter
      v-model:filters="filters"
      v-model:searchQuery="searchQuery"
      :mapVisible="mapVisible"
      @toggle-map="toggleMapView"
      @search="searchPosts"
    />

    <BoardMapView v-if="mapVisible" :posts="posts" :kakaoMap="kakaoMap" ref="mapViewRef" />

    <BoardContent
      :loading="loading"
      :filteredPosts="filteredPosts"
      :currentPage="currentPage"
      :totalPages="totalPages"
      :activeTab="activeTab"
      :isAdmin="isAdmin"
      @view-post="viewPost"
      @change-page="changePage"
      @write-post="writeNewPost"
      @preview-route="previewRoute"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import BoardHeader from '../components/board/BoardHeader.vue'
import BoardSearchFilter from '../components/board/BoardSearchFilter.vue'
import BoardMapView from '../components/board/BoardMapView.vue'
import BoardContent from '../components/board/BoardContent.vue'

// State
const router = useRouter()

const activeTab = ref('free')
const posts = ref([])
const loading = ref(true)
const currentPage = ref(1)
const postsPerPage = ref(10)
const searchQuery = ref('')
const filters = ref({
  departure: '',
  destination: '',
  waypoint: '',
})
const mapVisible = ref(false)
const kakaoMap = ref(null)
const isAdmin = ref(false) // Would be set based on user authentication
const mapViewRef = ref(null)

// Computed properties
const filteredPosts = computed(() => {
  let result = posts.value.filter((post) => {
    // Tab filtering
    if (activeTab.value === 'free' && post.type !== 'free') return false
    if (activeTab.value === 'notice' && post.type !== 'notice') return false
    if (activeTab.value === 'tips' && post.type !== 'tips') return false
    if (activeTab.value === 'food' && post.type !== 'food') return false
    if (activeTab.value === 'accommodation' && post.type !== 'accommodation') return false

    // Search query filtering
    if (searchQuery.value && !matchesSearchQuery(post)) return false

    // Route filtering
    if (
      filters.value.departure &&
      (!post.route || !post.route.departure.includes(filters.value.departure))
    )
      return false
    if (
      filters.value.destination &&
      (!post.route || !post.route.destination.includes(filters.value.destination))
    )
      return false
    if (
      filters.value.waypoint &&
      (!post.route || !post.route.waypoints.some((wp) => wp.includes(filters.value.waypoint)))
    )
      return false

    return true
  })

  // Pagination
  const startIdx = (currentPage.value - 1) * postsPerPage.value
  const endIdx = startIdx + postsPerPage.value
  return result.slice(startIdx, endIdx)
})

const totalPages = computed(() => {
  const filteredCount = posts.value.filter((post) => {
    // Tab filtering
    if (activeTab.value === 'free' && post.type !== 'free') return false
    if (activeTab.value === 'notice' && post.type !== 'notice') return false
    if (activeTab.value === 'tips' && post.type !== 'tips') return false
    if (activeTab.value === 'food' && post.type !== 'food') return false
    if (activeTab.value === 'accommodation' && post.type !== 'accommodation') return false

    // Search query filtering
    if (searchQuery.value && !matchesSearchQuery(post)) return false

    // Route filtering
    if (
      filters.value.departure &&
      (!post.route || !post.route.departure.includes(filters.value.departure))
    )
      return false
    if (
      filters.value.destination &&
      (!post.route || !post.route.destination.includes(filters.value.destination))
    )
      return false
    if (
      filters.value.waypoint &&
      (!post.route || !post.route.waypoints.some((wp) => wp.includes(filters.value.waypoint)))
    )
      return false

    return true
  }).length

  return Math.ceil(filteredCount / postsPerPage.value)
})

// Methods
const fetchPosts = () => {
  // Simulating API call with dummy data
  setTimeout(() => {
    posts.value = [
      {
        id: 1,
        type: 'free',
        title: '서울에서 부산까지 3박 4일 여행 후기',
        author: '여행자123',
        createdAt: new Date('2023-05-15'),
        content:
          '서울에서 출발해 부산까지 3박 4일 동안 여행했습니다. 중간에 대전과 경주를 들렀는데, 특히 경주의 역사 유적지가 인상적이었습니다. 부산에서는 해운대와 광안리 해변을 방문했고, 자갈치 시장에서 신선한 해산물을 맛볼 수 있었습니다.',
        views: 245,
        likes: 32,
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
          { url: 'https://via.placeholder.com/150?text=서울', alt: '서울 시작점' },
          { url: 'https://via.placeholder.com/150?text=경주', alt: '경주 불국사' },
          { url: 'https://via.placeholder.com/150?text=부산', alt: '부산 해운대' },
          { url: 'https://via.placeholder.com/150?text=광안리', alt: '광안리 야경' },
        ],
        tags: ['서울', '부산', '경주', '로드트립', '여행후기'],
      },
      {
        id: 2,
        type: 'notice',
        title: '여름 휴가철 인기 여행지 안전 공지',
        author: '관리자',
        createdAt: new Date('2023-06-01'),
        content:
          '여름 휴가철을 맞아 인기 여행지의 안전 정보를 안내드립니다. 해수욕장 이용 시 안전 수칙을 반드시 준수해 주시고, 산행 시에는 일기 예보를 확인하시기 바랍니다. 또한 코로나19 예방을 위한 방역 수칙도 계속해서 지켜주시길 부탁드립니다.',
        views: 512,
        likes: 45,
        images: [{ url: 'https://via.placeholder.com/150?text=안전공지', alt: '안전 공지' }],
        tags: ['공지사항', '안전', '여름휴가'],
      },
      {
        id: 3,
        type: 'food',
        title: '제주도 맛집 베스트 5',
        author: '맛집탐험가',
        createdAt: new Date('2023-05-20'),
        content:
          '제주도 여행 중 꼭 가봐야 할 맛집 5곳을 소개합니다. 흑돼지 구이부터 해산물, 전통 음식까지 다양하게 즐길 수 있는 곳들을 엄선했습니다. 각 맛집의 위치와 추천 메뉴, 가격대 정보도 함께 확인하세요.',
        views: 378,
        likes: 67,
        route: {
          departure: '제주공항',
          destination: '제주공항',
          waypoints: ['제주시', '서귀포시'],
          coordinates: [
            { lat: 33.5067, lng: 126.493 }, // 제주공항
            { lat: 33.4996, lng: 126.5312 }, // 제주시
            { lat: 33.2539, lng: 126.56 }, // 서귀포시
            { lat: 33.5067, lng: 126.493 }, // 제주공항
          ],
        },
        images: [
          { url: 'https://via.placeholder.com/150?text=흑돼지', alt: '제주 흑돼지' },
          { url: 'https://via.placeholder.com/150?text=해산물', alt: '신선한 해산물' },
          { url: 'https://via.placeholder.com/150?text=전통음식', alt: '제주 전통 음식' },
        ],
        tags: ['제주도', '맛집', '흑돼지', '해산물', '로컬푸드'],
      },
    ]
    loading.value = false
  }, 1000)
}

const matchesSearchQuery = (post) => {
  const query = searchQuery.value.toLowerCase()
  return (
    post.title.toLowerCase().includes(query) ||
    post.content.toLowerCase().includes(query) ||
    post.author.toLowerCase().includes(query) ||
    (post.tags && post.tags.some((tag) => tag.toLowerCase().includes(query)))
  )
}

const changePage = (page) => {
  currentPage.value = page
}

const searchPosts = () => {
  currentPage.value = 1 // Reset to first page when searching
}

const viewPost = (post) => {
  // Navigate to post-write detail page
  console.log('View post-write:', post.id)
  // router.push({ name: 'PostDetailPage', params: { id: post-write.id } });
}

const writeNewPost = () => {
  // Navigate to post-write writing page
  console.log('Write new post-write')
  router.push({ name: 'PostWritePage' });
}

const toggleMapView = () => {
  mapVisible.value = !mapVisible.value
  if (mapVisible.value && mapViewRef.value) {
    nextTick(() => {
      mapViewRef.value.initMap()
    })
  }
}

const previewRoute = (route) => {
  if (!mapVisible.value) {
    mapVisible.value = true
    nextTick(() => {
      if (mapViewRef.value) {
        mapViewRef.value.showRouteOnMap(route)
      }
    })
  } else if (mapViewRef.value) {
    mapViewRef.value.showRouteOnMap(route)
  }
}

// Lifecycle hooks
onMounted(() => {
  fetchPosts()
})
</script>

<style>
.travel-board-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Noto Sans KR', sans-serif;
}
</style>
