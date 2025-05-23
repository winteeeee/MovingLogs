<template>
  <div class="route-create-page">
    <!-- 간소화된 서브 헤더 -->
    <div class="sub-header">
      <div class="container">
        <h2>여행 커뮤니티</h2>
        <div class="sub-nav">
          <a href="#" class="sub-nav-item active">여행게시판</a>
          <a href="#" class="sub-nav-item">공지사항</a>
          <a href="#" class="sub-nav-item">여행 팁</a>
          <a href="#" class="sub-nav-item">인기 맛집</a>
          <a href="#" class="sub-nav-item">숙소 추천</a>
        </div>
      </div>
    </div>

    <!-- 메인 콘텐츠 영역 -->
    <div class="route-content">
      <div class="container-fluid">
        <div class="content-wrapper">
          <!-- 왼쪽 사이드바 (검색 및 AI 추천) -->
          <div class="left-sidebar" :class="{ 'sidebar-collapsed': leftSidebarCollapsed }">
            <div class="sidebar-toggle" @click="toggleLeftSidebar">
              <i class="bi" :class="leftSidebarCollapsed ? 'bi-chevron-right' : 'bi-chevron-left'"></i>
            </div>

            <div class="sidebar-content" v-if="!leftSidebarCollapsed">
              <div class="search-section">
                <h3>관광지 검색</h3>

                <div class="search-form">
                  <div class="form-row">
                    <div class="form-group">
                      <label>출발지</label>
                      <select class="form-select">
                        <option>출발지 선택</option>
                        <option>서울</option>
                        <option>부산</option>
                        <option>제주</option>
                      </select>
                    </div>

                    <div class="form-group">
                      <label>도착지</label>
                      <select class="form-select">
                        <option>도착지 선택</option>
                        <option>서울</option>
                        <option>부산</option>
                        <option>제주</option>
                      </select>
                    </div>
                  </div>

                  <div class="form-row">
                    <div class="form-group">
                      <label>관광지</label>
                      <select class="form-select">
                        <option>관광지 선택</option>
                        <option>명소</option>
                        <option>맛집</option>
                        <option>숙소</option>
                      </select>
                    </div>
                  </div>

                  <div class="search-input-group">
                    <input type="text" class="form-control" placeholder="검색어를 입력하세요">
                    <button class="btn btn-search">검색</button>
                    <button class="btn btn-ai">AI 지도 보기</button>
                  </div>
                </div>
              </div>

              <div class="results-section">
                <div class="results-header">
                  <h3>검색 결과</h3>
                  <span class="result-count">총 {{ searchResults.length }}개</span>
                </div>

                <div class="search-results">
                  <div class="place-card" v-for="place in searchResults" :key="place.id"
                       :class="{ 'active': selectedPlace && selectedPlace.id === place.id }">
                    <div class="place-image">
                      <img :src="place.first_image || 'https://via.placeholder.com/80?text=No+Image'" :alt="place.title">
                    </div>
                    <div class="place-info" @click="showPlaceDetail(place)">
                      <h4>{{ place.title }}</h4>
                      <p class="place-address">{{ place.addr1 }}</p>
                      <div class="place-tags">
                        <span class="tag">{{ getAreaName(place.area_code) }}</span>
                        <span class="tag">{{ getContentTypeName(place.content_type_id) }}</span>
                      </div>
                    </div>
                    <button class="btn-add" @click="addToRoute(place)">
                      <i class="bi bi-plus"></i>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 장소 상세 정보 패널 -->
          <div class="detail-panel" v-if="detailPanelOpen">
            <div class="detail-header">
              <button class="btn-close" @click="closeDetailPanel">
                <i class="bi bi-x-lg"></i>
              </button>
              <h3>{{ selectedPlace.title }}</h3>
              <button class="btn btn-primary btn-sm" v-if="!isInRoute(selectedPlace.id)" @click="addToRoute(selectedPlace)">
                경로 추가하기
              </button>
              <button class="btn btn-danger btn-sm" v-else @click="removeFromRoute(selectedPlace.id)">
                경로에서 제거
              </button>
            </div>

            <div class="detail-content">
              <div class="detail-image">
                <img :src="selectedPlace.first_image || 'https://via.placeholder.com/400x250?text=No+Image'"
                     :alt="selectedPlace.title">
              </div>

              <div class="detail-info">
                <div class="info-item">
                  <i class="bi bi-geo-alt"></i>
                  <div>
                    <strong>주소</strong>
                    <p>{{ selectedPlace.addr1 }} {{ selectedPlace.addr2 }}</p>
                  </div>
                </div>

                <div class="info-item" v-if="selectedPlace.tel">
                  <i class="bi bi-telephone"></i>
                  <div>
                    <strong>전화번호</strong>
                    <p>{{ selectedPlace.tel }}</p>
                  </div>
                </div>

                <div class="info-item" v-if="selectedPlace.homepage">
                  <i class="bi bi-globe"></i>
                  <div>
                    <strong>홈페이지</strong>
                    <a :href="selectedPlace.homepage" target="_blank">홈페이지 방문</a>
                  </div>
                </div>
              </div>

              <div class="detail-description" v-if="selectedPlace.overview">
                <h4>상세 설명</h4>
                <div class="description-text" v-html="formatOverview(selectedPlace.overview)"></div>
              </div>
            </div>
          </div>

          <!-- 지도 영역 -->
          <div class="map-container" :class="{ 'map-shrink': detailPanelOpen }">
            <div id="map" ref="mapContainer"></div>

            <div class="map-controls">
              <button class="map-btn" @click="zoomIn">
                <i class="bi bi-plus-lg"></i>
              </button>
              <button class="map-btn" @click="zoomOut">
                <i class="bi bi-dash-lg"></i>
              </button>
              <button class="map-btn" @click="resetMap">
                <i class="bi bi-geo-alt"></i>
              </button>
            </div>
          </div>

          <!-- 오른쪽 사이드바 -->
          <div class="right-sidebar" :class="{ 'sidebar-collapsed': rightSidebarCollapsed }">
            <div class="sidebar-toggle" @click="toggleRightSidebar">
              <i class="bi" :class="rightSidebarCollapsed ? 'bi-chevron-left' : 'bi-chevron-right'"></i>
            </div>

            <div class="sidebar-content" v-if="!rightSidebarCollapsed">
              <div class="plan-section">
                <h3>여행 계획 정보</h3>

                <div class="plan-form">
                  <div class="form-group">
                    <label>여행 계획 이름</label>
                    <input type="text" class="form-control" placeholder="예: 서울 3박 4일 여행">
                  </div>

                  <div class="form-row">
                    <div class="form-group">
                      <label>시작일</label>
                      <input type="date" class="form-control">
                    </div>
                    <div class="form-group">
                      <label>종료일</label>
                      <input type="date" class="form-control">
                    </div>
                  </div>

                  <div class="form-group">
                    <label>여행 계획 설명</label>
                    <textarea class="form-control" rows="3" placeholder="여행 계획에 대한 설명을 입력하세요."></textarea>
                  </div>
                </div>
              </div>

              <div class="route-section">
                <div class="route-header">
                  <h3>여행 경로</h3>
                  <div class="route-actions">
                    <button class="btn btn-outline-danger btn-sm" @click="clearRoute">
                      <i class="bi bi-trash"></i> 전체 삭제
                    </button>
                  </div>
                </div>

                <div class="route-list">
                  <div v-if="routePlaces.length === 0" class="empty-state">
                    <i class="bi bi-map"></i>
                    <p>여행지를 추가해주세요</p>
                  </div>

                  <draggable
                    v-else
                    v-model="routePlaces"
                    class="route-items"
                    item-key="id"
                    handle=".drag-handle"
                    @start="drag=true"
                    @end="drag=false"
                  >
                    <template #item="{element, index}">
                      <div class="route-item" :class="{ 'active': selectedPlace && selectedPlace.id === element.id }">
                        <div class="drag-handle">
                          <i class="bi bi-grip-vertical"></i>
                        </div>
                        <div class="route-number">{{ index + 1 }}</div>
                        <div class="route-info" @click="showPlaceDetail(element)">
                          <h4>{{ element.title }}</h4>
                          <p>{{ element.addr1 }}</p>
                        </div>
                        <div class="route-actions">
                          <button class="btn-icon" @click="showPlaceDetail(element)">
                            <i class="bi bi-info-circle"></i>
                          </button>
                          <button class="btn-icon text-danger" @click="removeFromRoute(element.id)">
                            <i class="bi bi-x-lg"></i>
                          </button>
                        </div>
                      </div>
                    </template>
                  </draggable>
                </div>

                <div class="route-footer">
                  <button class="btn btn-primary w-100">
                    <i class="bi bi-check-lg"></i> 저장하기
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { VueDraggableNext } from 'vue-draggable-next';

