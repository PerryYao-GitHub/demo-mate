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
      :items="filteredTagList"
  />

  <van-button type="primary" @click="doSearch">search</van-button>

</template>

<script setup lang="ts">
import {ref} from 'vue'
import {useRouter} from "vue-router";

const searchText = ref('');
const activeTagIds = ref([]);
const activeIndex = ref(0);

const tagList = ref([
  {
    text: 'hobby',
    children: [
      { text: 'anime', id: 'anime' },
      { text: 'sleep', id: 'sleep' },
    ],
  },
  {
    text: 'tech',
    children: [
      { text: 'java', id: 'java' },
      { text: 'python', id: 'python' },
    ],
  },
]);

// 过滤后的 tag 列表
const filteredTagList = ref([...tagList.value]);

const onSearch = (val: string) => {
  if (!val) {
    // 如果没有搜索关键字，重置为原始列表
    filteredTagList.value = [...tagList.value];
    return;
  }

  // 根据搜索关键词过滤子标签
  filteredTagList.value = tagList.value.map(parentTag => {
    const filteredChildren = parentTag.children.filter(child => child.text.toLowerCase().includes(val.toLowerCase()));
    return {
      ...parentTag,
      children: filteredChildren,
    };
  }).filter(parentTag => parentTag.children.length > 0); // 只保留有匹配子标签的父标签
};

const onCancel = () => {
  searchText.value = '';
  filteredTagList.value = [...tagList.value]; // 重置为初始状态
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

</script>


<style scoped>

</style>
