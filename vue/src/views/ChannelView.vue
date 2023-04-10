<template>
  <div>
    <!--    서버선택-->
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
            {{ selectServerInfo.name || '서버를 선택해주세요' }}
          </a>
          <ul
            aria-labelledby="navbarDarkDropdownMenuLink"
            class="dropdown-menu dropdown-menu-dark"
          >
            <li>
              <a
                class="dropdown-item"
                @click.prevent="showEditServerListModal = true"
                >설정</a
              >
            </li>
            <li>
              <hr class="dropdown-divider bg-light" />
            </li>
            <li
              v-for="server in serverList"
              :key="server.id"
              style="cursor: pointer"
            >
              <!-- 서버명 -->
              <span
                v-if="server.useYn === 'Y'"
                class="dropdown-item"
                @click="selectServer(server)"
              >
                <span class="text-light"> {{ server.name }}</span>
              </span>
            </li>
          </ul>
        </li>
      </ul>
    </div>
    <!--    서버끝-->
    <div v-if="selectServerInfo.name">
      <!--    탭 -->
      <div>
        <ul class="nav custom-tabbar">
          <li class="nav-item">
            <a
              aria-current="page"
              class="nav-link custom-tabBarItem cursor-pointer border-active"
              style="color: #c5c5c5"
              >채널목록</a
            >
          </li>
          <!--        <li class="nav-item">-->
          <!--          <a-->
          <!--            :class="{ 'border-active': active === '권한' }"-->
          <!--            class="nav-link custom-tabBarItem"-->
          <!--            style="color: #c5c5c5"-->
          <!--            @click="active = '권한'"-->
          <!--            >권한설정</a-->
          <!--          >-->
          <!--        </li>-->
        </ul>
      </div>
      <!--    탭 끝 -->
      <!--    상위 채널목록 -->
      <div style="display: inline-flex; width: 100%; overflow-x: hidden">
        <!-- 상위 채널이 존재할때    -->
        <div
          v-if="getSelectHighChannel.length !== 0"
          class="list-group list-group-flush channel-list"
          style="min-width: 200px; max-width: 200px; min-height: 500px"
        >
          <ul id="sidebar" class="list-unstyled ps-0" style="flex: 1">
            <div style="margin-bottom: 5px">활성카테고리</div>
            <template
              v-for="(channel, index) in getSelectHighChannel"
              :key="index"
            >
              <li
                v-if="
                  channel.parentId === selectServerInfo.id &&
                  channel.useYn === 'Y'
                "
                :class="{
                  'custom-button':
                    channel.id === channelNumber && channelNumber !== null,
                }"
                class="mb-2 nav-item btn border-0"
                style="display: flex; padding-top: 9px"
                @click="channelListNumber(channel)"
                @mouseleave="hoverList($event, channel)"
                @mouseover="hoverList($event, channel)"
                @contextmenu.prevent.stop="handleClick($event, channel)"
              >
                <div
                  :data-bs-target="`#channel${channel.id}`"
                  style="color: #cfcfcf; text-align: left; width: 125px"
                >
                  <span
                    v-if="channel.id !== editChannelId"
                    style="
                      color: #f2f3f5;
                      margin-right: 5px;
                      display: inline-block;
                      max-width: 140px;
                      overflow: hidden;
                      text-overflow: ellipsis;
                      white-space: nowrap;
                    "
                  >
                    {{ channel.name }}
                  </span>
                  <input
                    v-if="
                      channel.id === editChannelId &&
                      channel.id === channelNumber
                    "
                    :value="editChannelName"
                    maxlength="10"
                    style="
                      max-width: 165px;
                      color: #cfcfcf;
                      background-color: #1e1f22;
                      border-color: aliceblue;
                      margin-right: 5px;
                    "
                    @input="editChannelNameInput($event)"
                  />
                </div>

                <div style="bottom: -2.1px; position: relative">
                  <i
                    v-if="
                      editChannelId !== channel.id &&
                      channel.id === channelNumber
                    "
                    :class="{
                      [hoverClass]: isHovered && channel.id === hoverListId,
                    }"
                    style="
                      font-size: x-small;
                      margin-left: 15px;
                      display: inline-flex;
                    "
                    @click="editChannelNameList(channel)"
                  ></i>
                  <i
                    v-if="
                      editChannelId === channel.id &&
                      channel.id === channelNumber
                    "
                    class="bi-check"
                    @click="changeChannelName(channel)"
                  ></i>
                  <i
                    v-if="
                      channel.id === editChannelId &&
                      channel.id === channelNumber
                    "
                    class="bi-x"
                    style="font-size: large; position: relative"
                    @click="closeEditInput()"
                  ></i>
                </div>
              </li>
            </template>
            <hr />
            <div style="margin-bottom: 5px">비활성카테고리</div>
            <template
              v-for="(channel, index) in getSelectHighChannel"
              :key="index"
            >
              <li
                v-if="
                  channel.parentId === selectServerInfo.id &&
                  channel.useYn === 'N'
                "
                :class="{
                  'custom-button':
                    channel.id === channelNumber && channelNumber !== null,
                }"
                class="mb-2 nav-item btn border-0"
                style="display: flex; padding-top: 9px"
                @click="channelListNumber(channel)"
                @mouseleave="hoverList($event, channel)"
                @mouseover="hoverList($event, channel)"
                @contextmenu.prevent.stop="handleClick($event, channel)"
              >
                <div
                  :data-bs-target="`#channel${channel.id}`"
                  style="color: #cfcfcf; text-align: left; width: 125px"
                >
                  <span
                    v-if="channel.id !== editChannelId"
                    style="
                      color: #f2f3f5;
                      margin-right: 5px;
                      display: inline-block;
                      max-width: 140px;
                      overflow: hidden;
                      text-overflow: ellipsis;
                      white-space: nowrap;
                    "
                  >
                    {{ channel.name }}
                  </span>
                  <input
                    v-if="
                      channel.id === editChannelId &&
                      channel.id === channelNumber
                    "
                    :value="editChannelName"
                    maxlength="10"
                    style="
                      max-width: 165px;
                      color: #cfcfcf;
                      background-color: #1e1f22;
                      border-color: aliceblue;
                      margin-right: 5px;
                    "
                    @input="editChannelNameInput($event)"
                  />
                </div>

                <div style="bottom: -2.1px; position: relative">
                  <i
                    v-if="
                      editChannelId !== channel.id &&
                      channel.id === channelNumber
                    "
                    :class="{
                      [hoverClass]: isHovered && channel.id === hoverListId,
                    }"
                    style="
                      font-size: x-small;
                      margin-left: 15px;
                      display: inline-flex;
                    "
                    @click="editChannelNameList(channel)"
                  ></i>
                  <i
                    v-if="
                      editChannelId === channel.id &&
                      channel.id === channelNumber
                    "
                    class="bi-check"
                    @click="changeChannelName(channel)"
                  ></i>
                  <i
                    v-if="
                      channel.id === editChannelId &&
                      channel.id === channelNumber
                    "
                    class="bi-x"
                    style="font-size: large; position: relative"
                    @click="closeEditInput()"
                  ></i>
                </div>
              </li>
            </template>
          </ul>
          <button
            class="btn border-0"
            style="color: #f2f3f5; width: 100%; background-color: #5764f0"
            @click="openModal('catagory')"
          >
            카테고리 추가
          </button>
          <VueContextMenu
            v-if="selectUseYn.useYn === 'Y'"
            ref="vueSimpleContextMenuY"
            :options="menuOptionsN"
            element-id="myFirstMenuY"
            @option-clicked="optionClicked"
          ></VueContextMenu>
          <VueContextMenu
            v-else
            ref="vueSimpleContextMenuN"
            :options="menuOptionsY"
            element-id="myFirstMenuN"
            @option-clicked="optionClicked"
          ></VueContextMenu>
        </div>
        <!--  채널이 존재하지 않을때    -->
        <div v-else style="min-width: 1000px; margin-top: 50px">
          <div class="container py-5 h-100">
            <div
              class="row d-flex justify-content-center align-items-center h-100"
            >
              <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card bg-dark text-white border-0 text-center">
                  <div class="mt-3">
                    <img src="@/assets/images/chat.svg" />
                  </div>
                  <div class="mt-4">
                    <p class="h4 mb-3">해당 서버에 채널이 존재하지 않아요</p>
                    난처한 상황에 처하셨군요. 서버에 채널을 추가해보세요.
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!--    <ul-->
        <!--      채널목록-->
        <div v-if="highChannelStatus === 'Y'" style="width: 100%">
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
                  v-for="(element, index) in getSelectLowChannel"
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
                    class="custom-channel-list"
                    @mouseleave="hoverList($event, element)"
                    @mouseover="hoverList($event, element)"
                  >
                    <div
                      :class="{
                        'list-clicked': listClickd && index === listId,
                      }"
                      class="high-channels"
                      style="flex: 1 0 0; color: rgb(207, 207, 207)"
                    >
                      <span
                        v-if="element.id !== editChannelId"
                        style="flex: 1 0; color: #f2f3f5; margin-right: 5px"
                      >
                        {{ element.name }}
                      </span>
                      <input
                        v-else
                        :value="editChannelName"
                        maxlength="10"
                        style="
                          color: #cfcfcf;
                          background-color: #1e1f22;
                          border-color: aliceblue;
                          margin-right: 5px;
                        "
                        @input="editChannelNameInput($event)"
                      />
                      <i
                        v-if="element.id !== editChannelId"
                        :class="{
                          [hoverClass]: isHovered && element.id === hoverListId,
                        }"
                        style="font-size: x-small; display: inline-flex"
                        @click="editChannelNameList(element)"
                      ></i>
                      <i
                        v-else
                        class="bi-check"
                        @click="changeChannelName(element)"
                      ></i>
                      <i
                        v-if="element.id === editChannelId"
                        class="bi-x"
                        style="font-size: large; position: relative"
                        @click="closeEditInput()"
                      ></i>
                    </div>
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
              <div
                class="list-item"
                style="
                  margin-bottom: 8px;
                  background-color: #2b2d31;
                  max-width: 600px;
                  text-align: center;
                "
              >
                <div
                  v-if="channelNumber"
                  class="high-channels"
                  style="padding: 10px"
                  @click="openModal('channel')"
                >
                  <i class="bi-plus-lg" style="flex: 1 0; color: #f2f3f5"></i>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div
          v-else-if="highChannelStatus === ''"
          style="min-width: 1000px; margin-top: 50px"
        >
          <div class="container py-5 h-100">
            <div
              class="row d-flex justify-content-center align-items-center h-100"
            >
              <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card bg-dark text-white border-0 text-center">
                  <div class="mt-3">
                    <img src="@/assets/images/chat.svg" />
                  </div>
                  <div class="mt-4">
                    <p class="h4 mb-3">선택한 채널이 없습니다</p>
                    채널을 선택 해보세요
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else style="min-width: 1000px; margin-top: 50px">
          <div class="container py-5 h-100">
            <div
              class="row d-flex justify-content-center align-items-center h-100"
            >
              <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                <div class="card bg-dark text-white border-0 text-center">
                  <div class="mt-3">
                    <img src="@/assets/images/chat.svg" />
                  </div>
                  <div class="mt-4">
                    <p class="h4 mb-3">비활성화된 카테고리</p>
                    난처한 상황에 처하셨군요. 카테고리를 활성화 해보세요
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <edit-server-list
    :showEditServerListModal="showEditServerListModal"
    @update:showEditServerListModal="showEditServerListModal = false"
  />
  <add-list-modal
    :channelId="channelNumber"
    :modalType="modalType"
    :open="modalType !== ''"
    :serverId="selectServerInfo.id"
    @selectNewChannel="channelListNumber(getSelectHighChannelInfo, 'N')"
    @update:showAddModal="modalType = ''"
  />
