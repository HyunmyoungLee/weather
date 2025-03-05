import axios from "axios";
export default {
  data() {
    return {
      isLiked: false,
      isOwnedPost: false,
      isShowOptionModal: false,
    };
  },
  props: {
    selectedIndex: Number,
    userData: Array,
    loginUserEmail: String,
  },
  created() {
    this.checkUserLikedPost();
    this.checkOwnedPost();
  },
  methods: {
    async checkUserLikedPost() {
      try {
        await axios
          .get("http://localhost:8081/board/getLikedBoard", {
            params: {
              boardId: this.userData[this.selectedIndex].boardId,
              email: this.loginUserEmail,
            },
          })
          .then((res) => {
            if (res.data === true) {
              this.isLiked = true;
            } else {
              this.isLiked = false;
            }
          });
      } catch (err) {
        console.error(err);
      }
    },
    checkOwnedPost() {
      if (this.userData[this.selectedIndex].email === this.loginUserEmail) {
        this.isOwnedPost = true;
      } else {
        this.isOwnedPost = false;
      }
    },
    async deleteBoard() {
      try {
        await axios
          .delete("http://localhost:8081/board/deletePost", {
            params: {
              boardId: this.userData[this.selectedIndex].boardId,
            },
          })
          .then((res) => {
            alert(res.data);
            location.reload(true);
          });
      } catch (error) {
        console.error(error);
      }
    },

    async likePost() {
      const post = this.userData[this.selectedIndex];
      const originalLikeCount = post.numOfLike;
      try {
        const infos = {
          email: this.loginUserEmail,
          boardId: this.userData[this.selectedIndex].boardId,
        };
        if (!this.isLiked) {
          await axios
            .post("http://localhost:8081/board/addLikedBoard", infos)
            .then(() => {
              post.numOfLike += 1;
            });
        } else {
          await axios
            .delete("http://localhost:8081/board/deleteLikedBoard", {
              data: infos,
            })
            .then(() => {
              post.numOfLike -= 1;
            });
        }
      } catch (error) {
        console.error(error);
        post.numOfLike = originalLikeCount;
      }
      this.isLiked = !this.isLiked;
    },
    getUserAge(birthdate) {
      const today = new Date();
      const userBirthdate = new Date(birthdate);

      let age = today.getFullYear() - userBirthdate.getFullYear();
      const checkBirthdate =
        today.getMonth() > userBirthdate.getMonth() ||
        (today.getMonth() === userBirthdate.getMonth() &&
          today.getMonth() >= userBirthdate.getMonth());
      if (!checkBirthdate) age--;
      return age;
    },
    formattedCreatedDate(createdDate) {
      const postDate = new Date(createdDate);
      let formatDate =
        postDate.getFullYear() +
        "년 " +
        postDate.getMonth() +
        "월 " +
        postDate.getDate() +
        "일";
      return formatDate;
    },
    showOptionModal() {
      this.isShowOptionModal = true;
    },
    closeModal() {
      this.isShowOptionModal = false;
    },
  },
};
