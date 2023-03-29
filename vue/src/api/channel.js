import { channel } from './index';

/**
 * 채널목록조회
 * @returns
 */
const fetchChannelList = () => {
  return channel.get('/api/channels');
};
// 서버, 채널 이름 수정,비활성화
const fetchEditChannelName = params => {
  return channel.put('/api/channels', { ...params });
};

// 서버, 채널 추가
const fetchAddChanneList = params => {
  return channel.post('/api/channels', { ...params });
};

/**
 * 소속 채널의 인원 목록 조회
 * @returns
 */
const fetchChannelMemberList = async () => {
  return channel.get('/api/channel-member');
};

export {
  fetchChannelList,
  fetchEditChannelName,
  fetchChannelMemberList,
  fetchAddChanneList,
};
