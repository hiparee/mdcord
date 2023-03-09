import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

// A서버 1번채널에 새로운 채팅 데이터 추가
// const chatData = { ...newChatData };
// store.chatList.value.A서버[1].채팅목록.push(chatData);

export const useChatStore = defineStore('chat', () => {
  const chatList = ref([
    {
      channelId: 1,
      parentId: 9,
      messages: [
        // {
        //   message: '안녕하세요',
        //   name: '레몬',
        //   sendTime: '2023-03-08 10:41:06',
        //   commitTime: '2023-03-08 10:41:06',
        //   iconFileId: '01',
        //   memberId: 'lemon',
        // },
      ],
    },
  ]);

  return {
    chatList,
  };
});
