<template>
  <div id="wrap">
    <Main />
    <div></div>
  </div>
</template>

<script>
import Main from "./components/Main.vue";

export default {
  name: "App",
  components: {
    Main,
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  font-family: "Apple SD Gothic Neo", sans-serif;
}

#app {
  background-color: #ffffff;
}

#wrap {
  background-repeat: no-repeat;
  width: 800px;
  min-width: 400px;
  height: 1120px;
  max-height: 1000px;
  overflow-y: auto;
  margin: 0 auto;
  background-color: rgb(245, 242, 242);
}
</style>
