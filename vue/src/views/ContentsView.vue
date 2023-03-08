<template>
  <chat-component :isLoading="isLoading"></chat-component>
</template>

<script setup>
import ChatComponent from '@/components/ChatComponent.vue';
import { onBeforeMount, watch, ref } from 'vue';
import { useRoute } from 'vue-router';
import { useChannelStore } from '@/store/store.js';
const isLoading = ref(true);
const route = useRoute();

const setChatInfo = () => {
  isLoading.value = true;

  // Channel Title 설정
  setChatTitle();

  // Chant 정보 불러오기
  // --

  // chatlist callback에서 실행
  setTimeout(() => {
    isLoading.value = false;
  }, 800);
};

const setChatTitle = () => {
  const channelList = useChannelStore().getChannelList;
  channelList.forEach(item => {
    item.subChannel.forEach(sub => {
      if (sub.id == route.params.id) {
        useChannelStore().SET_CHAT_TITLE(sub.name);
        useChannelStore().SET_CHANNEL_ID(sub.id);
      }
    });
  });
};

onBeforeMount(() => {
  setChatInfo();
});

watch(
  () => route.params,
  () => {
    setChatInfo();
  },
);
</script>

<style></style>
