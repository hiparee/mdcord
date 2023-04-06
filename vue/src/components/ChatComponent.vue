<template>
  <!-- Page content wrapper-->
  <div
    id="page-content-wrapper"
    @dragover.prevent="onDragOver"
    @dragleave="onDragLeave"
    @drop="onDrop"
  >
    <!-- 상단 title 영역 -->
    <navbar-title-component></navbar-title-component>

    <!-- 첨부파일 drag & drop 영역 -->
    <transition name="drag">
      <div class="hidden-drag-area" v-if="fileDragOverStatus">
        <div>
          <div class="drag-box">
            <div class="box-inner">
              <div class="mb-3">
                <img src="@/assets/images/profile/05.png" />
              </div>
              <h3>첨부파일 올리기</h3>
              <div>
                <div>올리기 전에 댓글을 덧붙일 수 있어요.</div>
                <div>여러개 첨부가능</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <!-- Contents 영역 -->
    <div class="page-content">
      <div class="content-chat-area">
        <!-- 채팅 영역 -->
        <div class="chat-list">
          <!-- 채팅 리스트 -->
          <div class="list-box" ref="listBox" @scroll="handleScroll">
            <!-- 더보기 로딩 -->
            <div class="more-loading text-center text-light" v-if="moreLoading">
              <div class="spinner-grow spinner-grow-sm" role="status"></div>
              <div class="spinner-grow spinner-grow-sm" role="status"></div>
              <div class="spinner-grow spinner-grow-sm" role="status"></div>
            </div>

            <!-- 스켈레톤 -->
            <template v-if="isLoading">
              <skeleton-component></skeleton-component>
            </template>

            <!-- data list -->
            <template v-else>
              <template
                v-if="
                  accessedChannelData?.[0]?.[0]?.dataLast ||
                  !accessedChannelData?.length
                "
              >
                <div class="text-center text-white">
                  <h1># {{ channelStore.accessedChannelInfo.title }}에</h1>
                  <h1>오신 것을 환영합니다</h1>
                  <p>
                    # {{ channelStore.accessedChannelInfo.title }} 채널의
                    시작이에요.
                  </p>

                  <p v-if="accessedChannelData.length == 0">
                    채널의 첫 메세지를 전송해보세요.
                  </p>
                </div>
              </template>

              <template
                v-for="(chatAry, index) in accessedChannelData"
                :key="index"
              >
                <!-- 날짜 구분선 -->
                <div class="divider mb-4" v-if="getDateDiff(index) > 0">
                  <p class="text-center mx-3 mb-0">
                    {{ getDividerText(index, chatAry[0].createDate) }}
                  </p>
                </div>
                <div
                  class="d-flex mt-2"
                  :class="
                    chatAry[0].memberId == userInfo.memberId
                      ? 'flex-row-reverse'
                      : 'flex-row justify-content-start'
                  "
                >
                  <div class="text-center">
                    <img
                      class="profile-img"
                      :src="getImageUrl(`profile/${chatAry[0].iconFileId}.png`)"
                    />
                    <div class="text-white mt-1">{{ chatAry[0].name }}</div>
                  </div>
                  <div class="msg-wrap">
                    <!-- {{ chat.content }} -->
                    <template v-for="(chat, index) in chatAry" :key="index">
                      <p
                        class="msg"
                        v-html="renderMsgHtml(chat.content)"
                        :title="JSON.stringify(chat)"
                        :data-chat-id="chat.chatId"
                      ></p>
                      <!-- <p
                        class="msg"
                        :title="JSON.stringify(chat)"
                        :data-chat-id="chat.chatId"
                      >
                        {{ chat.content }}
                      </p> -->

                      <!-- <div v-if="isMatchYoutubeUrl(chat.content)"> -->
                      <div v-html="renderYoutubeIframe(chat.content)"></div>
                      <!-- </div> -->

                      <div
                        class="chat-file-list-wrap"
                        v-if="chat.attachFileList.length > 0"
                      >
                        <div
                          class="file-item"
                          v-for="(file, index) in chat.attachFileList"
                          :key="index"
                        >
                          <div class="file-img">
                            <img
                              v-if="getIsImage(file.fileExt)"
                              :src="`${viteAppApiUrl}/channels/${accessedChannelId}/image-file/${file.realFileName}${file.fileExt}`"
                              class="img-thumbnail"
                              alt="삭제되었거나 존재하지않는 이미지입니다"
                              v-on:load="chatScrollSetting"
                            />

                            <template v-else>
                              <div>
                                <img
                                  class="img-thumbnail m-3"
                                  :src="
                                    getImageUrl(
                                      `ext/${file.fileExt.substring(1)}.png`,
                                    )
                                  "
                                  style="width: 70px"
                                  v-on:load="chatScrollSetting"
                                />
                                <span class="file-name p-2">{{
                                  file.originFileName
                                }}</span>
                              </div>
                            </template>
                          </div>
                          <!-- <span class="file-name">{{
                            file.originFileName
                          }}</span> -->
                          <button
                            class="file-delete tooltip-top"
                            @click="fileDelete(index)"
                            data-tooltip="첨부 파일 삭제"
                            @mouseover="showTooltip"
                            @mouseleave="hideTooltip"
                            style="display: none"
                          >
                            <i class="bi bi-trash3-fill"></i>
                          </button>
                          <button
                            class="file-down tooltip-top"
                            style="display: none"
                            data-tooltip="첨부 파일 다운로드"
                            @mouseover="showTooltip"
                            @mouseleave="hideTooltip"
                            @click="attachFileDown(viteAppApiUrl, file.id)"
                          >
                            <i class="bi bi-download text-white"></i>
                          </button>
                        </div>
                      </div>
                    </template>
                    <!-- <template v-for="(msg, index) in chat.content" :key="index">
                        <p class="msg" v-html="renderMsgHtml(msg)"></p>
                      </template> -->

                    <p
                      class="small m-3 mb-3 mt-0 text-muted"
                      :title="`${chatAry[0].timeText}`"
                    >
                      {{ $dayjs().to(chatAry[0].createDate) }}
                    </p>
                  </div>
                </div>
              </template>
            </template>
          </div>

          <!-- 채팅 리스트 영역 끝 -->
        </div>

        <div
          class="chat-footer text-muted d-flex justify-content-start p-3"
          style="flex-direction: column"
        >
          <!-- 첨부파일 리스트 영역 -->
          <div class="m-0">
            <div class="upload-file-list-wrap">
              <div
                class="file-item"
                v-for="(file, index) in fileList"
                :key="index"
              >
                <div class="file-img">
                  <img
                    v-if="file.isImage"
                    class="img-thumbnail"
                    :src="file.src"
                  />
                  <img
                    v-else
                    class="img-thumbnail"
                    :src="getImageUrl(`ext/${file.ext}.png`)"
                    style="width: 70px"
                  />
                </div>
                <span class="file-name">{{ file.name }}</span>
                <button class="file-delete" @click="fileDelete(index)">
                  <i class="bi bi-trash3-fill text-danger"></i>
                </button>
              </div>
            </div>
          </div>

          <!-- <img
            src="http://localhost:9000/channels/6/attach-file/V1BjqC0Fhr.png"
            style="width: 100px; height: 100px"
          /> -->

          <!-- 첨부파일 리스트 영역 끝 -->
          <div
            class="textarea-message-wrap d-flex"
            style="flex-direction: row; width: 100%"
          >
            <!-- EmojiComponent 영역 -->
            <emoji-component
              :showToggle="emojiToggle"
              @emoji-click="appendEmoji"
              @emoji-toggle="closeEmojiList"
            ></emoji-component>

            <textarea
              type="text"
              class="form-control shadow-none text-light form-control-lg bg-dark border-0"
              id="refMessage"
              ref="refMessage"
              placeholder="메세지 보내기"
              v-model="message"
              @input="onInput"
              @paste="handlePaste"
              @keydown.enter="sendMessage"
            ></textarea>

            <span
              id="emojiBtn"
              class="ms-2 scale h2"
              @mouseover="changeEmojiIcon"
              @click="emojiToggle = !emojiToggle"
            >
              🙂
            </span>
            <span id="sendMessageBtn" class="ms-2 scale" @click="sendMessage"
              ><i class="bi bi-send-fill fs-3 text-light"></i
            ></span>
          </div>
        </div>
      </div>
      <div class="channel-member-list" style="overflow-y: scroll">
        <div>
          <div class="mb-3">
            <span class="text-warning"
              >{{ accessedServerName }} 참여자
              <span style="font-size: 12px">
                ― {{ accessedMemberList ? accessedMemberList.length : 0 }}
              </span>
            </span>
          </div>
          <div>
            <div v-for="member in accessedMemberList" :key="member.memberId">
              <div class="member-item" :class="{ on: member.state == 'ON' }">
                <img
                  class="profile-img"
                  :src="
                    getImageUrl(
                      `profile/${userProfileIcon(member.iconFileId)}.png`,
                    )
                  "
                />
                <span :title="member.memberId"> {{ member.memberName }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import EmojiComponent from '@/components/EmojiComponent.vue';
import NavbarTitleComponent from '@/components/layout/NavbarTitleComponent.vue';
import SkeletonComponent from '@/components/SkeletonComponent.vue';
import {
  useChannelStore,
  useChatStore,
  useUserStore,
  webSocketStore,
} from '@/store/store';
import { getImageUrl, userProfileIcon } from '@/utils/common.js';
import { getRandomEmojiIcon } from '@/utils/emoji.js';
import {
  computed,
  inject,
  nextTick,
  onBeforeMount,
  onMounted,
  ref,
  watch,
} from 'vue';
import { useRoute } from 'vue-router';
import { useToast } from 'vue-toast-notification';
import { fetchMultiFileUpload } from '../api/chat.js';

const route = useRoute();
const dayjs = inject('dayjs');
const userInfo = useUserStore().userInfo;
const channelStore = useChannelStore();
const chatStore = useChatStore();
const socketStore = webSocketStore();
const listBox = ref(null);
const refMessage = ref(null);
const message = ref('');
const fileList = ref([]);
const fileDragOverStatus = ref(false);
const mouseDown = ref(false);
const isLoading = ref(true);
const moreLoading = ref(false);
const emojiToggle = ref(false);

const accessedChannelId = computed(
  () => channelStore.accessedChannelInfo.channelId,
);
const accessedChannelData = computed(() => {
  return chatStore.chatList.channels[accessedChannelId.value];
});
const accessedServerId = computed(() => {
  return channelStore.accessedChannelInfo.serverId;
});
const accessedServerName = computed(() => {
  const serverId = accessedServerId.value;
  const obj = channelStore.serverList.find(item => item.id === serverId);
  const name = obj.name;
  return name;
});
const accessedMemberList = computed(() => {
  const memberList = channelStore.getMemberList[accessedServerId.value];
  if (memberList) {
    return memberList.sort((a, b) => {
      if (a.state !== b.state) {
        return b.state.localeCompare(a.state);
      }
      return a.memberName.localeCompare(b.memberName);
    });
  } else {
    return memberList;
  }
});
const viteAppApiUrl = computed(() => {
  return import.meta.env.VITE_APP_API_URL;
});
const getDividerText = (index, createDate) => {
  const nowDate = dayjs().format('YYYYMMDD');
  const divDate = dayjs(createDate).format('YYYYMMDD');

  if (nowDate == divDate) {
    return 'Today';
  } else {
    return dayjs(createDate).format('YYYY년 M월 D일');
  }
};

const onInput = () => {
  const textarea = refMessage.value;
  textarea.style.height = '';
  textarea.style.height = textarea.scrollHeight + 'px';
};

const sendMessage = event => {
  if (!event.shiftKey) {
    if (message.value == '') {
      event.preventDefault();
      return;
    }
    const fileYn = fileList.value.length > 0 ? 'Y' : 'N';
    const data = {
      fileYn,
      messageType: 'SEND_MESSAGE',
      channelId: accessedChannelId.value,
      memberId: userInfo.memberId,
      content: message.value,
      name: userInfo.name,
      sendTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
      commitTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
      iconFileId: userProfileIcon(userInfo.iconFileId),
    };

    console.log('전송데이터', JSON.stringify(data));

    // 서버로 채팅 data를 전송한다.
    socketStore.websocket.send(JSON.stringify(data));
    console.log(socketStore.websocket.readyState);

    message.value = '';
    const textarea = refMessage.value;
    textarea.style.height = '76px';

    event.preventDefault();
  }
};

const onDragOver = event => {
  if (fileDragOverStatus.value) return;

  const isDraggingFile =
    Array.from(event.dataTransfer.types).includes('Files') ||
    Array.from(event.dataTransfer.types).includes('application/x-moz-file');
  if (isDraggingFile && mouseDown.value == false) {
    fileDragOverStatus.value = true;
    // console.log('파일 드래그 중');
  } else {
    // console.log('파일이 아닌 다른 것을 드래그 중');
  }
};

const onDragLeave = event => {
  console.log('onDragLeave');
  fileDragOverStatus.value = false;
};

const onDrop = event => {
  mouseDown.value = false;
  if (fileDragOverStatus.value == false) return;

  fileDragOverStatus.value = false;
  event.preventDefault();

  const files = event.dataTransfer.files;

  addFiles(files);
};

const getFileExt = fileName => {
  const fileLen = fileName.length;
  const lastDot = fileName.lastIndexOf('.');
  const fileExt = fileName.substring(lastDot + 1, fileLen).toLowerCase();

  return fileExt;
};

/**
 *  채팅입력Box에 이미지 붙여넣기 이벤트 처리
 */
const handlePaste = async event => {
  const items = (event.clipboardData || event.originalEvent.clipboardData)
    .items;
  for (let i = 0; i < items.length; i++) {
    const item = items[i];
    const isImage = item.type.includes('image');

    if (isImage) {
      const blob = item.getAsFile();
      const file = new File([blob], 'image.png', { type: 'image/png' });
      const src = await readFiles(file, isImage);
      file.src = src;
      file.isImage = isImage;
      file.ext = getFileExt(file.name);
      // input file에 추가
      console.log(fileList.value);
      console.log(file.size);
      console.log(file.size, 1024 * 1024 * 0.1);

      return filesVaildate(file);
    }
  }
};

const filesVaildate = file => {
  const toast = useToast({
    duration: 300,
    position: 'bottom',
    queue: true,
  });

  if (file.size > 1024 * 1024 * 30) {
    return toast.error(`파일당 용량은 30MB 이하만 가능합니다.`);
  }

  if (fileList.value.length >= 10) {
    return toast.error(`최대 첨부파일 갯수는 10개입니다.`);
  }

  fileList.value.push(file);
};

const addFiles = async files => {
  for (let file of files) {
    console.log(file);
    const isImage = file.type.includes('image');
    const src = await readFiles(file, isImage);
    file.src = src;
    file.isImage = isImage;
    file.ext = getFileExt(file.name);

    filesVaildate(file);
  }
};

const fileDelete = index => {
  // fileList.value.
};

const getIsImage = ext => {
  const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.svg'];
  return imageExtensions.includes(ext.toLowerCase());
};

const readAndCompressImage = async (file, quality) => {
  const image = await createImageBitmap(file);

  const canvas = document.createElement('canvas');
  canvas.width = image.width;
  canvas.height = image.height;

  const ctx = canvas.getContext('2d');
  ctx.drawImage(image, 0, 0);

  const compressedDataURL = canvas.toDataURL('image/jpeg', quality);

  return compressedDataURL;
};

// FileReader를 통해 파일을 읽어 thumbnail 영역의 src 값으로 셋팅
const readFiles = (files, isImage) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    if (isImage) {
      const compressedDataURL = readAndCompressImage(files, 0.5); // 이미지 품질을 50%로 줄임
      reader.onload = async e => {
        resolve(compressedDataURL);
      };
    } else {
      reader.onload = async e => {
        resolve(e.target.result);
      };
    }
    reader.readAsDataURL(files);
  });
};

