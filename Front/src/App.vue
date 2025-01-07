<template>
  <div id="wrap">
    <router-view> </router-view>
    <div></div>
  </div>
</template>

<script>
export default {
  name: "App",
  components: {},
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
  width: 600px;
  min-width: 400px;
  height: 1120px;
  max-height: 1000px;
  overflow-y: auto;
  margin: 0 auto;
  background-color: rgb(245, 242, 242);
}
</style>
