// axiosUser.js
import axios from "axios";
// 创建 Axios 实例
const axiosUser = axios.create({
    baseURL: import.meta.env.VITE_API_URL + "/user",
    timeout: 10000,
});

axiosUser.defaults.withCredentials = true  // 携带 cookie

export default axiosUser;
