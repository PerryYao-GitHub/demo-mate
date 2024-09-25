<script setup>
import {onMounted, ref} from "vue";
import axiosUser from "../plugin/axios/user.ts";
import {showToast} from "vant";
import UserCardList from "../components/UserCardList.vue";

const userLst = ref([]);

onMounted(async () => {
  try {
    const resp = await axiosUser.get('/users/recommend', {
      params: {
        page: 1
      }
    });
    // console.log(resp.data); // 在这里处理请求的响应数据
    if (resp.data.code === 200) {
      let tmpUserLst = resp.data.data
      if (tmpUserLst) {
        tmpUserLst.forEach(user => {
          if (user.tags) user.tags = JSON.parse(user.tags);
        })
      }
      userLst.value = tmpUserLst
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
  <UserCardList :user-lst="userLst" />
</template>

<style scoped>

</style>

