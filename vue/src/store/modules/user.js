import { defineStore } from 'pinia';
import { ref } from 'vue';
import { webSocketStore } from '@/store/modules/socket';
import router from '@/router/routes';
import { useToast } from 'vue-toast-notification';
import { useChannelStore } from '@/store/modules/channel';
export const useUserStore = defineStore(
  'user',
  () => {
    const userInfo = ref({});

    const SET_SIGN_OUT = () => {
      // 소켓 닫고 userInfo 초기화
      webSocketStore().websocket.close();
      userInfo.value = {};
      useChannelStore().CLEAR_CHANNEL_SESSION();
    };

    return {
      userInfo,
      SET_SIGN_OUT,
    };
  },
  // pinia persistedstate 설정
  {
    persist: {
      storage: sessionStorage,
      paths: ['userInfo'],
    },
  },
);
