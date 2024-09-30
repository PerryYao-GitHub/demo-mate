<script setup lang="ts">
import {UserType} from "../models/user";
import {useRouter} from "vue-router";

interface UserCardListProps {
  loading: boolean
  userLst: UserType[];
}

const props = defineProps<UserCardListProps>();
const defaultAvatar = 'public/vite.svg'
const router = useRouter();
const gotoUserPage = (id: number)=> {
  router.push(`/user/${id}`)
}
</script>

<template>
  <van-skeleton title avatar :row="3" :loading="props.loading">
    <van-card
        v-for="user in props.userLst"
        :title="user.name"
        :thumb="user.avatar ? user.avatar : defaultAvatar"
        @click="gotoUserPage(user.id)"
    >
      <template #tags>
        <van-tag v-for="tag in user.tags" plain type="danger" style="margin-right: 5px">{{ tag }}</van-tag>
      </template>
    </van-card>
    <van-empty v-if="!props.userLst || props.userLst.length === 0" description="net error"></van-empty>
  </van-skeleton>
  <div style="padding: 40px"></div>
</template>

<style scoped>

</style>
