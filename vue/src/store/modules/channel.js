import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { fetchChannelList, fetchChannelMemberList } from '@/api/channel.js';
import { makeChannelTree } from '@/utils/channel.js';

export const useChannelStore = defineStore(
  'channel',
  () => {
    const channelList = ref([]);
    const serverList = ref([]);
    const memberList = ref({});
    const getChannelList = computed(() => channelList.value);
    const getServerList = computed(() => serverList.value);
    const getMemberList = computed(() => memberList.value);

    const SET_MEMBER_LIST = async () => {
      const { data } = await fetchChannelMemberList();
      memberList.value = {};
      for (const obj of data) {
        const channelId = obj.channelId;

        if (
          !Object.prototype.hasOwnProperty.call(memberList.value, channelId)
        ) {
          memberList.value[channelId] = [];
        }
        // console.log(`${channelId} 에 push 함`);
        memberList.value[channelId].push(obj);
      }
    };

    // 현재 접속된 채널 정보
    const accessedChannelInfo = ref({
      title: '',
      channelId: null,
      serverId: null,
    });

    const SET_ACCESSED_CHANNEL_INFO = (key, val) => {
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

    const CLEAR_CHANNEL_SESSION = () => {
      channelList.value = [];
      accessedChannelInfo.value = {
        title: '',
        channelId: null,
        serverId: null,
      };
    };

    return {
      channelList,
      serverList,
      memberList,
      getChannelList,
      getServerList,
      getMemberList,
      accessedChannelInfo,
      SET_ACCESSED_CHANNEL_INFO,
      SET_CHANNEL_LIST,
      SET_MEMBER_LIST,
      CLEAR_CHANNEL_SESSION,
    };
  },
  {
    persist: {
      storage: sessionStorage,
      paths: ['channelList', 'accessedChannelInfo'],
    },
  },
);
