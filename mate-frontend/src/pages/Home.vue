<script setup>
import {onMounted, ref} from "vue";
import axiosUser from "../plugin/axios/user.ts";
import {showToast} from "vant";
import UserCardList from "../components/UserCardList.vue";

const userLst = ref([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const resp = await axiosUser.get('/users/recommend', {
      params: {
        page: 1
      }
    });
    if (resp.data.code === 200) {
      let tmpUserLst = resp.data.data
      if (tmpUserLst) {
        tmpUserLst.forEach(user => {
          if (user.tags) user.tags = JSON.parse(user.tags);
        })
      }
      userLst.value = tmpUserLst
      loading.value = false;
    } else {
      showToast({
        message: resp.data.description,
        type: "fail"
      })
    }
  } catch (error) {
    console.log(error); // 处理错误
  }
});

</script>

<template>
  <UserCardList :user-lst="userLst" :loading="loading"/>
</template>

<style scoped>

</style>

