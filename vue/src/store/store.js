import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';

const store = createPinia();
store.use(piniaPluginPersistedstate);

export default store;

export * from './modules/channel';
export * from './modules/chat';
export * from './modules/user';
export * from './modules/socket';
