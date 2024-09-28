<template>
  <div class="header">
    <van-search v-model="teamSearchQuery.keyword" @search="onSearch" class="search" />
  </div>
  <TeamCardList :team-lst="teamLst" />
</template>

<script setup lang="ts">
import TeamCardList from "../components/TeamCardList.vue";
import { onMounted, ref } from "vue";
import axiosUser from "../plugin/axios/user.ts";
import { showToast } from "vant";


const teamSearchQuery = ref({
  keyword: "",
});
const onSearch = () => {
  checkTeams();
}
const teamLst = ref([]);
const checkTeams = async () => {
  const resp = await axiosUser.get("/check/teams", {
    params: {
      ...teamSearchQuery.value,
    }
  });
  if (resp.data.code === 200) {
    teamLst.value = resp.data.data;
  } else {
    showToast({
      type: "fail",
      message: resp.data.description,
    });
  }
}
onMounted(() => checkTeams());
</script>

<style scoped>

</style>
