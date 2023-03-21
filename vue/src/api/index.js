import axios from 'axios';
import interceptors from '@/api/config/interceptors.js';

// create Axios Instance
const create = () => {
  // instance config
  const instance = axios.create({
    withCredentials: true,
    baseURL: import.meta.env.VITE_APP_API_URL,
  });
  interceptors(instance);
  return instance;
};

// create api modules
export const user = create();
export const channel = create();
export const chat = create();
