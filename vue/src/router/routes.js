import { createWebHistory, createRouter } from 'vue-router';
import ErrorPage from '@/views/error.vue';
import Home from '@/views/home.vue';
import Login from '@/views/LoginPage.vue';
// import AdmUserCreate from "@/views/AdmUserCreate.vue";
// import AdmUserList from "@/views/AdmUserList.vue";
import { useChannelStore } from '@/store/store.js';
import { useToast } from 'vue-toast-notification';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login,
    },
    {
      path: '/:pathMatch(.*)',
      name: 'not-found',
      component: ErrorPage,
    },
    {
      path: '/main',
      name: 'main',
      component: Home,
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
  console.log(to);
  console.log(from);
  if (to.name !== 'login') {
    try {
      await useChannelStore().SET_CHANNEL_LIST();
    } catch (error) {
      useToast().error('로그인정보 만료');
      next({ name: 'login' });
    }
  }
  next();
});

export default router;
