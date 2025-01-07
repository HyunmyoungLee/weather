import { createStore } from "vuex";

export default createStore({
  state: {
    loginSuccess: null,
  },
  mutations: {
    setLoginSuccess(state, value) {
      state.loginSuccess = value;
    },
  },
  actions: {
    updateLoginSuccess({ commit }, value) {
      commit("setLoginSuccess", value);
      localStorage.setItem("loginSuccess", value);
    },
    initLoginStatus({ commit }) {
      const loginStatus = localStorage.getItem("loginSuccess");
      const cookie = getCookie("JSESSIONID");
      if (cookie) {
        commit("setLoginSuccess", loginStatus);
      } else {
        commit("setLoginSuccess", null);
      }
    },
  },
  getters: {
    isLoginSuccess: (state) => state.loginSuccess,
  },
});

function getCookie(cookieName) {
  const name = `${cookieName}=`;
  const decodedCookie = decodeURIComponent(document.cookie);
  const cookies = decodedCookie.split(";");

  for (let i = 0; i < cookies.length; i++) {
    let cookie = cookies[i];
    while (cookie.charAt(0) === " ") cookie = cookie.substring(1);
    if (cookie.indexOf(name) === 0) return true;
  }
  return false;
}
