import { channel } from './index';

const fetchChannelList = () => {
  return channel.get('/api/channels');
};

export { fetchChannelList };
