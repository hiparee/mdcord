<template>
  <div class="row align-items-center justify-content-center">
    <div class="col-10 col-md-8">
      <h1 class="m-0 header-text">사용자 목록</h1>
    </div>
    <div class="col-2 col-md-4" style="text-align: right">
      <button
        class="add-user-btn"
        data-bs-toggle="modal"
        data-bs-target="#exampleModal"
      >
        <!--      data-bs-target="#exampleModal"-->
        사용자 추가하기
      </button>
    </div>
  </div>
  <!--  contents  -->
  <div class="contents">
    <div class="people-column d-flex flex-column h-100">
      <!--  사용자 검색  -->
      <search-bar-component
        v-model="keyword"
        @clearKeywordEvent="clearKeywordEvent"
      />
      <div>
        <h2
          class="title-x4dI75"
          style="
            box-sizing: border-box;
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden;
            text-transform: uppercase;
            font-size: 14px;
            line-height: 16px;
            letter-spacing: 0.02em;
            margin-top: 10px;
          "
        >
          모든 사용자 — {{ searchedTotalCount }}명
        </h2>
      </div>
      <!--  사용자 목록  -->
      <div class="overflow-auto">
        <table class="table align-middle">
          <colgroup>
            <col />
            <col />
            <col />
            <col />
            <col />
            <col />
          </colgroup>
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">이름</th>
              <th scope="col">아이디</th>
              <th scope="col">역할</th>
              <th scope="col">등록일</th>
              <th scope="col">상태</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(user, index) in searchedMember" :key="index">
              <td>{{ index + 1 }}</td>
              <td>
                <span class="mr-2"
                  ><img
                    :src="imageUrl(userProfileIcon(user.iconFileId))"
                    alt=""
                    width="33"
                    height="33"
                /></span>
                <span>{{ user.name }}</span>
              </td>
              <td>{{ user.memberId }}</td>
              <td>{{ user.type === 'USER' ? '사용자' : '관리자' }}</td>
              <td>{{ $dayjs(user.createDate).format('YYYY년 MM월 DD일') }}</td>

              <td>{{ user.useYn }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 리스트 형식 -->
      <!--      <div class="people-list" role="list">
        <div
          v-for="(user, index) in searchedMember"
          :key="index"
          class="people-list-item"
          role="listitem"
        >
          <div class="list-item-contents">
            <div class="user-info d-flex flex-row">
              &lt;!&ndash;              <img
                :src="`/src/assets/images/profile/${userProfileIcon(
                  user.iconFileId,
                )}.png`"
                alt=""
                width="33"
                height="33"
              />&ndash;&gt;
              <img
                :src="imageUrl(userProfileIcon(user.iconFileId))"
                alt=""
                width="33"
                height="33"
              />
              <div>{{ user.name }}</div>
            </div>
          </div>
        </div>
      </div>-->
    </div>
  </div>
  <!--  pagination section  -->
  <!--    <nav aria-label="Page navigation example">
      <ul class="pagination">
        <li class="page-item">
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li class="page-item"><a class="page-link" href="#">1</a></li>
        <li class="page-item"><a class="page-link" href="#">2</a></li>
        <li class="page-item"><a class="page-link" href="#">3</a></li>
        <li class="page-item">
          <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>-->

  <!-- Modal -->
  <user-register-modal />
</template>

<script setup>
import { computed, onBeforeMount, onUpdated, ref } from 'vue';
import { fetchMembers } from '@/api/user';
import { userProfileIcon } from '@/utils/common';
import SearchBarComponent from '@/components/common/SearchBarComponent.vue';
import UserRegisterModal from '@/components/UserRegisterModal.vue';

const members = ref([]);
const totalCount = ref(0);

const keyword = ref('');
const openModal = ref(false);

onBeforeMount(async () => {
  const params = {};
  console.log('before mounted :::');
  try {
    const { data } = await fetchMembers();
    console.log('members ::', members.value);
    console.log(data);
    members.value = data.content;
    totalCount.value = data.totalElements;
  } catch (err) {
    console.log('error ::', err);
  }
});

const openPeopleAddModal = () => {
  console.log('사용자 추가 모달 띄울 예정');
  openModal.value = true;
};

// 검색창 비우기
const clearKeywordEvent = () => {
  keyword.value = '';
};

// 검색어에 따른 사용자 필터링
const searchedMember = computed(() => {
  return members.value.filter(member => {
    return (
      member.name.toLowerCase().indexOf(keyword.value.toLowerCase()) !== -1
    );
  });
});
// 모든 사용자 카운트 or 검색어로 필터링된 사용자 카운트
const searchedTotalCount = computed(() => {
  return keyword.value === '' ? totalCount : searchedMember.value.length;
});

onUpdated(() => {
  console.log('update?');
});

// 추후 대리님이 추가하신 공통함수 적용
const imageUrl = user => {
  return new URL(`../assets/images/profile/${user}.png`, import.meta.url).href;
};
</script>

<style scoped>
.add-user-btn {
  background-color: #248046;
  color: white;
  border-radius: 4px;
  margin: 0 8px;
  padding: 2px 8px;
  outline: 0;
  border: 0;
}
.contents {
  height: 100%;
  display: flex;
  flex-direction: row;
  position: relative;
  overflow: hidden;
}
.people-column {
  flex: 1 1 auto;
}
.people-list {
  overflow: hidden scroll;
  padding-right: 0px;
  margin-top: 8px;
  position: relative;
  box-sizing: border-box;
  min-height: 0;
  flex: 1 1 auto;
}
.people-list-item {
  height: 62px;
  display: flex;
  flex-direction: row;
  margin-right: 20px;
  font-weight: 500;
  font-size: 16px;
  line-height: 20px;
  overflow: hidden;
}
.list-item-contents {
  display: flex;
  flex-grow: 1;
  align-items: center;
  justify-content: space-between;
  max-width: 100%;
}

/* 사용자 목록 table styling*/
.table {
  color: #e0e1e5;
}
.table tr:last-child {
  border-bottom: 0;
}
</style>
