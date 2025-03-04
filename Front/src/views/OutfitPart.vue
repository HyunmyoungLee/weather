<template>
  <div id="outfit-part" v-if="outfitPart">
    <p class="outfit-title">O U T F I T</p>
    <div id="postBtn">
      <a @click="showPostModal"
        ><img src="@/images/t-shirt.png" /><span>나의 아웃핏 포스트</span></a
      >
    </div>
    <div v-if="isModalOpen" class="modal" :class="{ expanded: expandedModal }">
      <span class="close" @click="closePostModal">&times;</span>
      <div v-if="!expandedModal" class="modal-content">
        <div id="modal_header">
          <p class="title">새 게시물 만들기</p>
          <p v-if="isValidImage" @click="nextModal" class="next">다음</p>
        </div>
        <div id="preview">
          <img :src="imgSrc" alt="" />
        </div>
        <input
          @change="uploadImgFile"
          type="file"
          ref="fileInput"
          style="display: none"
        />
        <button @click="showFileUpload" v-if="!isValidImage">
          이미지 추가하기
        </button>
      </div>
      <div v-if="expandedModal" class="modal-content expanded-content">
        <div id="modal_header">
          <p class="title">새 게시물 만들기</p>
          <p v-if="isValidImage" @click="uploadPost" class="share">공유하기</p>
        </div>
        <div class="image-preview">
          <img v-if="isValidImage" :src="imgSrc" alt="preview" />
        </div>
        <div class="caption-area">
          <div class="userInfo-area">
            <img id="profileImg" :src="imageUrl" alt="" />
            <p id="user-nickname">{{ nickname }}</p>
          </div>
          <textarea
            v-model="content"
            placeholder="설명을 입력하세요"
          ></textarea>
          <div id="postLocation-part">
            <img src="@/images/pin.png" alt="location" />
            <select v-model="codeId">
              <option
                v-for="(location, index) in locations"
                :key="index"
                :value="index + 1"
              >
                {{ location }}
              </option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <div id="location-part">
      <ul
        id="location-list"
        ref="locationList"
        @mousedown="startDrag"
        @mouseleave="stopDrag"
        @mouseup="stopDrag"
        @mousemove="onDrag"
      >
        <li
          v-for="(location, index) in locations"
          :key="index"
          @click="selectLocation(location)"
          :class="{
            active: selectedLocation === location || activeIndex === index,
          }"
        >
          <a :class="{ active: activeIndex === index }">{{ location }}</a>
        </li>
      </ul>
      <div id="category-part">
        <div id="category-list">
          <div class="category-item">
            <label v-for="gender in genders" :key="gender">
              <input
                type="checkbox"
                :value="gender"
                v-model="selectedGender"
                @change="getBoard"
              />
              {{ gender }}
            </label>
            <label v-for="age in ages" :key="age">
              <input
                type="checkbox"
                :value="age"
                v-model="selectedAge"
                @change="getBoard"
              />
              {{ age }}
            </label>
            <label v-for="period in periods" :key="period">
              <input
                type="radio"
                :value="period"
                name="period"
                v-model="selectedPeriod"
                @change="getBoard"
              />
              {{ period }}
            </label>
          </div>
        </div>
      </div>
    </div>
    <div class="orderMethods">
      <a @click="sortBy('recommand')">추천순</a>
      <a @click="sortBy('newest')">최신순</a>
    </div>
    <div
      class="image-list"
      v-if="imageList && nicknameList && profileImages && createdDates"
    >
      <div
        class="item"
        v-for="(user, index) in userData"
        :key="index"
        @click="showBoardModal(index)"
      >
        <img class="main-img" :src="user.imageUrl" alt="" />
        <div class="item-footer">
          <div class="userInfo">
            <img class="profile-img" :src="user.profileImg" alt="Profile" />
            <p class="nickname">
              {{ user.nickName }}
            </p>
          </div>
          <p class="timestamp">{{ formattedDate(user.createdDate) }}</p>
        </div>
      </div>
    </div>
    <BoardModal
      v-if="isBoardModalOpen"
      :selectedIndex="selectedIndex"
      :userData="userData"
      :loginUserEmail="loginUserEmail"
      @close="closeBoardModal"
    />
  </div>
