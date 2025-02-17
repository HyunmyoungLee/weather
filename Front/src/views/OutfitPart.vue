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
          <p v-if="isValidImage" @click="nextModal" class="share">공유하기</p>
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
        <div
          v-for="(category, index) in categories"
          :key="index"
          class="category-item"
        >
          <input
            type="checkbox"
            :value="category"
            v-model="selectedCategories"
          />
          <label>{{ category }}</label>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { categories } from "@/js/category.js";
import { mapState } from "vuex";
export default {
  name: "OutfitPart",
  data() {
    return {
      selectedCategories: [],
      categories,
      isValidImage: false,
      file: null,
      imgSrc: require("@/images/image.png"),
      isModalOpen: false,
      expandedModal: false,
      content: "",
    };
  },
  props: {
    outfitPart: Boolean,
    locations: Array,
    selectedLocation: String,
    activeIndex: Number,
  },
  computed: {
    ...mapState(["imageUrl", "nickname"]),
  },
  methods: {
    showFileUpload() {
      this.$refs.fileInput.click();
    },
    uploadImgFile(e) {
      const imgFile = e.target.files[0];
      if (imgFile) {
        const maxSize = 10 * 1024 * 1024;
        const checkImg = imgFile.type.startsWith("image/");
        if (!checkImg) {
          alert("이미지 파일만 업로드 가능합니다");
          e.target.value = "";
          this.isValidImage = false;
          return;
        } else if (imgFile.size > maxSize) {
          alert("이미지는 10MB이하의 파일만 업로드 가능합니다");
          e.target.value = "";
          this.isValidImage = false;
          return;
        } else {
          this.file = imgFile;
          if (this.imgSrc && this.imgSrc.startsWith("blob:")) {
            URL.revokeObjectURL(this.imgSrc);
          }
          this.imgSrc = URL.createObjectURL(imgFile);
          this.isValidImage = true;
        }
      } else {
        this.file = null;
      }
      console.log(this.isValidImage);
    },
    startDrag(e) {
      this.isDown = true;
      this.startX = e.pageX - this.$refs.locationList.offsetLeft;
      this.scrollLeft = this.$refs.locationList.scrollLeft;
    },
    stopDrag() {
      this.isDown = false;
    },
    onDrag(e) {
      if (!this.isDown) return;
      e.preventDefault();
      const x = e.pageX - this.$refs.locationList.offsetLeft;
      const walk = (x - this.startX) * 2;
      this.$refs.locationList.scrollLeft = this.scrollLeft - walk;
    },
    selectLocation(location) {
      this.$emit("update:selectedLocation", location);
      this.$emit("update:activeIndex", this.locations.indexOf(location));
    },
    showPostModal() {
      this.isModalOpen = true;
    },
    closePostModal() {
      this.isModalOpen = false;
      this.imgSrc = require("@/images/image.png");
      this.isValidImage = false;
      this.expandedModal = false;
    },
    nextModal() {
      this.expandedModal = true;
    },
  },
};
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
}

.category-item {
  display: flex;
  align-items: center;
  gap: 5px;
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
  flex-direction: row; /* 사진과 설명란을 가로로 배치 */
  justify-content: space-between; /* 사진과 설명란 사이 공간 확보 */
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
</style>
