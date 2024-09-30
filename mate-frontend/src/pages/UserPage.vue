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
  <van-cell title="avatar" :value="user.avatar || 'No avatar'"></van-cell>
  <van-cell title="name" :value="user.name || 'No name'"></van-cell>
  <van-cell title="phone" :value="user.phone || 'No phone'"></van-cell>
  <van-cell title="email" :value="user.email || 'No email'"></van-cell>

  <!-- 展示用户的 tags -->
  <van-cell title="tags">
    <template #default>
      <van-tag v-for="(tag, index) in parsedTags" :key="index" type="primary" style="margin-right: 8px;">
        {{ tag }}
      </van-tag>
    </template>
  </van-cell>

  <!-- 展示用户的加入团队信息 -->
  <van-divider content-position="center">Joining Teams</van-divider>
<!--  <van-row gutter="12">-->
<!--    <van-col v-for="team in user.joiningTeams" :key="team.id" span="24">-->
<!--      <van-cell :title="team.name" :label="team.description"></van-cell>-->
<!--    </van-col>-->
<!--  </van-row>-->
  <TeamCardList :team-lst="user.joiningTeams" />
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import axiosUser from "../plugin/axios/user.ts";
import { showToast } from "vant";
import TeamCardList from "../components/TeamCardList.vue";

// 默认头像
const defaultAvatar = 'public/vite.svg';

// 用户信息
const user = ref({
  account: "",
  avatar: "",
  name: "",
  phone: "",
  email: "",
  tags: "[]",  // 初始值为空数组的 JSON 字符串
  joiningTeams: []  // 加入团队的列表
});

// 解析后的 tags 列表
const parsedTags = ref([]);

// 解析 tags 的方法
const parseTags = (tags: string) => {
  try {
    return JSON.parse(tags);
  } catch (error) {
    return [];
  }
};

const route = useRoute();
const id = route.params;

const checkOneUser = async () => {
  const resp = await axiosUser.get("/check/one/user", {
    params: {
      userId: id.id,
    }
  });
  if (resp.data.code === 200) {
    user.value = resp.data.data;
    // 解析并更新 tags
    parsedTags.value = parseTags(user.value.tags);
  } else {
    showToast({
      type: "fail",
      message: resp.data.description,
    });
  }
};

onMounted(() => {
  checkOneUser();
});
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
