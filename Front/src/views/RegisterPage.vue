<template>
  <div id="register_area">
    <div id="register_header">
      <h1 @click="goToMain">Weather & Outfit</h1>
    </div>

    <div id="register_box">
      <input type="text" placeholder="이메일을 입력해주세요" />
      <br />
      <input
        type="password"
        placeholder="비밀번호(영문,숫자,특수기호 포함 8-20자)"
      />
      <br />
      <input type="password" placeholder="비밀번호 재입력" />
      <br />
      <input type="text" placeholder="이름" />
      <br />
      <input type="text" id="postcode" placeholder="우편번호" />
      <br />
      <input type="button" @click="execDaumPostcode()" value="우편번호 찾기" />
      <br />
      <input type="text" id="roadAddress" placeholder="도로명 주소" />
      <br />
      <span class="submit"><button>회원가입</button></span>
    </div>
  </div>
</template>
<script>
import axios from "axios";

export default {
  data() {
    return {
      isPostCodeLoaded: false,
    };
  },
  created() {
    this.checkLogin();
  },
  mounted() {
    const script = document.createElement("script");
    script.src =
      "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
    script.async = true;
    script.onload = () => {
      this.isPostCodeLoaded = true;
      console.log("daum postcode script loaded successfully");
    };
    script.onerror = () => {
      console.log("Failed to load Daum postcode script");
    };
    document.head.appendChild(script);
  },
  methods: {
    checkLogin() {
      axios
        .get("http://localhost:8081/user/status")
        .then((response) => {
          if (response.data.name != null) {
            this.$router.push({ name: "Main" });
          }
        })
        .catch((err) => {
          if (err.status == 401) {
            console.log("로그인 정보가 없습니다");
          }
        });
    },
    goToMain() {
      this.$router.push({ name: "Main" });
    },
    execDaumPostcode() {
      if (this.isPostCodeLoaded && window.daum && window.daum.Postcode) {
        // eslint-disable-next-line no-undef
        new daum.Postcode({
          oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            var extraRoadAddr = ""; // 참고 항목 변수

            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
              extraRoadAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if (data.buildingName !== "" && data.apartment === "Y") {
              extraRoadAddr +=
                extraRoadAddr !== ""
                  ? ", " + data.buildingName
                  : data.buildingName;
            }
            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if (extraRoadAddr !== "") {
              extraRoadAddr = " (" + extraRoadAddr + ")";
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById("postcode").value = data.zonecode;
            document.getElementById("roadAddress").value = roadAddr;
          },
        }).open();
      } else {
        console.error("Daum Postcode API is not loaded.");
      }
    },
  },
};
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

#register_box input {
  width: 100%;
  height: 50px;
  padding: 15px 10px 14px;
  box-sizing: border-box;
  border: 1px solid #cccccc;
  background-color: #ffffff;
  margin-bottom: 10px;
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
