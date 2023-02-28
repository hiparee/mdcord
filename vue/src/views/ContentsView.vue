<template>
  <chat-component></chat-component>
</template>

<script setup>
import ChatComponent from '@/components/ChatComponent.vue';
import { onBeforeMount, watch } from 'vue';
import { useRoute } from 'vue-router';
import { useChannelStore } from '@/store/store.js';

const route = useRoute();
const setChatInfo = () => {
  // Channel Title 설정
  setChatTitle();

  // Chant 정보 불러오기
  // --
};

const setChatTitle = () => {
  const channelList = useChannelStore().getChannelList;
  channelList.forEach(item => {
    item.subChannel.forEach(sub => {
      if (sub.id == route.params.id) {
        useChannelStore().SET_CHAT_TITLE(sub.name);
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
