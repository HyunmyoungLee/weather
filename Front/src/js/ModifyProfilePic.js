import axios from "axios";
export default {
  data() {
    return {
      isValidImage: false,
      file: null,
      imgSrc: this.originPic,
    };
  },
  props: {
    originPic: String,
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
    },
    async changeProfilePic() {
      if (!this.file) {
        alert("이미지를 선택하세요");
        return;
      }
      const formdata = new FormData();
      formdata.append("file", this.file);

      try {
        await axios
          .put("http://localhost:8081/user/modifyProfilePic", formdata)
          .then((res) => {
            alert(res.data);
            location.href = "/mypage";
          });
      } catch (error) {
        console.error(error);
      }
    },
  },
};
