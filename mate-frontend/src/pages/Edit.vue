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
import {useRoute} from "vue-router";
import {ref} from "vue";

const route = useRoute();

const editInfo = ref({
  editKey: route.query.editKey,
  currentValue: route.query.currentValue,
  editField: route.query.editField,
})

const onSubmit = () => {

}
</script>

<style scoped>

</style>
