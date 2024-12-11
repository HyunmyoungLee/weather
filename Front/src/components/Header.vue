<template>
  <div>
    <div class="nav-bar"></div>
    <div class="search-div">
      <div class="input-group input-group-sm mb-3" style="width: 300px">
        <div class="input-wrapper">
          <input
            type="text"
            class="form-control search-input"
            aria-label="Sizing example input"
            aria-describedby="inputGroup-sizing-sm"
            v-model="searchCity"
            @input="filterCities"
            placeholder="Search city name..."
          />
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
  },
};
</script>

<style scoped>
.input-group {
  position: relative;
}
.search-div {
  /* background-color: rgb(241, 232, 232); */
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.input-wrapper {
  position: relative;
  width: 100%;
}

.search-input {
  width: 100%;
  padding: 10px 40px 10px 20px;
  border: 2px solid #ccc;
  border-radius: 30px;
  font-size: 16px;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.search-input:focus {
  border-color: #007bff;
  box-shadow: 0 0 8px rgba(0, 123, 255, 0.3);
  outline: none;
}

.city-list {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  max-height: 200px;
  overflow-y: auto;

  z-index: 10;
}

.city-list .list-group-item {
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.city-list .list-group-item:hover {
  background-color: #f1f1f1; /* 호버 시 배경색 변경 */
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
