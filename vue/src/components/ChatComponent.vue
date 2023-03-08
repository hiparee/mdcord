<template>
  <!-- Page content wrapper-->
  <div
    id="page-content-wrapper"
    @dragover.prevent="onDragOver"
    @dragleave="onDragLeave"
    @drop="onDrop"
  >
    <!-- 상단 title 영역 -->
    <nav class="navbar navbar-expand-lg navbar-light">
      <div class="container-fluid">
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarTogglerDemo01"
          aria-controls="navbarTogglerDemo01"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <p class="navbar-brand mb-0 p-0" href="#">
          <i class="bi bi-hash"></i>
          <span class="text-white">{{ store.onChatInfo.title }}</span>
        </p>
        <div class="collapse navbar-collapse" id="navbarTogglerDemo01">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <i class="bi bi-person-fill-add fs-4"></i>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Link</a>
            </li>
            <li class="nav-item">
              <a
                class="nav-link disabled"
                href="#"
                tabindex="-1"
                aria-disabled="true"
                >Disabled</a
              >
            </li>
          </ul>
          <form class="d-flex">
            <input
              class="form-control me-2"
              type="search"
              placeholder="Search"
              aria-label="Search"
            />
            <button class="btn btn-outline-success" type="submit">
              Search
            </button>
          </form>
        </div>
      </div>
    </nav>

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
        <div class="chat-list">
          <!-- 채팅 리스트 영역 -->
          <div class="list-box" ref="listBox">
            <!-- skeleton -->
            <template v-if="isLoading">
              <Skeleton />
            </template>

            <!-- data list -->
            <template v-else>
              <div class="d-flex flex-row-reverse">
                <div class="text-center">
                  <img
                    class="profile-img"
                    src="@/assets/images/profile/08.png"
                  />
                  <div class="text-white mt-1">나</div>
                </div>
                <div>
                  <p class="msg">Vuex</p>
                  <p
                    class="small m-3 mb-3 mt-0 text-muted"
                    title="2023년 02월 22일 23시 58분"
                  >
                    23:58
                  </p>
                </div>
              </div>

              <div class="divider mb-4">
                <p class="text-center mx-3 mb-0">Today</p>
              </div>

              <template v-for="(chat, index) in chatList" :key="index">
                <div
                  class="d-flex mb-3"
                  :class="
                    chat.memberId == userInfo.memberId
                      ? 'flex-row-reverse'
                      : 'flex-row justify-content-start'
                  "
                >
                  <div class="text-center">
                    <img
                      class="profile-img"
                      :src="`/src/assets/images/profile/${chat.iconFileId}.png`"
                    />
                    <div class="text-white mt-1">{{ chat.name }}</div>
                  </div>
                  <div>
                    <p class="msg">{{ chat.message }}</p>
                    <p
                      class="small m-3 mb-3 mt-0 text-muted"
                      :title="`${chat.commitTime}`"
                    >
                      {{ chat.timeAgo }}
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
            <div class="file-list-wrap">
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
                    :src="`/src/assets/images/ext/${file.ext}.png`"
                    style="width: 70px"
                  />
                </div>
                <span class="file-name">{{ file.name }}</span>
                <button class="file-delete">
                  <i class="bi bi-trash3-fill text-danger"></i>
                </button>
              </div>
            </div>
          </div>
          <!-- 첨부파일 리스트 영역 끝 -->
          <button @click="test()">ddddd</button>

          <div class="d-flex" style="flex-direction: row; width: 100%">
            <textarea
              type="text"
              class="form-control shadow-none text-light form-control-lg bg-dark border-0"
              id="refMessage"
              ref="refMessage"
              @input="onInput"
              v-model="message"
              @keydown.enter="sendMessage"
              placeholder="메세지 보내기"
            ></textarea>
            <a class="ms-1 text-muted" href="#!"
              ><i class="bi bi-paperclip fs-3 text-light"></i
            ></a>
            <a class="ms-3 text-muted" href="#!"
              ><i class="bi bi-emoji-smile fs-4 text-light"></i
            ></a>
            <a class="ms-3" href="#!"
              ><i class="bi bi-send-fill fs-4 text-light"></i
            ></a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, nextTick, watch, onMounted, watchEffect } from 'vue';
import { useChannelStore } from '../store/modules/channel';
import Skeleton from '../components/SkeletonComponent.vue';
import { useToast } from 'vue-toast-notification';
import { fetchMultiFileUpload } from '../api/chat.js';
import { useUserStore } from '../store/modules/user.js';
import { timeAgo } from '../composables/chat.js';
import dayjs from 'dayjs';
import 'dayjs/locale/ko';
dayjs.locale('ko');

const store = useChannelStore();
const userInfo = JSON.parse(useUserStore().userInfo);
const listBox = ref(null);
const refMessage = ref(null);
const message = ref('');
const fileList = ref([]);
const fileDragOverStatus = ref(false);
const props = defineProps({
  isLoading: {
    type: Boolean,
    default: true,
  },
});

