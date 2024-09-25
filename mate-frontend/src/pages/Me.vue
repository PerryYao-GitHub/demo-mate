<template>
  <div class="avatar-container">
    <van-image
      :src="user.avatar ? user.avatar : defaultAvatar"
      width="80px"
      height="80px"
      fit="cover"
      round
    />
  </div>

  <van-cell title="account" :value="user.account" class="left-align-cell"></van-cell>
  <van-cell title="avatar" :value="user.avatar || 'No avatar'" is-link to="/edit" @click="toEdit('avatar', 'avatar', user.avatar)"></van-cell>
  <van-cell title="name" :value="user.name || 'No name'" is-link to="/edit" @click="toEdit('name', 'name', user.name)"></van-cell>
  <van-cell title="phone" :value="user.phone || 'No phone'" is-link to="/edit" @click="toEdit('phone', 'phone', user.phone)"></van-cell>
  <van-cell title="email" :value="user.email || 'No email'" is-link to="/edit" @click="toEdit('email', 'email', user.email)"></van-cell>


  <van-button type="danger" @click="onLogout">Logout</van-button>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import axiosUser from "../plugin/axios/user.ts";
import { showToast } from "vant";
import {useUserStore} from "../plugin/pinia/user.ts";

const defaultAvatar = 'public/vite.svg'
const userStore = useUserStore();

// 用户信息
const user = ref({
  account: "",
  avatar: "",
  name: "",
  phone: "",
  email: "",
});

onMounted(() => {
  userStore.checkAccount()
  user.value = userStore.info
});

const router = useRouter();
const toEdit = (editKey: string, editField: string, currentValue: string) => {
  router.push({
    path: '/edit',
    query: {
      editKey,
      editField,
      currentValue,
    }
  });
};

const onLogout = () => {
  axiosUser.get("/logout");
  userStore.delUser();
  router.replace("/auth");
}

</script>

<style scoped>
.avatar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
}

van-image {
  border-radius: 50%;
}

/* 添加样式以确保标题左对齐 */
.left-align-cell .van-cell__title {
  text-align: left;
}
</style>
