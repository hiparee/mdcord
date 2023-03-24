import { createApp } from 'vue';
import router from './router/routes.js';

import '@/assets/css/index.css';
import '@/assets/css/style.css';
import 'bootstrap/dist/js/bootstrap.js';
import 'bootstrap-icons/font/bootstrap-icons.css';
import 'vue-toast-notification/dist/theme-sugar.css';
import { VueDraggableNext } from 'vue-draggable-next';
import VueContextMenu from 'vue-simple-context-menu';
import 'vue-simple-context-menu/dist/vue-simple-context-menu.css';
import App from './App.vue';
import store from '@/store/store.js';
import plugins from '@/plugins';

const app = createApp(App);
app.component('VueDraggable', VueDraggableNext);
app.component('VueContextMenu', VueContextMenu);

app.use(router);
app.use(store);
app.use(plugins);
app.mount('#app');
