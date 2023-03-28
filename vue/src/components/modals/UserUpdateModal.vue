<template>
  <Transition>
    <div v-if="showUpdateUserModal">
      <div class="modal-backdrop fade show"></div>
      <div
        @mousedown="closeUpdateModal()"
        class="modal fade show d-block"
        id="updateModal"
        tabindex="-1"
        aria-labelledby="updateModalLabel"
        aria-hidden="true"
        role="dialog"
        data-bs-backdrop="true"
      >
        <div class="modal-dialog modal-dialog-centered">
          <div
            @mousedown.stop
            class="modal-content"
            style="background-color: #313338"
          >
            <div class="modal-body p-0">
              <div style="margin-bottom: 40px">
                <div
                  role="button"
                  class="profile-update-button"
                  title="프로필 편집"
                  @click="clickEditButton()"
                >
                  <i class="bi bi-pencil-fill" style="color: #eeeeee"></i>
                </div>
                <div class="profile-banner"></div>
                <div class="position-relative">
                  <div
                    class="profile-update-button-image"
                    :style="{
                      background: `url(${imageUrl()}) 0% 0% / contain no-repeat`,
                    }"
                  ></div>
                </div>
              </div>
              <form class="profile-update-form" @submit.prevent="submitForm">
                <div class="mb-3 update-input-box">
                  <label class="d-block"> 이름</label>
                  <input
                    class="mt-1 custom-input"
                    type="text"
                    placeholder="이름을 입력해주세요."
                    autocomplete="off"
                    required
                    :readonly="!updateMode"
                    ref="nameInput"
                    v-model="updateMemberInfo.name"
                  />
                </div>
                <div class="mb-3 update-input-box">
                  <label class="d-block"> 아이디</label>
                  <input
                    class="mt-1 custom-input"
                    type="text"
                    placeholder="아이디를 입력해주세요."
                    required
                    :readonly="!updateMode"
                    v-model="updateMemberInfo.memberId"
                  />
                  <!--            type="email"-->
                </div>
                <div v-if="updateMode" class="mb-3 update-input-box">
                  <label class="d-block"> 비밀번호</label>
                  <input
                    v-model="password"
                    class="mt-1 custom-input"
                    type="password"
                    placeholder="변경할 비밀번호를 입력해주세요."
                    autocomplete="new-password"
                  />
                </div>
                <div class="update-input-box mb-3">
                  <label class="d-block"> 역할</label>

                  <div v-if="!updateMode" class="role-box">
                    <span style="" class="role-circle"></span>
                    <div
                      style="
                        max-width: 200px;
                        margin-right: 4px;
                        font-size: 14px;
                        line-height: 16px;
                      "
                    >
                      {{ userType(updateMemberInfo.role) }}
                    </div>
                  </div>
                  <div v-else>
                    <select
                      class="form-select"
                      aria-label="Default select example"
                      v-model="updateMemberInfo.role"
                    >
                      <option value="USER">사용자</option>
                      <option value="ADMIN">관리자</option>
                    </select>
                  </div>
                </div>
                <div v-if="!updateMode" class="update-input-box">
                  <label class="d-block"> 등록일</label>
                  <div
                    class="mt-1 p-0"
                    style="background-color: #1e1f22; pointer-events: none"
                  >
                    {{ $dayjs(memberInfo.createDate).format('MM월 DD, YYYY') }}
                  </div>
                </div>
                <span class="text-danger">{{ logMessage }}</span>
              </form>
              <div class="update-button-container">
                <button
                  type="button"
                  class="'btn custom-cancel-btn"
                  :style="{
                    'pointer-events': loading === true ? 'none' : '',
                  }"
                  @click="closeUpdateModal()"
                >
                  <div type="button">닫기</div>
                </button>
                <button
                  v-show="updateMode"
                  @click="submitForm()"
                  class="btn update-confirm-btn"
                  :class="`${formValid() === false ? 'disabled' : ''} `"
                  :style="{
                    'pointer-events': loading === true ? 'none' : '',
                  }"
                >
                  <spinner-component v-if="loading" />
                  <div v-else type="button">수정하기</div>
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
import SpinnerComponent from '@/components/common/SpinnerComponent.vue';
import { onBeforeUpdate, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useToast } from 'vue-toast-notification';
import { userProfileIcon, userType } from '@/utils/common';
import { fetchUpdateMember } from '@/api/user';

