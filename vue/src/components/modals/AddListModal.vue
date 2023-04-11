<template>
  <div
    v-if="props.open"
    aria-hidden="true"
    class="modal fade show d-block"
    data-bs-backdrop="true"
    role="dialog"
    tabindex="-1"
    @mousedown="closeModal()"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div
        v-if="modalType === 'channel'"
        class="modal-content"
        style="background-color: #313338; padding: 25px"
        @mousedown.stop
      >
        <div class="custom-modal-header d-flex align-items-baseline">
          <h4 class="fw-bold text-center mb-4" style="color: #f3f4f5">
            채널 생성
          </h4>
          <button
            ref="modalCloseBtn"
            aria-label="Close"
            class="btn-close"
            type="button"
            @click="closeModal()"
          ></button>
        </div>
        <div class="modal-body p-0">
          <form class="d-flex flex-column" @submit.prevent="submitForm">
            <div class="w-100">
              <div class="mb-2">
                <label class="d-block">채널명</label>
                <input
                  v-model="name"
                  autocomplete="off"
                  class="mt-1 custom-input"
                  maxlength="10"
                  placeholder="채널명을 작성해주세요."
                  required
                  type="text"
                />
              </div>
              <span class="text-danger">{{ logMessage }}</span>
              <button
                :class="`${
                  formValid() === false ? 'disabled' : ''
                } btn custom-confirm-btn`"
                :style="{
                  'pointer-events': loading === true ? 'none' : '',
                }"
                type="submit"
              >
                <spinner-component v-if="loading" />
                <div v-else type="button">생성하기</div>
              </button>
            </div>
          </form>
        </div>
      </div>
      <div
        v-else
        class="modal-content"
        style="background-color: #313338; padding: 25px"
        @mousedown.stop
      >
        <div class="custom-modal-header d-flex align-items-baseline">
          <h4 class="fw-bold text-center mb-4" style="color: #f3f4f5">
            카테고리 생성
          </h4>
          <button
            ref="modalCloseBtn"
            aria-label="Close"
            class="btn-close"
            data-bs-dismiss="modal"
            type="button"
            @click="closeModal()"
          ></button>
        </div>
        <div class="modal-body p-0">
          <form class="d-flex flex-column" @submit.prevent="submitForm">
            <div class="w-100">
              <div class="mb-2">
                <label class="d-block">카테고리명</label>
                <input
                  v-model="name"
                  autocomplete="off"
                  class="mt-1 custom-input"
                  maxlength="10"
                  placeholder="카테고리명을 작성해주세요."
                  required
                  type="text"
                />
              </div>
              <span class="text-danger">{{ logMessage }}</span>
              <button
                :class="`${
                  formValid() === false ? 'disabled' : ''
                } btn custom-confirm-btn`"
                :style="{
                  'pointer-events': loading === true ? 'none' : '',
                }"
                type="submit"
              >
                <spinner-component v-if="loading" />
                <div v-else type="button">생성하기</div>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import SpinnerComponent from '@/components/common/SpinnerComponent.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useToast } from 'vue-toast-notification';
import { useChannelStore } from '@/store/modules/channel';
import { fetchAddChanneList } from '@/api/channel';
import { webSocketStore } from '@/store/modules/socket';

const router = useRouter();
const store = useChannelStore();
const socketStore = webSocketStore();
const emits = defineEmits([
  'update:showAddModal',
  'selectNewChannel',
  'selectNewCategory',
]);

const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  serverId: {
    type: Number,
  },
  channelId: {
    type: Number,
  },
  modalType: {
    type: String,
  },
});
const loading = ref(false);

const name = ref('');

const logMessage = ref('');

const closeModal = () => {
  emits('update:showAddModal', false);
  name.value = '';
  logMessage.value = '';
};

const $toast = useToast({
  duration: 1000,
});

const submitForm = async () => {
  loading.value = true;
  if (props.modalType === 'category') {
    const channels = store.getChannelList.filter(
      item => item.parentId === props.serverId,
    );
    let newOrderList = channels.map(channel => channel.channelOrder);
    newOrderList = Math.max(...newOrderList);
    const params = {
      name: name.value,
      parentId: props.serverId,
      dept: 1,
      channelOrder: newOrderList + 1,
    };
    try {
      const res = await fetchAddChanneList(params);
      const createdData = res.data;
      console.log('콘솔지옥', createdData);
      const data = {
        messageType: 'CREATE_CHANNEL',
        channelId: createdData.id,
        channelDept: 1,
        serverId: props.serverId,
      };
      console.log('웹소켓전송', data);
      socketStore.websocket.send(JSON.stringify(data));
      name.value = '';
      await store.SET_CHANNEL_LIST();
      emits('selectNewCategory');
      closeModal();
    } catch (e) {
      console.log(e);
    } finally {
      loading.value = false;
    }
  } else {
    const channels = store.getChannelList.filter(
      item => item.id === props.channelId,
    );
    let newSubOrderList = channels.map(item => item.subChannel);
    newSubOrderList = newSubOrderList[0].map(item => item.channelOrder);
    newSubOrderList = Math.max(...newSubOrderList);
    const params = {
      name: name.value,
      parentId: props.channelId,
      dept: 2,
      channelOrder: newSubOrderList === -Infinity ? 1 : newSubOrderList,
    };
    try {
      const res = await fetchAddChanneList(params);
      const createdData = res.data;
      const data = {
        messageType: 'CREATE_CHANNEL',
        channelId: createdData.id,
        channelDept: 2,
        serverId: props.serverId,
      };
      console.log('웹소켓전송', data);
      socketStore.websocket.send(JSON.stringify(data));
      name.value = '';
      await store.SET_CHANNEL_LIST();
      emits('selectNewChannel', createdData.id);
      closeModal();
    } catch (e) {
      console.log(e);
    } finally {
      loading.value = false;
    }
  }
};

const formValid = () => {
  if (!name.value) {
    return false;
  }
};
</script>

<style scoped>
[class~='error'] {
  color: red;
}
</style>
