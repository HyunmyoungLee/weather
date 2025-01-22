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
      <label class="checkbox">
        <input type="checkbox" name="autoLogin" id="autoLogin" /> 자동 로그인
      </label>
      <ul id="sub_menu">
        <li><a @click="goToRegister">회원 가입</a></li>
        <li><a @click="goToIdInquiry">아이디 찾기</a></li>
        <li><a @click="goToPwdInquiry">비밀번호 찾기</a></li>
      </ul>
    </div>
    <div id="socialLogin">
      <a id="kakao" href="#"><span>카카오 로그인</span></a>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapActions } from "vuex";
axios.defaults.withCredentials = true;

export default {
  name: "LoginPage",
  data() {
    return {
      email: null,
      password: null,
      isLoading: false,
      loginErrorMessage: "",
    };
  },
  created() {
    this.checkLogin();
  },
  computed: {},
  methods: {
    checkLogin() {
      this.isLoading = true;

      axios
        .get("http://localhost:8081/user/status")
        .then((response) => {
          if (response.data.name != null) {
            this.$router.push({ name: "Main" });
          }
        })
        .catch((err) => {
          if (err.status == 401) {
            console.log("로그인 정보가 없습니다");
          }
        })
        .finally(() => {
          setTimeout(() => {
            this.isLoading = false;
          }, 500);
        });
    },
    ...mapActions(["updateLoginSuccess"]),
    async login() {
      console.log(this.email, this.password);

      try {
        const response = await axios.post(
          "http://localhost:8081/user/login",
          { email: this.email, password: this.password },
          { headers: { "Content-Type": "application/json" } }
        );
        if (response.status === 200) {
          this.updateLoginSuccess(response.data.loginSuccess);
          this.isLogin = true;
          this.$router.push({ name: "Main" });
        }
      } catch (error) {
        this.loginErrorMessage = error.response.data;
      }
    },
    goToMain() {
      this.$router.push({ name: "Main" });
    },
    goToRegister() {
      this.$router.push({ name: "Register" });
    },
    goToIdInquiry() {
      this.$router.push({ name: "IdInquiry" });
    },
    goToPwdInquiry() {
      this.$router.push({ name: "PwdInquiry" });
    },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

.spinner-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

ul {
  list-style-type: none;
}

a {
  text-decoration: none;
  color: black;
}

#login_area {
  width: 330px;
  height: 1000px;
  margin: 0 auto;
  position: relative;
}

#login_header {
  width: 100%;
  height: 20px;
  padding: 76px 0 60px;
  text-align: center;
  margin-bottom: 20px;
}

#login_header > h1:hover {
  cursor: pointer;
}
#login_box {
  width: 100%;
  margin: 0 auto;
}
#login_box .input {
  width: 100%;
  height: 50px;
  padding: 15px 10px 14px;
  box-sizing: border-box;
  border: 1px solid #cccccc;
  background-color: #ffffff;
  margin-bottom: 10px;
}
#loginError {
  color: red;
}
.checkbox {
  display: inline-block;
  font-size: 14px;
  color: #aaa;
}

.submit {
  display: block;
  margin: 10px 0 20px;
}

.submit > button {
  width: 100%;
  height: 60px;
  color: #ffffff;
  background-color: #000000;
  text-align: center;
  border: none;
  display: block;
  font-size: 18px;
  font-weight: bold;
}

#login_box > #sub_menu {
  margin: 36px 0;
  display: block;
  height: 22px;
}

#sub_menu > li {
  float: left;
  margin-left: 20px;
}

#sub_menu > li:first-child {
  margin-left: 10px;
}

#sub_menu > li:after {
  content: "";
  display: inline-block;
  width: 1px;
  height: 14px;
  background-color: #aaa;
  margin-left: 20px;
  position: relative;
  top: 2px;
}

#sub_menu > li:nth-child(3)::after {
  display: none;
}

#sub_menu > li > a {
  color: #aaa;
  font-size: 14px;
}

#socialLogin {
  width: auto;
  margin: 30px 0 35px;
}

#kakao {
  width: 100%;
  height: 42px;
  background-color: #fee500;
  border: 1px #fee500 solid;
  border-radius: 6px;
  text-align: center;
  padding-top: 9px;
  box-sizing: border-box;
  position: relative;
}

#socialLogin > a {
  display: block;
}

#socialLogin > a > span {
  font-size: 15px;
  font-weight: bold;
  position: relative;
  top: -13px;
}

#socialLogin > #kakao > span::before {
  content: "";
  width: 30px;
  height: 30px;
  display: inline-block;
  background: url(../images/kakao-talk.png) no-repeat;
  background-size: 30px 30px;
  position: relative;
  top: 10px;
  margin-right: 10px;
}
</style>
