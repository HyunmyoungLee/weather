import { createRouter, createWebHistory } from "vue-router";
import mainPage from "@/views/Main.vue";
import loginPage from "@/views/LoginPage.vue";
import registerPage from "@/views/RegisterPage.vue";
import forgetIdPage from "@/views/ForgetId.vue";
import forgetPwdPage from "@/views/ForgetPwd.vue";
import myPage from "@/views/MyPage.vue";

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
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
