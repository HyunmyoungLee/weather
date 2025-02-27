import { genders, ages, periods } from "@/js/category.js";
import { mapState } from "vuex";
import axios from "axios";
axios.defaults.withCredentials = true;
import defaultImg from "@/images/image.png";
import BoardModal from "@/views/BoardModal.vue";
export default {
  name: "OutfitPart",
  data() {
    return {
      selectedGender: [],
      selectedAge: [],
      selectedPeriod: "",
      genders,
      ages,
      periods,
      isValidImage: false,
      file: null,
      imgSrc: defaultImg,
      isModalOpen: false,
      expandedModal: false,
      content: "",
      codeId: 0,
      imageList: [],
      nicknameList: [],
      profileImages: [],
      createdDates: [],
      userData: [],
      isBoardModalOpen: false,
      selectedIndex: null,
    };
  },
  components: {
    BoardModal,
  },
  props: {
    outfitPart: Boolean,
    locations: Array,
    selectedLocation: String,
    activeIndex: Number,
    loginUserEmail: String,
  },
  computed: {
    ...mapState(["imageUrl", "nickname"]),
  },
  mounted() {
    this.getBoard();
  },
  watch: {
    selectedLocation(newLocation) {
      if (newLocation) {
        this.getBoard();
      }
    },
  },

  methods: {
    showFileUpload() {
      this.$refs.fileInput.click();
    },
    uploadImgFile(e) {
      const imgFile = e.target.files[0];
      if (imgFile) {
        const maxSize = 10 * 1024 * 1024;
        const checkImg = imgFile.type.startsWith("image/");
        if (!checkImg) {
          alert("이미지 파일만 업로드 가능합니다");
          e.target.value = "";
          this.isValidImage = false;
          return;
        } else if (imgFile.size > maxSize) {
          alert("이미지는 10MB이하의 파일만 업로드 가능합니다");
          e.target.value = "";
          this.isValidImage = false;
          return;
        } else {
          this.file = imgFile;
          if (this.imgSrc && this.imgSrc.startsWith("blob:")) {
            URL.revokeObjectURL(this.imgSrc);
          }
          this.imgSrc = URL.createObjectURL(imgFile);
          this.isValidImage = true;
        }
      } else {
        this.file = null;
      }
      console.log(this.isValidImage);
    },
    async uploadPost() {
      if (this.codeId === 0) {
        alert("지역은 필수 선택입니다");
      } else {
        const formData = new FormData();
        const json = JSON.stringify({
          content: this.content,
          codeId: this.codeId,
        });
        const blob = new Blob([json], { type: "application/json" });
        formData.append("outfitPost", blob);
        if (this.file) {
          formData.append("file", this.file);
        }

        try {
          await axios
            .post("http://localhost:8081/board/addPost", formData)
            .then((res) => {
              console.log("포스팅 데이터 : ", res);
              alert("게시물 등록이 완료되었습니다");
              this.file = null;
              this.codeId = 0;
              this.content = "";
              location.href = "/";
            });
        } catch (error) {
          alert("서버 오류로 인한 게시물 등록 실패");
        }
      }
    },
    async getBoard() {
      this.imageList = [];
      this.nicknameList = [];
      this.profileImages = [];
      this.createdDates = [];
      console.log(
        this.selectedLocation,
        this.selectedGender,
        this.selectedAge,
        this.selectedPeriod
      );
      const genderMap = {
        남성: "male",
        여성: "female",
      };

      const params = new URLSearchParams();
      params.append("location", this.selectedLocation);
      if (this.selectedGender.length > 0) {
        this.selectedGender.forEach((gender) => {
          params.append("genders", genderMap[gender] || gender);
        });
      } else {
        params.append("genders", "");
      }
      if (this.selectedAge.length > 0) {
        params.append("ages", this.selectedAge.join(","));
      } else {
        params.append("ages", "");
      }
      if (this.selectedPeriod) {
        params.append("period", this.selectedPeriod);
      } else {
        params.append("period", "");
      }
      try {
        await axios
          .get(`http://localhost:8081/board/getBoard?${params.toString()}`)
          .then((res) => {
            console.log(res);
            if (res.data) {
              this.userData = res.data;
              for (let i = 0; i < res.data.length; i++) {
                this.imageList.push(res.data[i].imageUrl);
                this.nicknameList.push(res.data[i].nickName);
                this.profileImages.push(res.data[i].profileImg);
                this.createdDates.push(res.data[i].createdDate);
              }
            }
          });
        console.log(this.imageList);
      } catch (error) {
        console.error("서버 오류로 인한 데이터 로딩 실패");
      }
    },
    startDrag(e) {
      this.isDown = true;
      this.startX = e.pageX - this.$refs.locationList.offsetLeft;
      this.scrollLeft = this.$refs.locationList.scrollLeft;
    },
    stopDrag() {
      this.isDown = false;
    },
    onDrag(e) {
      if (!this.isDown) return;
      e.preventDefault();
      const x = e.pageX - this.$refs.locationList.offsetLeft;
      const walk = (x - this.startX) * 2;
      this.$refs.locationList.scrollLeft = this.scrollLeft - walk;
    },
    selectLocation(location) {
      this.$emit("update:selectedLocation", location);
      this.$emit("update:activeIndex", this.locations.indexOf(location));
    },
    showPostModal() {
      console.log("this 확인 : ", this);
      this.isModalOpen = true;
      console.log("모달 열기 :", this.isModalOpen);
    },
    closePostModal() {
      this.isModalOpen = false;
      this.imgSrc = require("@/images/image.png");
      this.isValidImage = false;
      this.expandedModal = false;
      this.postLocation = null;
    },
    nextModal() {
      this.expandedModal = true;
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
  },
};
