<template>
  <nav class="navbar navbar-expand-lg navbar-light">
    <div class="container-fluid">
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarTogglerDemo01"
        aria-controls="navbarTogglerDemo01"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <p class="navbar-brand mb-0 p-0" href="#">
        <i class="bi bi-hash"></i>
        <span class="text-white">{{
          channelStore.accessedChannelInfo.title
        }}</span>
      </p>
      <div class="collapse navbar-collapse" id="navbarTogglerDemo01">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <i class="bi bi-person-fill-add fs-4"></i>
          </li>
        </ul>
        <form class="d-flex">
          <input
            class="form-control me-2"
            type="search"
            placeholder="Search"
            aria-label="Search"
          />
          <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
      </div>
    </div>
  </nav>
</template>
<script setup>
import { useChannelStore } from '@/store/store';
const channelStore = useChannelStore();
</script>
<style lang=""></style>
