<template>
  <div id="wrap">
    <Header />
    <Main />
    <div></div>
  </div>
</template>

<script>
import Header from "./components/Header.vue";
import Main from "./components/Main.vue";
import axios from "axios";

export default {
  name: "App",
  components: {
    Header,
    Main,
  },
  data() {
    return {
      data: null,
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      const apiKey = "d0dc294ff2755b5c5e7c0f6f83082eca";
      const cityIds = "Seoul,kr";
      await axios
        .get("https://api.openweathermap.org/data/2.5/weather", {
          params: {
            q: cityIds,
            appid: apiKey,
            units: "metric",
          },
        })
        .then((response) => {
          console.log(response);
          this.data = response.data;
        })
        .catch((error) => {
          console.error("Error fetching data : ", error);
        });
    },
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

body {
  font-family: "Apple SD Gothic Neo";
  background-color: #f1f1f1;
}

#wrap {
  width: 500px;
  min-width: 400px;
  height: 1120px;
  margin: 0 auto;
  background-color: #ffffff;
}
</style>
