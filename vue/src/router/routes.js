import { createWebHistory, createRouter } from 'vue-router';
import ErrorView from '@/views/ErrorView.vue';
import ContentsView from '@/views/ContentsView.vue';
import MainView from '@/views/MainView.vue';
import LoginView from '@/views/LoginView.vue';
import { useChannelStore } from '@/store/store.js';
import { useToast } from 'vue-toast-notification';
import RegisterView from '@/views/RegisterView.vue';
import SettingsView from '@/views/SettingsView.vue';
import UserListView from '@/views/UserListView.vue';
import ChannelView from '@/views/ChannelView.vue';
import { webSocketStore } from '@/store/store';

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
      children: [
        {
          path: 'adm/user',
          component: UserListView,
        },
        {
          path: 'adm/user/register',
          component: RegisterView,
        },
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
      const socket = webSocketStore().websocket;
      if (socket.readyState != 0 && socket.readyState != 1) {
        webSocketStore().WEB_SOCKET_CONNECT();
      }

      console.log(
        'websocket ********',
        webSocketStore().websocket,
        webSocketStore().websocket.readyState,
        typeof webSocketStore().websocket,
      );

      await useChannelStore().SET_CHANNEL_LIST();
      console.log(from, to);
    } catch (error) {
      useToast().error('로그인정보 만료');
      next({ name: 'login' });
    }
  }
  next();
});

export default router;
