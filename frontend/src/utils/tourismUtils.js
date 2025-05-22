/**
 * 관광지 타입에 따른 클래스 반환
 * @param {string} type - 관광지 타입
 * @returns {string} - CSS 클래스명
 */
export function getSpotTypeClass(type) {
  const typeClasses = {
    attraction: 'type-attraction',
    culture: 'type-culture',
    festival: 'type-festival',
    course: 'type-course',
    leports: 'type-leports',
    lodging: 'type-lodging',
    shopping: 'type-shopping',
    restaurant: 'type-restaurant',
  }

  return typeClasses[type] || 'type-default'
}

/**
 * 관광지 타입 이름 반환
 * @param {string} type - 관광지 타입
 * @returns {string} - 타입 이름
 */
export function getSpotTypeName(type) {
  const typeNames = {
    attraction: '관광지',
    culture: '문화시설',
    festival: '축제행사',
    course: '여행코스',
    leports: '레포츠',
    lodging: '숙박',
    shopping: '쇼핑',
    restaurant: '음식점',
  }

  return typeNames[type] || '기타'
}
