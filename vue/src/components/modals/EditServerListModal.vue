<template>
  <Transition>
    <div v-if="showEditServerListModal">
      <div class="modal-backdrop fade show"></div>
      <div
        @mousedown="closeModal()"
        class="modal modal-lg fade show d-block"
        id="updateModal"
        tabindex="-1"
        aria-labelledby="updateModalLabel"
        aria-hidden="true"
        role="dialog"
        data-bs-backdrop="true"
      >
        <div
          class="modal-dialog modal-dialog-centered"
          style="max-width: 1000px !important"
        >
          <div
            @mousedown.stop
            class="modal-content"
            style="background-color: #313338"
          >
            <div class="modal-body p-0">
              <form class="profile-update-form" @submit.prevent="submitForm">
                <div style="display: flex; padding: 10px">
                  <span style="flex: 1 0; color: #f2f3f5; font-size: 20px"
                    >서버명</span
                  >
                </div>
                <hr />
                <div
                  v-for="(server, index) in store.getServerList"
                  :key="server.id"
                  class="mb-3 update-input-box"
                >
                  <div style="display: flex; padding: 10px">
                    <span style="flex: 1 0; color: #cfcfcf">
                      {{ server.name }}
                    </span>
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
              </form>
              <div class="update-button-container">
                <button
                  type="button"
                  class="'btn custom-cancel-btn"
                  :style="{
                    'pointer-events': loading === true ? 'none' : '',
                  }"
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
import { fetchEditChannelName } from '@/api/channel';

const router = useRouter();
const store = useChannelStore();
const loading = ref(false);
const inputChecked = ref([]);
const listClickd = ref(false);
const listId = ref(null);
const editMode = ref(false); // 값이 false 이면 사용자 정보 조회 모드

