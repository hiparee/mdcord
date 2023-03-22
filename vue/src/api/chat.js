import { chat } from './index';

const fetchMultiFileUpload = async formData => {
  try {
    const res = await chat.post('/api/chat/file-upload', formData, {
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

const fetchChatlist = async channelId => {
  try {
    const res = await chat.get(`/api/channels/${channelId}/chat`);
    // console.log(res);
    return res;
  } catch (error) {
    console.log(error);
  }
};

const fetchMoreChatlist = async (channelId, chatId) => {
  try {
    const res = await chat.get(
      `/api/channels/${channelId}/chat?chatId=${chatId}`,
    );
    console.log(res);
    return res;
  } catch (error) {
    console.log(error);
  }
};

export { fetchMultiFileUpload, fetchChatlist, fetchMoreChatlist };