</template>

<script setup>
import { useChannelStore } from '@/store/modules/channel';
import { computed, getCurrentInstance, ref } from 'vue';
import { fetchEditChannelName } from '@/api/channel';
import VueContextMenu from 'vue-simple-context-menu';
import EditServerList from '@/components/modals/EditServerListModal.vue';
import AddListModal from '@/components/modals/AddListModal.vue';
import { channel } from '@/api';

const vm = getCurrentInstance();
const store = useChannelStore();
const serverList = computed(() => {
  return [...store.getServerList];
});
const channelList = computed(() => {
  return [...store.getChannelList];
});
// 선택한 서버정보
const selectServerInfo = ref({});
const selectChannelInfo = ref([]);

// 선택한 서버의 상위채널목록
const getSelectHighChannel = computed(() =>
  channelList.value.filter(i => i.parentId === selectServerInfo.value.id),
);
//선택한 상위채널의 하위채널 목록
const getSelectLowChannel = computed(() =>
  getSelectHighChannel.value
    .filter(channel => channel.id === channelNumber.value)
    .map(channel => channel.subChannel)
    .flat(),
);
// 선택한 상위채널 정보
const getSelectHighChannelInfo = computed(() =>
  getSelectHighChannel.value.filter(b => b.id === channelNumber.value),
);

