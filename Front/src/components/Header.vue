<template>
  <div>
    <div class="input-group input-group-sm mb-3">
      <div class="input-wrapper">
        <img src="../images/menu.png" alt="" class="menuImage" />
        <p class="input-title">Weather & Outfit</p>
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
            placeholder="Search city name..."
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
import cities from "../json/korean_cities.json";
export default {
  name: "AppHeader",
  data() {
    return {
      searchCity: "",
      cities: cities,
      filteredCities: [],
      showSearch: false,
      showSearchImg: true,
    };
  },
  methods: {
    filterCities() {
      if (!this.searchCity) {
        this.filteredCities = [];
        return;
      }

      const search = this.searchCity.toLowerCase();
      this.filteredCities = this.cities.filter((city) =>
        city.name.toLowerCase().includes(search)
      );
    },
    selectCity(city) {
      this.searchCity = city.name;
      this.filteredCities = [];
      this.$emit("selectedCity", city);
    },
    showSearchBar() {
      console.log("search Bar toggled");
      this.showSearch = true;
      this.showSearchImg = false;
    },
    //애니메이션 시작하기 전
    beforeEnter(el) {
      el.style.transform = "translateX(100%)"; //오른쪽 외부에 있도록 설정
      el.style.opacity = 0; //투명하게 만듬
    },
    //화면에 나타나기 시작하는 시점
    enter(el, done) {
      el.offsetHeight; // 애니메이션 실행
      el.style.transition =
        "transform 0.3s ease-in-out, opacity 0.3s ease-in-out";
      el.style.transform = "translateX(0)";
      el.style.opacity = 1; //불투명하게 만듬
      done(); // 완료
    },
    leave(el, done) {
      el.style.transition =
        "transform 0.3s ease-in-out, opacity 0.3s ease-in-out";
      el.style.transform = "translateX(100%)";
      el.style.opacity = 0;
      done();
    },
  },
};
</script>

<style scoped>
.input-group {
  position: relative;
}

.input-wrapper {
  margin: 20px 10px 20px 10px;
  position: relative;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
}

.menuImage {
  width: 25px;
  height: 25px;
  margin-right: auto;
  cursor: pointer;
}

.searchImage {
  width: 25px;
  height: 25px;
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
.search-input {
  width: 250px;
  padding: 10px 40px 10px 20px;
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
  width: 250px;
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
