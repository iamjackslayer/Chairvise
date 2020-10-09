import Vue from "vue";
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";
import Vuelidate from "vuelidate";
import App from "./App.vue";
import router from "./router";
import store from "./store";

import "./scss/global.scss";
// import "fullcalendar/dist/fullcalendar.css";

Vue.use(BootstrapVue);
Vue.use(Vuelidate);
Vue.use(IconsPlugin);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");

// change title of page
router.afterEach(to => {
  document.title = to.meta.title;
});
