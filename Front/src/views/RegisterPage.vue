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
      <p id="timer" v-if="timeCount > 0 && showAuthInput">{{ formatTime }}</p>
      <p v-if="timeCount === 0 && showAuthInput">
        인증번호가 만료되었습니다 다시 인증요청해주세요.
      </p>
      <button
        id="checkAuthButton"
        v-if="showAuthInput && timeCount > 0"
        @click="checkAuthCode"
      >
        인증번호 확인
      </button>
      <button
        id="authButton"
        v-if="timeCount === 0 && showAuthInput"
        @click="sendAuthCode()"
      >
        인증번호 재요청
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
      <input
        type="date"
        id="birthdate"
        v-model="userInfo.birthdate"
        :max="maxDate"
      />
      <br />
      <select id="gender" v-model="userInfo.gender">
        <option value="">성별</option>
        <option value="male">남성</option>
        <option value="femaie">여성</option>
      </select>
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
      validCode: false,
      maxDate: new Date().toISOString().split("T")[0],
      timer: null,
      timeCount: 0,
      userInfo: {
        email: "",
        password: "",
        name: "",
        gender: "",
        address: "",
        authNumber: "",
        birthdate: "",
      },
    };
  },
  created() {
    this.checkLogin();
    if (this.$route.query.email) {
      this.userInfo.email = this.$route.query.email;
      this.isEmailReadOnly = true;
      this.validCode = true;
    }
  },
  computed: {
    formatTime() {
      const minutes = Math.floor(this.timeCount / 60);
      const seconds = this.timeCount % 60;
      return `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
    },
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
    //이메일 인증코드 발송
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
            this.startTimer();
          });
      } catch (error) {
        console.error(error);
      }
    },

    //타이머 시작
    startTimer() {
      this.timeCount = 120; // 인증번호 유효 2분 설정
      this.timer = setInterval(() => {
        if (this.timeCount > 0) {
          this.timeCount--;
        } else {
          clearInterval(this.timer);
          this.timer = null;
        }
      }, 1000);
    },
    //타이머 종료
    stopTimer() {
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },

    //인증번호 확인
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
            this.showAuthCode = false;
            this.isEmailReadOnly = true;
            this.validCode = true;
            this.stopTimer();
          });
      } catch (error) {
        console.error(error);
        this.authErrorMessage = error.response.data;
      }
    },

    //이메일 중복 확인
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

    //최종 회원가입 등록
    async registerUser() {
      console.log("유저 주소지 : ", this.userInfo.address);

      if (this.checkValidBirthdate() && this.checkValidCode) {
        try {
          await axios
            .post("http://localhost:8081/user/register", this.userInfo, {
              headers: {
                "Content-Type": "application/json",
              },
            })
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
      }
    },

    //로그인 상태 여부 확인
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

    //메인화면으로 이동
    goToMain() {
      this.$router.push({ name: "Main" });
    },

    //생년월일 유효성 검사
    checkValidBirthdate() {
      if (!this.userInfo.birthdate) {
        this.errorMessage = "생년월일을 필수항목입니다";
      } else {
        this.errorMessage = "";
      }
      return this.errorMessage.length === 0;
    },

    checkValidCode() {
      if (!this.validCode) {
        this.errorMessage = "올바른 이메일 인증코드를 입력하세요";
      } else {
        this.errorMessage = "";
      }
      return this.errorMessage.length === 0;
    },
    execDaumPostcode() {
      if (this.isPostCodeLoaded && window.daum && window.daum.Postcode) {
        // eslint-disable-next-line no-undef
        new daum.Postcode({
          oncomplete: (data) => {
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
            this.userInfo.address = roadAddr;
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

#register_box input,
select {
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