</template>
<script>
import OutfitPartJs from "@/js/OutfitPart.js";
export default OutfitPartJs;
</script>

<style scoped>
ul {
  list-style: none;
}
.outfit-title {
  font-size: 30px;
  text-align: center;
}

#location-part {
  margin: 15px 5px;
  background-color: rgb(255, 255, 255);
  border-radius: 5px;
  box-sizing: border-box;
  border: 1px solid rgb(224, 224, 224);
}

#location-list {
  margin: 10px 0;
  display: flex;
  width: 100%;
  height: 25px;
  overflow-x: auto;
  padding: 0px 10px;
  cursor: grab;
}
#location-list:active {
  cursor: grabbing;
}

#location-list > li {
  flex: 0 0 auto;
  margin-right: 10px;
  padding: 4px 12px 4px 8px;
}

#location-list > li > a {
  color: rgb(0 0 0 / var(--tw-text-opacity, 1));
  font-size: 16px;
  font-weight: lighter;
  text-decoration: none;
  transition: font-weight 0.2s ease-in-out;
}
#location-list > li > a.active {
  font-weight: bold;
  color: #333;
}

#location-list::-webkit-scrollbar,
#category-list::-webkit-scrollbar {
  display: none;
}

#category-part {
  margin-left: 10px;
  width: 100%;
}

#category-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  padding: 10px 0;
  width: 100%;
}

.category-item {
  display: flex;
  align-items: center;
  overflow: hidden;
  flex-wrap: wrap;
  width: 100%;
  gap: 15px;
  flex: 0 0 auto;
  color: rgb(0 0 0 / var(--tw-text-opacity, 1));
  font-size: 16px;
}

#postBtn {
  display: flex;
  margin: 0px 5px;
}

#postBtn a {
  margin-left: auto;
  background-color: rgb(255, 255, 255);
  align-items: center;
  justify-content: center;
  padding: 6px 12px 6px 8px;
  border-radius: 5px;
  box-sizing: border-box;
  border: 1px solid rgb(224, 224, 224);
  text-decoration: none;
}

#postBtn a:hover {
  cursor: pointer;
}
#postBtn span {
  margin-left: 5px;
  color: rgb(0 0 0 / var(--tw-text-opacity, 1));
}

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

.expanded-content {
  width: 700px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
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
.next,
.share {
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

.image-preview {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f8f8f8;
}

.image-preview img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.caption-area {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
}
.userInfo-area {
  display: flex;
}

#profileImg {
  width: 28px;
  height: 28px;
  border-radius: 5px;
}
#user-nickname {
  margin-left: 10px;
  font-weight: bold;
  font-size: 15px;
}
.caption-area textarea {
  width: 100%;
  height: 250px;
  border: none;
  outline: none;
  resize: none;
  font-size: 16px;
  padding: 10px;
  border-radius: 5px;
  background: #f5f5f5;
}
#postLocation-part {
  display: flex;
  margin-top: 10px;
}
#postLocation-part img {
  width: 28px;
  height: 28px;
}
#postLocation-part select {
  width: 100%;
  height: 30px;
  border: 2px solid #ccc;
  border-radius: 8px;
  box-sizing: border-box;
  padding: 2px 5px 2px 5px;
  border: 1px solid #cccccc;
  background-color: #ffffff;
  margin-bottom: 10px;
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  cursor: pointer;
  transition: border-color 0.3s ease-in-out;
}
#postLocation-part select:focus {
  border-color: #007bfc;
  outline: none;
  box-shadow: 0 0 5px rgba(0, 123, 252, 0.5);
}
.orderMethods {
  display: flex;
  gap: 10px;
  margin: 0px 5px;
  justify-content: flex-end;
}
.orderMethods a:hover {
  cursor: pointer;
  font-weight: bold;
}

.image-list {
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
