<template>
  <div id="profile_area">
    <div id="profile_header">
      <h1>Weather & Outfit</h1>
    </div>

    <div id="profile_box">
      <input
        class="input"
        type="text"
        v-model="nickname"
        @blur="checkValidateNickname"
        placeholder="사용하실 유저 닉네임을 입력해 주세요"
      />

      <button
        v-if="isValidNickname && !isDefaultImg"
        @click="showFileInput = !showFileInput"
      >
        프로필 사진 설정
      </button>
      <br />
      <button v-if="isValidNickname && !isDefaultImg" @click="setDefaultImg">
        프로필 사진 기본 이미지로 설정
      </button>

      <div id="preview" v-if="showFileInput && !isDefaultImg">
        <img :src="imgSrc" />
      </div>
      <div v-if="showFileInput && !isDefaultImg" id="file-area">
        <label for="file-upload" class="custom-file-upload"> 사진 첨부 </label>
        <input @change="uploadImgFile" type="file" id="file-upload" />
        <span id="file-name">{{ fileName }}</span>
      </div>
      <p id="error" v-if="errorMessage">{{ errorMessage }}</p>
      <br />
      <span class="submit"
        ><button
          @click.prevent="addProfile"
          :disabled="!isValidNickname || !isValidImage"
        >
          프로필 등록 또는 변경
        </button></span
      >
    </div>
  </div>
</template>

<script>
import axios from "axios";
axios.defaults.withCredentials = true;
import { mapActions } from "vuex";
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
    console.log(
      "현재 프로필 상태",
      this.$store.state.nickname,
      this.$store.state.imageUrl
    );
  },
  computed: {},
  methods: {
    checkLogin() {
      axios.get("http://localhost:8081/user/status").catch((err) => {
        if (err.status == 401) {
          console.log("로그인 정보가 없습니다");
          this.$router.push({ name: "Main" });
        }
      });
    },
    ...mapActions(["fetchUser"]),
    async checkValidateNickname() {
      await axios
        .get("http://localhost:8081/user/checkNickname", {
          params: {
            nickname: this.nickname,
          },
        })
        .then((response) => {
          console.log(response);
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
      console.log(this.nickname, this.file);
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
          console.log(res);
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
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

#profile_area {
  width: 330px;
  height: 1000px;
  margin: 0 auto;
  position: relative;
}

#profile_header {
  width: 100%;
  height: 20px;
  padding: 76px 0 60px;
  text-align: center;
  margin-bottom: 20px;
}

#profile_header > h1:hover {
  cursor: pointer;
}
#profile_box {
  width: 100%;
  margin: 0 auto;
}
#profile_box .input {
  width: 100%;
  height: 50px;
  padding: 15px 10px 14px;
  box-sizing: border-box;
  border: 1px solid #cccccc;
  background-color: #ffffff;
  margin-bottom: 10px;
}
#preview {
  margin: 0 auto;
  margin-top: 10px;
  width: 100px;
  height: 100px;
  border: 1px solid #c9c3c3;
  border-radius: 50%;
  overflow: hidden;
}

#preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

#file-area {
  margin: 0 auto;
}
.custom-file-upload {
  display: inline-block;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  font-size: 14px;
  font-weight: bold;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.custom-file-upload:hover {
  background-color: #0056b3;
}

#file-upload {
  display: none;
}

#file-name {
  margin-left: 10px;
  font-size: 14px;
  color: #333;
}
#error {
  color: red;
}

.submit {
  display: block;
  margin: 10px 0 20px;
}
button {
  width: 100%;
  height: 60px;
  color: #ffffff;
  background-color: #000000;
  text-align: center;
  border: none;
  display: block;
  font-size: 18px;
  font-weight: bold;
}
</style>
