<script setup lang="ts">
import {TeamType} from "../models/team";
import {useRouter} from "vue-router";

interface TeamCardListProps {
  teamLst: TeamType[];
}
const props = defineProps<TeamCardListProps>();
const router = useRouter();
const checkOneTeam = (teamId:number) => {
  router.push(`/team/check/${teamId}`)
}
</script>

<template>
  <van-card
      v-for="team in props.teamLst"
      :title="team.name"
      :desc="team.description"
      :num="team.memberCnt"
      @click="checkOneTeam(team.id)"
  >
  </van-card>
  <van-empty v-if="!props.teamLst || props.teamLst.length === 0" description="net error"></van-empty>

  <div style="padding: 40px"></div>
</template>

<style scoped>

</style>
