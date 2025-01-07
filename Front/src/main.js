import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import { createVuetify } from "vuetify/lib/framework.mjs";
import "vuetify/styles";
import { aliases, mdi } from "vuetify/lib/iconsets/mdi-svg.mjs";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import { BootstrapVue3 } from "bootstrap-vue-3";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue-3/dist/bootstrap-vue-3.css";

const app = createApp(App);
const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: "mdi",
    aliases,
    sets: {
      mdi,
    },
  },
});
app.use(BootstrapVue3);
app.use(router);
app.use(vuetify);
app.use(store);
app.mount("#app");
