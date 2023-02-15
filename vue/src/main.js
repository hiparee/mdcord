import { createApp } from "vue";

import router from "./router/routes.js";
// import "bootstrap/dist/css/bootstrap.css";
import "@/assets/css/index.css";
import "@/assets/css/custom.css";
import "bootstrap/dist/js/bootstrap.js";
import "bootstrap-icons/font/bootstrap-icons.css";
import App from "./App.vue";

createApp(App).use(router).mount("#app");
