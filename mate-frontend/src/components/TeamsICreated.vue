<template>
  <van-button @click="goToTeamCreate" icon="add" >Create Team</van-button>
  <TeamCardList :team-lst="teamLst" />
</template>

<script setup lang="ts">

import TeamCardList from "../components/TeamCardList.vue";
import {onMounted, ref} from "vue";
import axiosUser from "../plugin/axios/user.ts";
import {showToast} from "vant";
import {useRouter} from "vue-router";

const teamSearchQuery = ref({
  keyword: "",
})
const teamLst = ref([])
const checkTeams = async ()=>{
  const resp = await axiosUser.get("/check/teams/created", {
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
    })
  }
}
onMounted(() => checkTeams())

const router = useRouter();
const goToTeamCreate = () => {
  router.push('/team/create');
}
</script>

<style scoped>
</style>
