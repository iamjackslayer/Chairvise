<template>
  <div class="container-fluid">
    <div class="row flex-xl-nowrap">
      <div class="col-12 col-md-3 col-xl-2 bd-sidebar">
        <div class="logo-container h3">
          <span class="logo">ChairVise</span>
          <button
            v-b-toggle.nav-collapse
            class="d-md-none p-0 ml-3 btn mobile-nav-toggle"
          >
            <b-icon icon="list" />
          </button>
        </div>
        <b-nav class="bd-links" vertical>
          <b-nav-item active>
            <router-link to="/home">Home</router-link>
          </b-nav-item>
          <b-nav-item active>
            <router-link to="importData">Import Data</router-link>
          </b-nav-item>
          <b-nav-item>
            <router-link to="/analyze">My Presentations</router-link>
          </b-nav-item>
          <b-nav-item>
            <router-link to="/conference">My Conferences</router-link>
          </b-nav-item>
        </b-nav>
        <div class="secondary-actions">
          <b-nav class="bd-links" vertical>
            <b-nav-item>
              <router-link to="userGuide">User Guide</router-link>
            </b-nav-item>
            <b-nav-item>
              <router-link to="logout">Logout</router-link>
            </b-nav-item>
          </b-nav>
        </div>
      </div>

      <div class="col-12 col-md-9 col-xl-10 py-md-3 pl-md-5 bd-content content">
        <b-overlay :show="isAppLoading" no-wrap></b-overlay>
        <!-- <el-header style="padding: 0;">
              <menu-bar style="position: fixed; width: 100vw; z-index: 1;"></menu-bar>
            </el-header> -->
        <transition name="fade">
          <router-view />
        </transition>
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
    },
    isFetchUserInfoError() {
      return this.$store.state.userInfo.isApiError;
    }
  }
};
</script>

<style lang="scss" scoped>
@import url("https://fonts.googleapis.com/css?family=Inter:300,400,500,600,700");

.logo-container {
  display: flex;
  margin-bottom: 0;
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

// TODO: Move sidebar stylings into own component
.row .bd-sidebar {
  order: 0;
  background-color: $gray-800;
  color: $gray-100;
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

.bd-links {
  display: block !important;
}
.bd-links {
  // flex-wrap: nowrap;
  max-height: calc(100vh - 5rem);
  overflow-y: auto;
  flex-grow: 1;
}

.logout {
  margin-top: auto;
}

// // All levels of nav
.bd-sidebar .nav > li > a {
  display: block;
  // @include font-size(110%);
  color: $gray-100;
  background-color: $gray-700;
  // color: rgba(0, 0, 0, .65);
}

.bd-sidebar .nav > li > a:hover {
  color: rgba(0, 0, 0, 0.85);
  text-decoration: none;
  background-color: transparent;
}

.bd-sidebar .nav > .active > a,
.bd-sidebar .nav > .active:hover > a {
  font-weight: 600;
  color: rgba(0, 0, 0, 0.85);
  background-color: transparent;
}
</style>
