<template>
  <div>
    <BannerDetail v-if="!isLogin"></BannerDetail>
    <FeatureListDetail v-if="!isLogin"></FeatureListDetail>
    <ConferenceList v-if="isLogin"></ConferenceList>
  </div>
</template>

<script>
// @ is an alias to /src
import BannerDetail from "@/components/homePageDetail/BannerDetail.vue";
import FeatureListDetail from "@/components/homePageDetail/FeatureListDetail.vue";
import ConferenceList from "@/components/homePageDetail/ConferenceList.vue";

export default {
  name: "home",
  components: {
    BannerDetail,
    FeatureListDetail,
    ConferenceList
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    }
  }
};
</script>
<style>
.navbar-submenu-menu {
  margin-top: -5px;
  left: initial !important;
  right: 0;
}

.el-menu--horizontal > .navbar-submenu.is-active .el-submenu__title {
  border-bottom: 0;
}

.el-submenu.is-active .el-submenu__title {
  border-bottom: 0;
}
</style>
