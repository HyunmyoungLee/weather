<template>
  <div>
    <Header @selectedCity="fetchWeather" />
    <div class="currentWeatherInfo" v-if="weatherInfo">
      <div>
        <img
          :src="`https://openweathermap.org/img/wn/${weatherInfo.weather[0].icon}@2x.png`"
          alt=""
        />
      </div>
      <h3>{{ weatherInfo.name }}</h3>
      <p>Temperature : {{ weatherInfo.main.temp.toFixed(1) }}°C</p>
      <p>{{ weatherInfo.weather[0].description }}</p>
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
    <div>
      <h3>Today's Outfit</h3>
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
    };
  },

  methods: {
    async fetchWeather(city) {
      const apiKey = "d0dc294ff2755b5c5e7c0f6f83082eca";
      const url = `https://api.openweathermap.org/data/2.5/weather?q=${city.name}&appid=${apiKey}&units=metric`;
      const forecastUrl = `https://api.openweathermap.org/data/2.5/forecast?q=${city.name}&cnt=40&appid=${apiKey}&units=metric`;

      await axios
        .get(url)
        .then((res) => {
          console.log(res);
          this.weatherInfo = res.data;
        })
        .catch((err) => {
          console.error(err);
        });
      await axios
        .get(forecastUrl)
        .then((res) => {
          console.log(res);
          this.forecastInfo = res.data.list;
        })
        .catch((err) => {
          console.error(err);
        });
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
  },
};
</script>

<style scoped>
.currentWeatherInfo {
  text-align: center;
}

.currentWeatherInfo img {
  width: 150px;
  height: 150px;
}

.forecast-group {
  overflow-x: auto;
  background-color: white;
  border-radius: 10px;
  margin: 0 20px;
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
