<script setup>
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import axiosUser from "../plugin/axios/user.ts";
import qs from 'qs'
import {showToast} from "vant";
import UserCardList from "../components/UserCardList.vue";

const userLst = ref([]);
const route = useRoute();

const {tags} = route.query;
onMounted(async () => {
  try {
        const resp = await axiosUser.get('/search_users_by_tags', {
          params: {
            tagNames: tags
          },
          paramsSerializer: params => {
            return qs.stringify(params, { arrayFormat: 'repeat' });
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
const defaultAvatar = "public/vite.svg"; // 替换为你的默认头像路径


</script>

<template>
  <UserCardList :user-lst="userLst" />
</template>

<style scoped>

</style>