// draggable 컴포넌트 등록
const draggable = VueDraggableNext;

// 드래그 상태
const drag = ref(false);

// 사이드바 상태
const leftSidebarCollapsed = ref(false);
const rightSidebarCollapsed = ref(false);
const detailPanelOpen = ref(false);

// 선택된 장소 정보
const selectedPlace = ref(null);

// 지도 관련 변수
const mapContainer = ref(null);
let map = null;

// 검색 결과 데이터
const searchResults = ref([
  {
    id: 'attr01',
    content_id: 1,
    title: '경복궁',
    content_type_id: 12,
    area_code: 1,
    si_gun_gu_code: 23,
    first_image: 'https://via.placeholder.com/300x200?text=경복궁',
    first_image2: 'https://via.placeholder.com/80?text=경복궁',
    map_level: 5,
    latitude: 37.579617,
    longitude: 126.977041,
    tel: '02-3700-3900',
    addr1: '서울 종로구 사직로 161',
    addr2: '세종로',
    homepage: 'http://www.royalpalace.go.kr',
    overview: '경복궁은 조선왕조의 법궁(法宮)으로, 1395년에 창건되었다. 경복궁은 동궐(창덕궁)과 서궐(경희궁)의 중간에 위치한 북궐로, 조선의 중심 법궁이었다.'
  },
  {
    id: 'attr02',
    content_id: 2,
    title: '남산서울타워',
    content_type_id: 12,
    area_code: 1,
    si_gun_gu_code: 24,
    first_image: 'https://via.placeholder.com/300x200?text=남산타워',
    first_image2: 'https://via.placeholder.com/80?text=남산타워',
    map_level: 5,
    latitude: 37.551245,
    longitude: 126.988222,
    tel: '02-3455-9277',
    addr1: '서울 용산구 남산공원길 105',
    addr2: '',
    homepage: 'http://www.seoultower.co.kr',
    overview: '남산서울타워는 서울 중구 남산 정상 부근에 위치한 전파 송출 및 관광용 타워이다.'
  },
  {
    id: 'attr03',
    content_id: 3,
    title: '명동 쇼핑거리',
    content_type_id: 38,
    area_code: 1,
    si_gun_gu_code: 24,
    first_image: 'https://via.placeholder.com/300x200?text=명동',
    first_image2: 'https://via.placeholder.com/80?text=명동',
    map_level: 5,
    latitude: 37.563576,
    longitude: 126.983431,
    tel: '',
    addr1: '서울 중구 명동길',
    addr2: '',
    homepage: '',
    overview: '명동은 서울특별시 중구에 있는 상업지역이다.'
  }
]);

