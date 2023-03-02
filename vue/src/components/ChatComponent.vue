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
          <span class="text-white">{{ store.chatInfo.title }}</span>
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
              <div class="d-flex flex-row justify-content-start mb-3">
                <div class="text-center">
                  <img
                    class="profile-img"
                    src="@/assets/images/profile/47.png"
                  />
                  <div class="text-white mt-1">백성우</div>
                </div>
                <div>
                  <p class="msg">223.130.130.222:8080</p>
                  <p
                    class="small m-3 mb-3 mt-0 text-muted"
                    title="2023년 02월 22일 21시 08분"
                  >
                    23시간 전
                  </p>
                </div>
              </div>

              <div class="d-flex flex-row justify-content-start">
                <div class="text-center">
                  <img
                    class="profile-img"
                    src="@/assets/images/profile/47.png"
                  />
                  <div class="profile-name text-white mt-1">백성우</div>
                </div>
                <div>
                  <p class="msg">
                    select distinct (to_char(ep."timestamp", 'yyyy-MM-dd'))
                    as<br />
                    a, count(1) from es_payload ep group by<br />
                    rollup((to_char(ep."timestamp", 'yyyy-MM-dd'))) order by
                    a<br />
                    asc ;
                  </p>
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

              <div class="d-flex flex-row-reverse">
                <div class="text-center">
                  <img
                    class="profile-img"
                    src="@/assets/images/profile/08.png"
                  />
                  <div class="text-white mt-1">나</div>
                </div>
                <div>
                  <p class="msg">
                    Vuex와 비교 피니아는 Vuex 5에 대한 핵심 팀 토론의 많은<br />
                    아이디어를 통합하여, Vuex의 다음 버전이 어떤 모습일지<br />
                    탐구하는 것으로 시작했습니다. 마침내 우리는 Vuex 5에서<br />
                    우리가 원하는 대부분을 피니아가 이미 구현하고 있다는 것을<br />
                    깨달았고, 이것을 새로운 권장 사항으로 만들기로
                    결정했습니다.<br />
                    Vuex와 비교할 때, 피니아는 더 간단한 API를 제공하고,<br />
                    컴포지션 API 스타일을 제공하며, 가장 중요한 것은<br />
                    TypeScript와 함께 사용할 때 견고한 유형 추론을 지원합니다.
                  </p>
                  <p
                    class="small m-3 mb-3 mt-0 text-muted"
                    title="2023년 02월 22일 23시 58분"
                  >
                    23:58
                  </p>
                </div>
              </div>
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
                  <img class="img-thumbnail" :src="file.src" />
                </div>
                <span class="file-name">{{ file.name }}</span>
                <button class="file-delete">
                  <i class="bi bi-trash3-fill text-danger"></i>
                </button>
              </div>
            </div>
          </div>
          <!-- 첨부파일 리스트 영역 끝 -->

          <div class="d-flex" style="flex-direction: row; width: 100%">
            <textarea
              type="text"
              class="form-control shadow-none text-light form-control-lg bg-dark border-0"
              style="
                overflow: hidden;
                height: 76px;
                background-color: #383a40 !important;
              "
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
import { ref, nextTick, watch, onMounted } from 'vue';
import { useChannelStore } from '../store/modules/channel';
import Skeleton from '../components/SkeletonComponent.vue';
import { useToast } from 'vue-toast-notification';

const store = useChannelStore();
const listBox = ref(null);
const refMessage = ref(null);
const message = ref('');
const fileDragOverStatus = ref(false);
const props = defineProps({
  isLoading: {
    type: Boolean,
    default: true,
  },
});

const onInput = () => {
  const textarea = refMessage.value;
  textarea.style.height = '';
  textarea.style.height = textarea.scrollHeight + 'px';
};

const sendMessage = event => {
  if (!event.shiftKey) {
    console.log(message.value);
    message.value = '';
    event.preventDefault();
    const textarea = refMessage.value;
    textarea.style.height = '76px';
    // refMessage.value.dispatchEvent(new Event('onInput'));
  }
};

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
  console.log(files);
  addFiles(files);
};

const fileList = ref([]);

const addFiles = async files => {
  for (let i = 0; i < files.length; i++) {
    const src = await readFiles(files[i]);
    files[i].src = src;

    console.log(files[i]);
    fileList.value.push(files[i]);
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

onMounted(() => {
  refMessage.value.addEventListener('input', onInput);
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
</script>
<style scoped lang="scss">
.profile-img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
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
    & .file-img {
      width: 120px;
      height: 120px;
      overflow: hidden;
      & .img-thumbnail {
        margin-right: 10px;
        border-radius: 4px;
        width: 100%;
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
    }
  }
}
</style>
