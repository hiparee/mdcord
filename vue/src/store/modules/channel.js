import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { fetchChannelList } from '../../api/index.js';
import { makeChannelTree } from '../../composables/channel.js';

export const useChannelStore = defineStore('channel', () => {
  const channelList = ref([]);
  const serverList = ref([]);
  const getChannelList = computed(() => channelList.value);
  const getServerList = computed(() => serverList.value);
  const chatInfo = ref({
    title: '',
    chatList: [],
  });

  const SET_CHAT_TITLE = title => {
    chatInfo.value.title = title;
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
    chatInfo,
    SET_CHAT_TITLE,
  };
});
