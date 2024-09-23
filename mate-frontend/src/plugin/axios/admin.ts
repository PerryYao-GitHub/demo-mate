// axiosAdmin.js
import axios from "axios";
import { useAdminStore } from "../pinia/admin.ts";
import { createPinia, getActivePinia, setActivePinia } from "pinia";

// 初始化 Pinia 并激活它
if (!getActivePinia()) {
    const pinia = createPinia();
    setActivePinia(pinia);
}

const adminStore = useAdminStore();

// 创建 Axios 实例
const axiosAdmin = axios.create({
    baseURL: import.meta.env.VITE_API_URL + "/admin", // 设置全局的后端路径前缀
    timeout: 10000,
});

// 设置请求拦截器 实时更新token
axiosAdmin.interceptors.request.use(config => {
    // 动态设置 Authorization 头
    config.headers.Authorization = `Bearer ${adminStore.token}`;
    return config;
}, error => {
    return Promise.reject(error);
});

export default axiosAdmin;
