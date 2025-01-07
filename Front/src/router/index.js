import { createRouter, createWebHistory } from "vue-router";
import mainPage from "@/views/Main.vue";
import loginPage from "@/views/LoginPage.vue";
import registerPage from "@/views/RegisterPage.vue";

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
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
