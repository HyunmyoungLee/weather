<template>
  <div>
    <div class="input-group input-group-sm mb-3">
      <div class="input-wrapper">
        <img
          v-if="!isLoginSuccess"
          src="../images/login.png"
          alt=""
          class="loginImage"
          @click="enterLoginPage"
        />

        <img
          v-if="isLoginSuccess"
          :src="imageUrl"
          alt=""
          class="userImage"
          @click="enterMyPage"
        />
        <p class="input-title">Weather</p>
        <img
          v-if="showSearchImg"
          src="../images/search.png"
          alt=""
          class="searchImage"
          @click="showSearchBar"
        />

        <transition
          name="slide"
          @before-enter="beforeEnter"
          @enter="enter"
          @leave="leave"
        >
          <input
            v-show="showSearch"
            type="text"
            class="form-control search-input"
            aria-label="Sizing example input"
            aria-describedby="inputGroup-sizing-sm"
            v-model="searchCity"
            @input="filterCities"
            placeholder="Search"
          />
        </transition>
      </div>
      <div class="city-list">
        <ul v-if="filteredCities.length" class="list-group">
          <li
            class="list-group-item"
            v-for="(city, index) in filteredCities"
            :key="index"
            @click="selectCity(city)"
          >
            {{ city.name }}
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import HeaderJS from "@/js/Header.js";
export default HeaderJS;
</script>

<style scoped>
.input-wrapper {
  margin: 20px 10px 20px 10px;
  position: relative;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.loginImage,
.userImage {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: auto;
  cursor: pointer;
  object-fit: contain;
}

.loginImage:hover,
.userImage:hover {
  cursor: pointer;
}

.searchImage {
  position: absolute;
  width: 25px;
  height: 25px;
  right: 10px;
  cursor: pointer;
}

.input-title {
  margin: 0;
  font-size: 35px;
  text-align: center;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}
.search-input {
  width: 180px;
  border: 2px solid #ccc;
  border-radius: 30px;
  font-size: 16px;
  margin-left: auto;
}

.search-input:focus {
  border-color: #007bff;
  box-shadow: 0 0 8px rgba(0, 123, 255, 0.3);
  outline: none;
}

.city-list {
  position: absolute;
  top: 100%;
  right: 0;
  width: 180px;
  max-height: 200px;
  overflow-y: auto;
  z-index: 10;
}

.city-list .list-group-item {
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.city-list .list-group-item:hover {
  background-color: #f1f1f1;
}

::-webkit-scrollbar {
  width: 10px;
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(90deg, #ff7e5f, #feb47b);
  border-radius: 10px;
}

::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(90deg, #feb47b, #ff7e5f);
}

::-webkit-scrollbar-track {
  background: #f4f4f4;
}
</style>
