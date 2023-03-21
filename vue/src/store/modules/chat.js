import { defineStore } from 'pinia';
import { ref, computed, inject } from 'vue';
import { fetchChatlist, fetchMoreChatlist } from '@/api/chat.js';
import { timeAgo } from '@/utils/chat.js';
import { userProfileIcon } from '@/utils/common.js';

export const useChatStore = defineStore('chat', () => {
  const dayjs = inject('dayjs');
  const chatList = ref({
    channels: {
      // 1: [{ chatId: 1 }, { chatId:  }],
      // 2: [{ chatId: 1 }],
    },
  });
  const lastChat = ref({
    // 10: {
    // chatId: 99,
    // fileYn: false
    // }
  });
  /**
   *
   * @param {*} channelId
   * 처음 받아오는 50개의 채팅 리스트 데이터
   */
  const SET_CHAT_LIST = async channelId => {
    const { data } = await fetchChatlist(channelId);
    const chatData = processChatData(data);

    const channels = chatList.value.channels;
    const newChannelData = Object.values(chatData).map(chatList =>
      chatList.reverse(),
    );

    channels[channelId] = [];
    return new Promise(resolve => {
      setTimeout(() => {
        console.log('데이터 적재');
        channels[channelId] = newChannelData
          .concat(channels[channelId] || [])
          .reverse();
        resolve();
      }, 500);
    });
  };

  /**
   *
   * @param {*} channelId
   * scrollTop이 0인경우 호출 더보기 50개 데이터
   */
  const MORE_CHAT_LIST = async channelId => {
    const channels = chatList.value.channels;
    const lastChatId = channels[channelId][0][0].chatId;
    const lastFlag = channels[channelId][0][0].dataLast;

    if (lastFlag) return;

    const { data } = await fetchMoreChatlist(channelId, lastChatId);
    const chatData = processChatData(data);
    console.log(chatData);

    const newChannelData = Object.values(chatData)
      .map(chatList => chatList.reverse())
      .reverse();
    console.log(newChannelData);

    return new Promise(resolve => {
      setTimeout(() => {
        channels[channelId] = newChannelData.concat(channels[channelId]);
        resolve();
      }, 500);
    });
  };

  const processChatData = data => {
    const dataLast = data.last;
    return data.content.reduce((acc, item) => {
      const createDate = dayjs(item.createDate).format('YYYY-MM-DD HH:mm:ss');
      const ymdhm = dayjs(item.createDate).format('YYYYMMDDHHmm');
      const chatKey = `${item.memberId}_${ymdhm}`;

      const pushData = {
        chatId: item.chatId,
        content: item.content,
        memberId: item.memberId,
        name: item.memberName,
        fileYn: item.fileYn,
        iconFileId: userProfileIcon(item.memberIconId),
        createDate: createDate,
        timeText: dayjs(createDate).format('YYYY. MM. DD A HH:mm:ss'),
        timeAgo: timeAgo(createDate),
        dataLast: dataLast,
      };

      if (!acc[chatKey]) {
        acc[chatKey] = [];
      }

      acc[chatKey].push(pushData);
      return acc;
    }, {});
  };

  const SEND_MESSAGE = data => {};

  const RECEIVE_CHAT_DATA = data => {};
  return {
    chatList,
    lastChat,
    MORE_CHAT_LIST,
    SET_CHAT_LIST,
  };
});
