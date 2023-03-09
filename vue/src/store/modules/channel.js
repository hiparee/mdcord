import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { fetchChannelList } from '@/api/channel.js';
import { makeChannelTree } from '@/composables/channel.js';

export const useChannelStore = defineStore(
  'channel',
  () => {
    const channelList = ref([]);
    const serverList = ref([]);
    const getChannelList = computed(() => channelList.value);
    const getServerList = computed(() => serverList.value);

    // Sidebar에서 선택되어 접속된 chat 정보만 담음
    const onChatInfo = ref({
      title: '',
      channelId: null,
    });

    const SET_CHAT_TITLE = title => {
      onChatInfo.value.title = title;
    };

    const SET_CHANNEL_ID = id => {
      onChatInfo.value.channelId = id;
    };

    const SET_CHANNEL_LIST = async () => {
      const res = await fetchChannelList();
      const data = res.data.channelLists;
      const serverData = [];
      const channelData = [];

      data.forEach(item => {
        item.dept == 0 ? serverData.push(item) : channelData.push(item);
      });

      const channelTreeData = makeChannelTree(channelData);

      serverList.value = serverData;
      channelList.value = channelTreeData;
    };

    return {
      channelList,
      serverList,
      getChannelList,
      getServerList,
      SET_CHANNEL_LIST,
      onChatInfo,
      SET_CHAT_TITLE,
      SET_CHANNEL_ID,
    };
  },
  {
    persist: {
      storage: sessionStorage,
      paths: ['channelList'],
    },
  },
);
