import { user } from './index';

const URL_PREFIX = `${import.meta.env.VITE_APP_BASE_API_URL}/members`;

const fetchLogin = async params => {
  try {
    const res = await user.post(`${URL_PREFIX}/signin`, params);
    console.log(res);
    return res;
  } catch (error) {
    console.log(error);
  }
};

export { fetchLogin };
