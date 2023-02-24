import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useChannelStore = defineStore('channel', () => {
    const channelList = ref([]);

    const getChannelList = computed(() => channelList.value);

    const SET_CHANNEL_LIST = state => {
      channelList.value = state;
    };

    return {
      channelList,
      getChannelList,
      SET_CHANNEL_LIST
    };
  },
);