/**
 * @param {*} text
 *  message 내용에서
 *  1. html 태그 치환
 *  2. URL을 찾아서 적절한 HTML 하이퍼링크로 변환하는 메서드를 작성
 */
const renderMsgHtml = text => {
  const map = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#039;',
  };

  text = text.replace(/[&<>"']/g, function (m) {
    return map[m];
  });

  // const urlRegex = /https?:\/\/[^\s]+/g;
  // const urlRegex = /^http[s]?:\/\/([\S]{3,})/gi;ss
  const urlRegex = /https?:\/\/(?!.*&gt;)[^\s]+/g;
  const replaceUrlText = text.replace(
    urlRegex,
    '<a href="$&" target="_blank" data-url-render>$&</a>',
  );

  // let makeHtml = replaceUrlText;
  // // let makeHtml = `<div class='msg-inner'>${replaceUrlText}</div>`;
  // makeHtml += '<div class="more-btn"><span>더보기</span></div>';
  return replaceUrlText;
};

const renderYoutubeIframe = text => {
  const youtubeUrl =
    /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#&?]*).*/;
  return text.match(youtubeUrl) ? text.replace(youtubeUrl, youtubeParser) : '';
};

const youtubeParser = (url, ...groups) => {
  const container = `
    <div class="video-container mr-3 ml-3">
      <iframe width="560" height="315" src="https://www.youtube.com/embed/#ID#" title="YouTube video player"
        frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
        allowfullscreen></iframe>
    </div>`;

  return groups && groups[6].length == 11
    ? container.replace('#ID#', groups[6])
    : url;
};

