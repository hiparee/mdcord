<template>
  <settings-sidebar-component />
  <div class="settings-content overflow-auto settings-bg">
    <router-view></router-view>
  </div>
  <div class="exit-button settings-bg">
    <div
      class="close-button"
      aria-label="닫기"
      role="button"
      tabindex="0"
      @click.stop="router.go(-1)"
    >
      <i class="bi bi-x-circle"></i>
    </div>
    <div class="close-text">ESC</div>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router';
import SettingsSidebarComponent from '@/components/layout/SettingsSidebarComponent.vue';
const router = useRouter();
</script>

<style scoped>
.settings-content {
  display: flex;
  flex-direction: column;
  flex: 1 1 auto;
  height: 100%;
  overflow: hidden;
  padding: 60px 25px 8px;
}
.exit-button {
  color: #b8b9bf;
  background-color: transparent;
  border: 0;
  width: 8rem;
  text-align: center;
  margin-top: 1rem;
}
.close-button {
  font-size: 2.5rem;
  height: 57px;
}
.close-text {
  font-weight: 500;
  font-size: 14px;
}
.settings-bg {
  background-color: #313338;
}
</style>
