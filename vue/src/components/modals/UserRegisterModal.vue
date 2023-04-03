<template>
  <div
    @mousedown="modalCloseBtn.click()"
    class="modal fade"
    id="registerModal"
    tabindex="-1"
    aria-labelledby="registerModalLabel"
    aria-hidden="true"
    role="dialog"
    data-bs-backdrop="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div
        @mousedown.stop
        class="modal-content"
        style="background-color: #313338; padding: 25px"
      >
        <div class="custom-modal-header d-flex align-items-baseline">
          <h4 class="fw-bold text-center mb-4" style="color: #f3f4f5">
            계정 만들기
          </h4>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
            ref="modalCloseBtn"
            @click="initInputForm()"
          ></button>
        </div>
        <div class="modal-body p-0">
          <form class="d-flex flex-column" @submit.prevent="submitForm">
            <div class="w-100">
              <div class="mb-2">
                <label class="d-block"> 아이디 </label>
                <input
                  v-model="memberId"
                  class="mt-1 custom-input"
                  type="text"
                  placeholder="아이디를 입력해주세요."
                  required
                />
                <!--            type="email"-->
              </div>
              <div class="mb-2">
                <label class="d-block"> 이름 </label>
                <input
                  v-model="name"
                  class="mt-1 custom-input"
                  type="text"
                  placeholder="이름을 입력해주세요."
                  autocomplete="off"
                  required
                />
              </div>
              <div class="mb-2 position-relative">
                <label class="d-block"> 비밀번호 </label>
                <input
                  v-model="password"
                  class="mt-1 custom-input"
                  :type="showPassword === true ? 'text' : 'password'"
                  placeholder="비밀번호를 입력해주세요."
                  autocomplete="off"
                  required
                />
                <div
                  class="show-password-button"
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

              <div class="mb-2">
                <label class="d-block">
                  서버 선택
                  <span style="font-size: 0.8rem"> (다중 선택 가능)</span>
                </label>
                <div>
                  <select
                    class="form-select"
                    style="background-color: #1e1f22"
                    aria-label="Default select example"
                    v-model="selectedServer"
                  >
                    <option value="0" selected disabled>선택</option>
                    <option
                      v-for="(item, index) in filterActiveServerList"
                      :key="index"
                      :value="item.id"
                      :disabled="
                        selectedServerIdList.some(
                          selectedServerId => selectedServerId === item.id,
                        )
                      "
                    >
                      {{ item.name }}
                    </option>
                  </select>
                </div>
                <div class="server-section">
                  <div
                    v-for="(data, index) in selectedServerList"
                    :key="index"
                    class="role-box mr-2"
                  >
                    <div class="server-name-text">
                      {{ data.name }}
                    </div>
                    <span
                      class="bi-x"
                      @click.stop="removeSelectedServer(data.id)"
                    ></span>
                  </div>
                </div>
              </div>
              <span class="text-danger">{{ logMessage }}</span>
              <button
                type="submit"
                :class="`${
                  formValid() === false ? 'disabled' : ''
                } btn custom-confirm-btn`"
                :style="{
                  'pointer-events': loading === true ? 'none' : '',
                }"
              >
                <spinner-component v-if="loading" />
                <div v-else type="button">등록하기</div>
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
import { computed, ref, watch } from 'vue';
import { signupUser } from '@/api/user';
import { useRouter } from 'vue-router';
import { useToast } from 'vue-toast-notification';
import { useChannelStore } from '@/store/modules/channel';

const router = useRouter();

const loading = ref(false);

// 회원 가입 form values
const memberId = ref('');
const name = ref('');
const password = ref('');
const showPassword = ref(false); // 비밀번호 보이기/가리기 기능

const selectedServer = ref(0); // 셀렉트박스에서 선택된 value
const selectedServerList = ref([]); // 선택된 서버의 데이터 객체를 담을 리스트
/*
 *  선택된 서버의 아이디를 담을 리스트
 *  ==> API에 담길 데이터
 * */
const selectedServerIdList = ref([]);
// store에 있는 서버 리스트에서 활성화된 서버 필터링
const filterActiveServerList = computed(() => {
  return useChannelStore().serverList.filter(item => item.useYn === 'Y');
});
// 서버 셀렉트박스에 대한 감시자
watch(selectedServer, newValue => {
  const index = filterActiveServerList.value.findIndex(
    option => option.id === newValue,
  );
  if (index > -1) {
    selectedServerIdList.value.push(selectedServer.value);
    selectedServerList.value.push({
      ...filterActiveServerList.value[index],
    });
  }
});

const removeSelectedServer = removeId => {
  if (selectedServer.value === removeId) {
    selectedServer.value = 0;
  }
  selectedServerIdList.value = selectedServerIdList.value.filter(
    item => item !== removeId,
  );
  selectedServerList.value = selectedServerList.value.filter(
    item => item.id !== removeId,
  );
};

const logMessage = ref('');

const modalCloseBtn = ref(null);

const $toast = useToast({
  duration: 1000,
});

const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
});
const emits = defineEmits(['reloadFetchMembers']);
const submitForm = async () => {
  loading.value = true;

  const payload = {
    memberId: memberId.value,
    name: name.value,
    password: password.value,
    channelIds: selectedServerIdList.value,
  };
  try {
    const { data } = await signupUser(payload);
    if (Object.prototype.hasOwnProperty.call(data, 'errorMessage')) {
      let detailErrrorType = '';
      if (data.details && data.details.split(' ').length === 1) {
        data.details === 'memberId'
          ? (detailErrrorType = '아이디 - ')
          : data.details === 'name'
          ? (detailErrrorType = '이름 - ')
          : data.details === 'password'
          ? (detailErrrorType = '비밀번호 - ')
          : (detailErrrorType = '서버 선택 - ');
      }
      logMessage.value = detailErrrorType + data.errorMessage;
    } else {
      modalCloseBtn.value.click();
      $toast.success('사용자 추가 완료');
      emits('reloadFetchMembers');
    }
  } catch (err) {
    console.log('error :::: ', err);
    logMessage.value = err.data.detailMessage;
  } finally {
    loading.value = false;
  }
};

const formValid = () => {
  if (
    !memberId.value ||
    !name.value ||
    !password.value ||
    selectedServerList.value.length === 0
  ) {
    return false;
  }
};

const initInputForm = () => {
  logMessage.value = '';
  memberId.value = '';
  name.value = '';
  password.value = '';
  showPassword.value = false;
  selectedServer.value = 0;
  selectedServerIdList.value = [];
  selectedServerList.value = [];
};
</script>

<style scoped>
[class~='error'] {
  color: red;
}

.show-password-button {
  position: absolute;
  right: 10px;
  top: 36px;
  cursor: pointer;
  width: 17px;
  height: 20px;
}
</style>
