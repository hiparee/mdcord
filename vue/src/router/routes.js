import { createWebHistory, createRouter } from 'vue-router';
import ErrorView from '@/views/ErrorView.vue';
import ContentsView from '@/views/ContentsView.vue';
import MainView from '@/views/MainView.vue';
import LoginView from '@/views/LoginView.vue';
import {
  useChannelStore,
  useUserStore,
  webSocketStore,
} from '@/store/store.js';
import { useToast } from 'vue-toast-notification';
import SettingsView from '@/views/SettingsView.vue';
import UserListView from '@/views/UserListView.vue';
import ChannelView from '@/views/ChannelView.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/:pathMatch(.*)',
      name: 'not-found',
      meta: { requiresAuth: false },
      component: ErrorView,
    },
    {
      path: '/',
      name: 'login',
      meta: { requiresAuth: false },
      component: LoginView,
      beforeEnter: (to, from, next) => {
        Object.entries(useUserStore().userInfo).length !== 0
          ? next('/channels')
          : next();
      },
    },
    {
      path: '/channels',
      name: 'channelsMain',
      component: MainView,
      meta: { requiresAuth: true },
    },
    {
      path: '/channels/:id',
      name: 'channels',
      meta: { requiresAuth: true },
      component: ContentsView,
    },
    {
      path: '/settings',
      name: 'settings',
      meta: { requiresAuth: true },
      component: SettingsView,
      beforeEnter: (to, from, next) => {
        if (useUserStore().userInfo.role === 'USER') {
          next('/');
        } else {
          next();
        }
      },
      redirect: '/settings/member',
      children: [
        {
          path: 'member',
          component: UserListView,
        },
        /* TODO :: adm/ 경로 빼주기 */
        {
          path: 'adm/channel',
          component: ChannelView,
        },
      ],
    },
  ],
});

router.beforeEach(async (to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    try {
      if (Object.entries(useUserStore().userInfo).length === 0) {
        router.push('/');
      }

      const socket = webSocketStore().websocket;
      if (socket.readyState != 0 && socket.readyState != 1) {
        webSocketStore().WEB_SOCKET_CONNECT();
      }

      await useChannelStore().SET_CHANNEL_LIST();
      // console.log(from, to);
    } catch (error) {
      console.log(error);
      useToast().error('로그인정보 만료');
      next({ name: 'login' });
    }
  }
  next();
});

export default router;
