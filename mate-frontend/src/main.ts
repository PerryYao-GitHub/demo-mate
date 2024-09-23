import { createApp } from 'vue';
import './style.css'; // 你的自定义样式
import App from './App.vue';
import * as VueRouter from 'vue-router'
import Vant from 'vant';  // 引入 Vant 全局组件库
import 'vant/lib/index.css';
import routes from "./config/routes.ts";
import {createPinia} from "pinia";  // 引入 Vant 的全局样式

const app = createApp(App);

// route
const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes,
})
app.use(router);

// 全局注册 Vant
app.use(Vant);

const pinia = createPinia();
app.use(pinia);

app.mount('#app');
