import axios from "axios";
export default {
  data() {
    return {
      maxDate: new Date().toISOString().split("T")[0],
      emailList: [],
      errorMessage: "",
      userInfo: {
        name: "",
        birthdate: "",
      },
    };
  },
  created() {
    this.checkLogin();
  },
  computed: {
    formatEmailList() {
      const emailAddress = [];
      const domain = [];
      for (let i = 0; i < this.emailList.length; i++) {
        emailAddress.push(this.emailList[i].split("@")[0]);
        domain.push(
          this.emailList[i].substring(this.emailList[i].indexOf("@"))
        );
      }
      const formatAddress = emailAddress.map((email) => {
        const showAddressLength =
          email.length - Math.floor(email.length / 2) + 1;
        const hideAddressLength = email.length - showAddressLength;

        const showPart = email.slice(0, showAddressLength);
        const hidePart = "*".repeat(hideAddressLength);

        return showPart + hidePart;
      });

      return formatAddress.map((email, index) => email + domain[index]);
    },
  },
  methods: {
    goToMain() {
      this.$router.push({ name: "Main" });
    },
    async checkId() {
      try {
        if (this.checkValidInfo() && this.checkValidBirthdate()) {
          this.userInfo.birthdate = new Date(this.userInfo.birthdate)
            .toISOString()
            .split("T")[0];
          await axios
            .get("http://localhost:8081/user/checkId", {
              params: {
                name: this.userInfo.name,
                birthdate: this.userInfo.birthdate,
              },
            })
            .then((res) => {
              if (res.data == "") {
                alert("일치하는 정보가 없습니다 회원가입 페이지로 이동합니다");
                this.$router.push({ name: "Register" });
              } else {
                this.emailList = res.data;
              }
            });
        }
      } catch (error) {
        console.error(error);
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
          console.error(err);
        });
    },
    checkValidInfo() {
      if (!this.userInfo.name) {
        this.errorMessage = "이름은 필수 항목입니다";
      } else {
        this.errorMessage = "";
      }
      return this.errorMessage.length === 0;
    },
    checkValidBirthdate() {
      if (!this.userInfo.birthdate) {
        this.errorMessage = "생년월일은 필수 항목입니다";
      } else {
        this.errorMessage = "";
      }
      return this.errorMessage.length === 0;
    },
  },
};
