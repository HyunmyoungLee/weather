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
    <OutfitPart
      v-if="outfitPart"
      :outfitPart="outfitPart"
      :locations="locations"
      v-model:selectedLocation="selectedLocation"
      v-model:activeIndex="activeIndex"
      v-model:loginUserEmail="loginUserEmail"
    />
  </div>
</template>
<script>
import MainJs from "@/js/Main.js";
export default MainJs;
</script>

<style scoped>
@import "@/css/Main.css";
</style>
