<template>
  <a-row id="globalHeader" class="grid-demo" align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectedKeys"
        :default-selected-keys="['1']"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="title-bar">
            <img class="logo" src="../assets/LOGO2.jpg" />
            <div class="title">YhyOJ</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path"
          >{{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="60px">
      <div class="title-bar">
        <a-dropdown>
          <a-button
            ><img class="userImage" src="../assets/UserImage.jpg"
          /></a-button>
          <template #content>
            <a-doption v-if="isLogin === 'yes'" @click="toUserInfo">
              {{ store.state.user?.loginUser?.userName }}
            </a-doption>
            <a-doption v-if="isLogin === 'no'" @click="login">
              去登录
            </a-doption>
            <a-doption @click="updateUser"> 更改登录信息</a-doption>
            <a-doption @click="logout">退出登录</a-doption>
          </template>
        </a-dropdown>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRoute, useRouter } from "vue-router";
import { computed, ref, onMounted } from "vue";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";
import AccessEnum from "@/access/accessEnum";
import { UserControllerService } from "../../generated";

const route = useRoute();
const router = useRouter();
const store = useStore();
const selectedKeys = ref([route.path]);

let isLogin = ref("");
let updateKey = ref(true);
const visibleRoutes = computed(() => {
  return routes.filter((item, idex) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    if (
      !checkAccess(store.state.user.loginUser, item?.meta?.access as string)
    ) {
      return false;
    }
    return true;
  });
});

onMounted(() => {
  if (checkAccess(store.state.user.loginUser, AccessEnum.USER)) {
    isLogin.value = "yes";
  } else {
    isLogin.value = "no";
  }
});
// setTimeout(() => {
//   store.dispatch("user/getLoginUser", {
//     userName: "Yhy管理员",
//     userRole: AccessEnum.ADMIN,
//   });
// }, 3000);

router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});

const logout = () => {
  UserControllerService.userLogoutUsingPost();
  router.push({ path: "/" });
  isLogin.value = "no";
  store.dispatch("user/getLoginUser", {
    userRole: AccessEnum.NOT_LOGIN,
  });
};

const login = () => {
  router.push({ path: "/user/login" });
};

const updateUser = () => {
  router.push({ path: "/user/update" });
};
const toUserInfo = () => {
  router.push({
    path: `/Info/${store.state.user.loginUser.id}`,
  });
};
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.logo {
  height: 48px;
}

.userImage {
  height: 36px;
}

.title {
  color: #444;
  margin-left: 16px;
}
</style>
