<template>
  <div class="route-create-page">
    <!-- 메인 콘텐츠 영역 -->
    <div class="route-content">
      <!-- 전체 화면 지도 -->
      <div class="map-container">
        <div id="map" ref="mapContainer"></div>

        <!-- 지도 컨트롤 -->
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

      <!-- 왼쪽 사이드바 오버레이 -->
      <div class="left-sidebar-overlay" :class="{ 'expanded': detailPanelOpen }">
        <div class="sidebar-layout">
          <!-- 검색 패널 (항상 표시) -->
          <div class="search-panel">
            <div class="search-section">
              <h3>관광지 검색</h3>

              <div class="search-form">
                <div class="form-row">
                  <div class="form-group">
                    <label>지역</label>
                    <select class="form-select">
                      <option value="">전체 지역</option>
                      <option value="1">서울특별시</option>
                      <option value="2">인천광역시</option>
                      <option value="3">대전광역시</option>
                      <option value="4">대구광역시</option>
                      <option value="5">광주광역시</option>
                      <option value="6">부산광역시</option>
                      <option value="7">울산광역시</option>
                      <option value="8">세종특별자치시</option>
                      <option value="31">경기도</option>
                      <option value="32">강원특별자치도</option>
                      <option value="33">충청북도</option>
                      <option value="34">충청남도</option>
                      <option value="35">경상북도</option>
                      <option value="36">경상남도</option>
                      <option value="37">전북특별자치도</option>
                      <option value="38">전라남도</option>
                      <option value="39">제주특별자치도</option>
                    </select>
                  </div>

                  <div class="form-group">
                    <label>시군구</label>
                    <select class="form-select">
                      <option value="">전체 시군구</option>
                      <option value="1">강남구</option>
                      <option value="2">강동구</option>
                      <option value="3">강북구</option>
                      <option value="4">강서구</option>
                      <option value="5">관악구</option>
                      <option value="6">광진구</option>
                      <option value="7">구로구</option>
                      <option value="8">금천구</option>
                      <option value="9">노원구</option>
                      <option value="10">도봉구</option>
                    </select>
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label>관광지 종류</label>
                    <select class="form-select">
                      <option value="">전체 종류</option>
                      <option value="12">관광지</option>
                      <option value="14">문화시설</option>
                      <option value="15">축제공연행사</option>
                      <option value="25">여행코스</option>
                      <option value="28">레포츠</option>
                      <option value="32">숙박</option>
                      <option value="38">쇼핑</option>
                      <option value="39">음식점</option>
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
                  <button
                    :class="['toggle-route-btn', isInRoute(place.id) ? 'is-remove' : 'is-add']"
                    @click="isInRoute(place.id) ? removeFromRoute(place.id) : addToRoute(place)"
                  >
                    <i class="bi bi-plus"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- 장소 상세 정보 패널 (조건부 표시) -->
          <div class="detail-panel" v-if="detailPanelOpen">
            <div class="detail-toggle" @click="closeDetailPanel">
              <i class="bi bi-chevron-left"></i>
            </div>

            <div class="detail-header">
              <h3>{{ selectedPlace.title }}</h3>
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

            <div class="detail-footer">
              <button class="btn btn-action w-100" v-if="!isInRoute(selectedPlace.id)" @click="addToRoute(selectedPlace)">
                <span class="btn-text">경로 추가하기</span>
              </button>
              <button class="btn btn-danger w-100" v-else @click="removeFromRoute(selectedPlace.id)">
                <span class="btn-text">경로에서 제거</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 오른쪽 사이드바 오버레이 -->
      <div class="right-sidebar-overlay">
        <div class="sidebar-content">
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

              <Draggable
                v-model="routePlaces"
                class="route-items"
                item-key="id"
                handle=".route-item"
                @start="drag=true"
                @end="drag=false"
              >
                <template #item="{element, index}">
                  <div class="route-item" :class="{ 'active': selectedPlace && selectedPlace.id === element.id }">
                    <div class="route-number">{{ index + 1 }}</div>
                    <div class="route-info" >
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
              </Draggable>
            </div>

            <div class="route-footer">
              <button class="btn btn-action w-100">
                <span class="btn-text">저장하기</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import Draggable from 'vuedraggable'
import api from '@/api/axios.js'

