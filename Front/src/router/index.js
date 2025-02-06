import { createRouter, createWebHistory } from "vue-router";
import mainPage from "@/views/Main.vue";
import loginPage from "@/views/LoginPage.vue";
import registerPage from "@/views/RegisterPage.vue";
import forgetIdPage from "@/views/ForgetId.vue";
import forgetPwdPage from "@/views/ForgetPwd.vue";
import myPage from "@/views/MyPage.vue";
import initProfile from "@/views/InitProfile.vue";
import store from "@/store";

const routes = [
  {
    path: "/",
    name: "Main",
    component: mainPage,
  },
  {
    path: "/login",
    name: "Login",
    component: loginPage,
  },
  {
    path: "/register",
    name: "Register",
    component: registerPage,
  },
  {
    path: "/idInquiry",
    name: "IdInquiry",
    component: forgetIdPage,
  },
  {
    path: "/pwdInquiry",
    name: "PwdInquiry",
    component: forgetPwdPage,
  },
  {
    path: "/mypage",
    name: "MyPage",
    component: myPage,
  },
  {
    path: "/initProfile",
    name: "InitProfile",
    component: initProfile,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to, from, next) => {
  await store.dispatch("restoreSession");
  const isLoggedIn = store.getters.isLoginSuccess;
  const hasProfile = store.getters.hasProfile;

  console.log("네비게이션 가드");
  ``;
  console.log("로그인 여부 : ", isLoggedIn);
  console.log("프로필 여부 : ", hasProfile);

  if (isLoggedIn && !hasProfile && to.path !== "/initProfile") {
    return next("/initProfile");
  } else {
    next();
  }
});

export default router;