const chatList = ref([]);

const onInput = () => {
  const textarea = refMessage.value;
  textarea.style.height = '';
  textarea.style.height = textarea.scrollHeight + 'px';
};

const sendMessage = event => {
  if (!event.shiftKey) {
    // console.log('메세지 전송 :', message.value);
    // console.log('useUserStore', userInfo);

    const data = {
      message: message.value,
      name: userInfo.name,
      sendTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
      commitTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
      iconFileId:
        userInfo.iconFileId < 10
          ? '0' + userInfo.iconFileId
          : userInfo.iconFileId,
      memberId: userInfo.memberId,
    };

    websocket.send(JSON.stringify(data));

    message.value = '';
    event.preventDefault();
    const textarea = refMessage.value;
    textarea.style.height = '76px';
    // refMessage.value.dispatchEvent(new Event('onInput'));
    listBox.value.scrollTop = listBox.value.scrollHeight;
    return;

    // let formData = new FormData();

    // for (let i = 0; i < fileList.value.length; i++) {
    //   console.log('fileList.value[i] ->', fileList.value[i]);
    //   formData.append('files', fileList.value[i]);
    // }
    // formData.append('channelId', store.onChatInfo.channelId);
    // console.log(formData);
    // const response = fetchMultiFileUpload(formData);
    // console.log(response.data);

    // // 업로드가 완료되면 fileList 배열 초기화
    // fileList.value = [];

    // alert('send message');
  }
};

// const uploadFiles = async () => {
//   try {
//     for (let i = 0; i < fileList.value.length; i++) {
//       const formData = new FormData();
//       console.log(fileList.value[i]);
//       formData.append('file', fileList.value[i]);
//       const response = await axios.post('/api/upload', formData);
//       console.log(response.data);
//     }
//     // 업로드가 완료되면 fileList 배열 초기화
//     // this.fileList = [];
//   } catch (error) {
//     // console.error(error);
//   }
// };

const onDragOver = event => {
  if (fileDragOverStatus.value) return;

  const isDraggingFile =
    Array.from(event.dataTransfer.types).includes('Files') ||
    Array.from(event.dataTransfer.types).includes('application/x-moz-file');
  if (isDraggingFile) {
    fileDragOverStatus.value = true;
    console.log('파일 드래그 중');
  } else {
    console.log('파일이 아닌 다른 것을 드래그 중');
  }
};

const onDragLeave = event => {
  console.log('onDragLeave');
  fileDragOverStatus.value = false;
};

const onDrop = event => {
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

const addFiles = async files => {
  for (let i = 0; i < files.length; i++) {
    const src = await readFiles(files[i]);
    const isImage = files[i].type.includes('image');
    files[i].src = src;
    files[i].isImage = isImage;
    files[i].ext = getFileExt(files[i].name);

    console.log(files[i]);

    fileList.value.push(files[i]);
    console.log(fileList.value);
  }
};

// FileReader를 통해 파일을 읽어 thumbnail 영역의 src 값으로 셋팅
const readFiles = async files => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = async e => {
      resolve(e.target.result);
    };
    reader.readAsDataURL(files);
  });
};

const websocket = new WebSocket('ws://localhost:9000/api/chat?memberId=lemon');

onMounted(() => {
  refMessage.value.addEventListener('input', onInput);
  websocket.onopen = () => {
    console.log('connected');
    websocket.onmessage = ({ data }) => {
      // console.log('메세지 수신 :', data);
      const parseData = JSON.parse(data);
      console.log('메세지 수신 :', parseData);

      chatList.value.push({
        message: parseData.message,
        name: parseData.name,
        sendTime: parseData.sendTime,
        commitTime: parseData.commitTime,
        iconFileId: parseData.iconFileId,
        memberId: parseData.memberId,
        timeText: dayjs(parseData.commitTime).format('YYYY. MM. DD A HH:mm'),
      });
    };
  };
});

watch(
  () => props.isLoading,
  (oldValue, newValue) => {
    console.log(`isLoading changed from ${oldValue} to ${newValue}`);
    nextTick(() => {
      listBox.value.scrollTop = listBox.value.scrollHeight;
    });
  },
);

const test = () => {
  listBox.value.scrollTop = listBox.value.scrollHeight;
};

watch(chatList.value, () => {
  chatList.value.forEach(v => {
    v['timeAgo'] = timeAgo(v.commitTime);
  });
  test();
});
</script>
<style scoped lang="scss">
#refMessage {
  overflow: hidden;
  height: 76px;
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
.msg {
  background-color: #535354;
  padding: 10px 15px;
  font-size: 17px;
  margin-left: 1rem;
  margin-right: 1rem;
  margin-bottom: 0.25rem;
  border-radius: var(--bs-border-radius-lg);
  white-space: pre-line;
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

.file-list-wrap {
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
</style>
