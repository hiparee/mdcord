import { createWebHistory, createRouter } from "vue-router";
import ErrorPage from "@/views/error.vue";
import Home from "@/views/home.vue";
import Login from "@/views/LoginPage.vue";
// import AdmUserCreate from "@/views/AdmUserCreate.vue";
// import AdmUserList from "@/views/AdmUserList.vue";

const router = createRouter({
  history: createWebHistory(),
  base: "/vue/",
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
      path: "/login",
      name: "login",
      component: Login,
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

export default router;
