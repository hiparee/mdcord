<template>
  <div class="search-bar-container">
    <input
      type="text"
      class="col-12 search-input"
      :placeholder="props.searchbarPlaceholder"
      aria-label="검색하기"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
    />
    <div
      role="button"
      class="icon-layout"
      :class="`${enteredInputBox ? 'pointer-x-icon' : 'point-search-icon'}`"
      @click="$emit('clearKeywordEvent')"
    >
      <div class="icon-container">
        <i
          class="icon bi bi-search"
          :class="{ 'icon-visible': !enteredInputBox }"
        ></i>
        <i
          class="icon bi bi-x-lg"
          :class="{ 'icon-visible': enteredInputBox }"
        ></i>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps(['modelValue', 'searchbarPlaceholder']);
defineEmits(['update:modelValue', 'clearKeywordEvent']);

// 입력폼 값 감시
const enteredInputBox = computed(() => {
  return props.modelValue.length >= 1;
});
</script>

<style scoped></style>
