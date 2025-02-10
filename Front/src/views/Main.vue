<template>
  <div>
    <Header @selectedCity="fetchWeather" />
    <div class="currentWeatherInfo" v-if="weatherInfo">
      <div>
        <p class="curTime">{{ getCurrentTime() }}</p>
        <p class="city">{{ weatherInfo.name }}</p>
        <p class="temperature">{{ weatherInfo.main.temp.toFixed(1) }}°C</p>
        <p class="description">{{ weatherInfo.weather[0].description }}</p>
      </div>
      <div>
        <img
          :src="`https://openweathermap.org/img/wn/${weatherInfo.weather[0].icon}@2x.png`"
          alt=""
        />
      </div>
    </div>
    <div class="forecast-group" v-if="forecastInfo">
      <ul class="forecast-list">
        <li
          class="forecast-item"
          v-for="(day, index) in forecastInfo"
          :key="index"
        >
          <p>{{ formatDate(day.dt_txt) }}</p>
          <p>{{ formatTime(day.dt_txt) }}</p>
          <p>{{ day.main.temp.toFixed(1) }}°C</p>
          <img
            :src="`https://openweathermap.org/img/wn/${day.weather[0].icon}@2x.png`"
          />
        </li>
      </ul>
    </div>
    <div id="outfit-part" v-if="outfitPart">
      <h3>Outfit</h3>
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
      </div>
      <div id="category-part"></div>
    </div>
  </div>
</template>
<script>
import Header from "./Header.vue";
import axios from "axios";
import { locations, locationMapping } from "@/js/location_mapping.js";