// 경로에 추가된 장소들
const routePlaces = ref([
  {
    id: 'attr01',
    content_id: 1,
    title: '경복궁',
    content_type_id: 12,
    area_code: 1,
    si_gun_gu_code: 23,
    first_image: 'https://via.placeholder.com/300x200?text=경복궁',
    first_image2: 'https://via.placeholder.com/80?text=경복궁',
    map_level: 5,
    latitude: 37.579617,
    longitude: 126.977041,
    tel: '02-3700-3900',
    addr1: '서울 종로구 사직로 161',
    addr2: '세종로',
    homepage: 'http://www.royalpalace.go.kr',
    overview: '경복궁은 조선왕조의 법궁(法宮)으로, 1395년에 창건되었다.'
  },
  {
    id: 'attr02',
    content_id: 2,
    title: '남산서울타워',
    content_type_id: 12,
    area_code: 1,
    si_gun_gu_code: 24,
    first_image: 'https://via.placeholder.com/300x200?text=남산타워',
    first_image2: 'https://via.placeholder.com/80?text=남산타워',
    map_level: 5,
    latitude: 37.551245,
    longitude: 126.988222,
    tel: '02-3455-9277',
    addr1: '서울 용산구 남산공원길 105',
    addr2: '',
    homepage: 'http://www.seoultower.co.kr',
    overview: '남산서울타워는 서울 중구 남산 정상 부근에 위치한 전파 송출 및 관광용 타워이다.'
  }
]);

