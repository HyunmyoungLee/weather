<template>
  <div id="input_area">
    <div id="forgetId_header">
      <h1 @click="goToMain">Weather & Outfit</h1>
    </div>

    <div id="input_box">
      <input
        type="text"
        v-model="userInfo.name"
        placeholder="이름을 입력해주세요"
      />
      <input
        type="date"
        id="birthdate"
        v-model="userInfo.birthdate"
        :max="maxDate"
      />
      <p id="errorMessage" v-if="errorMessage">{{ errorMessage }}</p>
      <button @click="checkId">아이디 찾기</button>
    </div>
    <div id="emailList" v-if="emailList != ''">
      <table class="emailTable">
        <thead>
          <tr>
            <th>검색 결과 이메일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(email, index) in formatEmailList" :key="index">
            <th>{{ email }}</th>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script>
import ForgetIdJS from "@/js/ForgetId.js";
export default ForgetIdJS;
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

#forgetId_header {
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
#errorMessage {
  color: red;
}

.emailTable {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
  font-size: 16px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.emailTable thead tr {
  background-color: black;
  color: white;
  text-align: left;
}

.emailTable tbody tr {
  border-bottom: 1px solid #dddddd;
}

.emailTable tbody tr:nth-of-type(odd) {
  background-color: #f3f3f3;
}

.emailTable tbody tr:nth-of-type(even) {
  background-color: #ffffff;
}
.emailTable tbody tr:hover {
  background-color: #f1f1f1;
}

.emailTable th,
.emailTalbe td {
  padding: 12px 15px;
  text-align: left;
}
</style>
