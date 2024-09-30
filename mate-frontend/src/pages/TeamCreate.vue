<script setup lang="ts">
import {ref} from "vue";
import axiosUser from "../plugin/axios/user.ts";
import {showToast} from "vant";
import {useRouter} from "vue-router";

const createTeamForm = ref({
  name: "",
  description: "",
})
const router = useRouter()
const onSubmit = async () => {
  const resp = await axiosUser.post("/create/team", {
    ...createTeamForm.value
  })
  if (resp.data.code === 200) {
    showToast({
      type: "success",
      message: resp.data.description,
    })
    router.back();
  } else {
    showToast({
      type: "fail",
      message: resp.data.description,
    })
    createTeamForm.value = { name: "", description: "" }
  }
}
</script>

<template>
  <van-form>
    <van-field
      v-model="createTeamForm.name"
      name="team name"
      label="name"
      placeholder="input team name"
      required
    />

    <van-field
      v-model="createTeamForm.description"
      name="team description"
      label="description"
      placeholder="input team description"
      required
    />
  </van-form>

  <van-button @click="onSubmit">Submit</van-button>
</template>

<style scoped>

</style>
