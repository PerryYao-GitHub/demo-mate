<script setup>
import { computed } from "vue";

const props = defineProps({
  team: {
    type: Object,
    required: true,
  },
});

const members = computed(() => {
  return props.team.members || [];
});
</script>

<template>
  <van-cell-group>
    <van-cell title="Team Name" :value="props.team.name"/>
    <van-cell title="Team Description" :value="props.team.description"/>
    <van-cell title="Members Count" :value="props.team.memberCnt"/>
    <van-cell title="Members">
      <template v-for="member in members" :key="member.id">
        <li>
          <van-icon name="manager-o" v-if="member.id === props.team.leaderId"/>
          {{ member.account }}
        </li>
      </template>
    </van-cell>
    <van-cell title="Team Create Time" :value="props.team.createTime"/>
  </van-cell-group>
</template>

<style scoped>
</style>
