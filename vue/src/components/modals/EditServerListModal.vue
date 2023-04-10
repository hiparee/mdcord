<template>
  <Transition>
    <div v-if="showEditServerListModal">
      <div
        id="updateModal"
        aria-hidden="true"
        aria-labelledby="updateModalLabel"
        class="modal modal-lg fade show d-block"
        data-bs-backdrop="true"
        role="dialog"
        style="overflow: hidden"
        tabindex="-1"
        @mousedown="closeModal()"
      >
        <div
          class="modal-dialog modal-dialog-centered"
          style="max-width: 600px !important"
        >
          <div
            class="modal-content"
            style="background-color: #313338"
            @mousedown.stop
          >
            <div class="modal-body p-0">
              <form class="profile-update-form" @submit.prevent>
                <div style="display: flex; padding: 10px">
                  <span style="flex: 1 0 0%; color: #f2f3f5; font-size: 20px"
                    >서버명</span
                  >
                  <div style="position: relative; display: flex">
                    <i
                      v-if="editMode === false"
                      class="bi-plus-lg"
                      style="position: absolute; left: -50px; bottom: 1px"
                      @click="editMode = true"
                    ></i>
                    <i
                      v-else
                      class="bi-check-lg"
                      style="position: absolute; left: -50px; bottom: 1px"
                      @click="addServer()"
                    ></i>
                  </div>
                </div>

                <div v-if="editMode" style="display: flex; padding: 10px">
                  <div>
                    <input
                      :value="newServerName"
                      maxlength="10"
                      placeholder="서버 이름"
                      style="
                        color: #cfcfcf;
                        background-color: #1e1f22;
                        border-color: aliceblue;
                      "
                      @input="updateInput($event)"
                      @click.prevent
                    />
                  </div>
                </div>
                <hr />
                <div
                  class="table-custom-scrollbar"
                  style="overflow-y: scroll !important; max-height: 400px"
                >
                  <div
                    v-for="server in store.getServerList"
                    :key="server.id"
                    class="mb-3 update-input-box"
                  >
                    <div
                      class="custom-server-list"
                      @mouseleave="hoverList($event, server.id)"
                      @mouseover="hoverList($event, server.id)"
                    >
                      <div style="flex: 1 0; color: #cfcfcf">
                        <span
                          v-if="server.id !== editServerId"
                          style="margin-right: 5px"
                          >{{ server.name }}</span
                        >
                        <input
                          v-else
                          :value="editServerName"
                          maxlength="10"
                          style="
                            color: #cfcfcf;
                            background-color: #1e1f22;
                            border-color: aliceblue;
                            margin-right: 5px;
                          "
                          @input="editServerNameInput($event)"
                        />
                        <i
                          v-if="server.id !== editServerId"
                          :class="{
                            [hoverClass]:
                              isHovered && server.id === hoverListId,
                          }"
                          style="font-size: x-small; display: inline-flex"
                          @click="editServerNameList(server)"
                        ></i>
                        <i
                          v-else
                          class="bi-check-lg"
                          @click="changeServerName(server)"
                        ></i>
                        <i
                          v-if="server.id === editServerId"
                          class="bi-x"
                          style="
                            font-size: large;
                            bottom: -0.9px;
                            position: relative;
                          "
                          @click="closeEditInput()"
                        ></i>
                      </div>
                      <div class="custom-checkbox bi-check">
                        <input
                          v-model="inputChecked"
                          :value="server.id"
                          style="
                            position: absolute;
                            z-index: 5;
                            width: 65%;
                            height: 100%;
                            opacity: 0;
                            right: 15px;
                          "
                          type="checkbox"
                          @click="changeChannelStatus(server)"
                        />
                        <label class="custom-checkbox-label"></label>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
              <div class="update-button-container">
                <button
                  :style="{
                    'pointer-events': loading === true ? 'none' : '',
                  }"
                  class="'btn custom-cancel-btn"
                  type="button"
                  @click="closeModal()"
                >
                  <div type="button">닫기</div>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { onBeforeMount, onBeforeUpdate, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useToast } from 'vue-toast-notification';
import { useChannelStore } from '@/store/modules/channel';
import { fetchAddChanneList, fetchEditChannelName } from '@/api/channel';

const router = useRouter();
const store = useChannelStore();
const emits = defineEmits(['update:showEditServerListModal']);
defineProps({
  showEditServerListModal: {
    type: Boolean,
    default: false,
  },
});
const loading = ref(false);
const inputChecked = ref([]);
const newServerName = ref('');
const editServerId = ref(null);
// const editing = ref(false);
const editMode = ref(false); // 값이 false 이면 사용자 정보 조회 모드
const editServerName = ref('');

const hoverClass = ref('bi-pencil-fill');
const isHovered = ref(false);
const hoverListId = ref(null);
onBeforeMount(() => {
  for (const server of store.serverList) {
    if (server.useYn === 'Y') {
      inputChecked.value.push(server.id);
    }
  }
});
const changeChannelStatus = async server => {
  if (inputChecked.value.includes(server.id)) {
    inputChecked.value = inputChecked.value.filter(i => i !== server.id);
  } else {
    inputChecked.value.push(server.id);
  }
  /**
   * {
   *   "id": 0,
   *   "useYn": "string",
   *   "channelName": "",
   *   "channelOrder": 1
   * }
   **/
  const params = {
    id: server.id,
    useYn: inputChecked.value.includes(server.id) ? 'Y' : 'N',
    channelName: server.name,
    channelOrder: server.channelOrder,
  };
  await fetchEditChannelName(params)
    .then(() => {
      store.SET_CHANNEL_LIST();
    })
    .catch(e => {
      console.log(e);
    });
};

const closeModal = () => {
  emits('update:showEditServerListModal', false);
  editMode.value = false;
};

