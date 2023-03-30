import { signOutUser } from '@/api/user';
import { useChannelStore, useChatStore, useUserStore } from '@/store/store';
import { userProfileIcon } from '@/utils/common.js';
import { askNotificationPermission, notify } from '@/utils/notification.js';
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { useToast } from 'vue-toast-notification';

import router from '@/router/routes';
import dayjs from 'dayjs';

export const webSocketStore = defineStore('socket', () => {
  // const dayjs = inject('dayjs');
  const memberId = useUserStore().userInfo.memberId;
  const socketUrl = import.meta.env.VITE_APP_WEB_SOCKET_URL;

  const websocket = ref({});
  const WEB_SOCKET_CONNECT = () => {
    websocket.value = new WebSocket(`${socketUrl}?memberId=${memberId}`);

    websocket.value.onopen = () => {
      console.log('%c# [WebSocket] : connected', 'color:green');
      askNotificationPermission();
    };

    websocket.value.onclose = async event => {
      console.log('%c# [WebSocket] : close', 'color:red');

      if (Object.entries(useUserStore().userInfo).length == 0) {
        useToast({
          duration: 5000,
          position: 'bottom-right',
          queue: true,
          pauseOnHover: true,
        }).error(`<div>로그아웃 되었습니다.</div>`);
      } else {
        useToast({
          duration: 5000,
          position: 'bottom-right',
          queue: true,
          pauseOnHover: true,
        }).error(`<div>연결이 종료되었습니다. 다시 로그인을 해주세요</div>`);
        useChannelStore().CLEAR_CHANNEL_SESSION();
        useUserStore().userInfo = {};
        router.replace('/');
      }
    };

    websocket.value.onmessage = ({ data }) => {
      console.log('▶ websocket.value.onmessage', JSON.parse(data));
      const parseData = JSON.parse(data);
      const accessedChannelId =
        useChannelStore().accessedChannelInfo.channelId ?? '';

      if (parseData.messageType == 'ACCESS') {
        for (const [key, val] of Object.entries(parseData.messageInfo)) {
          // console.log(`${key} is ${val}`);
          const memberList = useChannelStore().memberList;
          Object.values(memberList).forEach(objects => {
            objects.forEach(obj => {
              if (obj.memberId === key) {
                obj.state = val;
              }
            });

            // console.log('objects.sort 시작');
            // objects.sort((a, b) => {
            //   if (a.state !== b.state) {
            //     return b.state.localeCompare(a.state);
            //   }
            //   return a.memberName.localeCompare(b.memberName);
            // });
          });
        }
      } else if (parseData.messageType == 'FILE') {
        const channelId = parseData.channelId;
        const chatId = parseData.chatId;
        const fileData = JSON.parse(parseData.fileList);

        // console.log('file', fileData);
        // console.log('chatId', chatId);
        // console.log('channelId', channelId);

        const channelData = useChatStore().chatList.channels[channelId];

        [...channelData].reverse().forEach(v => {
          // console.log(v);
          const targetData = v.find(e => e.chatId === chatId);
          if (targetData) {
            console.log('targetData ->', targetData);
            targetData.attachFileList.push(...fileData);

            return false;
          }
        });
      } else if (parseData.messageType == 'SEND') {
        console.log('수신데이터', parseData);
        const parseChannelId = parseData.channelId;
        const parseChannelName = parseData.channelName;

        if (parseChannelId != accessedChannelId) {
          const $toast = useToast({
            duration: 1500,
            position: 'bottom-right',
            queue: true,
            pauseOnHover: true,
            onClick() {
              router.push(`/channels/${parseChannelId}`);
            },
          });
          $toast.success(`<div>${parseChannelName}</div>
            <div style='max-width:200px;overflow:hidden;text-overflow: ellipsis;
      white-space: nowrap;'>${parseData.content}</div>`);
        }

        if (document.visibilityState != 'visible') {
          notify(parseChannelName, parseData.content);
        }

        const createDate = dayjs(parseData.commitTime).format(
          'YYYY-MM-DD HH:mm:ss',
        );

        const pushData = [
          {
            chatId: parseData.chatId,
            content: parseData.content,
            memberId: parseData.memberId,
            name: parseData.name,
            fileYn: parseData.fileYn,
            iconFileId: parseData.iconFileId,
            createDate: createDate,
            timeText: dayjs(createDate).format('YYYY. MM. DD A HH:mm:ss'),
            dataLast: false,
            attachFileList: [],
          },
        ];

        const data = useChatStore().chatList.channels[parseChannelId];

        if (!data) {
          return;
        }

        if (data.length > 0) {
          const lastIndex = data.length - 1;
          const lastArray = data[lastIndex];
          const lastItem = lastArray[lastArray.length - 1];

          console.log('lastItem', lastItem);

          const newCreateDate = dayjs(parseData.createDate).format(
            'YYYY-MM-DD HH:mm',
          );
          const lastCreateDate = dayjs(lastItem.createDate).format(
            'YYYY-MM-DD HH:mm',
          );

          // 이전 채팅 친 사용자와 같고 시:분까지 동일한 경우
          if (
            newCreateDate == lastCreateDate &&
            parseData.memberId == lastItem.memberId
          ) {
            useChatStore().chatList.channels[parseChannelId][lastIndex].push(
              ...pushData,
            );
          } else {
            useChatStore().chatList.channels[parseChannelId].push(pushData);
          }
        } else {
          pushData[0].dataLast = true;
          useChatStore().chatList.channels[parseChannelId].push(pushData);
        }

        useChatStore().lastChat[parseChannelId] = {
          chatId: parseData.chatId,
          fileYn: parseData.fileYn,
          memberId: parseData.memberId,
        };

        if (parseData.fileYn == 'Y') {
          console.log('저장', parseData.chatId);
          useChatStore().uploadFileChatId = parseData.chatId;
        }
      }
    };
  };

  const processChatData = data => {
    return data.reduce((acc, item) => {
      const createDate = dayjs(item.createDate).format('YYYY-MM-DD HH:mm:ss');
      const ymdhm = dayjs(item.createDate).format('YYYYMMDDHHmm');
      const chatKey = `${item.memberId}_${ymdhm}`;

      const pushData = {
        chatId: item.chatId,
        content: item.content,
        memberId: item.memberId,
        name: item.memberName,
        fileYn: item.fileYn,
        iconFileId: userProfileIcon(item.memberIconId),
        timeText: dayjs(createDate).format('YYYY. MM. DD A HH:mm:ss'),
      };

      if (!acc[chatKey]) {
        acc[chatKey] = [];
      }

      acc[chatKey].push(pushData);
      return acc;
    }, {});
  };

  return {
    websocket,
    WEB_SOCKET_CONNECT,
  };
});
