import { defineStore } from "pinia";
import { computed, ref } from "vue";
import {UserType} from "../../models/user";
import axiosUser from "../axios/user.ts";

export const useUserStore = defineStore('user', () => {
    // states
    const info = ref({});

    // getters
    const isLogin = computed(() => {
        const cookie = document.cookie.split('; ').find(row => row.startsWith('userInfo='));
        return !!cookie; // 检查 Cookie 是否存在
    });

    // methods
    function setInfo(_info: UserType) {
        info.value = _info;
        // 将用户信息存储在 Cookie 中，设置有效期为 1 天
        const expireDate = new Date();
        expireDate.setDate(expireDate.getDate() + 1);
        document.cookie = `userInfo=${JSON.stringify(_info)}; expires=${expireDate.toUTCString()}; path=/`;
    }

    function delUser() {
        info.value = {};
        // 清除 Cookie
        document.cookie = 'userInfo=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    }

    // 在应用初始化时加载用户信息 从浏览器的 cookie 中加载用户信息
    function loadUserInfo() {
        const cookie = document.cookie.split('; ').find(row => row.startsWith('userInfo='));
        if (cookie) {
            const userInfo = JSON.parse(cookie.split('=')[1]);
            info.value = userInfo;
        }
    }

    // 从后端拉取
    async function checkAccount() {
        const resp = await axiosUser.get("/check_account");
        if (resp.data.code === 200) {
            info.value = resp.data.data;
            setInfo(resp.data.data);
        } else {
            throw new Error(resp.data.description);
        }
    }

    return {
        info,
        isLogin,
        setInfo,
        delUser,
        loadUserInfo,
        checkAccount,
    }
});
