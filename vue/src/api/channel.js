import { channel } from '@/api/index';

const fetchChannelList = () => {
  return channel.get('/api/channels');
};
// 채널 이름 수정,비활성화
const fetchEditChannelName = (params) => {
  return channel.put('/api/channels',{...params})
}
export { fetchChannelList, fetchEditChannelName };
