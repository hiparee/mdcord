<template>
  <div class="row align-items-center justify-content-center">
    <div class="col-10 col-md-8">
      <h1 class="m-0 settings-header-text">사용자 목록</h1>
    </div>
    <div class="col-2 col-md-4" style="text-align: right">
      <button
        class="add-member-btn"
        data-bs-toggle="modal"
        data-bs-target="#registerModal"
      >
        사용자 계정 만들기
      </button>
    </div>
  </div>
  <!--  contents  -->
  <div class="contents">
    <div class="people-column">
      <!--  사용자 검색바 컴포넌트  -->
      <search-bar-component
        v-model="keyword"
        @clearKeywordEvent="clearKeywordEvent"
      />

      <div>
        <h2
          class="all-user-title"
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
          모든 사용자 — {{ isLoading === true ? 0 : searchedTotalCount }}명
        </h2>
      </div>
      <!--  사용자가 없는 경우  -->
      <template v-if="searchedTotalCount === 0">
        <div class="empty-container">
          <div class="d-flex flex-column">
            <div class="empty-image"></div>
            <div class="empty-message">사용자가 없습니다.</div>
          </div>
        </div>
      </template>
      <!--  사용자가 있는 경우  -->
      <template v-else>
        <div class="overflow-auto position-relative" style="padding-top: 40px">
          <div class="member-table-fixed-header"></div>
          <div
            class="member-table-wrapper table-custom-scrollbar"
            style="
              overflow-x: hidden;
              overflow-y: auto;
              height: 100%;
              padding-right: 5px;
            "
          >
            <table class="table align-middle member-table mb-0">
              <colgroup>
                <col style="width: 7%" />
                <col style="width: 23%" />
                <col style="width: 25%" />
                <col style="width: 10%" />
                <col style="width: 23%" />
                <col style="width: 7%" />
              </colgroup>
              <thead>
                <tr>
                  <th><div class="th-text text-center">#</div></th>
                  <th><div class="th-text text-center">이름</div></th>
                  <th><div class="th-text text-center">아이디</div></th>
                  <th><div class="th-text text-center">역할</div></th>
                  <th><div class="th-text text-center">등록일</div></th>
                  <th><div class="th-text text-center">상태</div></th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="isLoading === true">
                  <td colspan="6" class="text-center">
                    <spinner-component></spinner-component>
                  </td>
                </tr>
                <tr
                  v-else
                  v-for="(user, index) in searchedMember"
                  :key="index"
                  class="member-row"
                >
                  <td>{{ index + 1 }}</td>
                  <td>
                    <div class="img-section d-flex flex-row align-items-center">
                      <span class="mr-2"
                        ><img
                          :src="imageUrl(userProfileIcon(user.iconFileId))"
                          alt=""
                          width="40"
                          height="40"
                      /></span>
                      <span>{{ user.name }}</span>
                    </div>
                  </td>
                  <td>{{ user.memberId }}</td>
                  <td>{{ user.type === 'USER' ? '사용자' : '관리자' }}</td>
                  <td>
                    {{ $dayjs(user.createDate).format('YYYY/MM/DD HH:mm') }}
                  </td>
                  <td>{{ user.useYn === 'Y' ? '활성' : '비활성' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </template>
    </div>
  </div>
  <!-- 사용자 추가 Modal -->
  <user-register-modal @reload-fetch-members="getMembers" />
</template>

<script setup>
import { computed, onBeforeMount, ref } from 'vue';
import { fetchMembers } from '@/api/user';
import { userProfileIcon } from '@/utils/common';
import SearchBarComponent from '@/components/common/SearchBarComponent.vue';
import UserRegisterModal from '@/components/UserRegisterModal.vue';
import SpinnerComponent from '@/components/common/SpinnerComponent.vue';
import SkeletonComponent from '@/components/SkeletonComponent.vue';

const members = ref([]);
const totalCount = ref(0);

const keyword = ref('');

const isLoading = ref(false);

onBeforeMount(async () => {
  await getMembers();
});

// 사용자 목록 조회
const getMembers = async () => {
  isLoading.value = true;
  const params = {};
  try {
    const { data } = await fetchMembers();
    members.value = data.content;
    totalCount.value = data.totalElements;
  } catch (err) {
    console.log('error ::', err);
  } finally {
    isLoading.value = false;
  }
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

// 추후 대리님이 추가하신 공통함수 적용
const imageUrl = user => {
  return new URL(`../assets/images/profile/${user}.png`, import.meta.url).href;
};
</script>

<style scoped>
.add-member-btn {
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
  display: flex;
  flex-direction: column;
  flex: 1 1 auto;
  height: 100%;
}

/* 사용자 목록 table styling*/
.member-table-fixed-header {
  border-top: 1px solid #3b3f2c;
  border-bottom: 1px solid #3b3f2c;
  text-align: center;
  color: #666;
  height: 40px;
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
}
table.member-table {
  width: 100%;
  border-collapse: collapse;
  --bs-table-border-color: #3f4147;
  color: #e0e1e5;
}
table.member-table th {
  padding: 0;
}
table.member-table th:first-child .th-text {
  border-left: none;
}
table.member-table .th-text {
  position: absolute;
  top: 0;
  width: inherit;
  line-height: 40px; /* header-bg height값 */
  border-color: #3b3f2c #c4c4c4;
  font-size: 13px;
  font-weight: 700;
  padding: 0 0.5rem;
}
table.member-table tbody tr:last-child td {
  /*border-bottom: 1px solid #ffffff;*/
}
table.member-table .member-row:hover {
  background-color: #3a3c42;
  cursor: pointer;
}
/* 스크롤바 styling */
.table-custom-scrollbar::-webkit-scrollbar {
  width: 3px;
}
.table-custom-scrollbar:hover::-webkit-scrollbar-thumb {
  background: #6c757d;
  border-radius: 10px;
}
.table-custom-scrollbar::-webkit-scrollbar-track {
  background: #2b2d31;
}
</style>
