<template>
  <div>
    <div
      class="jumbotron jumbotron-fluid"
      style="background:linear-gradient(to bottom, #add8e6, #fafafa)"
    >
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
    </div>
  </div>
</template>

<script>
import dayjs from "dayjs";
import EmptyConference from "@/components/emptyStates/EmptyConference.vue";

export default {
  name: "BannerDetail",
  data: () => ({
    show: false
  }),
  components: {
    EmptyConference
  },
  props: {
    msg: String
  },
  mounted() {
    this.loadBanner();
    this.$store.dispatch("getConferenceList");
    this.loadConferences();
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },
    isLoading() {
      return this.$store.state.conference.conferenceListStatus.isLoading;
    },
    conferences() {
      return this.$store.state.conference.conferenceList;
    },
    isConferenceListEmpty() {
      return this.$store.state.conference.conferenceList.length <= 0;
    }
  },
  methods: {
    loadBanner() {
      this.show = true;
    },
    loadConferences() {
      this.show = true;
    },
    dayjs(date) {
      return dayjs(date).format("dddd, MMMM D, YYYY");
    },
    viewConference(id) {
      this.$router.push("/conference/" + id);
    },
    createConference() {
      this.$router.push("/conference/add");
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
img {
  width: 250px;
  height: 300px;
  text-align: center;
  display: block;
  margin: 0 auto;
}
</style>
