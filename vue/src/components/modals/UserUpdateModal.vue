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
                <div
                  class="profile-box"
                  :class="[
                    updateMode === true ? 'update-mode' : '',
                    props.memberInfo.iconFileId !== updateMemberInfo.iconFileId
                      ? 'updating'
                      : '',
                  ]"
                >
                  <div
                    class="profile-update-button-image"
                    :style="{
                      background: `url(${getImageUrl(
                        `profile/${userProfileIcon(
                          updateMemberInfo.iconFileId,
                        )}.png`,
                      )}) 0% 0% / contain no-repeat`,
                    }"
                  ></div>
                  <div
                    class="profile-update-text"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                  >
                    <p>프로필 편집</p>
                  </div>
                  <ul class="dropdown-menu">
                    <template v-for="(image, index) in 50" :key="index">
                      <li @click="selectProfileImage(image)" :title="image">
                        <a class="dropdown-item" href="#">
                          <img
                            :src="
                              getImageUrl(
                                `profile/${userProfileIcon(image)}.png`,
                              )
                            "
                            width="50"
                            height="50"
                          />
                        </a>
                      </li>
                    </template>
                  </ul>
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
                <div v-if="!updateMode" class="mb-3 update-input-box">
                  <label class="d-block"> 아이디</label>
                  <div
                    class="mt-1 p-0"
                    style="
                      background-color: #1e1f22;
                      pointer-events: none;
                      font-size: 17px;
                    "
                  >
                    {{ props.memberInfo.memberId }}
                  </div>
                </div>
                <div
                  v-if="updateMode"
                  class="mb-3 update-input-box position-relative"
                >
                  <label class="d-block"> 비밀번호</label>
                  <input
                    v-model="password"
                    class="mt-1 custom-input"
                    :type="showPassword === true ? 'text' : 'password'"
                    placeholder="변경할 비밀번호를 입력해주세요."
                    autocomplete="new-password"
                  />
                  <div
                    class="position-absolute"
                    style="
                      right: 10px;
                      top: 30px;
                      cursor: pointer;
                      width: 17px;
                      height: 20px;
                    "
                    @click.stop="showPassword = !showPassword"
                  >
                    <i
                      class="bi position-absolute"
                      :class="
                        showPassword === true
                          ? 'bi-eye-fill'
                          : 'bi-eye-slash-fill'
                      "
                    ></i>
                  </div>
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
              <div
                class="update-button-container"
                :style="
                  props.memberInfo.updateDate !== '' &&
                  props.memberInfo.updateDate !== null &&
                  !updateMode
                    ? 'justify-content: space-between'
                    : ''
                "
              >
                <div
                  v-if="
                    props.memberInfo.updateDate !== '' &&
                    props.memberInfo.updateDate !== null &&
                    !updateMode
                  "
                  class="update-input-box"
                  style="padding-left: 0.5rem"
                >
                  <label class="d-block" style="color: #787878">
                    {{ $dayjs(memberInfo.updateDate).format('YY.MM.DD HH:mm') }}
                    last updated
                  </label>
                </div>
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
import { onBeforeUpdate, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useToast } from 'vue-toast-notification';
import { getImageUrl, userProfileIcon, userType } from '@/utils/common';
import { fetchUpdateMember } from '@/api/user';

const router = useRouter();

const loading = ref(false);

const $toast = useToast({
  duration: 1000,
});
const updateMode = ref(false); // 값이 false 이면 사용자 정보 조회 모드
const showPassword = ref(false); // 비밀번호 보이기/가리기 기능
const imageList = ref([]); // assets 디렉토리에 있는 프로필 이미지 파일 저장

onMounted(() => {
  // const images = require.context('@/assets/images/profile/', true);
  const images = import.meta.glob(
    '@/assets/images/profile/*.{png,jpg,jpeg,svg}',
  );
  imageList.value = Object.keys(images).map(key =>
    key
      .split('/')
      .pop()
      .replace(/\.[^/.]+$/, ''),
  );
});

const props = defineProps({
  showUpdateUserModal: {
    type: Boolean,
    default: false,
  },
  memberInfo: {
    type: Object,
    required: true,
    default: () => {
      return {
        memberId: '',
        name: '',
        role: '',
        useYn: '',
        iconFileId: '',
      };
    },
  },
});
const emits = defineEmits([
  'reloadFetchMembers',
  'update:showUpdateUserModal',
  'submitUpdateMemberInfo',
]);

// form values
const updateMemberInfo = ref({
  name: '',
  role: '',
  useYn: '',
  iconFileId: '',
});
const password = ref('');

onBeforeUpdate(() => {
  initInputForm();
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

const selectProfileImage = image => {
  updateMemberInfo.value.iconFileId = image;
};

const submitForm = async () => {
  loading.value = true;
  /**
   * 사용자 수정 API 호출 시 password, iconFileId는 null 가능, 나머지는 not null
   * {
   *   "name": "사용자",
   *   "password": "password1234",
   *   "iconFileId": 1,
   *   "useYn": "Y"
   *   "role": "USER"
   * }
   * */
  const payload = {
    ...updateMemberInfo.value,
    password: password.value,
  };
  try {
    const { data } = await fetchUpdateMember(
      props.memberInfo.memberId,
      payload,
    );
    if (Object.prototype.hasOwnProperty.call(data, 'errorMessage')) {
      let detailErrrorType = '';
      if (data.details && data.details.split(' ').length === 1) {
        // TODO 에러처리
        data.details === 'name'
          ? (detailErrrorType = '이름 - ')
          : (detailErrrorType = '비밀번호 - ');
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
  if (!updateMemberInfo.value.name || !updateMemberInfo.value.role) {
    return false;
  }
};

// input form 초기화
const initInputForm = () => {
  logMessage.value = '';
  showPassword.value = false;
  updateMemberInfo.value.memberId = props.memberInfo.memberId;
  updateMemberInfo.value.name = props.memberInfo.name;
  updateMemberInfo.value.role = props.memberInfo.role;
  updateMemberInfo.value.useYn = props.memberInfo.useYn;
  updateMemberInfo.value.iconFileId = props.memberInfo.iconFileId;
  password.value = '';
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
.updating .profile-update-button-image {
  animation: avatar-animation 3s ease infinite;
}
@keyframes avatar-animation {
  0% {
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(1turn);
    transform: rotate(1turn);
  }
}
.dropdown-menu.show {
  flex-wrap: wrap;
  display: flex;
  padding: 0.5rem 0.1rem;
}
.dropdown-menu {
  --bs-dropdown-link-active-bg: #e9ecef;
}
</style>
