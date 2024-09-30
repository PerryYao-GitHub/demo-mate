<template>
  <form action="/">
    <van-search
        v-model="searchText"
        show-action
        placeholder="Choose Tags ..."
        @search="onSearch"
        @cancel="onCancel"
    />
  </form>
  <van-divider v-show="activeTagIds.length > 0" content-position="center">chosen tags</van-divider>
  <van-row gutter="12">
    <van-col v-for="tagId in activeTagIds">
      <van-tag :show="true" closeable size="medium" type="primary" @close="closeTag(tagId)">
        {{ tagId }}
      </van-tag>
    </van-col>
  </van-row>
  <van-divider content-position="center">choosing tags</van-divider>
  <van-tree-select
      v-model:active-id="activeTagIds"
      v-model:main-active-index="activeIndex"
      :items="tagList"
  />

  <van-button type="primary" @click="doSearch">search</van-button>

</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {useRouter} from "vue-router";
import axiosUser from "../plugin/axios/user.ts";
import {showToast} from "vant";

const searchText = ref('');
const activeTagIds = ref([]);
const activeIndex = ref(0);

const getAllTags = async () => {
  const resp = await axiosUser.get("/get/all/tags");
  if (resp.data.code === 200) {
    tagList.value = resp.data.data;
  } else {
    showToast({
      type: "fail",
      message: resp.data.description,
    })
  }
}

const tagList = ref([]);

const onSearch = (val: string) => {
  if (!val) {
    // 如果没有搜索关键字，重置为原始列表
    getAllTags()
    return;
  }
};

const onCancel = () => {
  searchText.value = '';
  getAllTags()
};

const closeTag = (tagId: string) => {
  activeTagIds.value = activeTagIds.value.filter(item => item !== tagId);
};

const router = useRouter();
const doSearch = () => {
  router.push({
    path: "/search/result",
    query: {
      tags: activeTagIds.value,
    }
  })
}
onMounted(()=>getAllTags());
</script>


<style scoped>

</style>