// 선택한 서버 값 가져오기
const selectServer = server => {
  selectChannelInfo.value = [];
  channelNumber.value = null;
  selectServerInfo.value = server;
};

const selectUseYn = ref({});
const showEditServerListModal = ref(false);
const active = ref('채널목록');
const inputChecked = ref([]);
const listClickd = ref(false);
const listId = ref(null);
const channelNumber = ref(null);
const modalType = ref('');
const hoverClass = ref('bi-pencil-fill');
const isHovered = ref(false);
const hoverListId = ref(null);
const editChannelId = ref(null);
const editChannelName = ref('');
const highChannelStatus = ref('');
const hoverList = (event, item) => {
  hoverListId.value = item.id;
  if (event.type === 'mouseover') {
    selectUseYn.value = item;
    isHovered.value = true;
  } else {
    isHovered.value = false;
  }
};
// 사이드바
const editChannelNameList = channel => {
  editChannelId.value = channel.id;
  editChannelName.value = channel.name;
};
const editChannelNameInput = event => {
  editChannelName.value = event.target.value;
};

const changeChannelName = async channel => {
  if (editChannelName.value !== '') {
    const params = {
      id: channel.id,
      useYn: channel.useYn,
      channelName: editChannelName.value,
      channelOrder: channel.channelOrder,
    };
    try {
      await fetchEditChannelName(params);
      await store.SET_CHANNEL_LIST();
    } catch (e) {
      console.log(e);
    } finally {
      editChannelName.value = '';
      editChannelId.value = null;
    }
  } else {
    editChannelName.value = '';
    editChannelId.value = null;
  }
};
const closeEditInput = () => {
  editChannelId.value = null;
  editChannelName.value = '';
};

