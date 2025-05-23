<template>
  <div class="selected-spots-list">
    <RouteCreateRouteSpotItem
      v-for="(spot, index) in spots"
      :key="spot.id"
      :spot="spot"
      :index="index"
      :is-first="index === 0"
      :is-last="index === spots.length - 1"
      @move-up="moveUp"
      @move-down="moveDown"
      @remove="remove"
      @focus="focus"
    />
  </div>
</template>

<script setup>
import PlanningWaypointItem from './PlanningWaypointItem.vue'

const props = defineProps({
  spots: {
    type: Array,
    required: true,
  },
})

const emit = defineEmits(['move-up', 'move-down', 'remove', 'focus'])

// 관광지 순서 위로 이동
function moveUp(index) {
  emit('move-up', index)
}

// 관광지 순서 아래로 이동
function moveDown(index) {
  emit('move-down', index)
}

// 관광지 제거
function remove(index) {
  emit('remove', index)
}

// 관광지 포커스
function focus(spot) {
  emit('focus', spot)
}
</script>

<style scoped>
.selected-spots-list {
  display: flex;
  flex-direction: column;
}
</style>