onMounted(() => {
  refMessage.value.addEventListener('input', onInput);
});

window.addEventListener('mousedown', () => {
  mouseDown.value = true;
});

window.addEventListener('mouseup', () => {
  mouseDown.value = false;
});

const chatScrollSetting = () => {
  if (listBox.value) {
    listBox.value.scrollTop = listBox.value.scrollHeight;
    // console.log(listBox.value.scrollTop, listBox.value.scrollHeight);
  }
};

const getDateDiff = index => {
  const channels = chatStore.chatList.channels;
  const accessedChannel = channels[accessedChannelId.value];

  if (accessedChannel[index - 1]) {
    const date1 = dayjs(accessedChannel[index][0].createDate).format(
      'YYYY-MM-DD',
    );
    const date2 = dayjs(accessedChannel[index - 1][0].createDate).format(
      'YYYY-MM-DD',
    );
    const date3 = dayjs(date1, 'YYYY-MM-DD');
    const date4 = dayjs(date2, 'YYYY-MM-DD');
    // console.log(date3, date4);
    return date3.diff(date4, 'd');
  }
};

/**
 * 채팅리스트 Box 의 스크롤이벤트 처리함.
 * scrollTop 값이 0이되는경우 이전 채팅 내역을 불러온다.
 * 늘어난 scrollHeight 만큼 scrollTop을 변경한다.
 */
