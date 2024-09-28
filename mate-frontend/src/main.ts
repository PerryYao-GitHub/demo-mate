import {createApp, onMounted} from 'vue';
import './style.css'; // 你的自定义样式
import App from './App.vue';
import Vant from 'vant';  // 引入 Vant 全局组件库
import 'vant/lib/index.css';
import {createPinia} from "pinia";  // 引入 Vant 的全局样式
import router from './router'
import {useUserStore} from "./plugin/pinia/user.ts";

const app = createApp(App);

app.use(router);
// 全局注册 Vant
app.use(Vant);
app.use(createPinia());
const userStore = useUserStore();
onMounted(()=> {
    userStore.loadUserInfo();
})
app.mount('#app');
