import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

// A서버 1번채널에 새로운 채팅 데이터 추가
// const chatData = { ...newChatData };
// store.chatList.value.A서버[1].채팅목록.push(chatData);

export const useChatStore = defineStore('chat', () => {
  const chatList = ref({
    channel: {
      1: [],
      2: [],
    },
  });

  const SET_CHAT_LIST = list => {
    list.forEach(v => {
      chatList.value.channel[v.channelId] = v;
    });
  };

  return {
    chatList,
    SET_CHAT_LIST,
  };
});
