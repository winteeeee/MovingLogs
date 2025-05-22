/**
 * 두 지점 간의 거리 계산 (유클리드 거리)
 * @param {Object} spot1 - 첫 번째 지점 (lat, lng 속성 필요)
 * @param {Object} spot2 - 두 번째 지점 (lat, lng 속성 필요)
 * @returns {number} - 거리 (미터 단위)
 */
export function calculateDistance(spot1, spot2) {
  // 위도 1도 = 약 111km
  const latDistance = (spot1.lat - spot2.lat) * 111000

  // 경도 1도 = 약 88.4km (서울 기준)
  const lngDistance = (spot1.lng - spot2.lng) * 88400

  // 유클리드 거리 계산
  return Math.sqrt(latDistance * latDistance + lngDistance * lngDistance)
}

/**
 * 경로 최적화 (최근접 이웃 알고리즘)
 * @param {Array} waypoints - 경유지 배열
 * @param {Object} start - 출발지
 * @returns {Array} - 최적화된 경유지 배열
 */
export function optimizeRouteOrder(waypoints, start) {
  if (waypoints.length <= 1) return [...waypoints]

  // 거리 행렬 계산
  const distanceMatrix = []

  for (let i = 0; i < waypoints.length; i++) {
    distanceMatrix[i] = []
    for (let j = 0; j < waypoints.length; j++) {
      if (i === j) {
        distanceMatrix[i][j] = 0
      } else {
        distanceMatrix[i][j] = calculateDistance(waypoints[i], waypoints[j])
      }
    }
  }

  // 시작점에서의 거리 계산
  const startDistances = waypoints.map((waypoint) => calculateDistance(start, waypoint))

  // 최근접 이웃 알고리즘 (Nearest Neighbor)
  const visited = new Array(waypoints.length).fill(false)

  // 시작점에서 가장 가까운 지점을 첫 번째로 선택
  let minDist = Infinity
  let currentIndex = -1

  for (let i = 0; i < startDistances.length; i++) {
    if (startDistances[i] < minDist) {
      minDist = startDistances[i]
      currentIndex = i
    }
  }

  const optimizedIndices = [currentIndex]
  visited[currentIndex] = true

  // 나머지 지점 순서 결정
  while (optimizedIndices.length < waypoints.length) {
    currentIndex = optimizedIndices[optimizedIndices.length - 1]
    minDist = Infinity
    let nextIndex = -1

    for (let i = 0; i < waypoints.length; i++) {
      if (!visited[i] && distanceMatrix[currentIndex][i] < minDist) {
        minDist = distanceMatrix[currentIndex][i]
        nextIndex = i
      }
    }

    if (nextIndex !== -1) {
      optimizedIndices.push(nextIndex)
      visited[nextIndex] = true
    }
  }

  // 최적화된 경로 생성
  return optimizedIndices.map((index) => waypoints[index])
}
