import Vue from "vue";
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";
import Vuelidate from "vuelidate";
import vSelect from "vue-select";

import App from "./App.vue";
import router from "./router";
import store from "./store";

import "./scss/global.scss";
import "vue-select/dist/vue-select.css";

Vue.use(BootstrapVue);
Vue.use(Vuelidate);
Vue.use(IconsPlugin);
Vue.component("v-select", vSelect);

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