// 지역 코드에 따른 지역명 반환
const getAreaName = (areaCode) => {
  const areaNames = {
    1: '서울', 2: '인천', 3: '대전', 4: '대구', 5: '광주', 6: '부산', 7: '울산', 8: '세종',
    31: '경기도', 32: '강원도', 33: '충청북도', 34: '충청남도', 35: '경상북도', 36: '경상남도', 37: '전라북도', 38: '전라남도', 39: '제주도'
  };
  return areaNames[areaCode] || '기타';
};

// 콘텐츠 타입에 따른 타입명 반환
const getContentTypeName = (contentTypeId) => {
  const contentTypes = {
    12: '관광지', 14: '문화시설', 15: '축제공연행사', 25: '여행코스', 28: '레포츠', 32: '숙박', 38: '쇼핑', 39: '음식점'
  };
  return contentTypes[contentTypeId] || '기타';
};

// 사이드바 토글 함수들
const toggleLeftSidebar = () => {
  leftSidebarCollapsed.value = !leftSidebarCollapsed.value;
};

const toggleRightSidebar = () => {
  rightSidebarCollapsed.value = !rightSidebarCollapsed.value;
};

// 장소 상세 정보 표시
const showPlaceDetail = (place) => {
  if (selectedPlace.value && selectedPlace.value.id === place.id && detailPanelOpen.value) {
    closeDetailPanel();
    return;
  }

  selectedPlace.value = place;
  detailPanelOpen.value = true;
  highlightMarker(place);
};

// 상세 정보 패널 닫기
const closeDetailPanel = () => {
  detailPanelOpen.value = false;
  resetMarkerHighlight();
};

// 마커 관련 함수들
const highlightMarker = (place) => {
  if (map) {
    map.setCenter(new window.kakao.maps.LatLng(place.latitude, place.longitude));
    map.setLevel(3);
  }
};

const resetMarkerHighlight = () => {
  // 마커 강조 해제 로직
};

// 텍스트 포맷팅
const formatOverview = (text) => {
  if (!text) return '';
  return text.replace(/\n/g, '<br>');
};

// 경로 관련 함수들
const isInRoute = (placeId) => {
  return routePlaces.value.some(p => p.id === placeId);
};

const addToRoute = (place) => {
  const exists = routePlaces.value.some(p => p.id === place.id);
  if (!exists) {
    routePlaces.value.push(place);
  }
};

const removeFromRoute = (placeId) => {
  const index = routePlaces.value.findIndex(p => p.id === placeId);
  if (index !== -1) {
    routePlaces.value.splice(index, 1);
  }
};

const clearRoute = () => {
  if (confirm('모든 경로를 삭제하시겠습니까?')) {
    routePlaces.value = [];
  }
};

// 지도 관련 함수들
const zoomIn = () => {
  if (map) {
    const currentZoom = map.getZoom();
    map.setZoom(currentZoom + 1);
  }
};

const zoomOut = () => {
  if (map) {
    const currentZoom = map.getZoom();
    map.setZoom(currentZoom - 1);
  }
};

const resetMap = () => {
  if (map) {
    map.setCenter(new window.kakao.maps.LatLng(37.566826, 126.978656));
    map.setLevel(8);
  }
};

