<template>
  <div class="modal">
    <span class="close" @click="$emit('close')">&times;</span>
    <div class="modal-content">
      <div id="modal_header">
        <p class="title">프로필 사진 변경</p>
        <p @click="changeProfilePic" class="change">변경</p>
      </div>
      <div id="preview">
        <img :src="imgSrc" alt="" />
      </div>
      <input
        @change="uploadImgFile"
        type="file"
        ref="fileInput"
        style="display: none"
      />
      <button @click="showFileUpload" v-if="!isValidImage">
        프로필 이미지 선택
      </button>
    </div>
  </div>
</template>
<script>
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
</script>

<style scoped>
.modal {
  display: flex;
  position: fixed;
  margin: 0 auto;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  justify-content: center;
  align-items: center;
}

.modal-content {
  width: 600px;
  height: 500px;
  background: white;
  border-radius: 10px;
  overflow: hidden;
  padding: 20px;
  padding-top: 70px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  transition: width 0.3s ease-in-out;
}

#modal_header {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  box-sizing: border-box;
  border-bottom: 1px solid #ccc;
}
.title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  font-weight: bold;
}
.change {
  position: absolute;
  right: 20px;
  cursor: pointer;
  color: #3498db;
  font-weight: bold;
}

#preview {
  margin: 0 auto;
  margin-top: 10px;
  overflow: hidden;
}

#preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.modal-content button {
  margin-top: 15px;
  padding: 15px;
  background-color: #007bfc;
  padding: 6px 12px 6px 8px;
  color: white;
  border-radius: 5px;
}
.close {
  position: absolute;
  top: 10px;
  right: 15px;
  font-size: 50px;
  cursor: pointer;
  color: white;
}
</style>
