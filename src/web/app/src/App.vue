<template>
  <div class="container-fluid">
    <div class="row flex-xl-nowrap">
      <SideBar />

      <div class="col-12 col-lg-9 col-xl-10 py-lg-3 px-lg-5 bd-content content">
        <b-overlay :show="isAppLoading" no-wrap />
        <div class="page-container">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SideBar from "@/components/SideBar.vue";
export default {
  components: {
    SideBar
  },
  watch: {
    $route() {
      this.$store.dispatch("getAuthInfo");
    },
    isFetchUserInfoError() {
      if (!this.isFetchUserInfoError) {
        return;
      }
      this.$bvToast.toast(this.$store.state.userInfo.apiErrorMsg || "-", {
        title: "Auth request fail",
        variant: "danger",
        solid: true,
        autoHideDelay: 0
      });
    }
  },
  computed: {
    isAppLoading() {
      return this.$store.state.isPageLoading;
    },
    isFetchUserInfoError() {
      return this.$store.state.userInfo.isApiError;
    }
  }
};
</script>
