<template>
  <div class="d-flex" id="wrapper">
    <sidebar-component
      v-if="
        isLogin && !route.matched.some(record => record.name === 'settings')
      "
    />
    <router-view></router-view>
  </div>
</template>

<script setup>
import { onMounted, computed } from 'vue';
import SidebarComponent from '@/components/layout/SidebarComponent.vue';
import { webSocketStore } from '@/store/store';
import { useRoute } from 'vue-router';

// console.log(`웹소켓 연결`, webSocketStore().webSocket);

const route = useRoute();
const isLogin = computed(() => {
  return route.matched.some(record => record.meta.requiresAuth);
});

onMounted(() => {});
</script>

<style></style>
