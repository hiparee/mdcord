import { fetchChannelList, fetchChannelMemberList } from '@/api/channel.js';
import router from '@/router/routes';
import { useUserStore } from '@/store/store';
import { makeChannelTree } from '@/utils/channel.js';
import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import { useToast } from 'vue-toast-notification';

export const useChannelStore = defineStore(
  'channel',
  () => {
    const channelList = ref([]);
    const serverList = ref([]);
    const memberList = ref({});
    const getChannelList = computed(() => channelList.value);
    const getServerList = computed(() => serverList.value);
    const getMemberList = computed(() => memberList.value);
    const getAccessedChannelInfo = computed(() => accessedChannelInfo.value);
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
      try {
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
        if (
          accessedChannelInfo.value.serverId == null &&
          serverData.length !== 0
        ) {
          accessedChannelInfo.value.serverId = serverList.value[0].id;
        }
      } catch (error) {
        useToast({
          duration: 5000,
          position: 'bottom-right',
          queue: true,
          pauseOnHover: true,
        }).error(
          `<div>접근가능한 채널이 없습니다. 관리자에게 문의하세요</div>`,
        );
        CLEAR_CHANNEL_SESSION();
        useUserStore().userInfo = {};
        router.replace('/');
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
      getAccessedChannelInfo,
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
