<template>
  <div id="input_area">
    <div id="forgetPwd_header">
      <h1 @click="goToMain">Weather & Outfit</h1>
    </div>

    <div id="input_box">
      <input
        type="text"
        v-model="userInfo.email"
        @blur="checkEmail"
        placeholder="이메일을 입력해주세요"
        :readonly="isEmailReadonly"
        :disabled="isEmailReadOnly"
      />
      <p id="errorMsg">{{ errorMessage }}</p>
      <button v-if="showAuthCode" @click="sendAuthCode">인증번호 요청</button>
      <input
        type="text"
        v-if="showAuthInput"
        v-model="userInfo.authNumber"
        placeholder="인증번호 입력"
      />
      <button v-if="showAuthInput" @click="checkAuthCode">인증번호 확인</button>
      <input
        v-if="setNewPwdInput"
        type="password"
        v-model="pwd1"
        placeholder="새 비밀번호(영문,숫자,특수기호 포함 (8자-16자 사이))"
      />
      <input
        v-if="setNewPwdInput"
        type="password"
        v-model="pwd2"
        placeholder="비밀번호 재입력"
      />
      <button v-if="setNewPwdInput" @click="setNewPassword">
        비밀번호 변경
      </button>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import qs from "qs";
export default {
  data() {
    return {
      userInfo: {
        email: "",
        authNumber: "",
      },
      authCode: "",
      errorMessage: "",
      pwd1: "",
      pwd2: "",
      validEmail: false,
      showAuthCode: true,
      showAuthInput: false,
      setNewPwdInput: false,
      isEmailReadonly: false,
    };
  },
  created() {
    this.checkLogin();
  },
  methods: {
    goToMain() {
      this.$router.push({ name: "Main" });
    },
    async checkEmail() {
      console.log("check email", this.userInfo.email);
      try {
        await axios
          .get("http://localhost:8081/user/checkEmail", {
            params: {
              email: this.userInfo.email,
            },
          })
          .then((response) => {
            if (response.data === "") {
              console.log("존재하지 않은 이메일");
              this.errorMessage = "존재하지 않는 이메일입니다";
              this.validEmail = false;
            } else {
              console.log("이메일 확인 완료");
              this.errorMessage = "";
              this.validEmail = true;
            }
          });
      } catch (error) {
        if (error.status === 400) {
          this.errorMessage = error.response.data;
        }
      }
    },
    async sendAuthCode() {
      if (this.validEmail) {
        try {
          await axios
            .post(
              "http://localhost:8081/user/sendAuthCode",
              qs.stringify(this.userInfo)
            )
            .then((res) => {
              console.log(res);
              alert("인증번호가 전송되었습니다 메일을 확인해주세요");
              this.showAuthInput = true;
              this.showAuthCode = false;
            });
        } catch (error) {
          console.error(error);
        }
      }
    },
    async checkAuthCode() {
      try {
        await axios
          .get("http://localhost:8081/user/checkAuthCode", {
            params: {
              email: this.userInfo.email,
              authNumber: this.userInfo.authNumber,
            },
          })
          .then((res) => {
            alert(res.data);
            this.errorMessage = "";
            this.showAuthCode = false;
            this.showAuthInput = false;
            this.isEmailReadonly = true;
            this.setNewPwdInput = true;
          });
      } catch (error) {
        this.errorMessage = error.response.data;
      }
    },
    async setNewPassword() {
      if (this.matchPassword() && this.validPassword()) {
        console.log("새 비밀번호 저장 함수 호출");
        await axios
          .put("http://localhost:8081/user/setNewPassword", {
            email: this.userInfo.email,
            password: this.pwd1,
          })
          .then((res) => {
            console.log(res);
            alert(res.data);
            this.$router.push({ name: "Login" });
          })
          .catch((error) => {
            console.error(error);
            alert(error.response.data);
          });
      }
    },
    checkLogin() {
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
        });
    },
    validPassword() {
      const regex = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$";
      if (this.pwd1.match(regex)) {
        console.log("정규식 일치");
        this.errorMessage = "";
        return true;
      } else {
        console.log("정규식 불일치");
        this.errorMessage = "비밀번호 형식이 올바르지 않습니다";
        return false;
      }
    },
    matchPassword() {
      if (this.pwd1 === this.pwd2) {
        console.log("비밀번호 일치");
        this.errorMessage = "";
        return true;
      } else {
        console.log("비밀번호 불일치");
        this.errorMessage = "비밀번호가 일치하지 않습니다";
        return false;
      }
    },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

#input_area {
  width: 330px;
  height: 1000px;
  margin: 0 auto;
  position: relative;
}

#forgetPwd_header {
  width: 100%;
  height: 20px;
  padding: 76px 0 60px;
  text-align: center;
  margin-bottom: 20px;
}

#input_box {
  width: 100%;
  margin: 0 auto;
}

#input_box input {
  width: 100%;
  height: 50px;
  padding: 15px 10px 14px;
  box-sizing: border-box;
  border: 1px solid #cccccc;
  background-color: #ffffff;
  margin-bottom: 10px;
}
#errorMsg {
  color: red;
}

button {
  width: 100%;
  height: 60px;
  color: #ffffff;
  background-color: #000000;
  text-align: center;
  border: none;
  display: block;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}
</style>
