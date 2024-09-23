import {defineStore} from "pinia";
import {computed, ref} from "vue";

export const useUserStore = defineStore('user', () => {
    // states
    const storedToken = localStorage.getItem('token') || "";

    const token = ref(storedToken);
    const info = ref({});
    // getters
    const isLogin = computed(() => !!token.value);
    // methods
    function setToken (_token: string) {
        token.value = _token;
        localStorage.setItem('token', _token);
    }

    function setInfo (_info: object) {
        info.value = _info;
    }

    function delUser () {
        token.value = "";
        info.value = {};
        localStorage.removeItem('token')
    }

    return{
        token,
        info,
        isLogin,
        setToken,
        setInfo,
        delUser,
    }
})