const serverUrl = import.meta.env.VITE_API_SERVER_URL


const sidoList = ref([]);

onMounted(async () => {
  const response = await api.get(`${serverUrl}/api/v1/attractions/sidos`);
  console.log(response);
  // 여기에 DOM 조작 코드나 초기화 코드 작성
});



// 드래그 상태
const drag = ref(false);

// 사이드바 상태
const detailPanelOpen = ref(false);

// 선택된 장소 정보
const selectedPlace = ref(null);

// 지도 관련 변수
const mapContainer = ref(null);
let map = null;
let markers = [];
let polyline = null;
let highlightedMarker = null;

// 검색 결과 데이터
const searchResults = ref([
  {
    id: 'attr01',
    content_id: 1,
    title: '경복궁',
    content_type_id: 12,
    area_code: 1,
    si_gun_gu_code: 23,
    first_image: 'https://www.shinsegaegroupnewsroom.com/wp-content/uploads/2017/11/1-1.jpg',
    first_image2: 'https://www.shinsegaegroupnewsroom.com/wp-content/uploads/2017/11/1-1.jpg',
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
  },
  {
    id: 'attr04',
    content_id: 4,
    title: '창덕궁',
    content_type_id: 12,
    area_code: 1,
    si_gun_gu_code: 23,
    first_image: 'https://via.placeholder.com/300x200?text=창덕궁',
    first_image2: 'https://via.placeholder.com/80?text=창덕궁',
    map_level: 5,
    latitude: 37.582036,
    longitude: 126.991168,
    tel: '02-3668-2300',
    addr1: '서울 종로구 율곡로 99',
    addr2: '',
    homepage: 'http://www.cdg.go.kr',
    overview: '창덕궁은 조선시대 궁궐 중 가장 오랫동안 임금들이 거처한 궁궐로, 자연과 조화를 이룬 아름다운 궁궐이다.'
  },
  {
    id: 'attr05',
    content_id: 5,
    title: '북촌한옥마을',
    content_type_id: 12,
    area_code: 1,
    si_gun_gu_code: 23,
    first_image: 'https://via.placeholder.com/300x200?text=북촌한옥마을',
    first_image2: 'https://via.placeholder.com/80?text=북촌한옥마을',
    map_level: 5,
    latitude: 37.582014,
    longitude: 126.983394,
    tel: '02-2133-1371',
    addr1: '서울 종로구 계동길 37',
    addr2: '',
    homepage: '',
    overview: '북촌한옥마을은 경복궁과 창덕궁 사이에 위치한 전통 한옥마을로, 조선시대 양반들이 거주했던 지역이다.'
  },
  {
    id: 'attr06',
    content_id: 6,
    title: '인사동',
    content_type_id: 38,
    area_code: 1,
    si_gun_gu_code: 23,
    first_image: 'https://via.placeholder.com/300x200?text=인사동',
    first_image2: 'https://via.placeholder.com/80?text=인사동',
    map_level: 5,
    latitude: 37.574297,
    longitude: 126.985302,
    tel: '',
    addr1: '서울 종로구 인사동길',
    addr2: '',
    homepage: '',
    overview: '인사동은 전통문화와 현대문화가 공존하는 문화의 거리로, 다양한 전통공예품과 차문화를 체험할 수 있다.'
  },
  {
    id: 'attr07',
    content_id: 7,
    title: '동대문디자인플라자',
    content_type_id: 14,
    area_code: 1,
    si_gun_gu_code: 25,
    first_image: 'https://via.placeholder.com/300x200?text=DDP',
    first_image2: 'https://via.placeholder.com/80?text=DDP',
    map_level: 5,
    latitude: 37.566569,
    longitude: 127.009033,
    tel: '02-2153-0000',
    addr1: '서울 중구 을지로 281',
    addr2: '',
    homepage: 'http://www.ddp.or.kr',
    overview: '동대문디자인플라자는 자하 하디드가 설계한 현대적인 건축물로, 디자인과 문화의 복합공간이다.'
  },
  {
    id: 'attr08',
    content_id: 8,
    title: '홍대거리',
    content_type_id: 38,
    area_code: 1,
    si_gun_gu_code: 26,
    first_image: 'https://via.placeholder.com/300x200?text=홍대',
    first_image2: 'https://via.placeholder.com/80?text=홍대',
    map_level: 5,
    latitude: 37.556785,
    longitude: 126.922497,
    tel: '',
    addr1: '서울 마포구 와우산로',
    addr2: '',
    homepage: '',
    overview: '홍대거리는 젊음과 예술이 살아 숨쉬는 거리로, 다양한 클럽과 카페, 공연장이 밀집해 있다.'
  },
  {
    id: 'attr09',
    content_id: 9,
    title: '한강공원',
    content_type_id: 12,
    area_code: 1,
    si_gun_gu_code: 27,
    first_image: 'https://via.placeholder.com/300x200?text=한강공원',
    first_image2: 'https://via.placeholder.com/80?text=한강공원',
    map_level: 5,
    latitude: 37.529787,
    longitude: 126.936893,
    tel: '02-3780-0561',
    addr1: '서울 영등포구 여의동로 330',
    addr2: '',
    homepage: '',
    overview: '한강공원은 서울 시민들의 휴식공간으로, 자전거 타기, 피크닉, 수상스포츠 등을 즐길 수 있다.'
  },
  {
    id: 'attr10',
    content_id: 10,
    title: '롯데월드타워',
    content_type_id: 12,
    area_code: 1,
    si_gun_gu_code: 28,
    first_image: 'https://via.placeholder.com/300x200?text=롯데타워',
    first_image2: 'https://via.placeholder.com/80?text=롯데타워',
    map_level: 5,
    latitude: 37.512400,
    longitude: 127.102509,
    tel: '1661-2000',
    addr1: '서울 송파구 올림픽로 300',
    addr2: '',
    homepage: 'http://www.lwt.co.kr',
    overview: '롯데월드타워는 대한민국에서 가장 높은 건물로, 전망대에서 서울 전경을 한눈에 볼 수 있다.'
  }
]);

