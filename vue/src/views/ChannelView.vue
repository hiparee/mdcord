<template>
  <div>
    <div>
      서버선택
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
              aria-labelledby="navbarDarkDropdownMenuLink"
              class="dropdown-menu dropdown-menu-dark"
          >
            <li>
              <a class="dropdown-item" href="#" @click="emitEvent()"
              >서버추가</a
              >
            </li>
            <li>
              <hr class="dropdown-divider bg-light"/>
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
          <a :class="{'border-active': active ==='채널목록'}" aria-current="page" class="nav-link custom-tabBarItem"
             style="color: #c5c5c5"
             @click="active = '채널목록'">채널목록</a>
        </li>
        <li class="nav-item">
          <a :class="{'border-active': active ==='권한'}" class="nav-link custom-tabBarItem" style="color: #c5c5c5"
             @click="active = '권한'">권한설정</a>
        </li>
      </ul>
    </div>
    <div>
      <!--    // 채널목록-->

      <div v-if="active === '채널목록'" class="list-group list-group-flush">
        <ul id="sidebar" class="list-unstyled ps-0">
          <draggable class="dragArea list-group w-full">
            <template v-for="channel in store.getChannelList" :key="channel.id">
<!--            <li-->
<!--                v-for="channel in store.getChannelList"-->
<!--                :key="channel.id"-->
<!--                class="mb-2"-->
<!--            >-->
              <li
                  class="mb-2"
                  v-if="channel.parentId == store.accessedChannelInfo.serverId"
              >
              <button
                  :data-bs-target="`#channel${channel.id}`"
                  aria-expanded="true"
                  class="btn btn-toggle align-items-center rounded"
                  data-bs-toggle="collapse"
                  style="color: #ffffff"
              >
                {{ channel.name }}
              </button>
              <i :class="{'bi-gear-fill': channel.id !== channelId}"
                 class="bi float-end plus-icon"
                 @click="menuEdit(channel.id)"></i>
              <i :class="{'bi-x':channel.id === channelId}"
                 class="bi float-end plus-icon close-button"
                 @click="closeEdit()"
              ></i>
              <i :class="{'bi-check':channel.id === channelId}"
                 class="bi float-end plus-icon check-button"
                 @click="saveChannelList(channel)"
              ></i>
              <div :id="`channel${channel.id}`" class="collapse show">
                <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                  <draggable class="dragArea list-group w-full">

                    <li v-for="sub in channel.subChannel" :key="sub.id">
                      <!-- <router-link :to="`/channels/${sub.id}`" class="rounded"> -->
                      <!--                  <a v-if="sub.id !== listId" :class="{icon: channelId === sub.parentId}"-->
                      <div v-if="sub.useYn === 'Y'">
                        <a v-if="!deleteChannel.includes(sub.id)"
                           :class="{icon: channelId === sub.parentId}"
                           class="rounded"
                           href="javascript:;"
                           style="color:#c5c5c5"
                        >
                          <span v-if="sub.id !== listId">{{ sub.name }}</span>
                          <input v-if="editing && sub.id === listId" v-model="newName" class="edit-input"/>
                        </a>

                        <!--                  <input v-if="editing && sub.id === listId" v-model="newName" />-->
                        <!--                  채널이름 수정 및 삭제 -->
                        <i v-if="!deleteChannel.includes(sub.id)"
                           :class="{'bi-pencil':channel.id === channelId}"
                           class="bi plus-icon-sm edit-listName"
                           @click="editName(sub)"
                        ></i>
                        <i v-if="!deleteChannel.includes(sub.id)"
                           :class="{'bi-trash':channel.id === channelId}"
                           class="bi plus-icon-sm remove-list"
                           @click="removeChannel(sub)"
                        ></i>
                      </div>
                    </li>
                  </draggable>
                </ul>
              </div>
            </li>
              </template>
            <hr class="my-3"/>

            <!-- 고정메뉴 -->
            <li class="mb-2">
              <button
                  aria-expanded="true"
                  class="btn btn-toggle align-items-center rounded"
                  data-bs-target="#channel4"
                  data-bs-toggle="collapse"
                  style="color: #ffffff"
              >
                바로가기
              </button>
              <i :class="{'bi-gear-fill': channelId !== '실행'}"
                 class="bi float-end plus-icon"
                 @click="menuEdit('실행')"></i>
              <i :class="{'bi-x': channelId === '실행'}"
                 class="bi float-end plus-icon close-button"
                 @click="channelId = null"
              ></i>
              <i :class="{'bi-check': channelId === '실행'}"
                 class="bi float-end plus-icon check-button"
                 @click="saveChannelList(sub.id)"
              ></i>
              <div id="channel4" class="collapse show">
                <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                  <li>
                    <a
                        :class="{icon:channelId === '실행'}"
                        class="rounded"
                        href="https://office.hiworks.com/lemonhc.com"
                        target="_blank"
                    >Hiworks</a
                    >
                  </li>
                  <li>
                    <a
                        :class="{icon:channelId === '실행'}"
                        class="rounded"
                        href="https://auth.ncloud.com/nsa/cost"
                        target="_blank"
                    >Naver Cloud</a
                    >
                  </li>
                  <li>
                    <a
                        :class="{icon:channelId === '실행'}"
                        class="rounded"
                        href="https://jira.lemonhc.com/secure/Dashboard.jspa"
                        target="_blank"
                    >Jira</a
                    >
                  </li>
                  <li>
                    <a
                        :class="{icon:channelId === '실행'}"
                        class="rounded"
                        href="https://docs.lemonhc.com/login.action?os_destination=%2Findex.action&permissionViolation=true#all-updates"
                        target="_blank"
                    >Confluence</a
                    >
                  </li>
                  <li>
                    <a
                        :class="{icon:channelId === '실행'}"
                        class="rounded"
                        href="https://m.search.naver.com/search.naver?sm=mtb_hty.top&where=m&oquery=%EB%A3%B0%EB%A0%9B&tqi=h33g5sp0iqdssfp0eEossssst1s-130140&query=%EB%A3%B0%EB%A0%9B"
                        target="_blank"
                    >룰렛돌리기</a
                    >
                  </li>
                </ul>
              </div>
            </li>
            <hr class="my-3"/>
          </draggable>

        </ul>

      </div>
      <!--    <ul-->

    </div>
    <!--    권한설정-->
    <div v-if="active === '권한'">
      권한설정
      <button>버튼클릭</button>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {useChannelStore} from "@/store/modules/channel";
