<template>
  <div>
    <div class="input-group input-group-sm mb-3">
      <div class="input-wrapper">
        <img
          src="../images/home.png"
          alt=""
          class="homeImage"
          @click="enterMainPage"
        />

        <p class="input-title">Weather & Outfit</p>
        <img
          src="../images/settings.png"
          alt=""
          class="settingImage"
          @click="showSettingModal"
        />
      </div>
    </div>
    <div v-if="isShowModal" class="modal" @click="closeModal">
      <div class="modal-content" @click.stop>
        <p @click="logout">로그아웃</p>
        <button @click="closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      isShowModal: false,
    };
  },
  created() {
    this.checkLogin();
  },
  computed: {
    ...mapGetters(["isLoginSuccess"]),
  },
  methods: {
    checkLogin() {
      axios.get("http://localhost:8081/user/status").catch((err) => {
        if (err.status == 401) {
          console.log("로그인 정보가 없습니다");
          this.$router.push({ name: "Main" });
        }
      });
    },
    enterMainPage() {
      this.$router.push({ name: "Main" });
    },
    showSettingModal() {
      this.isShowModal = true;
    },
    closeModal() {
      this.isShowModal = false;
    },
    async logout() {
      try {
        await axios.post("http://localhost:8081/user/logout").then((res) => {
          console.log(res);
          this.$store.commit("setLoginSuccess", null);
          alert("로그아웃 되었습니다.");
          this.$router.push({ name: "Main" });
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
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  width: 300px;
}
.input-wrapper {
  margin: 20px 10px 20px 10px;
  position: relative;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.homeImage {
  width: 25px;
  height: 25px;
  margin-right: auto;
  cursor: pointer;
}

.settingImage {
  position: absolute;
  width: 25px;
  height: 25px;
  right: 10px;
  cursor: pointer;
}

.input-title {
  margin: 0;
  font-size: 25px;
  font-weight: bold;
  text-align: center;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}
</style>