const handleScroll = async () => {
  const channels = chatStore.chatList.channels[accessedChannelId.value];
  if (listBox.value.scrollTop === 0 && channels && channels.length > 0) {
    const scrollHeight = listBox.value.scrollHeight;
    console.log('scoll', scrollHeight);
    moreLoading.value = true;

    await chatStore.MORE_CHAT_LIST(accessedChannelId.value);
    moreLoading.value = false;

    listBox.value.scrollTop = listBox.value.scrollHeight - scrollHeight;
    console.log('로딩 종료');
  }
};

const setChatList = async () => {
  isLoading.value = true;

  // 현재 접속한 채널의 채팅메세지 50개를 가지고와서 chat store에 저장한다.
  chatStore.SET_CHAT_LIST(accessedChannelId.value).then(() => {
    isLoading.value = false;
    nextTick(() => {
      chatScrollSetting();
    });
  });
};

const setChatTitle = () => {
  // Channel Title 설정
  const channelList = channelStore.getChannelList;
  channelList.forEach(item => {
    item.subChannel.forEach(sub => {
      if (sub.id == route.params.id) {
        channelStore.SET_ACCESSED_CHANNEL_INFO('title', sub.name);
        channelStore.SET_ACCESSED_CHANNEL_INFO('channelId', sub.id);
      }
    });
  });
};
const setChannelMemberList = () => {
  channelStore.SET_MEMBER_LIST();
};

