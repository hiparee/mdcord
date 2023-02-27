<template>
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center">
      <div class="col-12 col-md-8 col-lg-6 col-xl-5">
        <div class="card bg-dark text-white border-0">
          <div class="mt-3">
            <p class="h1 text-info">길을 잘못 들었나요?</p>
            길을 잃으신 모양이에요. 길을 잃었을 땐 어떡하면 좋으냐고요? 뜨끈한
            국수 한 그릇 먹으면 되죠. 저희가 국수를 만들어 올 테니 그동안 잠시
            기다려주세요. 읽을 게 필요하시다고요? 그럼 이걸 한번 보세요.
          </div>
          <div class="mt-3">
            <img src="@/assets/images/404.gif" style="width: 100%" />
          </div>
          <div class="text-center mt-3">
            <router-link to="/">
              <button type="button" class="btn btn-info text-white">
                홈으로
              </button>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style></style>
