import { createWebHistory, createRouter } from "vue-router";
import ErrorPage from "@/views/error.vue";
import Home from "@/views/home.vue";
import Login from "@/views/LoginPage.vue";
// import AdmUserCreate from "@/views/AdmUserCreate.vue";
// import AdmUserList from "@/views/AdmUserList.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "login",
      component: Login,
    },
    {
      path: "/:pathMatch(.*)",
      name: "not-found",
      component: ErrorPage,
    },
    {
      path: "/main",
      name: "main",
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

router.beforeEach((to, from, next)=>{
  console.log( to );
  console.log( from );
  console.log( next );
  if(to.name !== 'login') {
    
  }
  next();
})

export default router;
