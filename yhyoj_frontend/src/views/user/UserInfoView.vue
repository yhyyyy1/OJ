<template>
  <div class="userInfoView">
    <h1>用户信息:{{ userId }}</h1>
    <h3>代办：</h3>
    信息修改<br />
    密码更新<br />
    头像？<br />
  </div>
</template>
<script setup lang="ts">
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {UserControllerService} from "../../../generated";

const route = useRoute();
const userId = ref(route.query.userId);

onMounted(() => {
  const res = UserControllerService.getUserVoByIdUsingGet(userId.value);
  if (res.)
});
</script>
