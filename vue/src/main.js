import { createApp } from 'vue';
import router from './router/routes.js';

import '@/assets/css/index.css';
import '@/assets/css/style.css';
import 'bootstrap/dist/js/bootstrap.js';
import 'bootstrap-icons/font/bootstrap-icons.css';
import 'vue-toast-notification/dist/theme-sugar.css';
import App from './App.vue';
import store from '@/store/store.js';

const app = createApp(App);
app.use(router);
app.use(store);
app.mount('#app');

// let instance = $toast.success('You did it!');
