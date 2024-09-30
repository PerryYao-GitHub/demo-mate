<template>
  <van-divider v-show="activeTagIds.length > 0" content-position="center">Chosen Tags</van-divider>
  <van-row gutter="12">
    <van-col v-for="tagId in activeTagIds" :key="tagId">
      <van-tag :show="true" closeable size="medium" type="primary" @close="closeTag(tagId)">
        {{ tagId }}
      </van-tag>
    </van-col>
  </van-row>

  <van-divider content-position="center">Choose Tags</van-divider>
  <van-tree-select
    v-model:main-active-index="activeIndex"
    :items="filteredTagList"
    :active-id="activeTagIds"
    @update:active-id="handleTagSelect"
  />

  <van-button type="primary" @click="submitTags" :disabled="activeTagIds.length === 0">Save Tags</van-button>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRouter, useRoute} from 'vue-router';
import {showToast} from 'vant';
import axiosUser from "../plugin/axios/user.ts";

const router = useRouter();
const route = useRoute();
const maxTags = 5;

// 接收路由传递的 tags，解析为数组
const activeTagIds = ref<string[]>(JSON.parse(route.params.tags) || []);

// 限制选择的标签数量最大为5
const handleTagSelect = (selectedIds: string[]) => {
  if (selectedIds.length > maxTags) {
    showToast({
      type: 'fail',
      message: `You can only select up to ${maxTags} tags`,
    });
    return;
  }
  activeTagIds.value = selectedIds;
};

// 关闭/删除标签
const closeTag = (tagId: string) => {
  activeTagIds.value = activeTagIds.value.filter(tag => tag !== tagId);
};

// 初始化 Tag 列表
const tagList = ref([]);

// 从后端获取 tagList
const getTagList = async () => {
  const resp = await axiosUser.get('/get/all/tags');
  if (resp.data.code === 200) {
    tagList.value = resp.data.data;
    filteredTagList.value = [...tagList.value]; // 更新过滤后的列表
  } else {
    showToast({
      type: 'fail',
      message: resp.data.description,
    });
  }
};

// 过滤后的 tag 列表，初始为 tagList
const filteredTagList = ref([...tagList.value]);

// 设置 main active index，用于 Tree Select
const activeIndex = ref(0);

// 提交选中的标签，将它们转为 JSON 字符串
const submitTags = async () => {
  const selectedTagsJson = JSON.stringify(activeTagIds.value);
  const resp = await axiosUser.post('/edit_account', {
    tags: selectedTagsJson,
  })
  if (resp.data.code === 200) {
    router.back();
  } else {
    showToast({
      type: 'fail',
      message: resp.data.description,
    });
  }
};

// 在挂载时获取 tagList
onMounted(() => getTagList());
</script>

<style scoped>
.van-divider {
  margin-bottom: 16px;
}

.van-row {
  margin-bottom: 16px;
}

.van-button {
  margin-top: 16px;
}
</style>
