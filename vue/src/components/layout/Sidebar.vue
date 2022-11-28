<template>
  <!-- Sidebar-->
  <div id="sidebar-wrapper">
    <div class="sidebar-heading">
      <div class="navbar-collapse" id="navbarNavDarkDropdown">
        <ul class="navbar-nav">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDarkDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <img src="@/assets/images/icon.png" alt="" style="width: 30px; height: 30px; margin-right: 5px" />
              미션개발팀
            </a>
            <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="navbarDarkDropdownMenuLink">
              <li><a class="dropdown-item" href="#" @click="emitEvent()">서버추가</a></li>
              <li><hr class="dropdown-divider bg-light" /></li>
              <li><a class="dropdown-item" href="#">의료컨텐츠사업부</a></li>
              <li><a class="dropdown-item" href="#">미션개발팀</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>

    <hr class="m-0" />

    <div class="list-group list-group-flush">
      <ul class="list-unstyled ps-0" id="sidebar">
        <li class="mb-2">
          <button class="btn btn-toggle align-items-center rounded" data-bs-toggle="collapse" data-bs-target="#channel1" aria-expanded="true">Home</button>

          <i class="bi bi-plus float-end plus-icon"></i>

          <div class="collapse show" id="channel1" style="">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="rounded">공지사항</a></li>
              <li><a href="#" class="rounded">잡담</a></li>
            </ul>
          </div>
        </li>
        <li class="mb-2">
          <button class="btn btn-toggle align-items-center rounded collapsed" data-bs-toggle="collapse" data-bs-target="#channel2" aria-expanded="true">정부과제</button>
          <i class="bi bi-plus float-end plus-icon"></i>
          <div class="collapse show" id="channel2" style="">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="rounded">생활치료센터</a></li>
              <li><a href="#" class="rounded">재택치료센터</a></li>
              <li><a href="#" class="rounded">비대면-비접촉</a></li>
              <li><a href="#" class="rounded">부천시 스마트경로당</a></li>
              <li><a href="#" class="rounded">세브란스 스마트 선도사업</a></li>
              <li><a href="#" class="rounded">국립암센터 국민건강스마트관리연구개발사업</a></li>
              <li><a href="#" class="rounded">감염병 빅데이터</a></li>
            </ul>
          </div>
        </li>
        <li class="mb-2">
          <button class="btn btn-toggle align-items-center rounded" data-bs-toggle="collapse" data-bs-target="#channel3" aria-expanded="true">전자동의서</button>
          <i class="bi bi-plus float-end plus-icon"></i>
          <div class="collapse show" id="channel3" style="">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="rounded">영남대학교병원</a></li>
              <li><a href="#" class="rounded">대구파티마병원</a></li>
            </ul>
          </div>
        </li>

        <li class="mb-2">
          <button class="btn btn-toggle align-items-center rounded" data-bs-toggle="collapse" data-bs-target="#channel4" aria-expanded="true">전자동의서</button>
          <i class="bi bi-plus float-end plus-icon"></i>
          <div class="collapse show" id="channel4" style="">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="#" class="rounded">영남대학교병원</a></li>
              <li><a href="#" class="rounded">대구파티마병원</a></li>
            </ul>
          </div>
        </li>

        <hr class="my-3" />

        <li class="mb-2">
          <button class="btn btn-toggle align-items-center rounded collapsed" data-bs-toggle="collapse" data-bs-target="#account-collapse" aria-expanded="true">바로가기</button>

          <div class="collapse show" id="account-collapse">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li><a href="https://office.hiworks.com/lemonhc.com" target="_blank" class="rounded">Hiworks</a></li>
              <li><a href="https://auth.ncloud.com/nsa/cost" target="_blank" class="rounded">Naver Cloud</a></li>
              <li><a href="https://jira.lemonhc.com/secure/Dashboard.jspa" target="_blank" class="rounded">Jira</a></li>
              <li><a href="https://docs.lemonhc.com/login.action?os_destination=%2Findex.action&permissionViolation=true#all-updates" target="_blank" class="rounded">Confluence</a></li>
              <li>
                <a
                  href="https://m.search.naver.com/search.naver?sm=mtb_hty.top&where=m&oquery=%EB%A3%B0%EB%A0%9B&tqi=h33g5sp0iqdssfp0eEossssst1s-130140&query=%EB%A3%B0%EB%A0%9B"
                  target="_blank"
                  class="rounded"
                  >룰렛돌리기</a
                >
              </li>
            </ul>
          </div>
        </li>
      </ul>
    </div>

    <div class="member-info">
      <div class="card" style="display: none">
        <div class="card-body">
          <div class="d-flex text-light">
            <div class="flex-shrink-0">
              <img
                src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-profiles/avatar-1.webp"
                alt="Generic placeholder image"
                class="img-fluid"
                style="width: 30px; border-radius: 10px"
              />
            </div>
            <div class="flex-grow-1 ms-3">
              <p class="mb-1 p-0" style="color: #2b2a2a">홍길동</p>
              <div class="d-flex flex-column" style="background-color: #efefef">
                <div class="flex:1">
                  <p class="small text-muted my-1 mx-1">글 <span>241</span></p>
                </div>
                <div class="flex:1">
                  <p class="small text-muted my-1 mx-1">첨부파일 <span>41</span></p>
                </div>
              </div>
              <div class="d-flex pt-1">
                <button type="button" class="btn btn-outline-danger text-dark btn-sm me-1 flex-grow-1">Chat</button>
                <button type="button" class="btn btn-danger btn-sm flex-grow-1">Logout</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue';
const emit = defineEmits(['pass', 'pass2']);
const emitEvent = () => {
  emit('pass1');
};
</script>

<style scoped>
#sidebar-wrapper {
  position: relative;
}

.member-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgb(145, 145, 145);
}

.plus-icon {
  font-size: 18px;
  cursor: pointer;
  pointer-events: auto;
}
</style>