// 경로에 추가된 장소들
const routePlaces = ref([]);

// 지역 코드에 따른 지역명 반환
const getAreaName = (areaCode) => {
  const areaNames = {
    1: '서울',
    2: '인천',
    3: '대전',
    4: '대구',
    5: '광주',
    6: '부산',
    7: '울산',
    8: '세종',
    31: '경기도',
    32: '강원도',
    33: '충청북도',
    34: '충청남도',
    35: '경상북도',
    36: '경상남도',
    37: '전라북도',
    38: '전라남도',
    39: '제주도'
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

// 장소 상세 정보 표시
const showPlaceDetail = (place) => {
  if (selectedPlace.value && selectedPlace.value.id === place.id && detailPanelOpen.value) {
    closeDetailPanel();
    return;
  }

  selectedPlace.value = place;
  detailPanelOpen.value = true;
  highlightMarker(place);

  // 지도 중심 이동
  map.setCenter(new window.kakao.maps.LatLng(place.latitude, place.longitude));
  map.setLevel(3);
};

// 상세 정보 패널 닫기
const closeDetailPanel = () => {
  detailPanelOpen.value = false;
  resetMarkerHighlight();
};

// 마커 관련 함수들
const highlightMarker = (place) => {
  if (!map) return;

  // 기존 하이라이트 마커 제거
  resetMarkerHighlight();

  // 하이라이트 마커 생성
  const position = new window.kakao.maps.LatLng(place.latitude, place.longitude);

  highlightedMarker = new window.kakao.maps.Marker({
    position: position,
    map: map,
    zIndex: 1
  });
};

const resetMarkerHighlight = () => {
  if (highlightedMarker) {
    highlightedMarker.setMap(null);
    if (highlightedMarker.infoWindow) {
      highlightedMarker.infoWindow.close();
    }
    highlightedMarker = null;
  }
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
    updateMapMarkers();

  }
};

const removeFromRoute = (placeId) => {
  const index = routePlaces.value.findIndex(p => p.id === placeId);
  if (index !== -1) {
    routePlaces.value.splice(index, 1);
    updateMapMarkers();
  }
};

const clearRoute = () => {
  if (confirm('모든 경로를 삭제하시겠습니까?')) {
    routePlaces.value = [];
    updateMapMarkers();
  }
};

// 지도 마커 및 경로선 업데이트
const updateMapMarkers = () => {
  if (!map) return;

  // 기존 마커 및 경로선 제거
  markers.forEach(marker => marker.setMap(null));
  markers = [];

  if (polyline) {
    polyline.setMap(null);
    polyline = null;
  }

  // 새 마커 생성
  routePlaces.value.forEach((place, index) => {
    const position = new window.kakao.maps.LatLng(place.latitude, place.longitude);

    // 커스텀 오버레이 생성
    const content = document.createElement('div');
    content.className = 'highlight-marker';
    content.style = `
    position: relative;
    width: auto;
    height: auto;
  `
    content.innerHTML = `
      <div class="speech-bubble">
        <div class="speech-bubble-content">${index+1}. ${place.title}</div>
        <div class="speech-bubble-arrow"></div>
      </div>
    `;

    // 마커 클릭 이벤트
    content.addEventListener('click', function() {
      showPlaceDetail(place);
    });

    const marker = new window.kakao.maps.CustomOverlay({
      position: position,
      content: content,
      map: map,
      yAnchor: 1.1,
      zIndex: 10 // 마커보다 높게
    });

    markers.push(marker);
  });

  // 경로선 그리기
  if (routePlaces.value.length > 1) {
    const path = routePlaces.value.map(place =>
      new window.kakao.maps.LatLng(place.latitude, place.longitude)
    );

    polyline = new window.kakao.maps.Polyline({
      path: path,
      strokeWeight: 3,
      strokeColor: '#FF6B35',
      strokeOpacity: 0.8,
      strokeStyle: 'solid'
    });

    polyline.setMap(map);
  }

  resetMap();

  // 선택된 장소가 있으면 하이라이트 마커 다시 표시
  if (selectedPlace.value && detailPanelOpen.value) {
    highlightMarker(selectedPlace.value);
  }
};

// 지도 관련 함수들
const zoomIn = () => {
  if (map) {
    const currentZoom = map.getLevel();
    map.setLevel(currentZoom - 1);
  }
};

const zoomOut = () => {
  if (map) {
    const currentZoom = map.getLevel();
    map.setLevel(currentZoom + 1);
  }
};

const resetMap = () => {
  if (map) {
    // 모든 마커가 보이도록 지도 범위 조���
    if (routePlaces.value.length > 0) {
      const bounds = new window.kakao.maps.LatLngBounds();
      routePlaces.value.forEach(place => {
        bounds.extend(new window.kakao.maps.LatLng(place.latitude, place.longitude));
      });
      map.setBounds(bounds);
      map.setLevel(map.getLevel()+1)
    }
  }
};

// 경로 변경 감지
watch(routePlaces, () => {
  updateMapMarkers();
}, { deep: true });

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

  // 초기 마커 및 경로선 설정
  updateMapMarkers();
};

