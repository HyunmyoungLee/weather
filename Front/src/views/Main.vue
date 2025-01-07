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
    <div v-if="outfitPart">
      <h3>Trendy Outfit</h3>
    </div>
  </div>
</template>
<script>
import Header from "./Header.vue";
import axios from "axios";

export default {
  name: "AppMain",
  components: {
    Header,
  },
  data() {
    return {
      weatherInfo: null,
      forecastInfo: [],
      outfitPart: false,
    };
  },

  methods: {
    async fetchWeather(city) {
      await axios
        .get("http://localhost:8081/api/weather/city", {
          params: { city: city.name },
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
        const lat = position.coords.latitude;
        const lon = position.coords.longitude;
        alert(`위도 : ${lat}, 경도 : ${lon}`);
        await this.fetchWeatherByCoordinates(lat, lon);
      } catch (error) {
        console.error("위치를 가져오지 못했습니다", error);
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
  },
  mounted() {
    this.getLocation();
  },
};
</script>

<style scoped>
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
</style>
