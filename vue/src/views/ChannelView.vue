<template>
  <div>
    <div>
      서버선택
      <ul class="navbar-nav">
        <li class="nav-item dropdown">
          <a
            id="navbarDarkDropdownMenuLink"
            aria-expanded="false"
            class="nav-link dropdown-toggle"
            data-bs-toggle="dropdown"
            href="#"
            role="button"
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
            aria-labelledby="navbarDarkDropdownMenuLink"
            class="dropdown-menu dropdown-menu-dark"
          >
            <li>
              <a class="dropdown-item" href="#" @click="emitEvent()"
                >서버추가</a
              >
            </li>
            <li>
              <hr class="dropdown-divider bg-light" />
            </li>
            <!--            <li><a class="dropdown-item" href="#">{{  }}</a></li>-->
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
    <div>
      <ul class="nav custom-tabbar">
        <li class="nav-item">
          <a
            :class="{ 'border-active': active === '채널목록' }"
            aria-current="page"
            class="nav-link custom-tabBarItem"
            style="color: #c5c5c5"
            @click="active = '채널목록'"
            >채널목록</a
          >
        </li>
        <li class="nav-item">
          <a
            :class="{ 'border-active': active === '권한' }"
            class="nav-link custom-tabBarItem"
            style="color: #c5c5c5"
            @click="active = '권한'"
            >권한설정</a
          >
        </li>
      </ul>
    </div>

    <!--    채널목록-->
    <div style="display: inline-flex; width: 100%">
      <div
        v-if="active === '채널목록'"
        class="list-group list-group-flush"
        style="width: 50%"
      >
        <ul id="sidebar" class="list-unstyled ps-0">
          <template v-for="channel in store.getChannelList" :key="channel.id">
            <li
              v-if="channel.parentId == store.accessedChannelInfo.serverId"
              class="mb-2"
            >
              <button
                :class="{ 'custom-button': channel.id === channelNumber }"
                :data-bs-target="`#channel${channel.id}`"
                aria-expanded="true"
                class="btn align-items-center rounded"
                data-bs-toggle="collapse"
                style="color: #ffffff"
                @click="channelListNumber(channel.id)"
              >
                {{ channel.name }}
              </button>
            </li>
          </template>
        </ul>
      </div>
      <!--    <ul-->
      <!--      채널목록-->
      <div style="width: 100%">
        <div>
          <div>
            <vue-draggable
              :list="channelList"
              class="draggable-list"
              group="my-group"
            >
              <div
                v-for="element in channelList"
                :key="element.name"
                class="list-item"
                style="margin-bottom: 8px; background-color: #2b2d31"
              >
                <div style="display: flex; padding: 10px">
                  <span> {{ element.name }} </span>
                  <div class="custom-checkbox bi-check">
                    <input
                      style="
                        position: absolute;
                        z-index: 5;
                        width: 65%;
                        height: 100%;
                        opacity: 0;
                      "
                      type="checkbox"
                    />
                    <label class="custom-checkbox-label"></label>
                  </div>
                </div>
              </div>
            </vue-draggable>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!--    권한설정-->
  <div v-if="active === '권한'">
    권한설정
    <button>버튼클릭</button>
  </div>
</template>

<script setup>
import { useChannelStore } from '@/store/modules/channel';
import { computed, onBeforeMount, ref } from 'vue';

const store = useChannelStore();
const list2 = ref([{ name: 'Drag Me Too!' }, { name: 'dada' }]);
const serverListValue = ref([]);
const channelListValue = ref([]);
const channelList = ref([]);
const channelName = ref([]);
const active = ref('채널목록');
const channelNumber = ref(null);
const channelListNumber = number => {
  channelNumber.value = number;

  channelList.value = channelListValue.value.filter(
    i => i.dept !== 1 && i.parentId === channelNumber.value,
  );
  // console.log(channelListValue)
};

const getchannelListValue = computed(() => {
  return channelListValue.value;
});
onBeforeMount(() => {
  for (const server of store.getServerList) {
    serverListValue.value.push(server);
  }
  for (const channel of store.getChannelList) {
    channelListValue.value.push(channel);
    channelName.value.push(channel.name);
    for (const sub of channel.subChannel) {
      channelListValue.value.push(sub);
    }
    console.log(channelListValue.value);
  }
});
</script>

