<script setup lang="ts">
import {ref, watch} from "vue";
import {useRoute} from "vue-router"; // 引入路由功能

// 当前选中的 tab
const activeTab = ref('home');
const title = ref('Home'); // 初始化 title 为 'Home'

// const router = useRouter();
const route = useRoute();

// 监听路由变化，更新 title 和 activeTab
watch(route, (newRoute) => {
  const path = newRoute.path;

  if (path.startsWith('/team')) {
    activeTab.value = 'team';
    title.value = 'Team';
  } else if (path.startsWith('/me')) {
    activeTab.value = 'me';
    title.value = 'Me';
    title.value = path === '/me/edit' ? 'Edit' : path === '/me/auth' ? 'Auth' : 'Me'; // 特殊情况
  } else if (path.startsWith('/search')) {
    activeTab.value = 'search';
    title.value = path === '/search/result' ? 'Result' : 'Search';
  } else if (path === '/setting') {
    activeTab.value = 'setting';
    title.value = 'Settings';
  } else if (path === '/home') {
    activeTab.value = 'home';
    title.value = 'Home';
  } else {
    activeTab.value = 'home'; // 默认路径
    title.value = 'Home';
  }
});

// 左侧返回按钮事件
const onClickLeft = () => history.back();
</script>

<template>
  <div class="page-container">
    <van-nav-bar
        :title="title"
        left-text="Back"
        left-arrow
        @click-left="onClickLeft"
    >
      <template #right>
        <router-link to="/search">
          <van-icon name="search" size="18"/>
        </router-link>
      </template>
    </van-nav-bar>

    <div class="content">
      <router-view/>
    </div>

    <van-tabbar v-model="activeTab" route class="fixed-tabbar">
      <van-tabbar-item to="/home" icon="home-o" name="home">Home</van-tabbar-item>
      <van-tabbar-item to="/team" icon="friends-o" name="team">Team</van-tabbar-item>
      <van-tabbar-item to="/me" icon="contact" name="me">Me</van-tabbar-item>
      <van-tabbar-item to="/setting" icon="setting-o" name="setting">Settings</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<style scoped>
.page-container {
  width: 100vw;
  display: flex;
  flex-direction: column;
  height: 100vh; /* 让容器占满整个页面 */
}

.content {
  flex: 1; /* 让内容区自动扩展 */
}

.fixed-tabbar {
  position: fixed;
  bottom: 0;
  width: 100%;
}
</style>
