<template>
  <!-- Sidebar-->
  <div id="sidebar-wrapper">
    <h3 class="sidebar-header">설정</h3>
    <!-- 상단 server 리스트 영역 -->
    <div class="navbar-collapse">
      <div class="list-group list-group-flush">
        <div class="navbar-nav" role="list">
          <div
            class=""
            style="font-size: 12px; line-height: 16px; padding: 6px 10px"
          >
            사용자 설정
          </div>
          <div class="nav-item" role="listitem">
            <router-link
              to="/settings/member"
              class="nav-link active"
              aria-current="page"
              replace
            >
              사용자 관리
            </router-link>
          </div>
          <!--          <div class="nav-item" role="listitem">
            <router-link
              to="/settings/adm/user/register"
              class="nav-link"
              replace
            >
              사용자 등록
            </router-link>
          </div>-->
          <div class="separator"></div>
          <div
            class=""
            style="font-size: 12px; line-height: 16px; padding: 6px 10px"
          >
            채널 설정
          </div>
          <div class="nav-item" role="listitem">
            <router-link
              to="/settings/adm/channel"
              class="nav-link active"
              aria-current="page"
              replace
            >
              채널 관리
            </router-link>
          </div>
          <div class="separator"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();
</script>

<style scoped>
#sidebar-wrapper {
  position: relative;
  background-color: #2b2d31;
  z-index: 0;
}
.sidebar-header {
  margin: 16px 0 16px 16px;
  font-weight: 700;
  line-height: 30px;
  color: #f3f4f5 !important;
}
.nav-item {
  box-sizing: border-box;
  display: block;
  padding: 1px 0;
  color: #b8b9bf;
  height: auto;
}
.nav-link {
  padding: 12px 9px;
  height: auto;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  font-size: 16px;
  line-height: 20px;
  font-weight: 500;
  border-radius: 4px;
}
.nav-link:hover {
  background-color: #3c3f45;
  color: #ffffff !important;
}
.router-link-active {
  background-color: #5662f6;
  color: #ffffff !important;
  pointer-events: none;
}
.router-link-active::before {
  color: #ffffff;
}
.separator {
  margin: 8px 10px;
  height: 1px;
  background-color: #43454c;
}
</style>
