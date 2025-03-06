import axiosInstance from "@/js/AxiosSetting.js";

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
      try {
        await axiosInstance
          .get("/user/checkEmail", {
            params: {
              email: this.userInfo.email,
            },
          })
          .then((response) => {
            if (response.data === "") {
              this.errorMessage = "존재하지 않는 이메일입니다";
              this.validEmail = false;
            } else {
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
          await axiosInstance
            .post("/user/sendAuthCode", qs.stringify(this.userInfo))
            .then(() => {
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
        await axiosInstance
          .get("/user/checkAuthCode", {
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
        await axiosInstance
          .put("/user/setNewPassword", {
            email: this.userInfo.email,
            password: this.pwd1,
          })
          .then((res) => {
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
    validPassword() {
      const regex = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$";
      if (this.pwd1.match(regex)) {
        this.errorMessage = "";
        return true;
      } else {
        this.errorMessage = "비밀번호 형식이 올바르지 않습니다";
        return false;
      }
    },
    matchPassword() {
      if (this.pwd1 === this.pwd2) {
        this.errorMessage = "";
        return true;
      } else {
        this.errorMessage = "비밀번호가 일치하지 않습니다";
        return false;
      }
    },
  },
};