const attachFileDown = (url, id) => {
  const path = `${url}/channels/${accessedChannelId.value}/attach-file/${id}`;

  window.open(path);
};

const showTooltip = t => {
  // 툴팁 표시
  t.currentTarget.classList.add('tooltip-show');
};

const hideTooltip = t => {
  // 툴팁 숨기기
  t.currentTarget.classList.remove('tooltip-show');
};

const changeEmojiIcon = t => {
  t.target.innerText = getRandomEmojiIcon();
};

const appendEmoji = text => {
  message.value += text;
  document.getElementById('refMessage').focus();
};

const closeEmojiList = flag => {
  emojiToggle.value = flag;
};

onBeforeMount(() => {
  // console.log('onBeforeMount 호출');
  setChannelMemberList();
  setChatTitle();
  setChatList();
});

watch(
  () => accessedServerId.value,
  () => {
    console.log('서버바뀜');
  },
);

watch(
  () => route.params,
  () => {
    setChatTitle();
    setChatList();
  },
);

watch(
  () => accessedMemberList,
  () => {
    console.log('memberList 변경이력있음');
  },
);

watch(
  () => chatStore.lastChat[accessedChannelId.value],
  async newValue => {
    console.log('watch !!!!!!!!!!!!!!!!!!!!!!!!!!!!!');
    console.log(newValue);

    if (
      newValue &&
      newValue.memberId == userInfo.memberId &&
      newValue.fileYn == 'Y' &&
      fileList.value.length > 0
    ) {
      console.log('upload 할 파일', fileList);
      const chatId = newValue.chatId;
      let formData = new FormData();

      for (let i = 0; i < fileList.value.length; i++) {
        console.log('fileList.value[i] ->', fileList.value[i]);
        formData.append('files', fileList.value[i]);
      }
      formData.append('chatId', newValue.chatId);

      formData.append('channelId', accessedChannelId.value);
      // console.log(formData);
      const response = await fetchMultiFileUpload(formData);
      // console.log('파일업로드 완료', response.data);

      //소켓 send
      const data = {
        messageType: 'UPLOAD_FILE',
        fileList: response.data,
        channelId: accessedChannelId.value,
        chatId: chatId,
      };

      socketStore.websocket.send(JSON.stringify(data));

      // 업로드가 완료되면 fileList 배열 초기화
      fileList.value = [];
    }

    nextTick(() => {
      chatScrollSetting();
    });
  },
);
</script>
<style scoped lang="scss">
#refMessage {
  overflow: hidden;
  resize: none;
  height: 76px;
  padding-right: 90px;
  background-color: #383a40 !important;
}
.profile-img {
  width: 50px;
  height: 50px;
  border-radius: 10px;
}
.profile-name {
  white-space: nowrap;
}