// 활성화된 채널 구분
const channelListNumber = (channel, type) => {
  highChannelStatus.value = channel.useYn;
  if (channel.id !== editChannelId.value) {
    if (channel.id) {
      inputChecked.value = [];
      channelNumber.value = channel.id;
      inputChecked.value = getSelectLowChannel.value
        .filter(i => i.useYn === 'Y')
        .map(i => i.id);
      closeEditInput();
    } else if (type === 'N') {
      inputChecked.value = [];
      inputChecked.value = getSelectLowChannel.value
        .filter(i => i.useYn === 'Y')
        .map(i => i.id);
    } else {
      channelNumber.value = channel;
      inputChecked.value = [];
    }
  }
};
// 채널 활성/비활성 로직
const changeChannelStatus = async channel => {
  if (inputChecked.value.includes(channel.id)) {
    inputChecked.value = inputChecked.value.filter(i => i !== channel.id);
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

// 컨텍스트 메뉴
const menuOptionsY = ref([
  // {
  //   name: '채널이름 수정',
  //   slug: 'edit-channel-name',
  //   class: 'custom-context-name',
  // },
  // { type: 'divider' },
  {
    name: '채널 활성화',
    slug: 'enable-channel',
    class: 'custom-context-name',
  },
]);
const menuOptionsN = ref([
  // {
  //   name: '채널이름 수정',
  //   slug: 'edit-channel-name',
  //   class: 'custom-context-name',
  // },
  // { type: 'divider' },
  {
    name: '채널 비활성화',
    slug: 'disable-channel',
    class: 'custom-context-disable',
  },
]);
const handleClick = (event, item) => {
  if (item.useYn === 'Y') {
    vm.refs.vueSimpleContextMenuY.showMenu(event, item);
  } else {
    vm.refs.vueSimpleContextMenuN.showMenu(event, item);
  }
};
const optionClicked = async event => {
  const channelInfo = event.item;
  const channelOption = event.option;

  console.log('channelInfo', channelInfo);
  console.log('channelOption', channelOption);
  console.log('event1', event);
  console.log('event2', event);
  const params = {
    id: channelInfo.id,
    useYn: channelInfo.useYn,
    channelName: channelInfo.name,
    channelOrder: channelInfo.channelOrder,
  };
  try {
    if (channelOption.slug === 'enable-channel') {
      params.useYn = 'Y';

      await fetchEditChannelName(params);
    } else if (channelOption.slug === 'disable-channel') {
      params.useYn = 'N';
      await fetchEditChannelName(params);
    }
  } catch (e) {
    console.log(e);
  } finally {
    await store.SET_CHANNEL_LIST();
  }
};

const changeBackgroundColor = event => {
  listId.value = event.oldIndex;
  listClickd.value = true;
};
const revertBackgroundColor = event => {
  listId.value = null;
  listClickd.value = false;
};

const openModal = type => {
  modalType.value = type;
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
  cursor: pointer;
  //pointer-events: none;
  opacity: 1 !important;
  border-radius: 3px;
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

.custom-channel-list {
  display: flex;
  padding: 10px;
}

.custom-channel-list:hover {
  background-color: #393c41;
  color: white;
  border-radius: 3px;
}

.bi-plus-lg:hover {
  color: gray;
}

.bi-pencil-fill:hover {
  color: gray;
}

.bi-x:hover {
  color: red;
}

.bi-check-lg:hover {
  color: green;
}
</style>