onUnmounted(() => {
  map = null;
});
</script>


<style scoped>
/* 전역 스타일 */
.route-create-page {
  min-height: auto;
  background-color: #f8f9fa;
}

/* 메인 콘텐츠 */
.route-content {
  position: relative;
  height: calc(100vh - 70px);
  overflow: hidden;
}

/* 전체 화면 지도 */
.map-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

#map {
  width: 100%;
  height: 100%;
}

/* 지도 컨트롤 */
.map-controls {
  position: absolute;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 5px;
  z-index: 10;
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

/* 왼쪽 사이드바 오버레이 */
.left-sidebar-overlay {
  position: absolute;
  top: 20px;
  left: 20px;
  width: 400px;
  height: calc(100% - 40px);
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  z-index: 100;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  min-width: fit-content;
}

.left-sidebar-overlay.expanded {
  width: 700px;
}

/* 오른쪽 사이드바 오버레이 */
.right-sidebar-overlay {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 400px;
  height: calc(100% - 40px);
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  z-index: 100;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

/* 사이드바 콘텐츠 */
.sidebar-content {
  padding: 20px;
  min-width: fit-content;
  height: 100%;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

/* 사이드바 레이아웃 */
.sidebar-layout {
  display: flex;
  height: 100%;
  gap: 20px;
  padding: 20px;
}

/* 검색 패널 */
.search-panel {
  width: 350px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow-y: auto;
}

/* 검색 섹션 */
.search-section {
  margin-bottom: 20px;
}

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
  background-color: rgba(255, 255, 255, 0.9);
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
  position: relative;
  overflow: hidden;
}

.btn-primary {
  background-color: #ff6b35;
  color: white;
}

.btn-primary:hover {
  background-color: #e55a2b;
}

.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn-danger:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #c82333;
}

.btn-search {
  background-color: #ff6b35;
  color: white;
}

.btn-ai {
  background-color: #28a745;
  color: white;
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
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
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
  overflow-y: auto;
  flex: 1;
}

.place-card {
  display: flex;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  position: relative;
  cursor: pointer;
  transition: background-color 0.2s;
  border-radius: 6px;
  margin-bottom: 8px;
}

.place-card:hover {
  background-color: rgba(248, 249, 250, 0.8);
}

.place-card.active {
  background-color: rgba(255, 107, 53, 0.1);
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
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.place-address {
  font-size: 0.8rem;
  color: #666;
  margin: 0 0 8px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.place-tags {
  display: flex;
  gap: 5px;
}

.tag {
  background-color: rgba(241, 243, 244, 0.8);
  color: #5f6368;
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 0.75rem;
}

/* 토글 버튼 애니메이션 */
.toggle-route-btn {
  position: absolute;
  right: 10px;
  top: 20%;
  width: 24px;
  height: 60%;
  border: none;
  border-radius: 0.3rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.toggle-route-btn i {
  transition: transform 0.3s ease;
}

.toggle-route-btn.is-add {
  border: 1px solid #ff6b35;
  background-color: #FFFFFF;
  color: #ff6b35;
  //background-color: #ff6b35;
}

.toggle-route-btn.is-remove {
  background-color: #dc3545;
  color: white;
}

.toggle-route-btn.is-add:hover {
  background-color: #e55a2b;
  color: #FFFFFF;
  transform: scale(1.1);
}

.toggle-route-btn.is-remove:hover {
  background-color: #c82333;
  transform: scale(1.1);
}

.toggle-route-btn.is-add i {
  transform: rotate(0deg);
}

.toggle-route-btn.is-remove i {
  transform: rotate(45deg);
}

/* 상세 정보 패널 */
.detail-panel {
  width: 330px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow-y: visible;
  border-left: 1px solid #eee;
  position: relative;
}

.detail-toggle {
  position: absolute;
  top: 50%;
  right: -33px;
  transform: translateY(-50%);
  width: 26px;
  height: 70px;
  background-color: #ff6b35;
  border: none;
  border-radius: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  color: white;
  font-size: 1.2rem;
  transition: all 0.2s ease;
}

.detail-toggle:hover {
  background-color: #e55a2b;
  transform: translateY(-50%) scale(1.05);
}

.detail-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  gap: 15px;
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

.detail-footer {
  padding: 20px;
  border-top: 1px solid #eee;
}

/* 액션 버튼 스타일 */
.btn-action {
  background-color: #ff6b35;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.btn-action:hover {
  background-color: #e55a2b;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.btn-action:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.btn-action::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 5px;
  height: 5px;
  background: rgba(255, 255, 255, 0.5);
  opacity: 0;
  border-radius: 100%;
  transform: scale(1, 1) translate(-50%);
  transform-origin: 50% 50%;
}

.btn-action:hover::after {
  animation: ripple 1s ease-out;
}

@keyframes ripple {
  0% {
    transform: scale(0, 0);
    opacity: 0.5;
  }
  20% {
    transform: scale(25, 25);
    opacity: 0.3;
  }
  100% {
    opacity: 0;
    transform: scale(40, 40);
  }
}

.btn-text {
  margin-right: 8px;
}

.btn-icon {
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 계획 섹션 */
.plan-section {
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
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
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
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
  background-color: rgba(248, 249, 250, 0.8);
  border-radius: 6px;
  border: 1px solid rgba(233, 236, 239, 0.8);
  cursor: grab;
  transition: all 0.2s;
}

.route-item:hover {
  background-color: rgba(240, 240, 240, 0.8);
}

.route-item.active {
  background-color: rgba(255, 107, 53, 0.1);
  border-color: #ff6b35;
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
  background-color: rgba(233, 236, 239, 0.8);
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


@keyframes pop-in {
  0% {
    transform: scale(0.5);
    opacity: 0;
  }
  80% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 반응형 디자인 */
@media (max-width: 1200px) {
  .left-sidebar-overlay {
    width: 300px;
  }

  .left-sidebar-overlay.expanded {
    width: 600px;
  }

  .search-panel {
    width: 300px;
  }

  .detail-panel {
    width: 280px;
  }
}

@media (max-width: 768px) {
  .left-sidebar-overlay,
  .right-sidebar-overlay {
    position: fixed;
    top: 20px;
    left: 10px;
    right: 10px;
    width: auto;
    transform: none;
    max-height: calc(100vh - 40px);
  }

  .right-sidebar-overlay {
    top: 20px;
  }

  .map-controls {
    top: 10px;
    left: 10px;
    transform: none;
  }

  .sidebar-layout {
    flex-direction: column;
  }

  .search-panel,
  .detail-panel {
    width: 100%;
  }

  .detail-panel {
    border-left: none;
    border-top: 1px solid #eee;
    margin-top: 20px;
  }

  .form-row {
    flex-direction: column;
  }

  .search-input-group {
    flex-direction: column;
  }
}

/* 스크롤바 스타일링 */
.sidebar-content::-webkit-scrollbar,
.search-panel::-webkit-scrollbar,
.search-results::-webkit-scrollbar,
.detail-panel::-webkit-scrollbar,
.route-list::-webkit-scrollbar,
.detail-content::-webkit-scrollbar {
  width: 2px;
}

.sidebar-content::-webkit-scrollbar-track,
.search-panel::-webkit-scrollbar-track,
.search-results::-webkit-scrollbar-track,
.detail-panel::-webkit-scrollbar-track,
.route-list::-webkit-scrollbar-track,
.detail-content::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar-content::-webkit-scrollbar-thumb,
.search-panel::-webkit-scrollbar-thumb,
.search-results::-webkit-scrollbar-thumb,
.detail-panel::-webkit-scrollbar-thumb,
.route-list::-webkit-scrollbar-thumb,
.detail-content::-webkit-scrollbar-thumb {
  background: rgba(95, 99, 104, 0.2);
  border-radius: 3px;
}

.sidebar-content::-webkit-scrollbar-thumb:hover,
.search-panel::-webkit-scrollbar-thumb:hover,
.search-results::-webkit-scrollbar-thumb:hover,
.detail-panel::-webkit-scrollbar-thumb:hover,
.route-list::-webkit-scrollbar-thumb:hover,
.detail-content::-webkit-scrollbar-thumb:hover {
  background: rgba(95, 99, 104, 0.4);
}

/* 스크롤바 화살표 제거 */
.sidebar-content::-webkit-scrollbar-button,
.search-panel::-webkit-scrollbar-button,
.search-results::-webkit-scrollbar-button,
.detail-panel::-webkit-scrollbar-button,
.route-list::-webkit-scrollbar-button,
.detail-content::-webkit-scrollbar-button {
  display: none;
}

/* 스크롤바가 필요할 때만 표시 */
.sidebar-content,
.search-panel,
.search-results,
.detail-panel,
.route-list,
.detail-content {
  scrollbar-width: thin;
  scrollbar-color: rgba(95, 99, 104, 0.2) transparent;
}
</style>

<style>
/* 하이라이트 마커 스타일 */
.highlight-marker {
  position: relative;
  width: auto;
  height: auto;
}

.highlight-speech-bubble {
  position: relative;
  background: #28a745;
  color: white;
  border-radius: 8px;
  padding: 8px 12px;
  font-size: 14px;
  font-weight: 600;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  white-space: nowrap;
  animation: pop-in 0.3s ease-out;
  transform-origin: bottom center;
}

.highlight-speech-bubble-content {
  position: relative;
  z-index: 2;
}

.highlight-speech-bubble-arrow {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
  border-top: 8px solid #28a745;
}


.speech-bubble {
  position: relative;
  background: #ff6b35;
  color: white;
  border-radius: 8px;
  padding: 8px 12px;
  font-size: 14px;
  font-weight: 600;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  white-space: nowrap;
  animation: pop-in 0.3s ease-out;
  transform-origin: bottom center;
}

.speech-bubble-content {
  position: relative;
  z-index: 2;
}

.speech-bubble-arrow {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
  border-top: 8px solid #ff6b35;
}
</style>