.msg:hover {
  background: #444;
}
.msg {
  transition: 0.2s;
  background-color: #535354;
  padding: 10px 15px;
  font-size: 18px;
  margin-left: 1rem;
  margin-right: 1rem;
  margin-bottom: 0.25rem;
  border-radius: var(--bs-border-radius-lg);
  white-space: pre-wrap;
  width: fit-content;
  word-break: break-all;
  overflow: hidden;
  text-overflow: ellipsis;
  & .msg-inner {
    max-height: 5000px;
    overflow: hidden;
  }
}
.more-btn {
  display: flex;
  padding: 0px 15px;
  cursor: pointer;
  > span {
    color: #9ba3f7;
    justify-content: center;
    width: 100%;
    margin-bottom: 10px;
  }
}
.flex-row-reverse .msg-wrap {
  display: flex;
  flex-direction: column;
  align-items: end;
  overflow: hidden;
}

.drag-enter-active,
.drag-leave-active {
  transition: opacity 0.2s ease;
}

.drag-enter-from,
.drag-leave-to {
  opacity: 0;
}
.hidden-drag-area {
  background-color: rgba(0, 0, 0, 0.7);
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  bottom: 0;
  z-index: 9999;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;

  & .drag-box {
    background: #5865f2;
    display: inline-block;
    border-radius: 10px;
    padding: 10px;

    & .box-inner {
      border-radius: 10px;
      border: 2px dashed #9ba3f7;
      padding: 30px;
      color: #ffffff;
    }
  }
}

