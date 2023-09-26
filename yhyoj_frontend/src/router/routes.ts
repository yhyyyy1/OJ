import { RouteRecordRaw } from "vue-router";
import NoAuthView from "@/views/NoAuthView.vue";
import AccessEnum from "@/access/accessEnum";
import UserLoginView from "@/views/user/UserLoginView.vue";
import UserRegisterView from "@/views/user/UserRegisterView.vue";
import UserLayout from "@/layouts/UserLayout.vue";
import AddQuestionView from "@/views/question/AddQuestionView.vue";
import ManageQuestionView from "@/views/question/ManageQuestionView.vue";
import QuestionsView from "@/views/question/QuestionsView.vue";
import ViewQuestionView from "@/views/question/ViewQuestionView.vue";
import QuestionSubmitView from "@/views/question/QuestionSubmitView.vue";
import UserInfoView from "@/views/UserInfoView.vue";
import QuestionSubmitResultView from "@/views/question/QuestionSubmitResultView.vue";
import UserUpdateView from "@/views/user/UserUpdateView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/user",
    name: "用户",
    component: UserLayout,
    children: [
      {
        path: "/user/login",
        name: "用户登录",
        component: UserLoginView,
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: UserRegisterView,
      },
      {
        path: "/user/update",
        name: "用户数据更新",
        component: UserUpdateView,
      },
    ],
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/",
    name: "浏览题目",
    component: QuestionsView,
  },
  {
    path: "/submitList/question",
    name: "题目提交记录",
    component: QuestionSubmitView,
    meta: {
      access: AccessEnum.USER,
    },
  },
  {
    path: `/getSubmitList/question/:questionId`,
    name: "某个题目题目提交记录",
    component: QuestionSubmitView,
    meta: {
      access: AccessEnum.USER,
      hideInMenu: true,
    },
  },
  {
    path: "/Info/:id",
    name: "用户信息",
    component: UserInfoView,
    meta: {
      access: AccessEnum.USER,
      hideInMenu: true,
    },
  },
  {
    path: "/view/question/:id",
    name: "在线做题",
    component: ViewQuestionView,
    props: true,
    meta: {
      access: AccessEnum.USER,
      hideInMenu: true,
    },
  },
  {
    path: "/SubmitView/question/:id",
    name: "做题提交情况",
    component: QuestionSubmitResultView,
    props: true,
    meta: {
      access: AccessEnum.USER,
      hideInMenu: true,
    },
  },
  {
    path: "/add/question",
    name: "创建题目",
    component: AddQuestionView,
    meta: {
      access: AccessEnum.ADMIN,
    },
  },
  {
    path: "/update/question",
    name: "更新题目",
    component: AddQuestionView,
    meta: {
      access: AccessEnum.ADMIN,
      hideInMenu: true,
    },
  },
  {
    path: "/manage/question",
    name: "题目管理",
    component: ManageQuestionView,
    meta: {
      access: AccessEnum.ADMIN,
    },
  },
  {
    path: "/noAuth",
    name: "无权限",
    component: NoAuthView,
    meta: {
      hideInMenu: true,
    },
  },
];
