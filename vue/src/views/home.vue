<template>
  <!-- Page content wrapper-->
  <div id="page-content-wrapper">
    <!-- Top navigation-->
    <!-- 컴포넌트로뺴기 -->
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
          <span class="text-white"> 세브란스 스마트 선도사업 </span>
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
              <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>
            </li>
          </ul>
          <form class="d-flex">
            <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" />
            <button class="btn btn-outline-success" type="submit">Search</button>
          </form>
        </div>
      </div>
    </nav>

    교육훈련비 크릭 하고나서 세부내역 작성 결재라인 사내 결재 규정 교육훈련비 신

    <!-- Page content-->
    <div class="d-flex flex-column" style="">
      <div class="" style="background: none; border: 0">
        <div class="p-3">
          <div class="d-flex flex-row justify-content-start">
            <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava3-bg.webp" alt="avatar 1" style="width: 45px; height: 100%" />
            <div>
              <p class="small p-2 ms-3 mb-1 rounded-3" style="background-color: #f5f6f7">
                Hi How are you ...???<br />

                What are you doing<br />
                tomorrow? Can we come up a bar?
              </p>
              <p class="small ms-3 mb-3 rounded-3 text-muted">23:58</p>
            </div>
          </div>

          <div class="divider d-flex align-items-center justify-content-center mb-4">
            <p class="text-center mx-3 mb-0" style="color: #a2aab7">Today</p>
          </div>

          <div class="d-flex flex-row justify-content-end mb-4 pt-1">
            <div>
              <p class="small p-2 me-3 mb-1 text-white rounded-3 bg-primary">Hiii, I'm good.</p>
              <p class="small p-2 me-3 mb-1 text-white rounded-3 bg-primary">How are you doing?</p>
              <p class="small p-2 me-3 mb-1 text-white rounded-3 bg-primary">Long time no see! Tomorrow office. will be free on sunday.</p>
              <p class="small me-3 mb-3 rounded-3 text-muted d-flex justify-content-end">00:06</p>
            </div>
            <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava4-bg.webp" alt="avatar 1" style="width: 45px; height: 100%" />
          </div>
        </div>

        <div class="chat-footer text-muted d-flex justify-content-start align-items-center p-3">
          <input type="text" class="form-control shadow-none text-light form-control-lg bg-dark border-0" id="exampleFormControlInput1" placeholder="메세지 보내기" />
          <a class="ms-1 text-muted" href="#!"><i class="bi bi-paperclip fs-3 text-light"></i></a>
          <a class="ms-3 text-muted" href="#!"><i class="bi bi-emoji-smile  fs-4 text-light"></i></a>
          <a class="ms-3" href="#!"><i class="bi bi-send-fill  fs-4 text-light"></i></a>
        </div>
      </div>

      <!-- <div>
        <div>
          <div v-for="(item, index) in chatList" :key="index">
            <div>아이콘</div>
            <div>
              <div>
                <span>{{ item.name }}</span>
                <span>{{ item.time }}</span>
              </div>
              <div>
                <span style="white-space: pre-line">{{ item.msg }}</span>
              </div>
            </div>
          </div>
        </div>
      </div> -->
    </div>
  </div>
</template>

<script setup>
// import { reactive } from '@vue/reactivity';

// const chatList = reactive([
//   {
//     name: '홍길동',
//     time: '2022.10.12',
//     msg: '가나다라마바사',
//   },
//   {
//     name: '홍길동',
//     time: '2022.10.11',
//     msg: 'ㅇㅇ',
//   },
// ]);
</script>

<style>

</style>
