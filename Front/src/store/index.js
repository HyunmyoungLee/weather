import { createStore } from "vuex";
import axios from "axios";
import router from "@/router";

export default createStore({
  state: {
    loginSuccess: localStorage.getItem("loginSuccess") || null,
    nickname: localStorage.getItem("nickname") || null,
    imageUrl: localStorage.getItem("imageUrl") || null,
  },
  mutations: {
    setLoginSuccess(state, value) {
      state.loginSuccess = value;
      if (value === null) {
        localStorage.removeItem("loginSuccess");
      } else {
        localStorage.setItem("loginSuccess", value);
      }
    },
    setProfile(state, userInfo) {
      (state.imageUrl = userInfo.imageUrl || null),
        (state.nickname = userInfo.nickname || null);
      localStorage.setItem("nickname", userInfo.nickname || "");
      localStorage.setItem("imageUrl", userInfo.imageUrl || "");
    },
  },
  actions: {
    restoreSession({ commit }) {
      const loginSuccess = localStorage.getItem("loginSuccess");
      const nickname = localStorage.getItem("nickname");
      const imageUrl = localStorage.getItem("imageUrl");
      if (loginSuccess) {
        commit("setLoginSuccess", loginSuccess);
      }

      if (nickname && imageUrl) {
        commit("setProfile", { nickname, imageUrl });
      }
    },
    async initLoginStatus({ commit }) {
      await axios
        .get("http://localhost:8081/user/info", {
          withCredentials: true,
        })
        .then((res) => {
          if (res.data.name) {
            commit("setLoginSuccess", res.data.name);
            commit("setProfile", {
              nickname: res.data.nickname,
              imageUrl: res.data.imageUrl,
            });
            localStorage.setItem("loginSuccess", res.data.name);
            localStorage.setItem("nickname", res.data.nickname || "");
            localStorage.setItem("imageUrl", res.data.imageUrl || "");
          } else {
            throw new Error("로그인 세션 없음");
          }
        })
        .catch((err) => {
          console.error(err);
          commit("setLoginSuccess", null);
          commit("setProfile", { nickname: null, imageUrl: null });
          localStorage.removeItem("loginSuccess");
          localStorage.removeItem("nickname");
          localStorage.removeItem("imageUrl");
          router.push({ name: "Login" });
        });
    },
  },
  getters: {
    isLoginSuccess: (state) => !!state.loginSuccess,
    hasProfile: (state) => !!state.nickname && !!state.imageUrl,
  },
});