export default {
  name: "AppMain",
  components: {
    Header,
  },
  data() {
    return {
      lat: 0,
      lon: 0,
      weatherInfo: null,
      forecastInfo: [],
      outfitPart: false,
      isDown: false,
      startX: 0,
      scrollLeft: 0,
      activeIndex: null,
      locations,
      selectedLocation: "",
      locationMapping,
      cataegory: [
        "남성",
        "여성",
        "10대",
        "20대",
        "30대",
        "40대",
        "50대",
        "60대 이상",
        "오늘",
        "최근 1주일",
        "최근 한달",
      ],
    };
  },
  created() {
    this.checkLogin();
    console.log(
      "현재 프로필 상태",
      this.$store.state.nickname,
      this.$store.state.imageUrl
    );
  },

  methods: {
    checkLogin() {
      axios
        .get("http://localhost:8081/user/status", {})
        .then((response) => {
          if (response.data.name != null) {
            console.log(response.data);
          } else {
            console.log("오류");
          }
        })
        .catch((err) => {
          if (err.status == 401) {
            console.log("로그인 정보가 없습니다");
          }
        });
    },
    async fetchWeather(city) {
      await axios
        .get("http://localhost:8081/api/weather/city", {
          params: { city: city.name },
        })
        .then((res) => {
          console.log(res);
          this.weatherInfo = res.data;
          this.outfitPart = true;
          this.lat = res.data.coord.lat;
          this.lon = res.data.coord.lon;
          this.setLocationByCoordinates();
        })
        .catch((err) => {
          console.error(err);
        });
      await axios
        .get("http://localhost:8081/api/weather/forecast/city", {
          params: { city: city.name },
        })
        .then((res) => {
          console.log(res);
          this.forecastInfo = res.data.list;
        })
        .catch((err) => {
          console.error(err);
        });
    },
    getCurrentTime() {
      const date = new Date();
      let hours = date.getHours();
      const minutes = date.getMinutes();
      const ampm = hours < 12 ? "AM" : "PM";
      hours = hours % 12 || 12;
      const formattedHour = hours.toString().padStart(2, "0");
      const formattedMinute = minutes.toString().padStart(2, "0");
      return `${formattedHour}:${formattedMinute} ${ampm}`;
    },

    formatDate(dateTime) {
      const date = new Date(dateTime.replace(" ", "T"));

      const month = date.getMonth() + 1;
      const day = date.getDate();

      return `${month}/${day}`;
    },
    formatTime(dateTime) {
      const date = new Date(dateTime.replace(" ", "T"));
      let hours = date.getHours();
      const ampm = hours < 12 ? "AM" : "PM";
      hours = hours % 12 || 12;
      return `${hours}${ampm}`;
    },
    async getLocation() {
      if (!navigator.geolocation) {
        console.error("이 브라우저는 Geolocation을 지원하지 않습니다");
      }
      try {
        const position = await new Promise((resolve, reject) => {
          navigator.geolocation.getCurrentPosition(resolve, reject);
        });
        this.lat = position.coords.latitude;
        this.lon = position.coords.longitude;
        alert(`위도 : ${this.lat}, 경도 : ${this.lon}`);
        this.setLocationByCoordinates();
        await this.fetchWeatherByCoordinates(this.lat, this.lon);
      } catch (error) {
        console.error("위치를 가져오지 못했습니다", error);
        this.fetchWeather({ name: "seoul" });
      }
    },
    async fetchWeatherByCoordinates(lat, lon) {
      await axios
        .get("http://localhost:8081/api/weather/coordinate", {
          params: { lat: lat, lon: lon },
        })
        .then((res) => {
          console.log(res);
          this.weatherInfo = res.data;
          this.outfitPart = true;
        })
        .catch((err) => {
          console.error(err);
        });
      await axios
        .get("http://localhost:8081/api/weather/forecast/coordinate", {
          params: { lat: lat, lon: lon },
        })
        .then((res) => {
          console.log(res);
          this.forecastInfo = res.data.list;
        })
        .catch((err) => {
          console.error(err);
        });
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
    setActive(index) {
      this.activeIndex = index;
    },
    selectLocation(location) {
      this.selectedLocation = location;
      this.activeIndex = this.locations.indexOf(location);
    },
    setLocationByCoordinates() {
      console.log(`user location : lat=${this.lat} lon = ${this.lon}`);
      for (let location in this.locationMapping) {
        const { latMin, latMax, lonMin, lonMax } =
          this.locationMapping[location];
        console.log(
          `checking ${location} : lat(${latMin}-${latMax}), lon${lonMin}-${lonMax}`
        );

        if (
          this.lat >= latMin &&
          this.lat <= latMax &&
          this.lon >= lonMin &&
          this.lon <= lonMax
        ) {
          this.selectedLocation = location;
          this.activeIndex = this.locations.indexOf(location);
          break;
        }
      }
    },
  },
  mounted() {
    this.getLocation();
  },
};
</script>

<style scoped>
ul {
  list-style-type: none;
}
.currentWeatherInfo {
  background-color: white;
  margin: 20px 20px;
  padding: 10px;
  border-radius: 10px;
  justify-content: center;
  display: flex;
}

.currentWeatherInfo img {
  width: 150px;
  height: 150px;
}

.forecast-group {
  overflow-x: auto;
  background-color: white;
  border-radius: 10px;
  margin: 20px 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.forecast-list {
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
}

.forecast-item {
  flex: 0 0 auto;
  width: 120px;
  text-align: center;
  margin-right: 10px;
}

.forecast-item img {
  width: 60px;
  height: 60px;
}

.forecast-item p {
  font-size: 14px;
  margin: 5px 0 0;
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

#location-part {
  margin-top: 10px;
  background-color: rgb(235 235 235);
}

#location-list {
  margin: 10px 0;
  display: flex;
  width: 100%;
  height: 50px;
  overflow-x: auto;
  padding: 0 10px;
  cursor: grab;
}
#location-list:active {
  cursor: grabbing;
}

#location-list > li {
  flex: 0 0 auto;
  margin-right: 10px;
}

#location-list > li > a {
  color: #aaa;
  font-size: 16px;
  text-decoration: none;
  transition: font-weight 0.2s ease-in-out;
}
#location-list > li > a.active {
  font-weight: bold;
  color: #333;
}

#location-list::-webkit-scrollbar {
  display: none;
}
</style>
