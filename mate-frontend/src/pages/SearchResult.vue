<script setup>
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import axiosUser from "../plugin/axios/user.ts";
import qs from 'qs'
import {Toast} from "vant";

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
        console.log(resp.data); // 在这里处理请求的响应数据
        if (resp.data.code === 200) {
          let tmpUserLst = resp.data.data
          if (tmpUserLst) {
            tmpUserLst.forEach(user => {
              if (user.tags) user.tags = JSON.parse(user.tags);
            })
          }
          userLst.value = tmpUserLst
        } else {
          Toast.fail(resp.data.description)
        }
      } catch (error) {
        console.log(error); // 处理错误
      }

    });
const defaultAvatar = "public/vite.svg"; // 替换为你的默认头像路径


</script>

<template>
  <van-card
      v-for="user in userLst"
      :title="user.name"
      :thumb="user.avatar ? user.avatar : defaultAvatar"
  >
    <template #tags>
      <van-tag v-for="tag in user.tags" plain type="danger" style="margin-right: 5px">{{ tag }}</van-tag>
    </template>
  </van-card>
  <van-empty v-if="!userLst || userLst.length === 0" description="no adapted users"></van-empty>
</template>

<style scoped>

</style>
