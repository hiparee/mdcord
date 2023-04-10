<template>
  <!-- Sidebar-->
  <div id="sidebar-wrapper">
    <!-- 상단 server 리스트 영역 -->
    <div class="sidebar-heading">
      <div id="navbarNavDarkDropdown" class="navbar-collapse">
        <ul class="navbar-nav">
          <li class="nav-item dropdown">
            <a
              id="navbarDarkDropdownMenuLink"
              aria-expanded="false"
              class="nav-link dropdown-toggle text-warning"
              data-bs-toggle="dropdown"
              href="#"
              role="button"
            >
              <img
                src="@/assets/images/icon.png"
                style="width: 30px; height: 30px; margin-right: 5px"
              />
              {{ getServername }}
            </a>
            <ul
              aria-labelledby="navbarDarkDropdownMenuLink"
              class="dropdown-menu dropdown-menu-dark"
            >
              <!-- <li>
                <a class="dropdown-item" href="#" @click="emitEvent()"
                  >서버추가</a
                >
              </li> -->
              <!-- <li><hr class="dropdown-divider bg-light" /></li> -->
              <li
                v-for="server in store.getServerList"
                :key="server.id"
                style="cursor: pointer"
              >
                <!-- 서버명 -->
                <span
                  v-if="server.useYn === 'Y'"
                  class="dropdown-item"
                  @click="serverChange(server.id)"
                >
                  <span class="text-light"> {{ server.name }}</span>
                </span>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>

    <hr class="m-0" />

    <div class="list-group list-group-flush">
      <ul id="sidebar" class="list-unstyled ps-0">
        <template
          v-for="(channel, index) in store.getChannelList"
          :key="channel.id"
        >
          <li
            v-if="
              channel.parentId == store.accessedChannelInfo.serverId &&
              channel.useYn === 'Y'
            "
            class="mb-2"
          >
            <button
              :data-bs-target="`#channel${channel.id}`"
              aria-expanded="true"
              class="btn btn-toggle align-items-center rounded"
              data-bs-toggle="collapse"
            >
              {{ channel.name }}
            </button>

            <i class="bi bi-plus float-end plus-icon"></i>

            <div :id="`channel${channel.id}`" class="collapse show">
              <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                <li v-for="sub in channel.subChannel" :key="sub.id">
                  <div v-if="sub.useYn === 'Y'">
                    <!-- <router-link :to="`/channels/${sub.id}`" class="rounded"> -->
                    <router-link
                      :to="{ path: `/channels/${sub.id}` }"
                      class="rounded"
                    >
                      {{ sub.name }}
                    </router-link>
                  </div>
                </li>
              </ul>
            </div>
          </li>
          <p v-if="index === 0 && channel.useYn === 'N'">
            활성화된 채널이 없습니다
          </p>
        </template>

        <hr class="my-3" />

        <!-- 고정메뉴 -->
        <li class="mb-2">
          <button
            aria-expanded="true"
            class="btn btn-toggle align-items-center rounded"
            data-bs-target="#channel4"
            data-bs-toggle="collapse"
          >
            바로가기
          </button>

          <div id="channel4" class="collapse show">
            <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
              <li>
                <a
                  class="rounded"
                  href="https://office.hiworks.com/lemonhc.com"
                  target="_blank"
                  >Hiworks</a
                >
              </li>
              <li>
                <a
                  class="rounded"
                  href="https://auth.ncloud.com/nsa/cost"
                  target="_blank"
                  >Naver Cloud</a
                >
              </li>
              <li>
                <a
                  class="rounded"
                  href="https://jira.lemonhc.com/secure/Dashboard.jspa"
                  target="_blank"
                  >Jira</a
                >
              </li>
              <li>
                <a
                  class="rounded"
                  href="https://docs.lemonhc.com/login.action?os_destination=%2Findex.action&permissionViolation=true#all-updates"
                  target="_blank"
                  >Confluence</a
                >
              </li>
              <li>
                <a
                  class="rounded"
                  href="https://m.search.naver.com/search.naver?sm=mtb_hty.top&where=m&oquery=%EB%A3%B0%EB%A0%9B&tqi=h33g5sp0iqdssfp0eEossssst1s-130140&query=%EB%A3%B0%EB%A0%9B"
                  target="_blank"
                  >룰렛돌리기</a
                >
              </li>
            </ul>
          </div>
        </li>
      </ul>
    </div>

    <div class="member-info">
      <div class="card" style="display: block; --bs-card-bg: #232428">
        <div class="card-body">
          <div class="d-flex text-light">
            <div class="flex-shrink-0">
              <img
                :src="
                  getImageUrl(
                    `profile/${userProfileIcon(
                      userStore.userInfo.iconFileId,
                    )}.png`,
                  )
                "
                class="profile-img"
                style="width: 30px; border-radius: 10px"
              />
            </div>
            <div class="flex-grow-1 ms-3 align-self-center">
              <p class="m-0 p-0 text-white">
                {{ userStore.userInfo.name }}
              </p>
            </div>
            <div>
              <button
                class="btn btn-outline-secondary bottom-button"
                type="button"
                @click.stop="clickSignOut()"
              >
                <span> <i class="bi bi-box-arrow-right"></i></span>
              </button>
              <button
                v-if="userStore.userInfo.role === 'ADMIN'"
                class="btn btn-outline-secondary bottom-button"
                type="button"
                @click.stop="router.push('/settings')"
              >
                <span> <i class="bi-gear"></i></span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useChannelStore, useUserStore } from '@/store/store.js';
import { useRouter } from 'vue-router';
import { signOutUser } from '@/api/user';
import { userProfileIcon, getImageUrl } from '@/utils/common.js';

const store = useChannelStore();
const userStore = useUserStore();
const router = useRouter();
const serverChange = serverId => {
  router.push('/channels');
  store.SET_ACCESSED_CHANNEL_INFO('serverId', serverId);
  store.SET_ACCESSED_CHANNEL_INFO('channelId', null);
};

const getServername = computed(() => {
  return store.getServerList.find(server => {
    return store.accessedChannelInfo.serverId == server.id;
  })?.name;
});
const clickSignOut = async () => {
  try {
    await signOutUser();
    userStore.SET_SIGN_OUT();
    await router.replace('/');
  } catch (e) {
    console.log('err ::', e);
  }
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

.bottom-button {
  width: 33px;
  height: 33px;
  font-size: 20px;
  padding: 0;
  border: none;
  --bs-btn-hover-bg: #3a3b42;
  --bs-btn-active-bg: #3a3b42;
}
</style>
