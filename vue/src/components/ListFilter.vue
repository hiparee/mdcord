<template>
  <form @submit.prevent>
    <div class="row g-3">
      <div class="col">
        <input
          :value="title"
          @input="changeTitle"
          type="text"
          class="form-control"
          placeholder="사용자 정보를 입력해주세요."
        />
      </div>
    </div>
  </form>
</template>

<script setup></script>

<style lang="scss" scoped></style>