.flex-row .chat-file-list-wrap {
  & .file-item {
    margin-left: 0;
    margin-right: 10px;
  }
}

.flex-row-reverse .chat-file-list-wrap {
  text-align: right;
}

.chat-file-list-wrap {
  margin: 5px 15px;
  margin-bottom: 10px;

  // & .file-item:not(:last-child) {
  //   margin-left: 20px;
  // }
  & .file-item {
    margin-left: 10px;
    margin-right: 0;
    position: relative;
    display: inline-block;
    background: #222222;
    border-radius: 10px;
    text-align: center;
    max-height: 200px;
    max-width: 450px;

    &:hover {
      & .file-img {
        opacity: 0.8;
        transition: 0.2s;
      }
      & .file-delete {
        display: block !important;
      }

      & .file-down {
        display: block !important;
      }
    }

    & .file-img {
      overflow: hidden;
      // cursor: pointer;
      display: inline-block;
      width: 100%;
      height: 100%;
      & .img-thumbnail {
        width: 100%;
        border: 0;
        padding: 0;
      }
    }

    & button.file-delete {
      position: absolute;
      top: 0px;
      right: -15px;
      width: 30px;
      height: 30px;
      display: inline-block;
      text-align: center;
      border-top-right-radius: 4px;
      border-bottom-right-radius: 4px;
      background: #56595f;
      border: 0;

      &:hover {
        background: #b32d32;
        transition: 0.2s;
        & .bi {
          color: #ffffff;
        }
      }

      & .bi {
        color: #e0e0e0;
      }
    }
    & button.file-down {
      position: absolute;
      top: 0px;
      right: 15px;
      width: 30px;
      height: 30px;
      display: inline-block;
      text-align: center;
      border-top-left-radius: 4px;
      border-bottom-left-radius: 4px;
      background: #56595f;
      border: 0;

      &:hover {
        background: #3c57a3;
        transition: 0.2s;
      }
    }

    & .file-name {
      color: #f1f1f1;
      max-width: 200px;
      display: inline-block;
      // text-overflow: ellipsis;
      // white-space: nowrap;
      // overflow: hidden;
    }
  }
}
.upload-file-list-wrap {
  & .file-item {
    position: relative;
    display: inline-block;
    background: #222222;
    padding: 15px;
    border-radius: 10px;
    margin-right: 10px;
    margin-bottom: 10px;
    text-align: center;
    & .file-img {
      height: 120px;
      overflow: hidden;
      & .img-thumbnail {
        margin-right: 10px;
        border-radius: 4px;
        max-width: 120px;
        border: 0;
        padding: 0;
        margin-bottom: 10px;
      }
    }

    & button.file-delete {
      position: absolute;
      top: -10px;
      right: -10px;
      width: 30px;
      height: 30px;
      display: inline-block;
      text-align: center;
      border-radius: 4px;
      background: #666666;
      border: 1px solid #333333;
    }

    & .file-name {
      color: #f1f1f1;
      max-width: 200px;
      display: inline-block;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }
  }
}

.tooltip-top {
  position: relative;
}
.tooltip-show::before {
  content: attr(data-tooltip);
  width: max-content;
  position: absolute;
  top: -40px;
  right: 0px;
  background: #4a4a4a;
  color: #dadada;
  padding: 5px;
  border-radius: 5px;
  z-index: 9999;
}
.file-down.tooltip-show::before {
  right: -30px;
}

.textarea-message-wrap {
  position: relative;
}
.textarea-message-wrap .scale {
  position: absolute;
  top: 5px;
  right: 10px;
  cursor: pointer;
  transition: transform 0.1s;
}
.textarea-message-wrap .scale:hover {
  transform: scale(1.2);
}
#emojiBtn {
  right: 55px;
}
#emojiBtn:hover i {
  color: #ffcc4d !important;
}

#sendMessageBtn:hover i {
  color: #4ba7e0 !important;
}
</style>
