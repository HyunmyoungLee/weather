import axios from "axios";
export default {
  data() {
    return {
      nickname: "",
      isValidNickname: false,
      errorMessage: "",
    };
  },
  methods: {
    async checkValidateNickname() {
      await axios
        .get("http://localhost:8081/user/checkNickname", {
          params: {
            nickname: this.nickname,
          },
        })
        .then((response) => {
          if (response.data.email != null) {
            this.isValidNickname = false;
            this.errorMessage = "중복된 닉네임입니다";
          } else {
            this.errorMessage = "";
            this.isValidNickname = true;
          }
        })
        .catch((error) => {
          console.error(error);
          this.errorMessage = error.response.data;
        });
    },
    async modifyNickname() {
      await axios
        .put("http://localhost:8081/user/modifyNickname", {
          nickname: this.nickname,
        })
        .then((res) => {
          alert(res.data);
          location.href = "/mypage";
        })
        .catch((err) => {
          console.error(err);
        });
    },
  },
};
