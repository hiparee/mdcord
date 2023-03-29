<template>
  <div>
    <div>
      서버선택
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
            <li>
              <a
                class="dropdown-item"
                href="#"
                @click="showEditServerListModal = true"
                >설정</a
              >
            </li>
            <li><hr class="dropdown-divider bg-light" /></li>
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
    <div>
      <ul class="nav custom-tabbar">
        <li class="nav-item">
          <a
            :class="{ 'border-active': active === '채널목록' }"
            aria-current="page"
            class="nav-link custom-tabBarItem cursor-pointer"
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
        style="min-width: 250px; max-width: 200px; min-height: 500px"
        v-if="active === '채널목록' && getChannelListValue"
        class="list-group list-group-flush channel-list"
      >
        <ul id="sidebar" class="list-unstyled ps-0">
          <template
            v-for="(channel, index) in store.getChannelList"
            :key="index"
          >
            <li
              v-if="channel.parentId == store.accessedChannelInfo.serverId"
              class="mb-2 nav-item"
              style="display: grid"
            >
              <button
                :class="{ 'custom-button': channel.id === channelNumber }"
                :data-bs-target="`#channel${channel.id}`"
                class="btn border-0"
                style="color: #cfcfcf; text-align: left"
                @click="channelListNumber(channel)"
                @contextmenu.prevent.stop="handleClick($event, channel)"
              >
                {{ channel.name }}
              </button>
            </li>
          </template>
        </ul>
        <VueContextMenu
          ref="vueSimpleContextMenu"
          :options="menuOptions"
          element-id="myFirstMenu"
          @option-clicked="optionClicked"
        ></VueContextMenu>
      </div>
      <div v-else style="min-width: 250px; max-width: 500px">
        채널을 추가해주세요
      </div>
      <!--    <ul-->
      <!--      채널목록-->
      <div style="width: 100%">
        <div>
          <div>
            <VueDraggable
              :list="channelList"
              class="draggable-list"
              group="my-group"
              @choose="changeBackgroundColor"
              @dragstart="onDragStart"
              @unchoose="revertBackgroundColor"
            >
              <div
                v-for="(element, index) in channelList"
                :key="element.name"
                class="list-item"
                style="
                  margin-bottom: 8px;
                  background-color: #2b2d31;
                  max-width: 600px;
                "
                @mousedown="onDragStart(element)"
                @mouseup="onDragStart(element)"
              >
                <div
                  :class="{
                    'list-clicked': listClickd && index === listId,
                  }"
                  class="high-channels"
                  style="display: flex; padding: 10px"
                >
                  <span style="flex: 1 0; color: #f2f3f5">
                    {{ element.name }}
                  </span>
                  <div class="custom-checkbox bi-check">
                    <input
                      v-model="inputChecked"
                      :value="element.id"
                      style="
                        position: absolute;
                        z-index: 5;
                        width: 65%;
                        height: 100%;
                        opacity: 0;
                        right: 15px;
                      "
                      type="checkbox"
                      @click="changeChannelStatus(element)"
                    />
                    <label class="custom-checkbox-label"></label>
                  </div>
                </div>
              </div>
            </VueDraggable>
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
  <edit-server-list
    :showEditServerListModal="showEditServerListModal"
    :serverList="store.getServerList"
    @update:showEditServerListModal="showEditServerListModal = false"
  />
</template>

<script setup>
import { useChannelStore } from '@/store/modules/channel';
import { computed, getCurrentInstance, onBeforeMount, ref } from 'vue';
import { fetchEditChannelName } from '@/api/channel';
import VueContextMenu from 'vue-simple-context-menu';
import EditServerList from '@/components/modals/EditServerListModal.vue';

const vm = getCurrentInstance();

const store = useChannelStore();
const serverListValue = ref([]);
const channelListValue = ref([]);
const showEditServerListModal = ref(false);
const channelList = ref([]);
const channelName = ref([]);
const active = ref('채널목록');
const inputChecked = ref([]);
const listClickd = ref(false);
const listId = ref(null);
const channelNumber = ref(null);
// 사이드바
const serverChange = serverId => {
  store.SET_ACCESSED_CHANNEL_INFO('serverId', serverId);
  store.SET_ACCESSED_CHANNEL_INFO('channelId', null);
  channelList.value = [];
  channelNumber.value = null;
};
const getServername = computed(() => {
  return store.getServerList.find(server => {
    return store.accessedChannelInfo.serverId == server.id;
  })?.name;
});
const getChannelListValue = computed(() => {
  const data = channelListValue.value.map(item => item.parentId);
  return data.includes(store.getAccessedChannelInfo);
  // return data[0];
});
console.log(getChannelListValue);
console.log('serverId', store.getAccessedChannelInfo);

