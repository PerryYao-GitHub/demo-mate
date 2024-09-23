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

  <van-cell title="account" :value="user.account"></van-cell>
  <van-cell title="avatar" :value="user.avatar" is-link to="/edit" @click="toEdit('avatar', 'avatar', user.avatar)"></van-cell>
  <van-cell title="name" :value="user.name" is-link to="/edit" @click="toEdit('name', 'name', user.name)"></van-cell>
  <van-cell title="phone" :value="user.phone" is-link to="/edit" @click="toEdit('phone', 'phone', user.phone)"></van-cell>
  <van-cell title="email" :value="user.email" is-link to="/edit" @click="toEdit('email', 'email', user.email)"></van-cell>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";

// 用户信息
const user = ref({
  id: 8964,
  account: "asd",
  name: "asdddsa",
  avatar: "", // 如果头像为空则展示默认头像
  phone: "12345678",
  email: "xxxx",
  tags: [],
  status: 1,
  loginTime: new Date(),
  createTime: new Date(),
});

// 默认头像路径
const defaultAvatar = "public/vite.svg"; // 替换为你的默认头像路径

const router = useRouter();

// 编辑功能
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
</style>