const router = useRouter();

const loading = ref(false);

const $toast = useToast({
  duration: 1000,
});

const updateMode = ref(false); // 값이 false 이면 사용자 정보 조회 모드

const props = defineProps({
  showUpdateUserModal: {
    type: Boolean,
    default: false,
  },
  memberInfo: {
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

onBeforeUpdate(() => {
  updateMemberInfo.value.memberId = props.memberInfo.memberId;
  updateMemberInfo.value.name = props.memberInfo.name;
  updateMemberInfo.value.useYn = props.memberInfo.useYn;
  updateMemberInfo.value.role = props.memberInfo.role;
  updateMemberInfo.value.iconFileId = props.memberInfo.iconFileId;
});

const logMessage = ref('');

const closeUpdateModal = () => {
  emits('update:showUpdateUserModal', false);
  updateMode.value = false;
};

// 수정 버튼 DOM refs
const nameInput = ref(null);
// 수정 버튼 클릭 이벤트
const clickEditButton = () => {
  updateMode.value = !updateMode.value;
  nameInput.value.focus();
  initInputForm();
};

const submitForm = async () => {
  loading.value = true;
  /**
   * 사용자 수정 API 호출 시 password, iconFileId는 null 가능, 나머지는 not null
   * {
   *   "memberId": "lemon",
   *   "name": "사용자",
   *   "password": "password1234",
   *   "iconFileId": 1,
   *   "useYn": "Y"
   *   "role": "USER"
   * }
   * */
  const payload = {
    memberId: updateMemberInfo.value.memberId,
    name: updateMemberInfo.value.name,
    password: password.value,
    useYn: updateMemberInfo.value.useYn,
    role: updateMemberInfo.value.role,
  };
  try {
    const { data } = await fetchUpdateMember(payload);
    if (Object.prototype.hasOwnProperty.call(data, 'errorMessage')) {
      let detailErrrorType = '';
      if (data.details && data.details.split(' ').length === 1) {
        // TODO 에러처리
        /*data.details === 'memberId'
          ? (detailErrrorType = '아이디 - ')
          : data.details === 'name'
          ? (detailErrrorType = '이름 - ')
          : (detailErrrorType = '비밀번호 - ');*/
      }
      logMessage.value = detailErrrorType + data.errorMessage;
    } else {
      updateMode.value = false;
      $toast.success('사용자 정보 수정 완료');
      emits('submitUpdateMemberInfo', updateMemberInfo.value);
    }
  } catch (err) {
    console.log('error :::: ', err);
  } finally {
    setTimeout(() => {
      loading.value = false;
    }, 200);
  }
};

const formValid = () => {
  if (
    !updateMemberInfo.value.memberId ||
    !updateMemberInfo.value.name ||
    !updateMemberInfo.value.role
  ) {
    return false;
  }
};

// input form 초기화
const initInputForm = () => {
  logMessage.value = '';
  updateMemberInfo.value.memberId = props.memberInfo.memberId;
  updateMemberInfo.value.name = props.memberInfo.name;
  updateMemberInfo.value.role = props.memberInfo.role;
};

// 추후 대리님이 추가하신 공통함수 적용
const imageUrl = () => {
  return new URL(
    `../../assets/images/profile/${userProfileIcon(
      props.memberInfo.iconFileId,
    )}.png`,
    import.meta.url,
  ).href;
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
</style>