// 지도 초기화
onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap();
  } else {
    console.error('Kakao Maps API is not loaded');
  }
});

const initMap = () => {
  const container = mapContainer.value;
  const options = {
    center: new window.kakao.maps.LatLng(37.566826, 126.978656),
    level: 8
  };

  map = new window.kakao.maps.Map(container, options);
};

onUnmounted(() => {
  map = null;
});
</script>

<style scoped>
/* 전역 스타일 */
.route-create-page {
  min-height: 100vh;
  background-color: #f8f9fa;
}

/* 서브 헤더 */
.sub-header {
  background-color: white;
  border-bottom: 1px solid #e9ecef;
  padding: 20px 0;
}

.sub-header h2 {
  font-size: 1.8rem;
  font-weight: 700;
  margin: 0 0 15px 0;
  text-align: center;
}

.sub-nav {
  display: flex;
  justify-content: center;
  gap: 40px;
}

.sub-nav-item {
  color: #666;
  text-decoration: none;
  padding: 10px 0;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
}

.sub-nav-item.active {
  color: #ff6b35;
  border-bottom-color: #ff6b35;
}

.sub-nav-item:hover {
  color: #ff6b35;
}

/* 메인 콘텐츠 */
.route-content {
  flex: 1;
  padding: 20px 0;
}

.content-wrapper {
  display: flex;
  gap: 20px;
  height: calc(100vh - 120px);
}

/* 왼쪽 사이드바 */
.left-sidebar {
  width: 350px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: relative;
  transition: width 0.3s ease;
}

.left-sidebar.sidebar-collapsed {
  width: 50px;
}

