<template>
  <div class="container-fluid">
    <div class="row flex-xl-nowrap">
      <div class="col-12 col-md-3 col-xl-2 sidebar">
        <div class="logo-container h3">
          <span class="logo">ChairVise</span>
          <button
            v-b-toggle.nav-collapse
            class="d-md-none p-0 ml-3 btn mobile-nav-toggle"
          >
            <b-icon icon="list" />
          </button>
        </div>
        <b-nav class="sidebar-links" vertical>
          <!-- TODO: Add check for login and logout views -->
          <b-nav-item to="/home">
            <b-icon icon="house-door-fill" class="mr-2"></b-icon>
            Home
          </b-nav-item>
          <b-nav-item to="/importData">
            <b-icon icon="cloud-arrow-up-fill" class="mr-2"></b-icon>
            Import Data
          </b-nav-item>
          <b-nav-item to="/analyze">
            <b-icon icon="file-image-fill" class="mr-2"></b-icon>
            My Presentations
          </b-nav-item>
          <b-nav-item to="/conference">
            <b-icon icon="calendar-week-fill" class="mr-2"></b-icon>
            My Conferences
          </b-nav-item>
        </b-nav>
        <div class="secondary-actions">
          <b-nav class="sidebar-links" vertical>
            <b-nav-item to="/userGuide">
              <b-icon icon="patch-question-fll" class="mr-2"></b-icon>
              User Guide
            </b-nav-item>
            <b-nav-item to="/logout">
              <b-icon icon="arrow-down-right-square-fill" class="mr-2"></b-icon>

              Logout
            </b-nav-item>
          </b-nav>
        </div>
      </div>

      <div class="col-12 col-md-9 col-xl-10 py-md-3 px-md-5 bd-content content">
        <b-overlay :show="isAppLoading" no-wrap />
        <!-- <el-header style="padding: 0;">
              <menu-bar style="position: fixed; width: 100vw; z-index: 1;"></menu-bar>
            </el-header> -->
        <div class="page-container">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
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
  data() {
    return {
      collapsed: true
    };
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

<style lang="scss" scoped>
// TODO: Move sidebar stylings into own component
.logo-container {
  display: flex;
  font-size: 2rem;
  padding: 0 1rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
}

.logo {
  font-weight: bold;
  display: block;
  flex: 1;
}

.mobile-nav-toggle {
  display: flex;
  align-items: center;
  color: white;
}

.mobile-nav-toggle svg {
  height: 30px;
  width: 30px;
}

#nav-collapse {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.secondary-actions {
  margin-top: auto;
}

.row .sidebar {
  order: 0;
  color: $gray-300;
  background-color: $gray-800;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;

  padding: 1rem;

  @include media-breakpoint-up(md) {
    @supports (position: sticky) {
      position: sticky;
      top: 0;
      z-index: 1000;
      height: 100vh;
    }
    border-right: 1px solid rgba(0, 0, 0, 0.1);
  }

  @include media-breakpoint-up(xl) {
    flex: 0 1 320px;
  }
}

.sidebar-links {
  display: block !important;
}
.sidebar-links {
  // flex-wrap: nowrap;
  max-height: calc(100vh - 5rem);
  overflow-y: auto;
  flex-grow: 1;
}

.logout {
  margin-top: auto;
}

// // All levels of nav
.sidebar .nav > li > a {
  display: block;
  color: $gray-300;
  border-radius: 5px;
  margin: 5px 0;
  // outline: 1px solid blue;
  outline: none;
  font-size: 0.9rem;
  // background-color: $gray-700;
}

.sidebar .nav > li > a:hover {
  text-decoration: none;
  background-color: $gray-900;
}

.sidebar .nav > li > a.router-link-active {
  background-color: $gray-900;
  box-shadow: inset 0 2px 2px hsla(0, 0%, 0%, 0.1);
  // background-color: $teal-400;
}
</style>
