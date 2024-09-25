<script setup lang="ts">
import {ref} from "vue";
import axiosUser from "../plugin/axios/user.ts";

import {useRouter} from "vue-router";
import {showToast} from "vant";
import {useUserStore} from "../plugin/pinia/user.ts";

const userStore = useUserStore();
const router = useRouter();
const form = ref({
  account: "",
  password: "",
  confirmPassword: "",
});

const resetForm = () => {
  form.value.account = "";
  form.value.password = "";
  form.value.confirmPassword = "";
};

const onSubmit = async () => {
  const resp = await axiosUser.post(`/${action.value}`, {
    ...form.value,
  });
  // 处理响应逻辑
  if (action.value === "login") {
    if (resp.data.code === 200) {
      showToast({
        message: resp.data.description,
        type: "success"
      })
      /**
       * 十分重要 !!!!!!
       * 在使用session - cookie 模式时, 一定要在登录的同时 更新用户的全局状态
       * 因为在此模式下, 用户的登录状态 (isLogin) 和 用户信息 (info) 是绑定的
       * 不像 JWT 模式下, 二者是分离的 (存在 token <=> isLogin)
       * 如果不在这里获取信息把 isLogin 变成 true, 那么就会跳转到 /me 又跳转回来
       */
      userStore.setInfo(resp.data.data);
      resetForm()
      await router.replace("/me");
    } else {
      showToast({
        message: resp.data.description,
        type: "fail",
      })
    }
  } else {
    if (resp.data.code === 200) {
      action.value = "login";
      resetForm()
    } else {
      showToast({
        message: resp.data.description,
        type: "fail",
      })
    }
  }

};

const action = ref("login");
const changeAction = (_action: string) => {
  action.value = _action;
  resetForm();
};
</script>

<template>
  <div>
    <van-form @submit.prevent="onSubmit">
      <van-cell-group inset>
        <van-field
            v-model="form.account"
            name="Account"
            label="Account"
            placeholder="Account"
            :rules="[{ required: true, message: 'Input Account' }]"
        />
        <van-field
            v-model="form.password"
            type="password"
            name="Password"
            label="Password"
            placeholder="Password"
            :rules="[{ required: true, message: 'Input Password' }]"
        />
        <van-field
            v-if="action === 'register'"
            v-model="form.confirmPassword"
            type="password"
            name="Confirm Password"
            label="Confirm Password"
            placeholder="Confirm Password"
            :rules="[{ required: true, message: 'Confirm Password' }]"
        />
      </van-cell-group>
      <van-button type="info" @click="onSubmit" style="width: 100%; border-radius: 20px;">
        Submit
      </van-button>
    </van-form>
    <div style="margin-top: 20px;">
      <van-button @click="changeAction('login')" style="margin-right: 10px;">Login</van-button>
      <van-button @click="changeAction('register')">Register</van-button>
    </div>
  </div>
</template>

<style scoped>
/* 如果需要更多样式调整，可以在这里添加 */
</style>