.sidebar-toggle {
  position: absolute;
  right: -12px;
  top: 20px;
  width: 24px;
  height: 24px;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.sidebar-content {
  padding: 20px;
  height: 100%;
  overflow-y: auto;
}

/* 검색 섹션 */
.search-section h3 {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 20px;
  color: #333;
}

.search-form {
  margin-bottom: 30px;
}

.form-row {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.form-group {
  flex: 1;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-size: 0.9rem;
  font-weight: 500;
  color: #555;
}

.form-control, .form-select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
}

.search-input-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary {
  background-color: #ff6b35;
  color: white;
}

.btn-primary:hover {
  background-color: #e55a2b;
}

.btn-search {
  background-color: #ff6b35;
  color: white;
}

.btn-ai {
  background-color: #28a745;
  color: white;
}

.btn-outline-secondary {
  background-color: transparent;
  color: #6c757d;
  border: 1px solid #6c757d;
}

.btn-outline-danger {
  background-color: transparent;
  color: #dc3545;
  border: 1px solid #dc3545;
}

/* 검색 결과 */
.results-section {
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.results-header h3 {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
}

.result-count {
  font-size: 0.9rem;
  color: #666;
}

.search-results {
  max-height: 400px;
  overflow-y: auto;
}

.place-card {
  display: flex;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  position: relative;
  cursor: pointer;
  transition: background-color 0.2s;
}

.place-card:hover {
  background-color: #f8f9fa;
}

.place-card.active {
  background-color: #fff3f0;
  border-left: 3px solid #ff6b35;
}

.place-image {
  width: 60px;
  height: 60px;
  margin-right: 12px;
  border-radius: 4px;
  overflow: hidden;
}

.place-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.place-info {
  flex: 1;
}

.place-info h4 {
  font-size: 0.95rem;
  font-weight: 600;
  margin: 0 0 5px 0;
  color: #333;
}

.place-address {
  font-size: 0.8rem;
  color: #666;
  margin: 0 0 8px 0;
}

.place-tags {
  display: flex;
  gap: 5px;
}

.tag {
  background-color: #f1f3f4;
  color: #5f6368;
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 0.75rem;
}

.btn-add {
  position: absolute;
  right: 10px;
  top: 10px;
  width: 24px;
  height: 24px;
  background-color: #ff6b35;
  color: white;
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

/* 상세 정보 패널 */
.detail-panel {
  width: 400px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.detail-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  gap: 15px;
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #666;
}

.detail-header h3 {
  flex: 1;
  margin: 0;
  font-size: 1.2rem;
  font-weight: 600;
}

.detail-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.detail-image {
  margin-bottom: 20px;
}

.detail-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 6px;
}

.detail-info {
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  margin-bottom: 15px;
}

.info-item i {
  color: #ff6b35;
  margin-top: 2px;
}

.info-item strong {
  display: block;
  font-size: 0.9rem;
  margin-bottom: 3px;
}

.info-item p, .info-item a {
  margin: 0;
  font-size: 0.85rem;
  color: #666;
}

.detail-description h4 {
  font-size: 1rem;
  margin-bottom: 10px;
  color: #333;
}

.description-text {
  font-size: 0.9rem;
  line-height: 1.6;
  color: #555;
}

/* 지도 영역 */
.map-container {
  flex: 1;
  position: relative;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: margin-left 0.3s ease;
}

.map-container.map-shrink {
  margin-left: 420px;
}

#map {
  width: 100%;
  height: 100%;
}

.map-controls {
  position: absolute;
  top: 20px;
  left: 20px;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.map-btn {
  width: 36px;
  height: 36px;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.map-btn:hover {
  background-color: #f8f9fa;
}

/* 오른쪽 사이드바 */
.right-sidebar {
  width: 350px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: relative;
  transition: width 0.3s ease;
}

.right-sidebar.sidebar-collapsed {
  width: 50px;
}

.right-sidebar .sidebar-toggle {
  left: -12px;
}

/* 계획 섹션 */
.plan-section {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.plan-section h3 {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 20px;
  color: #333;
}

.plan-form .form-group {
  margin-bottom: 15px;
}

/* 경로 섹션 */
.route-section {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.route-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.route-header h3 {
  font-size: 1.2rem;
  font-weight: 600;
  margin: 0;
  color: #333;
}

.route-list {
  flex: 1;
  overflow-y: auto;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #999;
}

.empty-state i {
  font-size: 3rem;
  margin-bottom: 15px;
}

.route-items {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.route-item {
  display: flex;
  align-items: center;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e9ecef;
  cursor: pointer;
  transition: all 0.2s;
}

.route-item:hover {
  background-color: #f0f0f0;
}

.route-item.active {
  background-color: #fff3f0;
  border-color: #ff6b35;
}

.drag-handle {
  cursor: grab;
  color: #aaa;
  margin-right: 8px;
  padding: 0 5px;
}

.drag-handle:active {
  cursor: grabbing;
}

.route-number {
  width: 28px;
  height: 28px;
  background-color: #ff6b35;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  font-weight: 600;
  margin-right: 12px;
}

.route-info {
  flex: 1;
}

.route-info h4 {
  font-size: 0.9rem;
  font-weight: 600;
  margin: 0 0 3px 0;
  color: #333;
}

.route-info p {
  font-size: 0.8rem;
  color: #666;
  margin: 0;
}

.route-actions {
  display: flex;
  gap: 5px;
}

.btn-icon {
  width: 24px;
  height: 24px;
  background: none;
  border: none;
  border-radius: 3px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #666;
}

.btn-icon:hover {
  background-color: #e9ecef;
}

.btn-icon.text-danger {
  color: #dc3545;
}

.route-footer {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.w-100 {
  width: 100%;
}

/* 반응형 디자인 */
@media (max-width: 1200px) {
  .content-wrapper {
    flex-direction: column;
    height: auto;
  }

  .left-sidebar, .right-sidebar, .detail-panel {
    width: 100%;
    margin-bottom: 20px;
  }

  .map-container {
    height: 400px;
    margin-left: 0 !important;
  }

  .sidebar-toggle {
    display: none;
  }
}

@media (max-width: 768px) {
  .sub-nav {
    gap: 20px;
    flex-wrap: wrap;
  }

  .form-row {
    flex-direction: column;
  }

  .search-input-group {
    flex-direction: column;
  }
}
</style>
