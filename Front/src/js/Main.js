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
      loginUserEmail: "",
    };
  },
  created() {
    this.checkLogin();
  },

  methods: {
    checkLogin() {
      axios
        .get("http://localhost:8081/user/status", {})
        .then((response) => {
          if (response.data.name != null) {
            this.loginUserEmail = response.data.email;
          }
        })
        .catch((err) => {
          console.error(err);
        });
    },
    async fetchWeather(city) {
      await axios
        .get("http://localhost:8081/api/weather/city", {
          params: { city: city.name },
        })
        .then((res) => {
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
          this.forecastInfo = res.data.list;
        })
        .catch((err) => {
          console.error(err);
        });
    },

    setLocationByCoordinates() {
      for (let location in this.locationMapping) {
        const { latMin, latMax, lonMin, lonMax } =
          this.locationMapping[location];

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