// 서버 추가
const addServer = async () => {
  if (newServerName.value !== '') {
    let newOrderList = store.serverList.map(server => server.channelOrder);
    newOrderList = Math.max(...newOrderList);

    const params = {
      name: newServerName.value,
      parentId: 0,
      dept: 0,
      channelOrder: newOrderList + 1,
    };
    try {
      const res = await fetchAddChanneList(params);
      const errorCode = res.data.httpStatus;
      if (errorCode === 400) {
        useToast(res.data.errorMessage);
      } else {
        editMode.value = false;
        newServerName.value = '';
        inputChecked.value.push(res.data.id);
        await store.SET_CHANNEL_LIST();
      }
    } catch (e) {
      console.log(e);
    }
  } else {
    editMode.value = false;
  }
};
const updateInput = event => {
  newServerName.value = event.target.value;
};
const editServerNameList = server => {
  editServerId.value = server.id;
  editServerName.value = server.name;
};

const changeServerName = async server => {
  if (editServerName.value !== '') {
    const params = {
      id: server.id,
      useYn: server.useYn,
      channelName: editServerName.value,
      channelOrder: server.channelOrder,
    };
    await fetchEditChannelName(params);
    try {
      await store.SET_CHANNEL_LIST();
      editServerName.value = '';
      editServerId.value = null;
    } catch (e) {
      console.log(e);
    }
  } else {
    editServerName.value = '';
    editServerId.value = null;
  }
};
const closeEditInput = () => {
  editServerId.value = null;
  editServerName.value = '';
};
const hoverList = (event, id) => {
  hoverListId.value = id;
  if (event.type === 'mouseover') {
    isHovered.value = true;
  } else {
    isHovered.value = false;
  }
};
const editServerNameInput = event => {
  editServerName.value = event.target.value;
};
</script>

<style scoped>
[class~='error'] {
  color: red;
}

/* modal tansition 태그 styling */
.v-enter-from,
.v-leave-to {
  opacity: 0;
}

.v-enter-active,
.v-leave-active {
  transition: all 0.5s ease;
}

.v-enter-to,
.v-leave-from {
  opacity: 1;
}

.modal {
  --bs-modal-width: 500px;
}

/* 프로필 상단 영역

/* 프로필 수정 Form */
.profile-update-form {
  display: flex;
  flex-direction: column;
  background-color: #1e1f22;
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  padding: 1.2em;
  color: white;
  margin: 1rem;
}

.update-input-box label {
  font-size: 13px;
  color: #bcbcbd;
}

.update-input-box .custom-input {
  font-size: 17px;
  background-color: #313338;
}

.update-input-box .custom-input[readonly] {
  background-color: #1e1f22;
  padding: 0;
  height: auto;
  cursor: default;
}

/* 닫기 및 수정하기 버튼 영역 */
.update-button-container {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  border-radius: 0px 0px 4px 4px;
  background-color: rgb(16, 25, 16);
  padding: 16px;
}

.custom-cancel-btn {
  background-color: transparent;
  border: none;
  width: auto;
  height: 32px;
  min-width: 60px;
  min-height: 32px;
  color: #fdfdfd;
  margin-right: 12px;
}

.custom-cancel-btn:hover {
  text-decoration: underline;
}

.bi-plus-lg:hover {
  color: gray;
}

.bi-pencil-fill:hover {
  color: gray;
}

.bi-x:hover {
  color: red;
}

.bi-check-lg:hover {
  color: green;
}

button.edit-button {
  background-color: black;
  border: none;
  color: #ffffff;
  font-size: 16px;
  font-weight: bold;
  text-decoration: underline;
  cursor: pointer;
}

button.edit-button:hover {
  text-decoration: none;
}

button.edit-button:focus {
  outline: none;
}

button.edit-button:active {
  background-color: rgba(0, 122, 255, 0.1);
  box-shadow: inset 0px 3px 5px rgba(0, 0, 0, 0.2);
}

.custom-server-list {
  display: flex;
  padding: 10px;
}

.custom-server-list:hover {
  background-color: #393c41;
  color: white;
  border-radius: 3px;
}

.custom-checkbox {
  position: relative;
  display: flex;
}

.custom-checkbox-label {
  width: 50px;
  background: #eaeaea;
  box-shadow: 0 0 1px 2px rgba(0, 0, 0, 0.15);
  height: 25px;
  position: relative;
  display: inline-block;
  border-radius: 46px;
  transition: 0.4s;
}

.custom-checkbox:before {
  position: relative;
  left: 22px;
  bottom: -4px;
  color: #fff;
  z-index: 1;
}

.custom-checkbox:after {
  content: '\F62A';
  color: #aaa;
  position: relative;
  right: 23px;
  bottom: -4px;
  display: inline-block;
  font-family: bootstrap-icons !important;
  font-style: normal;
  font-weight: normal !important;
  font-variant: normal;
  text-transform: none;
  line-height: 1;
  vertical-align: -0.125em;
  -webkit-font-smoothing: antialiased;
}

.custom-checkbox label:after {
  top: 0;
  width: 25px;
  height: 25px;
  content: '';
  position: absolute;
  border-radius: 100%;
  left: 0;
  z-index: 2;
  background: #fff;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  transition: 0.4s;
}

.custom-checkbox input:checked + label {
  background: green;
}

.custom-checkbox input:checked + label:after {
  left: 25px;
}

.table-custom-scrollbar::-webkit-scrollbar {
  width: 3px;
}

.table-custom-scrollbar:hover::-webkit-scrollbar-thumb {
  background: #6c757d;
  border-radius: 10px;
}

.table-custom-scrollbar::-webkit-scrollbar-track {
  background: #2b2d31;
}
</style>
