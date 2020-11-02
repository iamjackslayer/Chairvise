<template>
  <div class="col-12 col-lg-3 col-xl-2 p-2 p-lg-3 sidebar">
    <div class="logo-container">
      <span v-if="isChairhubHomeRoute" class="logo d-md-none">
        Chairhub
        <span class="logo--small">by ChairVise</span>
      </span>
      <span v-else class="logo">ChairVise</span>
      <button
        @click="toggleCollapse"
        class="d-lg-none p-0 ml-3 btn mobile-nav-toggle"
      >
        <b-icon v-if="visible" icon="x" />
        <b-icon v-else icon="list" />
      </button>
    </div>
    <b-collapse v-model="visible" id="nav-collapse">
      <b-col class="d-flex flex-column p-0 h-100">
        <b-nav v-if="isLogin" class="sidebar-links" vertical>
          <!-- TODO: Add check for login and logout views -->
          <b-nav-item to="/conference" @click="onNavItemClicked">
            <b-icon icon="house-door-fill" class="mr-2"></b-icon>
            Conferences
          </b-nav-item>
          <b-nav-item to="/importData" @click="onNavItemClicked">
            <b-icon icon="cloud-arrow-up-fill" class="mr-2"></b-icon>Import Data
          </b-nav-item>
          <b-nav-item to="/presentation" @click="onNavItemClicked">
            <b-icon icon="file-image-fill" class="mr-2"></b-icon>
            Presentations
          </b-nav-item>
          <b-nav-item to="/calendar" @click="onNavItemClicked">
            <b-icon icon="calendar-week-fill" class="mr-2"></b-icon>
            Calendar
          </b-nav-item>
          <b-nav-item to="/chairhub/home" @click="onNavItemClicked">
            <b-icon icon="people-fill" class="mr-2"></b-icon>Chairhub
          </b-nav-item>
        </b-nav>
        <b-nav v-else class="sidebar-links">
          <!-- To change to login view route -->
          <b-nav-item v-if="!isLogin" @click="login">
            <b-icon icon="arrow-right-square-fill" class="mr-2"></b-icon>
            Login
          </b-nav-item>
        </b-nav>
        <div class="secondary-actions">
          <b-nav class="sidebar-links" vertical>
            <b-nav-item to="/userGuide" @click="onNavItemClicked">
              <b-icon icon="patch-question-fll" class="mr-2"></b-icon>User Guide
            </b-nav-item>
            <!-- To change to logout view route -->
            <b-nav-item v-if="isLogin" @click="logout">
              <b-icon icon="arrow-down-right-square-fill" class="mr-2"></b-icon
              >Logout
            </b-nav-item>
            <b-nav-text v-if="isLogin">
              <div class="user-email">
                <b-icon icon="person-circle" class="mr-2"></b-icon>
                {{ $store.state.userInfo.userEmail }}
              </div>
            </b-nav-text>
          </b-nav>
        </div>
      </b-col>
    </b-collapse>
  </div>
</template>

<script>
export default {
  name: "SideBar",
  data() {
    return {
      innerWidth: window.innerWidth,
      visible: this.checkWindowLarge(),
      isWindowLarge: this.checkWindowLarge()
    };
  },
  mounted() {
    // Expands collapse when larger than lg and vice versa.
    window.addEventListener("resize", ev => {
      const innerWidth = ev.currentTarget.innerWidth;
      this.isWindowLarge = this.visible = innerWidth >= 992;
    });
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },
    userNickname() {
      return this.$store.state.userInfo.userNickname;
    },
    isApiError() {
      return this.$store.state.userInfo.isApiError;
    },
    isChairhubHomeRoute() {
      return this.$route.name === "chairhubHome";
    }
  },
  methods: {
    login() {
      window.location.href = this.$store.state.userInfo.loginUrl;
    },
    logout() {
      window.location.href = this.$store.state.userInfo.logoutUrl;
    },
    toggleCollapse() {
      this.visible = !this.visible;
    },
    onNavItemClicked() {
      if (!this.isWindowLarge) {
        this.toggleCollapse();
      }
    },
    checkWindowLarge() {
      return window.innerWidth >= 992;
    }
  }
};
</script>
<style lang="scss" scoped>
.logo-container {
  display: flex;
  font-size: 1.4rem;
  padding: 0.5rem 0.5rem;
  align-items: center;

  @include media-breakpoint-up(lg) {
    font-size: 2rem;
    padding: 0 1rem;
    margin-top: 1rem;
    margin-bottom: 1rem;
  }
}

.logo {
  font-weight: bold;
  line-height: 1;
  display: block;
  flex: 1;
}

.mobile-nav-toggle {
  font-size: inherit;
  display: flex;
  align-items: center;
  color: white;
}

.mobile-nav-toggle:hover {
  color: $indigo-600;
}

#nav-collapse {
  display: flex;
  flex-direction: column;
  flex: 1;
  height: 100%;
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

  @include media-breakpoint-up(lg) {
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
  outline: none;
  font-size: 0.9rem;
}

.navbar-text {
  display: block;
  padding: 0;
}

.user-email {
  font-size: 0.9rem;
  padding: 0.5rem 1rem;

  > svg {
    font-size: 125%;
  }
}

.sidebar .nav > li > a:hover {
  text-decoration: none;
  background-color: $gray-900;
}

.sidebar .nav > li > a.router-link-active {
  background-color: $gray-900;
  box-shadow: inset 0 2px 2px hsla(0, 0%, 0%, 0.1);
}
</style>
