import { createWebHistory, createRouter } from 'vue-router';
import ErrorView from '@/views/ErrorView.vue';
import ContentsView from '@/views/ContentsView.vue';
import MainView from '@/views/MainView.vue';
import LoginView from '@/views/LoginView.vue';
// import AdmUserCreate from "@/views/AdmUserCreate.vue";
// import AdmUserList from "@/views/AdmUserList.vue";
import { useChannelStore } from '@/store/store.js';
import { useToast } from 'vue-toast-notification';

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
    // {
    //   path: "/adm/create",
    //   name: "userCreate",
    //   component: AdmUserCreate,
    // },
    // {
    //   path: "/adm/user",
    //   name: "userList",
    //   component: AdmUserList,
    // },
  ],
});

router.beforeEach(async (to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    try {
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
