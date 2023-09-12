<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<style>
#app {
}
</style>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import AccessEnum from "@/access/accessEnum";
import { onMounted } from "vue";

/**
 * 全局初始化函数，有全局单次调用的代码，都可以写在这里
 */
const doInit = () => {
  console.log("hello 欢迎来到我的项目，你好~~~~~~~");
};

onMounted(() => {
  doInit();
});

const router = useRouter();
const store = useStore();
router.beforeEach((to, from, next) => {
  // todo 还需要优化
  if (to.meta?.access === "canAdmin") {
    if (store.state.user.loginUser?.role !== AccessEnum.ADMIN) {
      next("/noAuth");
      return;
    }
  }
  next();
});
</script>
