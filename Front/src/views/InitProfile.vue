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
import InitProfileJS from "@/js/InitProfile.js";
export default InitProfileJS;
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
