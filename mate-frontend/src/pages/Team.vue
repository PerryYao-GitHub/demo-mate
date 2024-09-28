<template>
  <van-tabs v-model="active">
    <van-tab title="All Teams">
      <TeamsAll />
    </van-tab>
    <van-tab title="Created">
      <TeamsICreated />
    </van-tab>
    <van-tab title="Joined">
      <TeamsIJoined />
    </van-tab>
  </van-tabs>
</template>

<script setup>
import { ref } from 'vue';
import TeamsAll from "../components/TeamsAll.vue";
import TeamsICreated from "../components/TeamsICreated.vue";
import TeamsIJoined from "../components/TeamsIJoined.vue";

const active = ref(0); // 使用索引来控制选中的 tab
</script>

<style scoped>
</style>
