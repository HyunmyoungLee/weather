<template>
  <div id="register_area">
    <div id="register_header">
      <h1 @click="goToMain">Weather & Outfit</h1>
    </div>

    <div id="register_box">
      <input
        type="email"
        v-model="userInfo.email"
        @blur="checkDuplicateEmail"
        placeholder="이메일을 입력해주세요"
        :readonly="isEmailReadOnly"
        :disabled="isEmailReadOnly"
      />
      <br />
      <button id="authButton" v-if="showAuthCode" @click="sendAuthCode()">
        인증번호 요청
      </button>
      <input
        type="text"
        v-if="showAuthInput"
        v-model="userInfo.authNumber"
        placeholder="인증번호"
      />
      <p id="timer" v-if="timeCount > 0 && showAuthInput">{{ formatTime }}</p>
      <p v-if="timeCount === 0 && showAuthInput">
        인증번호가 만료되었습니다 다시 인증요청해주세요.
      </p>
      <button
        id="checkAuthButton"
        v-if="showAuthInput && timeCount > 0"
        @click="checkAuthCode"
      >
        인증번호 확인
      </button>
      <button
        id="authButton"
        v-if="timeCount === 0 && showAuthInput"
        @click="sendAuthCode()"
      >
        인증번호 재요청
      </button>
      <p id="authErrorMessage" v-if="authErrorMessage">
        {{ authErrorMessage }}
      </p>
      <input
        type="password"
        v-model="userInfo.password"
        placeholder="비밀번호(영문,숫자,특수기호 포함 8-20자)"
      />
      <br />

      <input type="text" v-model="userInfo.name" placeholder="이름" />
      <br />
      <input
        type="date"
        id="birthdate"
        v-model="userInfo.birthdate"
        :max="maxDate"
      />
      <br />
      <select id="gender" v-model="userInfo.gender">
        <option value="">성별</option>
        <option value="male">남성</option>
        <option value="femaie">여성</option>
      </select>
      <input type="text" id="postcode" placeholder="우편번호" />
      <br />
      <input type="button" @click="execDaumPostcode()" value="우편번호 찾기" />
      <br />
      <input
        type="text"
        id="roadAddress"
        v-model="userInfo.address"
        placeholder="도로명 주소"
      />
      <br />
      <p id="errorMsg" v-if="errorMessage">{{ errorMessage }}</p>
      <span class="submit"
        ><button @click="registerUser">회원가입</button></span
      >
    </div>
  </div>
</template>
<script>
import RegisterPageJS from "@/js/RegisterPage.js";
export default RegisterPageJS;
</script>
<style scoped>
* {
  margin: 0;
  padding: 0;
}

#register_area {
  width: 330px;
  height: 1000px;
  margin: 0 auto;
  position: relative;
}

#register_header {
  width: 100%;
  height: 20px;
  padding: 76px 0 60px;
  text-align: center;
  margin-bottom: 20px;
}

#register_header > h1:hover {
  cursor: pointer;
}

#register_box {
  width: 100%;
  margin: 0 auto;
}

#register_box input,
select {
  width: 100%;
  height: 50px;
  padding: 15px 10px 14px;
  box-sizing: border-box;
  border: 1px solid #cccccc;
  background-color: #ffffff;
  margin-bottom: 10px;
}
#authButton,
#checkAuthButton {
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
#errorMsg,
#authErrorMessage {
  color: red;
}
.submit {
  display: block;
  margin: 10px 0 20px;
}
.submit > button {
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
