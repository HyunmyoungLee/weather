<template>
  <div id="input_area">
    <div id="forgetPwd_header">
      <h1 @click="goToMain">Weather & Outfit</h1>
    </div>

    <div id="input_box">
      <input
        type="text"
        v-model="userInfo.email"
        @blur="checkEmail"
        placeholder="이메일을 입력해주세요"
        :readonly="isEmailReadonly"
      />
      <p id="errorMsg">{{ errorMessage }}</p>
      <button v-if="showAuthCode" @click="sendAuthCode">인증번호 요청</button>
      <input
        type="text"
        v-if="showAuthInput"
        v-model="userInfo.authNumber"
        placeholder="인증번호 입력"
      />
      <button v-if="showAuthInput" @click="checkAuthCode">인증번호 확인</button>
      <input
        v-if="setNewPwdInput"
        type="password"
        v-model="pwd1"
        placeholder="새 비밀번호(영문,숫자,특수기호 포함 (8자-16자 사이))"
      />
      <input
        v-if="setNewPwdInput"
        type="password"
        v-model="pwd2"
        placeholder="비밀번호 재입력"
      />
      <button v-if="setNewPwdInput" @click="setNewPassword">
        비밀번호 변경
      </button>
    </div>
  </div>
</template>
<script>
import ForgetPwdJS from "@/js/ForgetPwd.js";
export default ForgetPwdJS;
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

#input_area {
  width: 330px;
  height: 1000px;
  margin: 0 auto;
  position: relative;
}

#forgetPwd_header {
  width: 100%;
  height: 20px;
  padding: 76px 0 60px;
  text-align: center;
  margin-bottom: 20px;
}

#input_box {
  width: 100%;
  margin: 0 auto;
}

#input_box input {
  width: 100%;
  height: 50px;
  padding: 15px 10px 14px;
  box-sizing: border-box;
  border: 1px solid #cccccc;
  background-color: #ffffff;
  margin-bottom: 10px;
}
#errorMsg {
  color: red;
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
  margin-bottom: 10px;
}
</style>
