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
  <van-cell title="avatar" :value="user.avatar || 'No avatar'" is-link @click="toEdit('avatar', 'avatar', user.avatar)"></van-cell>
  <van-cell title="name" :value="user.name || 'No name'" is-link @click="toEdit('name', 'name', user.name)"></van-cell>
  <van-cell title="phone" :value="user.phone || 'No phone'" is-link @click="toEdit('phone', 'phone', user.phone)"></van-cell>
  <van-cell title="email" :value="user.email || 'No email'" is-link @click="toEdit('email', 'email', user.email)"></van-cell>

  <!-- 展示 tags -->
  <van-cell title="tags" @click="toEditTags(user.tags)">
    <template #default>
      <van-tag
        v-for="(tag, index) in parsedTags"
        :key="index"
        type="primary"
        style="margin-right: 8px;"
      >
        {{ tag }}
      </van-tag>
    </template>
  </van-cell>

  <van-button type="danger" @click="onLogout">Logout</van-button>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import axiosUser from "../plugin/axios/user.ts";
import { useUserStore } from "../plugin/pinia/user.ts";

// 默认头像
const defaultAvatar = 'public/vite.svg';
const userStore = useUserStore();

// 用户信息
const user = ref({
  account: "",
  avatar: "",
  name: "",
  phone: "",
  email: "",
  tags: "[]",  // 初始值为空数组的 JSON 字符串
});

// 解析后的 tags 列表
const parsedTags = ref<string[]>([]);

// 解析 tags 的方法
const parseTags = (tags: string) => {
  try {
    return JSON.parse(tags);
  } catch (error) {
    return [];
  }
};

// 获取用户信息
onMounted(() => {
  userStore.checkAccount();
  user.value = userStore.info;
  parsedTags.value = parseTags(user.value.tags);  // 解析 tags
});

const router = useRouter();
const toEdit = (editKey: string, editField: string, currentValue: string) => {
  router.push({
    path: '/me/edit',
    query: {
      editKey,
      editField,
      currentValue,
    }
  });
};

const toEditTags = (tags: string) => {
  router.push(`/me/edit/tags/${tags}`)
}

const onLogout = () => {
  axiosUser.get("/logout");
  userStore.delUser();
  router.replace("/me/auth");
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

/* 添加样式以确保标题左对齐 */
.left-align-cell .van-cell__title {
  text-align: left;
}
</style>
