import axiosInstance from "@/js/AxiosSetting.js";

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
    };
    script.onerror = () => {};
    document.head.appendChild(script);
  },
  methods: {
    //이메일 인증코드 발송
    async sendAuthCode() {
      try {
        await axiosInstance
          .post("/user/sendAuthCode", qs.stringify(this.userInfo))
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
      try {
        await axiosInstance
          .get("/user/checkAuthCode", {
            params: {
              email: this.userInfo.email,
              authNumber: this.userInfo.authNumber,
            },
          })
          .then((res) => {
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
      try {
        await axiosInstance
          .get("/user/checkEmail", {
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
      if (this.checkValidBirthdate() && this.checkValidCode()) {
        try {
          await axiosInstance
            .post("/user/register", this.userInfo, {
              headers: {
                "Content-Type": "application/json",
              },
            })
            .then(() => {
              this.errorMessage = "";
              alert("회원가입이 성공적으로 완료되었습니다");
              this.validCode = false;
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
      axiosInstance
        .get("/user/status")
        .then((response) => {
          if (response.data.name != null) {
            this.$router.push({ name: "Main" });
          }
        })
        .catch((err) => {
          console.error(err);
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

// import RegisterPageJs from "@/js/RegisterPage.js";
// export default RegisterPageJs;
