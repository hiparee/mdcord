import { chat } from '@/api/index';

const fetchMultiFileUpload = async formData => {
  try {
    const res = await chat.post('/api/fileUpload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    console.log(res);
    return res;
  } catch (error) {
    console.log(error);
  }
};

export { fetchMultiFileUpload };
