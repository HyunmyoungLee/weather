import axios from "axios";
axios.defaults.withCredentials = true;
export default {
  data() {
    return {
      nickname: "",
      errorMessage: "",
      fileName: "선택된 파일 없음",
      file: null,
      isValidNickname: false,
      isValidImage: false,
      showFileInput: false,
      isDefaultImg: false,
      imgSrc: require("@/images/user.png"),
    };
  },

  created() {
    this.checkLogin();
  },
  computed: {},
  methods: {
    checkLogin() {
      axios.get("http://localhost:8081/user/status").catch((err) => {
        if (err.status == 401) {
          this.$router.push({ name: "Main" });
        }
      });
    },
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
    uploadImgFile(e) {
      const imgFile = e.target.files[0];
      if (imgFile) {
        const maxSize = 10 * 1024 * 1024;
        const checkImg = imgFile.type.startsWith("image/");
        if (!checkImg) {
          alert("이미지 파일만 업로드 가능합니다");
          e.target.value = "";
          return;
        } else if (imgFile.size > maxSize) {
          alert("이미지는 10MB이하의 파일만 업로드 가능합니다");
          e.target.value = "";
          return;
        } else {
          this.file = imgFile;
          this.fileName = imgFile.name;
          if (this.imgSrc && this.imgSrc.startsWith("blob:")) {
            URL.revokeObjectURL(this.imgSrc);
          }
          this.imgSrc = URL.createObjectURL(imgFile);
          this.isValidImage = true;
        }
      } else {
        this.file = null;
        this.fileName = "선택된 파일 없음";
      }
    },
    setDefaultImg() {
      this.isDefaultImg = true;
      this.isValidImage = true;
      this.file = null;
    },
    async addProfile() {
      const formData = new FormData();
      const json = JSON.stringify({
        nickName: this.nickname,
      });

      const blob = new Blob([json], { type: "application/json" });
      formData.append("userProfile", blob);
      if (this.file) {
        formData.append("file", this.file);
      }

      await axios
        .post("http://localhost:8081/user/addProfile", formData)
        .then((res) => {
          alert("프로필 등록이 완료되었습니다");
          this.$store.commit("setProfile", {
            nickname: res.data.nickname,
            imageUrl: res.data.imageUrl,
          });
          this.$router.push({ name: "Main" });
          this.file = null;
        })
        .catch((err) => {
          alert("서버 오류로 인한 프로필 등록 실패 : ", err);
        });
    },
  },
};
