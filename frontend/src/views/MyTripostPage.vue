<template>
  <div class="container-fluid travel-board-container">
    <div class="board-header">
      <h1>내 게시글</h1>
      <hr>
    </div>
    <BoardSearchFilter
      v-show="false"
      v-model:searchType="searchType"
      v-model:searchQuery="searchQuery"
      :mapVisible="mapVisible"
      @toggle-map="toggleMapView"
      @search="searchPosts"
    />

    <BoardMapView v-if="mapVisible" :posts="posts" :kakaoMap="kakaoMap" ref="mapViewRef" />

    <BoardContent
      :loading="loading"
      :triposts="pageData.content"
      :currentPage="pageData.page"
      :totalPages="pageData.totalPages"
      :isAdmin="isAdmin"
      @view-tripost="viewTriost"
      @change-page="changePage"
      @preview-route="previewRoute"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore.js'
import BoardHeader from '../components/board/BoardHeader.vue'
import BoardSearchFilter from '../components/board/BoardSearchFilter.vue'
import BoardMapView from '../components/board/BoardMapView.vue'
import BoardContent from '../components/board/BoardContent.vue'
import api from '@/api/axios.js'

const authStore = useAuthStore();
// State
const router = useRouter()

const pageData = reactive({})

const activeTab = ref('tripost')
const posts = ref([])
const loading = ref(true)
const currentPage = ref(1)
const postsPerPage = ref(10)
const searchQuery = ref(authStore.name);
const searchType = ref('AUTHOR')
const mapVisible = ref(false)
const kakaoMap = ref(null)
const isAdmin = ref(false) // Would be set based on user authentication
const mapViewRef = ref(null)

// Methods
const fetchPosts = async () => {
  try {
    const response = await api.get(`/api/v1/triposts`, {
      params: {
        type: searchType.value,
        page: currentPage.value,
        size: postsPerPage.value,
        query: searchQuery.value,
      }
    });

    Object.assign(pageData, response.data.result)

    pageData.content.map(tripost => {
      tripost.waypoints = tripost.waypoints.sort((a, b) => a.seq-b.seq);
    })
    loading.value = false
  } catch (e) {
    console.error("fetch 오류 발생", e)
  }
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
  fetchPosts();
}

const searchPosts = () => {
  currentPage.value = 1
  fetchPosts();
}

const viewTriost = (tripost) => {
  // Navigate to tripost-write detail page
  console.log('View tripost:', tripost)
  router.push({
    name: 'TripostDetailPage',
    params: { tripostId: tripost.id }
  });
}

const toggleMapView = () => {
  mapVisible.value = !mapVisible.value
  if (mapVisible.value && mapViewRef.value) {
    nextTick(() => {
      mapViewRef.value.initMap()
    })
  }
}

const previewRoute = (waypoints) => {
  if (!mapVisible.value) {
    mapVisible.value = true
    nextTick(() => {
      if (mapViewRef.value) {
        mapViewRef.value.showWaypointsOnMap(waypoints)
      }
    })
  } else if (mapViewRef.value) {
    mapVisible.value = false;
    mapViewRef.value.showWaypointsOnMap(waypoints)
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

.board-header {
  padding-bottom: solid 1px #777777;
  margin-top: 30px;
  margin-left: 15px;
  margin-bottom: 20px;
}

.board-header h1 {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 15px;
  color: #333;
}
</style>
