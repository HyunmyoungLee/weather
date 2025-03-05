<template>
  <div class="modal">
    <span class="close" @click="$emit('close')">&times;</span>
    <div class="modal-content">
      <div
        class="post-image"
        :style="{ backgroundImage: `url(${userData[selectedIndex].imageUrl})` }"
      ></div>
      <div class="caption-area">
        <div class="userInfo-area">
          <img
            id="profileImg"
            :src="userData[selectedIndex].profileImg"
            alt="profileImage"
          />
          <p id="user-nickname">{{ userData[selectedIndex].nickName }}</p>
          <p id="user-age">
            {{ getUserAge(userData[selectedIndex].birthdate) }}세
          </p>
          <p id="user-gender">
            {{ userData[selectedIndex].gender == "male" ? "남" : "여" }}
          </p>
          <img
            v-if="isOwnedPost"
            id="option-img"
            src="@/images/option.png"
            alt="option"
            @click="showOptionModal"
          />
        </div>
        <div class="content-area">
          <p>
            {{
              userData[selectedIndex].content === ""
                ? "등록된 내용이 없습니다"
                : userData[selectedIndex].content
            }}
          </p>
        </div>
        <div class="like-area">
          <img
            :src="
              isLiked
                ? require('@/images/heart-filled.png')
                : require('@/images/heart.png')
            "
            @click="likePost"
            alt=""
            srcset=""
          />
          <p id="numoflike">
            {{ userData[selectedIndex].numOfLike }}명이 이 게시물을 좋아합니다
          </p>
          <p id="createdDate">
            {{ formattedCreatedDate(userData[selectedIndex].createdDate) }}
          </p>
        </div>
      </div>
    </div>
    <div v-if="isShowOptionModal" class="option-modal" @click="closeModal">
      <div class="option-modal-content" @click.stop>
        <p @click="deleteBoard">게시물 삭제</p>
        <button @click="closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>
<script>
import BoardModalJS from "@/js/BoardModal.js";
export default BoardModalJS;
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
  width: 900px;
  height: 600px;
  background: white;
  border-radius: 10px;
  overflow: hidden;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  position: relative;
  transition: width 0.3s ease-in-out;
}

.post-image {
  flex: 1.3;
  width: 100%;
  height: 100%;
  background-size: contain;
  background-position: center;
  background-repeat: no-repeat;
  background-color: black;
}

.caption-area {
  flex: 1;
  margin-top: 20px;
  margin-left: 10px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  position: relative;
  height: 100%;
}
.userInfo-area {
  width: 100%;
  display: flex;

  margin-bottom: 10px;
  border-bottom: 1px solid #ccc;
  box-sizing: border-box;
}
#profileImg {
  width: 28px;
  height: 28px;
  border-radius: 50%;
}
#user-nickname {
  margin-left: 15px;
  font-weight: bold;
  font-size: 15px;
}

#user-age {
  margin-top: 5px;
  margin-left: 10px;
  font-size: 13px;
}

#user-gender {
  margin-top: 5px;
  margin-left: 4px;
  font-size: 13px;
}

#option-img {
  width: 28px;
  height: 28px;
  margin-left: auto;
  margin-right: 10px;
}

.content-area {
  flex: 1;
  overflow-y: auto;
  border-bottom: 1px solid #ccc;
  box-sizing: border-box;
}
.like-area {
  margin-top: auto;
  align-items: center;
}

.like-area img {
  margin-top: 10px;
  width: 25px;
  height: 25px;
}

#numoflike {
  margin-top: 5px;
  font-weight: bold;
}
#createdDate {
  margin-top: -15px;
  font-size: 12px;
}
.close {
  position: absolute;
  top: 10px;
  right: 15px;
  font-size: 50px;
  cursor: pointer;
  color: white;
}

.option-modal {
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
.option-modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  width: 300px;
}
</style>
