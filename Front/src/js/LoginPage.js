import axiosInstance from "@/js/AxiosSetting.js";
axiosInstance.defaults.withCredentials = true;

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

      axiosInstance
        .get("/user/status")
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
        const response = await axiosInstance.post(
          "/user/login",
          { email: this.email, password: this.password },
          { headers: { "Content-Type": "application/json" } }
        );
        console.log(response);
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
        window.location.href =
          "http://ec2-43-201-47-190.ap-northeast-2.compute.amazonaws.com:8081/user/socialLogin";
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
