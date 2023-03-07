import { channel } from '@/api/index';

const fetchChannelList = () => {
  return channel.get('/api/channels');
};

export { fetchChannelList };
