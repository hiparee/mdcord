<template>
  <div class="chat-skeleton">
    <div
      class="skeleton-wrap"
      v-for="box in skeletonBoxes"
      :key="box.id"
      :class="{ left: box.left, right: !box.left }"
    >
      <div class="skeleton-profile"></div>
      <div class="skeleton-box" :class="{ left: box.left, right: !box.left }">
        <div class="skeleton-msg" :style="{ height: box.height }"></div>
        <div class="skeleton-time"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
function getRandomNumberBetween(start, end) {
  return Math.floor(Math.random() * (end - start + 1)) + start;
}

function generateSkeletonBoxes() {
  const count = getRandomNumberBetween(10, 20);
  const boxes = [];
  for (let i = 0; i < count; i++) {
    boxes.push({
      id: i,
      left: getRandomNumberBetween(0, 10) === 2,
      height: getRandomNumberBetween(30, 200) + 'px',
    });
  }
  // console.log('boxes', boxes);
  return boxes;
}

const skeletonBoxes = generateSkeletonBoxes();
</script>

<style scoped>
.chat-skeleton {
  display: flex;
  flex-direction: column;
  position: absolute;
  bottom: 0;
  overflow: hidden;
  top: 0;
  right: 0;
  left: 0;
  padding: 20px;
  z-index: 800;
  background-color: #32353b;
}

.skeleton-wrap {
  display: flex;
  flex-direction: row;
}
.skeleton-wrap.left {
  flex-direction: row-reverse;
}

.skeleton-profile {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: #f2f2f2;
  background-image: linear-gradient(90deg, #4e4e4e, #3d3d3d, #4e4e4e);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s linear infinite;
}

.skeleton-box {
  display: flex;
  flex: 1;
  flex-direction: column;
  margin-bottom: 20px;
}
.skeleton-msg {
  height: 40px;
  border-radius: 8px;
  background-color: #f2f2f2;
  background-image: linear-gradient(90deg, #4e4e4e, #3d3d3d, #4e4e4e);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s linear infinite;
  padding: 10px;
}

@keyframes skeleton-loading {
  0% {
    background-position: 100% 0;
  }
  100% {
    background-position: -100% 0;
  }
}
.skeleton-box.left .skeleton-msg {
  /* align-self: flex-start; */
  margin-left: 50%;
  margin-right: 10px;
}
.skeleton-box.right .skeleton-msg {
  /* align-self: flex-end; */
  margin-right: 50%;
  margin-left: 10px;
}

.skeleton-box .skeleton-time {
  height: 20px;
  width: 90px;
  border-radius: 8px;
  background-color: #f2f2f2;
  background-image: linear-gradient(90deg, #4e4e4e, #3d3d3d, #4e4e4e);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s linear infinite;
}

.skeleton-box.left .skeleton-time {
  margin: 10px;
  margin-left: auto;
}
.skeleton-box.right .skeleton-time {
  margin: 10px;
  margin-right: auto;
}
</style>