onBeforeMount(() => {
  for (const server of store.serverList) {
    if (server.useYn === 'Y') {
      inputChecked.value.push(server.id);
    }
  }
});
const changeChannelStatus = async server => {
  console.log(server);
  if (inputChecked.value.includes(server.id)) {
    console.log('del');
    inputChecked.value = inputChecked.value.filter(i => i !== server.id);
    console.log('now', inputChecked.value);
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
const props = defineProps({
  showEditServerListModal: {
    type: Boolean,
    default: false,
  },
  serverList: {
    type: Object,
    required: true,
  },
});
const emits = defineEmits([
  'reloadFetchMembers',
  'update:showUpdateUserModal',
  'submitUpdateModal',
]);

// form values
const updateMemberInfo = ref({
  memberId: '',
  name: '',
  role: '',
  useYn: '',
  iconFileId: '',
});

const password = ref('');
// 타입 셀렉트 박스 show/hide 여부
const selectBoxShow = ref(false);

// onBeforeUpdate(() => {
//   updateMemberInfo.value.memberId = props.memberInfo.memberId;
//   updateMemberInfo.value.name = props.memberInfo.name;
//   updateMemberInfo.value.useYn = props.memberInfo.useYn;
//   updateMemberInfo.value.role = props.memberInfo.role;
//   updateMemberInfo.value.iconFileId = props.memberInfo.iconFileId;
// });

const logMessage = ref('');

const closeModal = () => {
  emits('update:showEditServerListModal', false);
  editMode.value = false;
};

// 수정 버튼 DOM refs
const nameInput = ref(null);
// 수정 버튼 클릭 이벤트
// const clickEditButton = () => {
//   editMode.value = !editMode.value;
//   nameInput.value.focus();
//   initInputForm();
// };

// const submitForm = async () => {
//   loading.value = true;
//   /**
//    * 사용자 수정 API 호출 시 password, iconFileId는 null 가능, 나머지는 not null
//    * {
//    *   "memberId": "lemon",
//    *   "name": "사용자",
//    *   "password": "password1234",
//    *   "iconFileId": 1,
//    *   "useYn": "Y"
//    *   "role": "USER"
//    * }
//    * */
//   const payload = {
//     memberId: updateMemberInfo.value.memberId,
//     name: updateMemberInfo.value.name,
//     password: password.value,
//     useYn: updateMemberInfo.value.useYn,
//     role: updateMemberInfo.value.role,
//   };
//   try {
//     const { data } = await fetchUpdateMember(payload);
//     if (Object.prototype.hasOwnProperty.call(data, 'errorMessage')) {
//       let detailErrrorType = '';
//       if (data.details && data.details.split(' ').length === 1) {
//         // TODO 에러처리
//         /*data.details === 'memberId'
//           ? (detailErrrorType = '아이디 - ')
//           : data.details === 'name'
//           ? (detailErrrorType = '이름 - ')
//           : (detailErrrorType = '비밀번호 - ');*/
//       }
//       logMessage.value = detailErrrorType + data.errorMessage;
//     } else {
//       editMode.value = false;
//       $toast.success('사용자 정보 수정 완료');
//       emits('submitUpdateMemberInfo', updateMemberInfo.value);
//     }
//   } catch (err) {
//     console.log('error :::: ', err);
//   } finally {
//     setTimeout(() => {
//       loading.value = false;
//     }, 200);
//   }
// };

// const formValid = () => {
//   if (
//     !updateMemberInfo.value.memberId ||
//     !updateMemberInfo.value.name ||
//     !updateMemberInfo.value.role
//   ) {
//     return false;
//   }
// };

// input form 초기화
// const initInputForm = () => {
//   logMessage.value = '';
//   updateMemberInfo.value.memberId = props.memberInfo.memberId;
//   updateMemberInfo.value.name = props.memberInfo.name;
//   updateMemberInfo.value.role = props.memberInfo.role;
// };
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
/* 프로필 상단 영역 */
.profile-banner {
  min-height: 116px;
  border-radius: 4px 4px 0 0;
  background-color: #1e1f22;
}
/* 프로필 편집 버튼 */
.profile-update-button {
  top: 14px;
  right: 16px;
  cursor: pointer;
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 5px;
  border-radius: 50%;
}
.profile-update-button-image {
  width: 120px;
  height: 120px;
  position: absolute;
  top: -80px;
  left: 22px;
  border-radius: 6%;
}
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
/* 사용자 역할 Box */
.role-box {
  display: flex;
  align-items: center;
  box-sizing: border-box;
  width: fit-content;
  height: 22px;
  margin: 0 4px 4px 0;
  padding: 4px;
  background: #161616;
  border-radius: 4px;
}
.role-circle {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  width: 10px;
  height: 10px;
  padding: 0;
  margin: 0 4px;
  background-color: #c4c9ce;
  border-radius: 50%;
  forced-color-adjust: none;
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
.update-confirm-btn {
  width: auto;
  min-width: 60px;
  min-height: 32px;
  background-color: #5865f2;
  transition: background-color 0.17s ease, color 0.17s ease;
  outline: 0;
  border: none;
  border-radius: 3px;
  color: #fdfdfd;
}
.update-confirm-btn.disabled {
  background-color: #5865f2;
}
.update-confirm-btn:hover {
  background-color: #4752c4;
  color: #ffffff;
}

/* 사용자 역할 선택 select box custom styling*/
.form-select {
  background-image: url('/src/assets/images/expand.svg');
  background-color: #313338;
  color: #f3f4f5;
  border: 1px solid #313338;
  cursor: pointer;
}
.form-select:focus {
  border: 1px solid #313338;
  box-shadow: none;
}
.form-select option {
  background-color: #313338;
  color: #f3f4f5;
  cursor: pointer;
}
.bi-pencil:hover {
  color: green;
}

.bi-trash:hover {
  color: red;
}

.edit-listName {
  margin-right: 5px;
  display: inline-flex;
}

.remove-list {
  display: inline-flex;
}

.check-button {
  color: green;
}

.close-button {
  color: red;
}

.plus-icon {
  font-size: 22px;
  cursor: pointer;
  pointer-events: auto;
}

.plus-icon-sm {
  font-size: 15px;
  cursor: pointer;
  pointer-events: auto;
}

.custom-tabBarItem {
  padding-bottom: 16px;
  margin-right: 16px;
}

.custom-tabbar {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: horizontal;
  -webkit-box-direction: normal;
  -ms-flex-direction: row;
  flex-direction: row;
  padding-top: 6px;
  margin-top: 0px;
  margin-bottom: 16px;
  border-bottom: 1px solid;
  border-bottom-color: #3f4046;
}

.border-active {
  color: #ffffff !important;
  border-bottom: 2px solid;
  border-bottom-color: #939bf5;
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

/*아이콘 떨림 애니메이션*/
.icon {
  position: relative;
  animation-name: shake;
  animation-duration: 1s;
  animation-iteration-count: infinite;
}

/*.icon:focus {*/

/*}*/

@keyframes shake {
  0% {
    transform: translate(0);
  }
  25% {
    transform: translate(-1px, -1px) rotate(-1deg);
  }
  50% {
    transform: translate(1px, 1px) rotate(1deg);
  }
  75% {
    transform: translate(-1px, 1px) rotate(-1deg);
  }
  100% {
    transform: translate(1px, -1px) rotate(1deg);
  }
}

/*수정할 인풋 텍스트 커스텀 */
.edit-input {
  background: transparent;
  border: inset;
  color: white;
}

/*채널리스트 이동시 애니메이션 효과 */
.flip-list-move {
  transition: transform 0.5s;
}

.no-move {
  transition: transform 0s;
}

.ghost {
  opacity: 0.5;
  background: #c8ebfb;
}

.list-group {
  min-height: 20px;
}

.list-group-item {
  cursor: move;
}

.list-group-item i {
  cursor: pointer;
}

.deactivation-list {
  text-decoration: line-through;
}

.custom-list {
}

.custom-button {
  background-color: #5561f4ff !important;
  color: #ffffff !important;
  cursor: auto;
  opacity: 1 !important;
}

.mb-2:hover {
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

.hover-class {
  color: #ffffff; /* 텍스트 색상 변경 */
  background-color: #fdfdfd; /* 배경색 변경 */
  cursor: pointer; /* 커서 모양 변경 */
}

.list-clicked {
  background-color: #42434a !important;
}

/* 마우스가 호버될 요소를 선택 */
.high-channels:hover {
  /* hover-class 스타일 적용 */
  color: #fff;
  background-color: #393c41;
  cursor: pointer;
}

.channel-list {
  width: 25%;
  max-width: 15%;
  background-color: #2b2d31;
  margin-right: 5px;
  padding: 10px;
}
</style>
