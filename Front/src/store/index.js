import { createStore } from "vuex";
import axios from "axios";
import router from "@/router";

export default createStore({
  state: {
    loginSuccess: null,
    user: null,
    nickname: null,
  },
  mutations: {
    setLoginSuccess(state, value) {
      state.loginSuccess = value;
    },
    setProfile(state, userInfo) {
      (state.user = userInfo), (state.nickname = userInfo.nickname);
    },
  },
  actions: {
    updateLoginSuccess({ commit }, value) {
      commit("setLoginSuccess", value);
      localStorage.setItem("loginSuccess", value);
    },
    async initLoginStatus({ commit }) {
      await axios
        .get("http://localhost:8081/user/status", {
          withCredentials: true,
        })
        .then((res) => {
          if (res.data.name) {
            commit("setLoginSuccess", res.data.name);
          } else {
            commit("setLoginSuccess", null);
          }
        })
        .catch((err) => {
          console.error(err);
        });
    },
    async fetchUser({ commit }) {
      try {
        await axios.get("http://localhost:8081/user/info").then((res) => {
          console.log(res);
          commit("setProfile", res.data);
        });
      } catch (err) {
        alert("로그인 정보가 없습니다");
        console.error("유저 정보 로딩 실패", err);
        router.push({ name: "Main" });
      }
    },
  },
  getters: {
    isLoginSuccess: (state) => state.loginSuccess,
    isAuthenticated: (state) => !!state.user,
    hasProfile: (state) => state.nickname,
  },
});
