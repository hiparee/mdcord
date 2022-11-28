import { createWebHistory, createRouter } from 'vue-router';
import ErrorPage from '@/views/error';
import Home from "@/views/home";
import Login from "@/views/LoginPage";


const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "home",
      component: Home,
    },
    {
      path: "/:pathMatch(.*)",
      name: "not-found",
      component: ErrorPage,
    },
    {
      path: '/login',
      name: "login",
      component: Login,
    }
  ],
});

export default router;