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

    // 현재 접속된 채널 정보
    const accessedChannelInfo = ref({
      title: '',
      channelId: null,
      serverId: null,
    });

    const SET_ACCESSED_CHANNEL_INFO = (key, val) => {
      console.log(key, val);
      console.log(serverList.value[0].id);
      console.log(accessedChannelInfo.value[key]);
      accessedChannelInfo.value[key] = val;
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

      // 접속시 Server list중 첫번째 server를 deafult 선택해주도록
      if (accessedChannelInfo.value.serverId == null) {
        accessedChannelInfo.value.serverId = serverList.value[0].id;
      }
    };

    return {
      channelList,
      serverList,
      getChannelList,
      getServerList,
      accessedChannelInfo,
      SET_ACCESSED_CHANNEL_INFO,
      SET_CHANNEL_LIST,
    };
  },
  {
    persist: {
      storage: sessionStorage,
      paths: ['channelList'],
    },
  },
);
