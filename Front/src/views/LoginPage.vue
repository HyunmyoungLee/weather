<template>
  <div v-if="isLoading" class="spinner-overlay">
    <v-progress-circular indeterminate color="primary" :size="70" :width="5">
    </v-progress-circular>
  </div>
  <div id="login_area">
    <div id="login_header">
      <h1 @click="goToMain">Weather & Outfit</h1>
    </div>

    <div id="login_box">
      <input
        class="input"
        type="text"
        v-model="email"
        placeholder="이메일을 입력해 주세요"
      />
      <br />
      <input
        class="input"
        type="password"
        v-model="password"
        placeholder="비밀번호를 입력해 주세요"
      />
      <p id="loginError" v-if="loginErrorMessage">{{ loginErrorMessage }}</p>
      <br />
      <span class="submit"><button @click.prevent="login">로그인</button></span>

      <ul id="sub_menu">
        <li><a @click="goToRegister">회원 가입</a></li>
        <li><a @click="goToIdInquiry">아이디 찾기</a></li>
        <li><a @click="goToPwdInquiry">비밀번호 찾기</a></li>
      </ul>
    </div>
    <div id="socialLogin">
      <a id="kakao"><button @click="socialLogin">카카오 로그인</button></a>
    </div>
  </div>
</template>

<script>
import LoginPageJs from "@/js/LoginPage.js";
export default LoginPageJs;
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}
@import "@/css/LoginPage.css";
</style>
