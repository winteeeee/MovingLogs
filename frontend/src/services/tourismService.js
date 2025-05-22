/**
 * 시도 목록 조회
 * @returns {Promise<Array>} - 시도 목록
 */
export async function getRegions() {
  // 실제로는 API 호출
  return [
    { code: 'seoul', name: '서울시' },
    { code: 'gyeonggi', name: '경기도' },
    { code: 'incheon', name: '인천시' },
    { code: 'gangwon', name: '강원도' },
    { code: 'chungbuk', name: '충청북도' },
    { code: 'chungnam', name: '충청남도' },
    { code: 'daejeon', name: '대전시' },
    { code: 'sejong', name: '세종시' },
    { code: 'gyeongbuk', name: '경상북도' },
    { code: 'gyeongnam', name: '경상남도' },
    { code: 'daegu', name: '대구시' },
    { code: 'ulsan', name: '울산시' },
    { code: 'busan', name: '부산시' },
    { code: 'jeonbuk', name: '전라북도' },
    { code: 'jeonnam', name: '전라남도' },
    { code: 'gwangju', name: '광주시' },
    { code: 'jeju', name: '제주도' },
  ]
}

/**
 * 구군 목록 조회
 * @param {string} regionCode - 시도 코드
 * @returns {Promise<Array>} - 구군 목록
 */
export async function getDistricts(regionCode) {
  // 실제로는 API 호출
  if (regionCode === 'seoul') {
    return [
      { code: 'gangnam', name: '강남구' },
      { code: 'gangdong', name: '강동구' },
      { code: 'gangbuk', name: '강북구' },
      { code: 'gangseo', name: '강서구' },
      { code: 'gwanak', name: '관악구' },
      { code: 'gwangjin', name: '광진구' },
      { code: 'guro', name: '구로구' },
      { code: 'geumcheon', name: '금천구' },
      { code: 'nowon', name: '노원구' },
      { code: 'dobong', name: '도봉구' },
      { code: 'dongdaemun', name: '동대문구' },
      { code: 'dongjak', name: '동작구' },
      { code: 'mapo', name: '마포구' },
      { code: 'seodaemun', name: '서대문구' },
      { code: 'seocho', name: '서초구' },
      { code: 'seongdong', name: '성동구' },
      { code: 'seongbuk', name: '성북구' },
      { code: 'songpa', name: '송파구' },
      { code: 'yangcheon', name: '양천구' },
      { code: 'yeongdeungpo', name: '영등포구' },
      { code: 'yongsan', name: '용산구' },
      { code: 'eunpyeong', name: '은평구' },
      { code: 'jongno', name: '종로구' },
      { code: 'jung', name: '중구' },
      { code: 'jungnang', name: '중랑구' },
    ]
  } else if (regionCode === 'busan') {
    return [
      { code: 'gangseo', name: '강서구' },
      { code: 'geumjeong', name: '금정구' },
      { code: 'gijang', name: '기장군' },
      { code: 'nam', name: '남구' },
      { code: 'dong', name: '동구' },
      { code: 'dongnae', name: '동래구' },
      { code: 'busanjin', name: '부산진구' },
      { code: 'buk', name: '북구' },
      { code: 'sasang', name: '사상구' },
      { code: 'saha', name: '사하구' },
      { code: 'seo', name: '서구' },
      { code: 'suyeong', name: '수영구' },
      { code: 'yeonje', name: '연제구' },
      { code: 'yeongdo', name: '영도구' },
      { code: 'jung', name: '중구' },
      { code: 'haeundae', name: '해운대구' },
    ]
  }

  return []
}

/**
 * 관광지 타입 목록 조회
 * @returns {Promise<Array>} - 관광지 타입 목록
 */
export async function getTourismTypes() {
  // 실제로는 API 호출
  return [
    { code: 'attraction', name: '관광지' },
    { code: 'culture', name: '문화시설' },
    { code: 'festival', name: '축제공연행사' },
    { code: 'course', name: '여행코스' },
    { code: 'leports', name: '레포츠' },
    { code: 'lodging', name: '숙박' },
    { code: 'shopping', name: '쇼핑' },
    { code: 'restaurant', name: '음식점' },
  ]
}

/**
 * 관광지 검색
 * @param {Object} params - 검색 파라미터
 * @returns {Promise<Array>} - 검색 결과
 */
export async function searchSpots(params) {
  // 실제로는 API 호출
  const dummyResults = [
    {
      id: 1,
      name: '경복궁',
      type: 'attraction',
      address: '서울특별시 종로구 사직로 161',
      lat: 37.579617,
      lng: 126.977041,
      description: '조선 왕조의 법궁(法宮)으로, 1395년에 창건되었다.',
    },
    {
      id: 2,
      name: '국립중앙박물관',
      type: 'culture',
      address: '서울특별시 용산구 서빙고로 137',
      lat: 37.523281,
      lng: 126.980642,
      description: '한국의 역사와 예술을 소개하는 국립박물관이다.',
    },
    {
      id: 3,
      name: '남산서울타워',
      type: 'attraction',
      address: '서울특별시 용산구 남산공원길 105',
      lat: 37.551245,
      lng: 126.988222,
      description: '서울의 상징적인 타워로, 전망대에서 서울 시내를 한눈에 볼 수 있다.',
    },
    {
      id: 4,
      name: '롯데월드',
      type: 'leports',
      address: '서울특별시 송파구 올림픽로 240',
      lat: 37.511361,
      lng: 127.098306,
      description: '실내외 테마파크로, 다양한 놀이기구와 공연을 즐길 수 있다.',
    },
    {
      id: 5,
      name: '명동성당',
      type: 'culture',
      address: '서울특별시 중구 명동길 74',
      lat: 37.563545,
      lng: 126.987608,
      description: '서울대교구 주교좌 성당으로, 고딕 양식의 건축물이다.',
    },
    {
      id: 6,
      name: '서울숲',
      type: 'attraction',
      address: '서울특별시 성동구 뚝섬로 273',
      lat: 37.544167,
      lng: 127.037778,
      description: '도심 속 생태공원으로, 다양한 테마의 정원과 산책로가 있다.',
    },
    {
      id: 7,
      name: '한강공원',
      type: 'leports',
      address: '서울특별시 영등포구 여의동로 330',
      lat: 37.526389,
      lng: 126.934167,
      description: '한강변을 따라 조성된 공원으로, 다양한 레저 활동을 즐길 수 있다.',
    },
    {
      id: 8,
      name: '서울시립미술관',
      type: 'culture',
      address: '서울특별시 중구 덕수궁길 61',
      lat: 37.565833,
      lng: 126.975278,
      description: '현대 미술 작품을 전시하는 미술관으로, 다양한 기획전이 열린다.',
    },
  ]

  // 검색 조건에 따라 필터링
  let filteredResults = [...dummyResults]

  if (params.region) {
    // 실제로는 API에서 필터링
  }

  if (params.district) {
    // 실제로는 API에서 필터링
  }

  if (params.type) {
    filteredResults = filteredResults.filter((spot) => spot.type === params.type)
  }

  if (params.keyword) {
    const keyword = params.keyword.toLowerCase()
    filteredResults = filteredResults.filter(
      (spot) =>
        spot.name.toLowerCase().includes(keyword) || spot.address.toLowerCase().includes(keyword),
    )
  }

  return filteredResults
}
