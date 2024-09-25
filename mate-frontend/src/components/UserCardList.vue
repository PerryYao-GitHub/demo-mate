<script setup lang="ts">
import {UserType} from "../models/user";

interface UserCardListProps {
  userLst: UserType[];
}
const props = defineProps<UserCardListProps>();
const defaultAvatar = 'public/vite.svg'
</script>

<template>
  <van-card
      v-for="user in props.userLst"
      :title="user.name"
      :thumb="user.avatar ? user.avatar : defaultAvatar"
  >
    <template #tags>
      <van-tag v-for="tag in user.tags" plain type="danger" style="margin-right: 5px">{{ tag }}</van-tag>
    </template>
  </van-card>
  <van-empty v-if="!props.userLst || props.userLst.length === 0" description="net error"></van-empty>

  <div style="padding: 40px"></div>
</template>

<style scoped>

</style>