import {fetchEditChannelName} from "@/api/channel";

const list = ref([
  {name: 'John', id: 1},
  {name: 'Joao', id: 2},
  {name: 'Jean', id: 3},
  {name: 'Gerard', id: 4},
])
const store = useChannelStore();

const serverId = ref("");
const serverName = ref("")
const channelId = ref(null)
const active = ref('채널목록')
const channelName = ref('');
const channelStatus = ref();
const editing = ref(false)

// 수정할 채널 이름
const newName = ref('')
// 수정할 채널 id 값
const listId = ref('')
const channelOrder = ref(1)
const useYn = ref([])
const deleteChannel = ref([])
const serverChange = serverId => {
  // router.push('/channels');
  store.SET_ACCESSED_CHANNEL_INFO('serverId', serverId);
};
const editName = (sub) => {
  console.log("선택", sub)
  console.log("수정할 채널이름 id값 --->", sub.id)
  console.log("수정할 채널이름 값 --->", sub.name)
  console.log("수정할 채널목록 순서 구분값  ---->", sub.channelOrder)
  editing.value = true;
  newName.value = sub.name;
  listId.value = sub.id;

}

// 서버를 선택후 해당 서버의 값을 저장
const selectServerName = (id, name) => {
  serverId.value = id
  serverName.value = name
}


// 톱니바퀴 버튼을 눌러 메뉴를 수정할때 발생하는 이벤트 처리
const menuEdit = (data) => {
  if(channelId.value === data) {
    channelId.value = null

  } else {
    channelId.value = data

  }
  channelName.value = null
  editing.value = false
  newName.value = ''
  listId.value = '';
  deleteChannel.value = []
}

// 변경한 채널리스트 목록 저장
const saveChannelList = async(data) => {
  console.log(data)
  try {
    console.log(channelId.value !== null)
    console.log((deleteChannel.value.length > 0 || newName.value.length > 0) === true)
    console.log(channelId.value !== null && (deleteChannel.value.length > 0 || newName.value.length > 0))
    if(channelId.value !== null && (deleteChannel.value.length > 0 || newName.value.length > 0)) {
      console.log(newName.value, "변경할 채널에 이름 ")

      for(const element of deleteChannel.value) {
        for(const useElement of useYn.value) {
          const params = {
            id: element,
            useYn: useElement,
            channelName: channelName.value,
            channelOrder: channelOrder.value,
          }
          await fetchEditChannelName(params)
          // const res = await fetchDeleteChannel(element)
        }
      }
      if(newName.value.length > 0) {
        const params = {
          id: listId.value,
          useYn: 'Y',
          channelName: newName.value,
          channelOrder: channelOrder.value,
        }
        await fetchEditChannelName(params)
      }
      console.log("삭제할 채널에 갯수", deleteChannel.value.length)
      console.log("삭제할 채널에 id값", deleteChannel.value)
    }
    await store.SET_CHANNEL_LIST()
    channelId.value = null
    newName.value = '';
    editing.value = false;
    listId.value = null
    deleteChannel.value = []
    useYn.value = []
  } catch(error) {
    console.log(error)
  }
}


const closeEdit = () => {
  channelId.value = null
  channelName.value = null
  editing.value = false
  newName.value = ''
  listId.value = '';
  deleteChannel.value = []
}

// 채널삭제
const removeChannel = (sub) => {
  useYn.value.push('N')
  console.log(sub)
  channelName.value = sub.name
  deleteChannel.value.push(sub.id)

}
/**
 *        "id": 5,     고유 id
 *       "name": "Home",  채널 이름
 *       "dept": 1,       ex) 1 채널타이틀이름 2 채널이름
 *       "parentId": 9,    채널 id  ex) home,정부과제,전자동의서
 *       "channelOrder": 1,
 *       "useYn": "Y"
 * **/

</script>

<style scoped>
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
  border-bottom: 2px solid
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
  color: white
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


</style>

