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
        <p @click="changeProfilePic">프로필 사진 변경</p>
        <p @click="changeNickname">프로필 닉네임 변경</p>
        <p @click="logout">로그아웃</p>
        <button @click="closeModal">닫기</button>
      </div>
    </div>
    <div class="profile-area">
      <img :src="imageUrl" alt="" />
      <div class="p-area">
        <p id="nickname">{{ nickname }}</p>
        <p id="numOfBoard">아웃핏 {{ boardList.length }}개</p>
      </div>
    </div>
    <div class="border-line"></div>
    <div class="board-beginning">
      <p>나의 아웃핏</p>
    </div>
    <div class="board-list" v-if="boardList">
      <div
        class="item"
        v-for="(board, index) in boardList"
        :key="index"
        @click="showBoardModal(index)"
      >
        <img class="main-img" :src="board.imageUrl" alt="" />
        <div class="item-footer">
          <div class="userInfo">
            <img class="profile-img" :src="board.profileImg" alt="Profile" />
            <p class="nickname">
              {{ board.nickName }}
            </p>
          </div>
          <p class="timestamp">{{ formattedDate(board.createdDate) }}</p>
        </div>
      </div>
    </div>
    <BoardModal
      v-if="isBoardModalOpen"
      :selectedIndex="selectedIndex"
      :userData="boardList"
      :loginUserEmail="loginUserEmail"
      @close="closeBoardModal"
    />
    <ModifyProfilePic
      v-if="isModifyPicModal"
      :originPic="imageUrl"
      @close="closeModifyPicModal"
    />
    <ModifyNickname
      v-if="isModifyNicknameModal"
      @close="closeModifyNicknameModal"
    />
  </div>
</template>

<script>
import MyPageJS from "@/js/MyPage.js";
export default MyPageJS;
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
  font-size: 30px;
  text-align: center;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.profile-area {
  display: flex;
  margin: 100px 0px 0px 80px;
}
.profile-area img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
}
.p-area {
  margin-left: 50px;
}
#nickname {
  font-size: 25px;
}
.border-line {
  margin-top: 50px;
  border-bottom: 1px solid #ccc;
}

.board-beginning {
  margin-top: 15px;
  text-align: center;
  font-size: 15px;
  font-weight: bold;
}

.board-list {
  margin: 15px auto;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  max-width: 600px;
  justify-content: center;
}
.item {
  display: flex;
  flex-direction: column;
  width: 270px;
  background: #f9f9f9;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
}
.main-img {
  width: 250px;
  height: 250px;
  object-fit: cover;
  display: block;
  border-radius: 5px;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.userInfo {
  display: flex;
  align-items: center;
}

.profile-img {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  object-fit: cover;
}
.nickname {
  font-size: 14px;
  font-weight: bold;
  margin-left: 10px;
}
.timestamp {
  font-size: 12px;
  color: grey;
}
</style>