// 활성화된 채널 구분
const channelListNumber = channel => {
  inputChecked.value = [];
  console.log(channel);
  channelNumber.value = channel.id;
  channelList.value = channelListValue.value.filter(
    i => i.dept !== 1 && i.parentId === channelNumber.value,
  );
  for (const sub of channel.subChannel) {
    if (sub.useYn === 'Y') {
      console.log(sub.id);
      inputChecked.value.push(sub.id);
    }
  }
};
// 채널 활성/비활성 로직
const changeChannelStatus = async channel => {
  if (inputChecked.value.includes(channel.id)) {
    console.log('del');
    inputChecked.value = inputChecked.value.filter(i => i !== channel.id);
    console.log('now', inputChecked.value);
  } else {
    inputChecked.value.push(channel.id);
  }
  /**
   * {
   *   "id": 0,
   *   "useYn": "string",
   *   "channelName": "",
   *   "channelOrder": 1
   * }
   **/
  const params = {
    id: channel.id,
    useYn: inputChecked.value.includes(channel.id) ? 'Y' : 'N',
    channelName: channel.name,
    channelOrder: channel.channelOrder,
  };
  await fetchEditChannelName(params)
    .then(() => {
      store.SET_CHANNEL_LIST();
    })
    .catch(e => {
      console.log(e);
    });
};
// 스토어에서 가져온 서버,채널리스트
onBeforeMount(() => {
  console.log(inputChecked.value);
  for (const server of store.getServerList) {
    serverListValue.value.push(server);
  }
  for (const channel of store.getChannelList) {
    channelListValue.value.push(channel);
    channelName.value.push(channel.name);
    for (const sub of channel.subChannel) {
      channelListValue.value.push(sub);
    }
  }
});

// 컨텍스트 메뉴
const menuOptions = ref([
  {
    name: '채널이름 수정',
    slug: 'edit-channel-name',
    class: 'custom-context-name',
  },
  { type: 'divider' },
  {
    name: '채널 활성화',
    slug: 'enable-channel',
    class: 'custom-context-name',
  },
  {
    name: '채널 비활성화',
    slug: 'disable-channel',
    class: 'custom-context-disable',
  },
]);
const handleClick = (event, item) => {
  console.log('vm', vm.refs);
  console.log(vm.refs.vueSimpleContextMenu.showMenu);
  vm.refs.vueSimpleContextMenu.showMenu(event, item);
};
const optionClicked = event => {
  event.preventDefault();
  console.log(event);
};
const changeBackgroundColor = event => {
  listId.value = event.oldIndex;
  listClickd.value = true;
};
const revertBackgroundColor = event => {
  listId.value = null;
  listClickd.value = false;
};

const onDragStart = event => {};
</script>

<style lang="scss" scoped>
#sidebar-wrapper {
  position: relative;
}
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
  border-bottom: 1px solid;
  border-bottom-color: #3f4046;
}

.border-active {
  color: #ffffff !important;
  border-bottom: 2px solid;
  border-bottom-color: #939bf5;
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
  background-color: #5561f4ff !important;
  color: #ffffff !important;
  cursor: auto;
  opacity: 1 !important;
}

.mb-2:hover {
  background-color: #393c41;
  color: white;
  border-radius: 3px;
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

.hover-class {
  color: #ffffff; /* 텍스트 색상 변경 */
  background-color: #fdfdfd; /* 배경색 변경 */
  cursor: pointer; /* 커서 모양 변경 */
}

.list-clicked {
  background-color: #42434a !important;
}

/* 마우스가 호버될 요소를 선택 */
.high-channels:hover {
  /* hover-class 스타일 적용 */
  color: #fff;
  background-color: #393c41;
  cursor: pointer;
}

.channel-list {
  width: 25%;
  max-width: 15%;
  background-color: #2b2d31;
  margin-right: 5px;
  padding: 10px;
}

.vue-simple-context-menu {
  /* 기본 스타일 */
  background-color: #111214 !important;

  &--active {
    /* 활성화된 스타일 */
  }

  &__item {
    /* 아이템 스타일 */

    &:hover {
      /* 아이템에 마우스 오버 했을 때 스타일 */
    }
  }

  &__divider {
    /* 구분선 스타일 */
  }
}
</style>
