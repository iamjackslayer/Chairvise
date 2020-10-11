<template>
  <div class="container-fluid">
    <div class="row flex-xl-nowrap">
      <SideBar />

      <div class="col-12 col-md-9 col-xl-10 py-md-3 px-md-5 bd-content content">
        <b-overlay :show="isAppLoading" no-wrap />
        <el-header style="padding: 0;">
          <!-- <menu-bar
            style="position: fixed; width: 100vw; z-index: 1;"
          ></menu-bar> -->
        </el-header>
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
      this.$notify.error({
        title: "Auth request fail",
        message: this.$store.state.userInfo.apiErrorMsg,
        duration: 0
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
