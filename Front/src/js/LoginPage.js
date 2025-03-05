import axios from "axios";
axios.defaults.withCredentials = true;

export default {
  name: "LoginPage",
  data() {
    return {
      email: null,
      password: null,
      isLoading: false,
      loginErrorMessage: "",
      userProfile: {
        nickname: null,
        imageUrl: null,
      },
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
          console.error(err);
        })
        .finally(() => {
          setTimeout(() => {
            this.isLoading = false;
          }, 500);
        });
    },
    async login() {
      try {
        const response = await axios.post(
          "http://localhost:8081/user/login",
          { email: this.email, password: this.password },
          { headers: { "Content-Type": "application/json" } }
        );
        if (response.status === 200) {
          this.isLoading = true;
          const redirectUrl = response.data.redirectUrl;
          this.userProfile.nickname = response.data.nickname;
          this.userProfile.imageUrl = response.data.imageUrl;
          this.$store.commit("setLoginSuccess", response.data.loginSuccess);
          this.$store.commit("setProfile", this.userProfile);
          this.$router.push(redirectUrl);
        }
      } catch (error) {
        this.loginErrorMessage = error.response.data;
      }
    },
    async socialLogin() {
      try {
        window.location.href = "http://localhost:8081/user/socialLogin";
      } catch (error) {
        console.error(error);
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
