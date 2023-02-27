<template>
  <div class="d-flex" id="wrapper">
    <PageSideBar v-if="isLogin()" v-on:pass="emitTest()" />
    <router-view />
  </div>
</template>

<script setup>
import PageSideBar from '@/components/layout/Sidebar.vue';
import { useRoute } from 'vue-router';
// import { ref } from 'vue';
const route = useRoute();
// const msg = ref('안녕하세요');
const isLogin = () => {
  return (route.name || 'login') != 'login';
};

const emitTest = () => {
  alert(123);
};
</script>

<style></style>
