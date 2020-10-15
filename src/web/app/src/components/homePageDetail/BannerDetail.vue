<template>
  <div
    class="jumbotron jumbotron-fluid"
    style="background:linear-gradient(to bottom, #add8e6, #fafafa)"
  >
    <zoom-center-transition :duration="500">
      <b-row style="text-align:center">
        <b-col>
          <img alt="Vue logo" src="@/assets/chair2.png" />
        </b-col>
        <b-col>
          <h2>
            ChairVisE -- The Conference Data Visualisation Management System
          </h2>
        </b-col>
      </b-row>
    </zoom-center-transition>
  </div>
</template>

<script>
import { ZoomCenterTransition } from "vue2-transitions";
export default {
  name: "BannerDetail",
  data: () => ({
    show: false
  }),
  props: {
    msg: String
  },
  mounted() {
    this.loadBanner();
  },
  methods: {
    loadBanner() {
      this.show = true;
    }
  },
  components: {
    ZoomCenterTransition
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h2 {
  font-family: Garamond;
}
img {
  width: 250px;
  height: 300px;
  text-align: center;
  display: block;
  margin: 0 auto;
}
.el-row {
  background: #1e9fc4;
  padding: 32px;
}
</style>