<style lang="scss" scoped>
#sidebar-wrapper {
  position: relative;
}

/*.bi-pencil{*/

/*}*/
.bi-pencil:hover {
  color: green;
}

.bi-trash:hover {
  color: red;
}

.edit-listName {
  margin-right: 5px;
  display: inline-flex;
}

.remove-list {
  display: inline-flex;
}

.check-button {
  color: green;
}

.close-button {
  color: red;
}

.plus-icon {
  font-size: 22px;
  cursor: pointer;
  pointer-events: auto;
}

.plus-icon-sm {
  font-size: 15px;
  cursor: pointer;
  pointer-events: auto;
}

.custom-tabBarItem {
  padding-bottom: 16px;
  margin-right: 16px;
}

.custom-tabbar {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: horizontal;
  -webkit-box-direction: normal;
  -ms-flex-direction: row;
  flex-direction: row;
  padding-top: 6px;
  margin-top: 0px;
  margin-bottom: 16px;
}

.border-active {
  border-bottom: 2px solid;
}

button.edit-button {
  background-color: black;
  border: none;
  color: #ffffff;
  font-size: 16px;
  font-weight: bold;
  text-decoration: underline;
  cursor: pointer;
}

button.edit-button:hover {
  text-decoration: none;
}

button.edit-button:focus {
  outline: none;
}

button.edit-button:active {
  background-color: rgba(0, 122, 255, 0.1);
  box-shadow: inset 0px 3px 5px rgba(0, 0, 0, 0.2);
}

/*아이콘 떨림 애니메이션*/
.icon {
  position: relative;
  animation-name: shake;
  animation-duration: 1s;
  animation-iteration-count: infinite;
}

/*.icon:focus {*/

/*}*/

@keyframes shake {
  0% {
    transform: translate(0);
  }
  25% {
    transform: translate(-1px, -1px) rotate(-1deg);
  }
  50% {
    transform: translate(1px, 1px) rotate(1deg);
  }
  75% {
    transform: translate(-1px, 1px) rotate(-1deg);
  }
  100% {
    transform: translate(1px, -1px) rotate(1deg);
  }
}

/*수정할 인풋 텍스트 커스텀 */
.edit-input {
  background: transparent;
  border: inset;
  color: white;
}

/*채널리스트 이동시 애니메이션 효과 */
.flip-list-move {
  transition: transform 0.5s;
}

.no-move {
  transition: transform 0s;
}

.ghost {
  opacity: 0.5;
  background: #c8ebfb;
}

.list-group {
  min-height: 20px;
}

.list-group-item {
  cursor: move;
}

.list-group-item i {
  cursor: pointer;
}

.deactivation-list {
  text-decoration: line-through;
}

.custom-list {
}

.custom-button {
  background-color: #5561f4ff;
}

button:hover {
  background-color: #5561f4ff;
  color: white;
}

.custom-checkbox {
  position: relative;
  display: flex;
}

.custom-checkbox-label {
  width: 50px;
  background: #eaeaea;
  box-shadow: 0 0 1px 2px rgba(0, 0, 0, 0.15);
  height: 25px;
  position: relative;
  display: inline-block;
  border-radius: 46px;
  transition: 0.4s;
}

.custom-checkbox:before {
  position: relative;
  left: 22px;
  bottom: -4px;
  color: #fff;
  z-index: 1;
}

.custom-checkbox:after {
  content: '\F62A';
  color: #aaa;
  position: relative;
  right: 23px;
  bottom: -4px;
  display: inline-block;
  font-family: bootstrap-icons !important;
  font-style: normal;
  font-weight: normal !important;
  font-variant: normal;
  text-transform: none;
  line-height: 1;
  vertical-align: -0.125em;
  -webkit-font-smoothing: antialiased;
}

.custom-checkbox label:after {
  top: 0;
  width: 25px;
  height: 25px;
  content: '';
  position: absolute;
  border-radius: 100%;
  left: 0;
  z-index: 2;
  background: #fff;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  transition: 0.4s;
}

.custom-checkbox input:checked + label {
  background: green;
}

.custom-checkbox input:checked + label:after {
  left: 25px;
}
</style>
