<template>
  <div
    class="modal fade"
    id="exampleModal"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
    role="dialog"
    data-bs-backdrop="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">Modal title</h1>
        </div>
        <div class="modal-body">
          <div class="register-form-container">
            <form class="register-form" @submit.prevent="submitForm">
              <div class="d-flex">
                <h4 class="fw-bold text-center mb-4" style="color: #f3f4f5">
                  계정 만들기
                </h4>
                <button
                  type="button"
                  class="btn-close"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                ></button>
              </div>
              <div class="w-100">
                <div class="mb-2">
                  <label class="d-block"> 아이디 </label>
                  <input
                    v-model="memberId"
                    class="mt-1 register-input"
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
                    class="mt-1 register-input"
                    type="text"
                    placeholder="이름을 입력해주세요."
                    autocomplete="off"
                    required
                  />
                </div>
                <div class="mb-2">
                  <label class="d-block"> 비밀번호 </label>
                  <input
                    v-model="password"
                    class="mt-1 register-input"
                    type="text"
                    placeholder="***"
                    autocomplete="off"
                    required
                  />
                </div>
                <span class="text-danger">{{ logMessage }}</span>
                <button
                  type="submit"
                  :class="`${
                    formValid() === false ? 'disabled' : ''
                  } btn register-btn`"
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
      <!--        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Close
          </button>
          <button type="button" class="btn btn-primary">Save changes</button>
        </div>-->
    </div>
  </div>
</template>

<script setup>
import SpinnerComponent from '@/components/common/SpinnerComponent.vue';
import { ref } from 'vue';
import { signupUser } from '@/api/user';
import { useRouter } from 'vue-router';
import { useToast } from 'vue-toast-notification';
const router = useRouter();

const loading = ref(false);

// 회원 가입 form values
const memberId = ref('');
const name = ref('');
const password = ref('');

const logMessage = ref('');

const $toast = useToast({
  duration: 1000,
});

const submitForm = async () => {
  loading.value = true;

  const payload = {
    memberId: memberId.value,
    name: name.value,
    password: password.value,
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
          : (detailErrrorType = '비밀번호 - ');
      }
      logMessage.value = detailErrrorType + data.errorMessage;
    } else {
      logMessage.value = '';
      alert('사용자 등록 성공 !');
      // await router.replace('/');
      memberId.value = '';
      name.value = '';
      password.value = '';
    }
  } catch (err) {
    console.log('error :::: ', err);
    logMessage.value = err.data.detailMessage;
  } finally {
    loading.value = false;
  }
};

const formValid = () => {
  if (!memberId.value || !name.value || !password.value) {
    return false;
  }
};
</script>

<style scoped>
[class~='error'] {
  color: red;
}
</style>
