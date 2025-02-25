import Header from "@/views/Header.vue";
import axios from "axios";
import { locations, locationMapping } from "@/js/location_mapping.js";
import OutfitPart from "@/views/OutfitPart.vue";

export default {
  name: "AppMain",
  components: {
    Header,
    OutfitPart,
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
            console.log("로그인 유저 정보  : ", response.data);
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
