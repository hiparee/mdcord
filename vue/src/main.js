import { createApp } from "vue";
// import axios from "axios";
import router from "./router/routes.js";
// import "bootstrap/dist/css/bootstrap.css";

import "@/assets/css/index.css";
import "bootstrap/dist/js/bootstrap.js";
import "bootstrap-icons/font/bootstrap-icons.css";
import App from "./App.vue";

const app = createApp(App);
// app.config.globalProperties.$axios = axios;
app.use(router);
// app.provide(axios);
// app.use(axios);
app.mount("#app");
