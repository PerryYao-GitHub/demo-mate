// axiosUser.js
import axios from "axios";
import { useUserStore } from "../pinia/user";
import { createPinia, getActivePinia, setActivePinia } from "pinia";

// 初始化 Pinia 并激活它
if (!getActivePinia()) {
    const pinia = createPinia();
    setActivePinia(pinia);
}

const userStore = useUserStore();

// 创建 Axios 实例
const axiosUser = axios.create({
    baseURL: import.meta.env.VITE_API_URL + "/user",
    timeout: 10000,
});

// 设置请求拦截器
axiosUser.interceptors.request.use(config => {
    // 动态设置 Authorization 头
    config.headers.Authorization = `Bearer ${userStore.token}`;
    return config;
}, error => {
    return Promise.reject(error);
});

export default axiosUser;
