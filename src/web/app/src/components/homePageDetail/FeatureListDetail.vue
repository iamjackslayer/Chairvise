<template>
  <div class="page">
    <div class="feature-list" v-if="!isLogin">
      <div class="jumbotron">
        <h1>Here's what you can do</h1>
        <b-row type="flex" :gutter="16" align="middle" justify="center">
          <zoom-x-transition :duration="500" :delay="600">
            <b-col :sm="24" :md="8" :lg="8" :xl="8" v-show="show">
              <b-card shadow="hover" class="feature-card">
                <img src="@/assets/upload.png" />
                <p>Upload all your conference data</p>
              </b-card>
            </b-col>
          </zoom-x-transition>
          <zoom-x-transition :duration="500" :delay="600">
            <b-col :sm="24" :md="8" :lg="8" :xl="8" v-show="show">
              <b-card shadow="hover" class="feature-card">
                <img src="@/assets/collaboration.png" />
                <p>Create and share presentation with others</p>
              </b-card>
            </b-col>
          </zoom-x-transition>
          <zoom-x-transition :duration="500" :delay="600">
            <b-col :sm="24" :md="8" :lg="8" :xl="8" v-show="show">
              <b-card shadow="hover" class="feature-card">
                <img src="@/assets/presentation.png" />
                <p>Generate various visualisation</p>
              </b-card>
            </b-col>
          </zoom-x-transition>
        </b-row>
      </div>
    </div>
    <div class="options-list" v-if="isLogin">
      <b-row>
        <h1>Exploring Chairvise</h1>
      </b-row>
      <b-row type="flex" :gutter="16" align="middle" justify="center">
        <zoom-x-transition :duration="500" :delay="600">
          <b-col v-show="show">
            <b-card class="bcard">
              <img src="@/assets/upload.png" />
              <br />
              <b-button variant="primary" class="button" @click="importData">
                Import Data
              </b-button>
            </b-card>
          </b-col>
        </zoom-x-transition>
        <zoom-x-transition :duration="500" :delay="600">
          <b-col v-show="show">
            <b-card class="bcard">
              <img src="@/assets/stadistics.png" />
              <br />
              <b-button variant="primary" class="button" @click="analyze">
                Analyse Data
              </b-button>
            </b-card>
          </b-col>
        </zoom-x-transition>
        <zoom-x-transition :duration="500" :delay="600">
          <b-col v-show="show">
            <b-card class="bcard">
              <img src="@/assets/schedule.png" />
              <br />
              <b-button variant="primary" class="button" @click="conference">
                Track Conference
              </b-button>
            </b-card>
          </b-col>
        </zoom-x-transition>
      </b-row>
    </div>
  </div>
</template>

<script>
import { ZoomXTransition } from "vue2-transitions";

export default {
  name: "FeatureListDetail",
  data: () => ({
    show: false
  }),
  props: {
    msg: String
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    }
  },
  mounted() {
    this.loadFeatures();
  },
  methods: {
    analyze() {
      this.$router.push("/analyze");
    },
    importData() {
      this.$router.push("/importData");
    },
    loadFeatures() {
      this.show = true;
    },
    conference() {
      this.$router.replace("/conference");
    }
  },
  components: {
    ZoomXTransition
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
i {
  font-size: 4rem;
  margin-bottom: 1.5rem;
}
.bcard {
  box-shadow: 1px 1px 3px grey;
}
.bcard:hover {
  box-shadow: 2px 2px 4px grey;
  -webkit-transform: scale(1.02);
  -ms-transform: scale(1.02);
  transform: scale(1.02);
}

.el-card {
  margin: 0px;
  text-align: center;
}

.el-button--text {
  color: black;
}

.page {
  padding: 20px;
}

.options-list .el-button {
  width: 100%;
}
</style>
