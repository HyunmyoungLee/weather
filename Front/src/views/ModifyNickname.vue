<template>
  <div class="modal">
    <span class="close" @click="$emit('close')">&times;</span>
    <div class="modal-content">
      <div id="modal_header">
        <p class="title">프로필 닉네임 변경</p>
      </div>

      <div id="profile_box">
        <input
          class="input"
          type="text"
          v-model="nickname"
          @blur="checkValidateNickname"
          placeholder="사용하실 유저 닉네임을 입력해 주세요"
        />
        <p id="error" v-if="errorMessage">{{ errorMessage }}</p>
        <br />
        <span class="submit"
          ><button @click.prevent="modifyNickname" :disabled="!isValidNickname">
            프로필 닉네임 변경
          </button></span
        >
      </div>
    </div>
  </div>
</template>
<script>
import ModifyNicknameJS from "@/js/ModifyNickname.js";
export default ModifyNicknameJS;
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
#error {
  color: red;
}

.submit {
  display: block;
  margin: 10px 0 20px;
}
</style>
