<template>
  <div class="userRegisterView">
    <h2 style="margin-bottom: 16px">用户注册</h2>
    <a-form
      :model="form"
      style="max-width: 500px; margin: 0 auto"
      label-align="left"
      auto-label-width
      @submit="handleSubmit"
    >
      <a-form-item
        field="userAccount"
        tooltip="账号长度不能低于四位"
        label="账号"
        :rules="[
          { required: true, message: '账号不能为空' },
          { minLength: 4, message: '账号长度不能低于四位' },
        ]"
      >
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item
        field="userName"
        label="用户名"
        :rules="[{ required: true, message: '用户名不能为空' }]"
      >
        <a-input v-model="form.userName" placeholder="请输入账号用户名" />
      </a-form-item>
      <a-form-item
        field="userPassword"
        tooltip="密码长度不能低于八位"
        label="密码"
        :rules="[
          { required: true, message: '密码不能为空' },
          { minLength: 8, message: '密码长度不能低于八位' },
        ]"
      >
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item
        field="checkPassword"
        tooltip="密码长度不能低于八位"
        label="再次输入"
        :rules="[
          { required: true, message: '密码不能为空' },
          { minLength: 8, message: '密码长度不能低于八位' },
        ]"
      >
        <a-input-password
          v-model="form.checkPassword"
          placeholder="再次输入密码"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="注册">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from "vue";
import { UserControllerService, UserRegisterRequest } from "../../../generated";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { Message } from "@arco-design/web-vue";

const router = useRouter();
const store = useStore();
const form = reactive<UserRegisterRequest>({
  userName: "",
  userAccount: "",
  userPassword: "",
  checkPassword: "",
});

const handleSubmit = async () => {
  if (
    form.userName < 2 ||
    form.userAccount.length < 4 ||
    form.userPassword.length < 8
  ) {
    return;
  }
  if (
    form.checkPassword.length !== form.userPassword.length ||
    form.checkPassword !== form.userPassword
  ) {
    Message.error("两次输入密码不一致");
    return;
  }

  const res = await UserControllerService.userRegisterUsingPost(form);

  if (res.code === 0) {
    Message.success("注册成功！");
    await store.dispatch("getLoginUser");
    await router.push({
      path: "/user/login",
      replace: true,
    });
  } else {
    Message.error(res.message);
  }
};
</script>
