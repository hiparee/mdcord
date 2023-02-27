import axios from 'axios';

const instance = axios.create({
  withCredentials: true,
  baseURL: import.meta.env.VITE_APP_API_URL,
});

const fetchLogin = async params => {
  try {
    const res = await instance.post('/api/members/signin', params);
    console.log(res);
    return res;
  } catch (error) {
    console.log(error);
  }
};

const fetchChannelList = () => {
  return instance.get('/api/channels');
};

export { fetchLogin, fetchChannelList };
