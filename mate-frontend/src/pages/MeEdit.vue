<template>
  <van-form @submit="onSubmit">
    <van-field
      v-model="editInfo.currentValue"
      :name="editInfo.editKey"
      :label="editInfo.editField"
      :placeholder="`input ${editInfo.editField}`"
    />
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        Submit
      </van-button>
    </div>
  </van-form>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";
import axiosUser from "../plugin/axios/user.ts";
import {showToast} from "vant";

const route = useRoute();
const router = useRouter();

const editInfo = ref({
  editKey: route.query.editKey,
  currentValue: route.query.currentValue,
  editField: route.query.editField,
})

const onSubmit = async () => {
  const resp = await axiosUser.post("/edit_account", {
    [editInfo.value.editKey as string]: editInfo.value.currentValue,
  })

  if (resp.data.code === 200) {
    showToast({
      message: resp.data.description,
      type: "success",
    })
    router.back();
  } else {
    showToast({
      message: resp.data.description,
      type: "fail",
    })
  }

}
</script>

<style scoped>

</style>
