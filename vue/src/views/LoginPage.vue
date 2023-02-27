<template>
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-12 col-md-8 col-lg-6 col-xl-5">
        <div class="card bg-dark text-white" style="border-radius: 1rem">
          <div class="card-body p-5 text-center shadow">
            <div class="mb-md-5 mt-md-4 pb-5">
              <pre class="text-white">
  ╔╦╗╔╦╗╔═╗╔═╗╦═╗╔╦╗
  ║║║ ║║║  ║ ║╠╦╝ ║║
  ╩ ╩═╩╝╚═╝╚═╝╩╚══╩╝
  </pre
              >

              <div class="form-outline form-white mb-3">
                <input
                  type="text"
                  id="userId"
                  v-model="id"
                  class="form-control form-control-lg bg-dark text-white fs-6"
                  placeholder="ID"
                />
              </div>

              <div class="form-outline form-white mb-5">
                <input
                  type="password"
                  id="password"
                  v-model="password"
                  @keyup.enter="submit()"
                  class="form-control form-control-lg bg-dark text-white fs-6"
                  placeholder="PASSWORD"
                />
              </div>

              <button
                id="submitBtn"
                class="btn btn-light btn-lg px-5 w-100"
                type="submit"
                @click="submit()"
              >
                Login
              </button>

              <hr />
              <div class="d-flex justify-content-center text-center mt-4 pt-1">
                <a href="#!" class="text-white"
                  ><i class="bi bi-google mx-2 fs-3"></i
                ></a>
                <a href="#!" class="text-white"
                  ><i class="bi bi-github mx-2 fs-3"></i
                ></a>
              </div>
            </div>

            <div>
              <p class="mb-0">
                Don't have an account?
                <a href="#!" class="text-white-50 fw-bold">Sign Up</a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router/dist/vue-router';
import { onMounted, ref, inject } from 'vue';
import { fetchLogin } from '../api/index.js';
import { useToast } from 'vue-toast-notification';

const router = useRouter();
const $toast = useToast({
  duration: 1000,
});

const id = ref('lemon');
const password = ref('password1234');

onMounted(() => {
  onFocus();
});

const submit = () => {
  disToggle(true);

  fetchLogin({
    memberId: id.value,
    password: password.value,
  })
    .then(response => {
      console.log(response);
      console.log(
        Object.prototype.hasOwnProperty.call(response.data, 'httpStatus'),
      );

      if (Object.prototype.hasOwnProperty.call(response.data, 'httpStatus')) {
        $toast.error('Login Failed', {
          onDismiss: () => {
            disToggle(false);
            onFocus();
          },
        });
      } else {
        $toast.success('Login Success', {
          onDismiss: () => {
            router.push('/main');
            disToggle(false);
          },
        });
      }
    })
    .catch(() => {
      $toast.error('Server error');
      disToggle(false);
    });
};

const onFocus = () => {
  const elId = document.getElementById('userId');
  const elPw = document.getElementById('password');
  elId.value == '' ? elId.focus() : elPw.focus();
};

const disToggle = disabled => {
  document.getElementById('userId').disabled = disabled;
  document.getElementById('password').disabled = disabled;
  document.getElementById('submitBtn').disabled = disabled;
};
</script>

<style scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 2s ease;
}
.v-enter-from,
.v-leave-to {
  opacity: 0;
}
input:disabled {
  transition: 0.2s;
  background: #000000 !important;
}
</style>
