import axios from "axios";
import { mapGetters, mapState } from "vuex";
import BoardModal from "@/views/BoardModal.vue";
import ModifyProfilePic from "@/views/ModifyProfilePic.vue";
import ModifyNickname from "@/views/ModifyNickname.vue";
export default {
  data() {
    return {
      isShowModal: false,
      boardList: [],
      isBoardModalOpen: false,
      loginUserEmail: "",
      selectedIndex: null,
      isModifyPicModal: false,
      isModifyNicknameModal: false,
    };
  },
  created() {
    this.checkLogin();
  },
  mounted() {
    this.getMyBoard();
  },
  components: {
    BoardModal,
    ModifyProfilePic,
    ModifyNickname,
  },
  computed: {
    ...mapGetters(["isLoginSuccess"]),
    ...mapState(["nickname", "imageUrl"]),
  },
  methods: {
    checkLogin() {
      axios
        .get("http://localhost:8081/user/status")
        .then((res) => {
          if (res.data.name != null) {
            this.loginUserEmail = res.data.email;
          }
        })
        .catch((err) => {
          if (err.status == 401) {
            this.$router.push({ name: "Main" });
          }
        });
    },
    async getMyBoard() {
      try {
        await axios
          .get("http://localhost:8081/board/getMyBoard")
          .then((res) => {
            this.boardList = res.data;
            this.boardList.sort(
              (a, b) => new Date(b.createdDate) - new Date(a.createdDate)
            );
          });
      } catch (error) {
        console.error(error);
      }
    },
    enterMainPage() {
      this.$router.push({ name: "Main" });
    },
    showSettingModal() {
      this.isShowModal = true;
    },
    closeModal() {
      this.isShowModal = false;
    },
    changeNickname() {
      this.isModifyNicknameModal = true;
    },
    changeProfilePic() {
      this.isModifyPicModal = true;
    },
    async logout() {
      try {
        await axios.post("http://localhost:8081/user/logout").then(() => {
          this.$store.commit("setLoginSuccess", null);
          this.$store.commit("setProfile", { nickname: null, imageUrl: null });
          localStorage.removeItem("loginSuccess");
          localStorage.removeItem("nickname");
          localStorage.removeItem("imageUrl");
          alert("로그아웃 되었습니다.");
          this.$router.push({ name: "Main" });
        });
      } catch (error) {
        console.error(error);
      }
    },
    formattedDate(postDate) {
      const now = new Date();
      const createDate = new Date(postDate);
      const gap = Math.floor((now - createDate) / 1000);

      if (gap < 3600) {
        return `${Math.floor(gap / 60)}분 전`;
      } else if (gap < 86400) {
        return `${Math.floor(gap / 3600)}시간 전`;
      } else if (gap < 604800) {
        return `${Math.floor(gap / 86400)}일 전`;
      } else if (gap < 2592000) {
        return `${Math.floor(gap / 604800)}주 전`;
      } else if (gap < 31536000) {
        return `${Math.floor(gap / 2592000)}달 전`;
      } else {
        return `${Math.floor(gap / 31536000)}년 전`;
      }
    },
    showBoardModal(index) {
      this.isBoardModalOpen = true;
      this.selectedIndex = index;
    },
    closeBoardModal() {
      this.isBoardModalOpen = false;
    },
    closeModifyPicModal() {
      this.isModifyPicModal = false;
    },
    closeModifyNicknameModal() {
      this.isModifyNicknameModal = false;
    },
  },
};
