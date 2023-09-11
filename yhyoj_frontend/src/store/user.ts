import { StoreOptions } from "vuex";

export default {
  namespaced: true,
  state: () => ({
    loginUser: {
      userName: "未登录",
      role: "notLogin",
    },
  }),
  actions: {
    getLoginUser({ commit, state }, payload) {
      // todo 改为从远程获取登录信息
      commit("updateUser", { userName: "Yhy", role: "admin" });
      // 触发了mutation的更改(目前是一个临时定义的死变量)
    },
  },
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
