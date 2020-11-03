import Vue from "vue";
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";
import Vuelidate from "vuelidate";
// import vSelect from "vue-select";
import { Select, Option, OptionGroup } from "element-ui";

import App from "./App.vue";
import router from "./router";
import store from "./store";
import "./scss/global.scss";
import "vue-select/dist/vue-select.css";
import lang from "element-ui/lib/locale/lang/en";
import locale from "element-ui/lib/locale";

Vue.use(BootstrapVue);
Vue.use(Vuelidate);
Vue.use(IconsPlugin);

// ElementUI Select used in BasicSectionDetail
Vue.component(Select.name, Select);
Vue.component(Option.name, Option);
Vue.component(OptionGroup.name, OptionGroup);
locale.use(lang);

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
