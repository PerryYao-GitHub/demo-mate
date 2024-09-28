<script setup>
import {useRoute, useRouter} from "vue-router";
import axiosUser from "../plugin/axios/user.ts";
import {showToast} from "vant";
import {computed, onMounted, ref} from "vue";
import TeamOne from "../components/TeamOne.vue";
import {useUserStore} from "../plugin/pinia/user.ts";

const userStore = useUserStore();
userStore.loadUserInfo();
const userId = userStore.info.id;

const route = useRoute();
const teamId = route.params;
const team = ref({});

const checkOneTeam = async () => {
  try {
    const resp = await axiosUser.get('/check/one/team', {
      params: {...teamId}
    });
    if (resp.data.code === 200) {
      team.value = resp.data.data;
    } else {
      showToast({
        type: "fail",
        message: resp.data.description,
      });
    }
  } catch (error) {
    console.error(error);
    showToast({
      type: "fail",
      message: "请求失败，请重试",
    });
  }
};

// 计算属性来判断按钮的显示
const buttonVisibility = computed(() => {
  const members = team.value.members || [];
  const isMember = members.some(member => member.id === userId);

  if (!isMember) {
    return 'join'; // 显示 Join 按钮
  }

  if (userId === team.value.leaderId) {
    return 'edit or delete'; // 显示 Edit or Delete 按钮
  } else {
    return 'quit'; // 显示 Quit 按钮
  }
});

const router = useRouter();

const onJoin = async () => {
  // 加入团队的逻辑
  const resp = await axiosUser.get("/join/team", {
    params: {...teamId,}
  })
  if (resp.data.code === 200) {
    showToast({
      type: "success",
      message: resp.data.description,
    })
    await checkOneTeam();
  } else {
    showToast({
      type: "fail",
      message: resp.data.description,
    })
  }
};

const onQuit = async () => {
  // 退出团队的逻辑
  const resp = await axiosUser.get("/quit/team", {
    params: {...teamId,}
  })
  if (resp.data.code === 200) {
    showToast({
      type: "success",
      message: resp.data.description,
    })
    router.back();
  } else {
    showToast({
      type: "fail",
      message: resp.data.description,
    })
  }
};

const onEdit = () => {
  router.push(`/team/edit/${teamId.teamId}`)
}

const onDelete = async () => {
  // 删除团队的逻辑
  const resp = await axiosUser.get("/delete/team", {
    params: {...teamId,}
  })
  if (resp.data.code === 200) {
    showToast({
      type: "success",
      message: resp.data.description,
    })
    router.back();
  } else {
    showToast({
      type: "fail",
      message: resp.data.description,
    })
  }
};

onMounted(() => checkOneTeam());
</script>

<template>
  <TeamOne :team="team"/>
  <van-button
      v-if="buttonVisibility === 'join'"
      type="primary"
      @click="onJoin">Join
  </van-button>

  <van-button
      v-if="buttonVisibility === 'quit'"
      type="danger"
      @click="onQuit">Quit
  </van-button>

  <van-button
      v-if="buttonVisibility === 'edit or delete'"
      type="primary"
      @click="onEdit">Edit
  </van-button>

  <van-button
      v-if="buttonVisibility === 'edit or delete'"
      type="danger"
      @click="onDelete">Delete
  </van-button>
</template>

<style scoped>
.van-button {
  margin-left: 10px;
}
</style>
