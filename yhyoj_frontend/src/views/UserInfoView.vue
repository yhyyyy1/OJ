<template>
  <div class="userInfoView">
    <h1>用户信息:</h1>
    <h3>代办：</h3>
    信息修改<br />
    密码更新<br />
    头像？<br />
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
onMounted(() => {
  const userId = route.params.id;
  console.log(userId);
});
</script>
