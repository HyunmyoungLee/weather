<template>
  <div id="register_area">
    <div id="register_header">
      <h1 @click="goToMain">Weather & Outfit</h1>
    </div>

    <div id="register_box">
      <input
        type="email"
        v-model="userInfo.email"
        @blur="checkDuplicateEmail"
        placeholder="이메일을 입력해주세요"
        :readonly="isEmailReadOnly"
        :disabled="isEmailReadOnly"
      />
      <br />
      <button id="authButton" v-if="showAuthCode" @click="sendAuthCode()">
        인증번호 요청
      </button>
      <input
        type="text"
        v-if="showAuthInput"
        v-model="userInfo.authNumber"
        placeholder="인증번호"
      />
      <button id="checkAuthButton" v-if="showAuthInput" @click="checkAuthCode">
        인증번호 확인
      </button>
      <p id="authErrorMessage" v-if="authErrorMessage">
        {{ authErrorMessage }}
      </p>
      <input
        type="password"
        v-model="userInfo.password"
        placeholder="비밀번호(영문,숫자,특수기호 포함 8-20자)"
      />
      <br />

      <input type="text" v-model="userInfo.name" placeholder="이름" />
      <br />
      <input type="text" id="postcode" placeholder="우편번호" />
      <br />
      <input type="button" @click="execDaumPostcode()" value="우편번호 찾기" />
      <br />
      <input
        type="text"
        id="roadAddress"
        v-model="userInfo.address"
        placeholder="도로명 주소"
      />
      <br />
      <p id="errorMsg" v-if="errorMessage">{{ errorMessage }}</p>
      <span class="submit"
        ><button @click="registerUser">회원가입</button></span
      >
    </div>
  </div>
</template>
<script>
import axios from "axios";
import qs from "qs";

export default {
  data() {
    return {
      isPostCodeLoaded: false,
      showAuthCode: false,
      showAuthInput: false,
      isEmailReadOnly: false,
      authErrorMessage: "",
      errorMessage: "",
      userInfo: {
        email: "",
        password: "",
        name: "",
        address: "",
        authNumber: "",
      },
    };
  },
  created() {
    this.checkLogin();
  },
  mounted() {
    const script = document.createElement("script");
    script.src =
      "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
    script.async = true;
    script.onload = () => {
      this.isPostCodeLoaded = true;
      console.log("daum postcode script loaded successfully");
    };
    script.onerror = () => {
      console.log("Failed to load Daum postcode script");
    };
    document.head.appendChild(script);
  },
  methods: {
    async sendAuthCode() {
      console.log("check valid email : ", this.userInfo.email);

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
    },
    async checkAuthCode() {
      console.log(this.userInfo.authNumber);
      try {
        await axios
          .get("http://localhost:8081/user/checkAuthCode", {
            params: {
              email: this.userInfo.email,
              authNumber: this.userInfo.authNumber,
            },
          })
          .then((res) => {
            console.log(res);
            alert(res.data);
            this.authErrorMessage = "";
            this.showAuthInput = false;
            this.isEmailReadOnly = true;
          });
      } catch (error) {
        console.error(error);
        this.authErrorMessage = error.response.data;
      }
    },
    async checkDuplicateEmail() {
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
              this.showAuthCode = true;
              this.errorMessage = "";
            } else {
              this.errorMessage = "이미 사용중인 이메일입니다.";
              this.showAuthCode = false;
            }
          });
      } catch (error) {
        if (error.status === 400) {
          this.errorMessage = error.response.data;
        }
      }
    },
    async registerUser() {
      try {
        await axios
          .post(
            "http://localhost:8081/user/register",
            qs.stringify(this.userInfo)
          )
          .then((response) => {
            console.log(response);
            this.errorMessage = "";
            alert("회원가입이 성공적으로 완료되었습니다");
            location.href = "/login";
          });
      } catch (error) {
        console.error(error.response);
        if (error.status === 400) {
          this.errorMessage = error.response.data;
        }
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
    goToMain() {
      this.$router.push({ name: "Main" });
    },
    execDaumPostcode() {
      if (this.isPostCodeLoaded && window.daum && window.daum.Postcode) {
        // eslint-disable-next-line no-undef
        new daum.Postcode({
          oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            var extraRoadAddr = ""; // 참고 항목 변수

            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
              extraRoadAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if (data.buildingName !== "" && data.apartment === "Y") {
              extraRoadAddr +=
                extraRoadAddr !== ""
                  ? ", " + data.buildingName
                  : data.buildingName;
            }
            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if (extraRoadAddr !== "") {
              extraRoadAddr = " (" + extraRoadAddr + ")";
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById("postcode").value = data.zonecode;
            document.getElementById("roadAddress").value = roadAddr;
          },
        }).open();
      } else {
        console.error("Daum Postcode API is not loaded.");
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

#register_area {
  width: 330px;
  height: 1000px;
  margin: 0 auto;
  position: relative;
}

#register_header {
  width: 100%;
  height: 20px;
  padding: 76px 0 60px;
  text-align: center;
  margin-bottom: 20px;
}

#register_header > h1:hover {
  cursor: pointer;
}

#register_box {
  width: 100%;
  margin: 0 auto;
}

#register_box input {
  width: 100%;
  height: 50px;
  padding: 15px 10px 14px;
  box-sizing: border-box;
  border: 1px solid #cccccc;
  background-color: #ffffff;
  margin-bottom: 10px;
}
#authButton,
#checkAuthButton {
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
#errorMsg,
#authErrorMessage {
  color: red;
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
</style>
