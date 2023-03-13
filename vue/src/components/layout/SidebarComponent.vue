<template>
  <!-- Sidebar-->
  <div id="sidebar-wrapper">
    <!-- 상단 server 리스트 영역 -->
    <div class="sidebar-heading">
      <div class="navbar-collapse" id="navbarNavDarkDropdown">
        <ul class="navbar-nav">
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              id="navbarDarkDropdownMenuLink"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              <img
                src="@/assets/images/icon.png"
                style="width: 30px; height: 30px; margin-right: 5px"
              />
              {{
                store.getServerList.find(server => {
                  return store.accessedChannelInfo.serverId == server.id;
                }).name
              }}
            </a>
            <ul
              class="dropdown-menu dropdown-menu-dark"
              aria-labelledby="navbarDarkDropdownMenuLink"
            >
              <li>
                <a class="dropdown-item" href="#" @click="emitEvent()"
                  >서버추가</a
                >
              </li>
              <li><hr class="dropdown-divider bg-light" /></li>
              <li v-for="server in store.getServerList" :key="server.id">
                <!-- 서버명 -->
                <span class="dropdown-item" @click="serverChange(server.id)"
                  ><i class="bi bi-play-fill mr-1"></i> {{ server.name }}</span
                >
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>

    <hr class="m-0" />

    <div class="list-group list-group-flush">
      <ul class="list-unstyled ps-0" id="sidebar">
        <template v-for="channel in store.getChannelList" :key="channel.id">
          <li
            class="mb-2"
            v-if="channel.parentId == store.accessedChannelInfo.serverId"
          >
            <button
              class="btn btn-toggle align-items-center rounded"
              data-bs-toggle="collapse"
              :data-bs-target="`#channel${channel.id}`"
              aria-expanded="true"
            >
              {{ channel.name }}
            </button>

            <i class="bi bi-plus float-end plus-icon"></i>

            <div class="collapse show" :id="`channel${channel.id}`">
              <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                <li v-for="sub in channel.subChannel" :key="sub.id">
                  <!-- <router-link :to="`/channels/${sub.id}`" class="rounded"> -->
                  <router-link
                    :to="{ path: `/channels/${sub.id}` }"
                    class="rounded"
                  >
                    {{ sub.name }}
                  </router-link>
                </li>
              </ul>
            </div>
          </li>
        </template>

        <hr class="my-3" />

        <!-- 고정메뉴 -->
        <li class="mb-2">
          <button
            class="btn btn-toggle align-items-center rounded"
            data-bs-toggle="collapse"
            data-bs-target="#channel4"
            aria-expanded="true"
          >
            바로가기
          </button>

          <div class="collapse show" id="channel4">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <a
                  href="https://office.hiworks.com/lemonhc.com"
                  target="_blank"
                  class="rounded"
                  >Hiworks</a
                >
              </li>
              <li>
                <a
                  href="https://auth.ncloud.com/nsa/cost"
                  target="_blank"
                  class="rounded"
                  >Naver Cloud</a
                >
              </li>
              <li>
                <a
                  href="https://jira.lemonhc.com/secure/Dashboard.jspa"
                  target="_blank"
                  class="rounded"
                  >Jira</a
                >
              </li>
              <li>
                <a
                  href="https://docs.lemonhc.com/login.action?os_destination=%2Findex.action&permissionViolation=true#all-updates"
                  target="_blank"
                  class="rounded"
                  >Confluence</a
                >
              </li>
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
        <hr class="my-3" />
        <li class="mb-2" v-if="pageType === 'A'">
          <button
            class="btn btn-toggle align-items-center rounded collapsed"
            data-bs-toggle="collapse"
            data-bs-target="#channel5"
            aria-expanded="true"
          >
            관리자
          </button>
          <div class="collapse show" id="channel5">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <a
                  href="javascript:;"
                  @click="$router.push('/adm/user')"
                  class="rounded"
                  >사용자 관리</a
                >
              </li>
              <!-- <li>
                <a
                  href="javascript:"
                  class="rounded"
                  @click="$router.push('/adm/create')"
                  >사용자 등록</a
                >
              </li> -->
              <li>
                <a href="javascript:" class="rounded"></a>
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
                  <p class="small text-muted my-1 mx-1">
                    첨부파일 <span>41</span>
                  </p>
                </div>
              </div>
              <div class="d-flex pt-1">
                <button
                  type="button"
                  class="btn btn-outline-danger text-dark btn-sm me-1 flex-grow-1"
                >
                  Chat
                </button>
                <button type="button" class="btn btn-danger btn-sm flex-grow-1">
                  Logout
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useChannelStore } from '@/store/store.js';
import { useRouter } from 'vue-router/dist/vue-router';
const store = useChannelStore();
const router = useRouter();
const serverChange = serverId => {
  router.push('/channels');
  store.SET_ACCESSED_CHANNEL_INFO('serverId', serverId);
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
.router-link-active {
  background-color: #3c3f45;
  color: #ffffff !important;
}
.router-link-active::before {
  color: #ffffff;
}
</style>
