<template>
  <div class="d-flex" id="wrapper">
    <sidebar-component v-if="isLogin()" v-on:pass="emitTest()" />
    <router-view />
  </div>
</template>

<script setup>
import SidebarComponent from '@/components/layout/SidebarComponent.vue';
import { useRoute } from 'vue-router';
// import { ref } from 'vue';
const route = useRoute();
// const msg = ref('안녕하세요');
const isLogin = () => {
  return route.matched.some(record => record.meta.requiresAuth);
};

const emitTest = () => {
  alert(123);
};
</script>

<style></style>
