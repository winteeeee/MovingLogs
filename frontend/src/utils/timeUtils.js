/**
 * 초 단위 시간을 시간과 분 형식으로 변환
 * @param {number} seconds - 초 단위 시간
 * @returns {string} - 포맷팅된 시간 문자열
 */
export function formatTime(seconds) {
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)

  if (hours > 0) {
    return `${hours}시간 ${minutes}분`
  } else {
    return `${minutes}분`
  }
}

/**
 * 날짜 형식 변환 (YYYY-MM-DD)
 * @param {Date|string} date - 날짜 객체 또는 문자열
 * @returns {string} - YYYY-MM-DD 형식의 문자열
 */
export function formatDate(date) {
  if (!date) return ''

  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')

  return `${year}-${month}-${day}`
}

/**
 * 상대적 시간 표시 (예: '3일 전', '방금 전')
 * @param {Date|string} date - 날짜 객체 또는 문자열
 * @returns {string} - 상대적 시간 문자열
 */
export function getRelativeTime(date) {
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
    return formatDate(date)
  }
}
