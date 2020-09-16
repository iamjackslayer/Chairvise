<template>
  <div class="container-fluid">
    
    <div class="row flex-xl-nowrap">
      <div class="col-12 col-md-3 col-xl-2 bd-sidebar">
        <div id="logo" class="h3">
          <span>ChairVise</span>
        </div>
        <b-nav class="bd-links" vertical>
          <b-nav-item  active>
            <router-link to="home">Home</router-link>
          </b-nav-item>
          <b-nav-item>
            <router-link to="analyze">My Presentations</router-link>
          </b-nav-item>
          <b-nav-item>
            <router-link to="conference">My Conferences</router-link>
          </b-nav-item>
        </b-nav>
        <div class="logout">
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
        <b-overlay :show="isAppLoading" no-wrap>
        </b-overlay>
            <!-- <el-header style="padding: 0;">
              <menu-bar style="position: fixed; width: 100vw; z-index: 1;"></menu-bar>
            </el-header> -->
            <transition name="fade">
              <router-view/>
            </transition>
        
      </div>
    </div>
  </div>
</template>

<script>
  import MenuBar from '@/components/MenuBar.vue'

  export default {
    watch: {
      '$route'() {
        this.$store.dispatch('getAuthInfo');
      },
      'isFetchUserInfoError'() {
        if (!this.isFetchUserInfoError) {
          return
        }
        this.$notify.error({
          title: 'Auth request fail',
          message: this.$store.state.userInfo.apiErrorMsg,
          duration: 0
        });
      }
    },
    components: {
      'menu-bar': MenuBar,
    },
    computed: {
      isAppLoading() {
        return this.$store.state.isPageLoading
      },
      isFetchUserInfoError() {
        return this.$store.state.userInfo.isApiError
      }
    },
  }
</script>

<style lang="scss">
  @import url('https://fonts.googleapis.com/css?family=Inter:300,400,500,600,700');

  #logo {
    font-weight: bold;
  }


// 1. review this ;)
// 2. Add collapsible toggle
// 3. Style sidebar and icons
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
  color: rgba(0, 0, 0, .85);
  text-decoration: none;
  background-color: transparent;
}

.bd-sidebar .nav > .active > a,
.bd-sidebar .nav > .active:hover > a {
  font-weight: 600;
  color: rgba(0, 0, 0, .85);
  background-color: transparent;
}

</style>
