<template>
  <div>
    <div class="title-bar">
      <h1 class="title">Conferences</h1>
      <b-button
        class="title-action"
        variant="primary"
        v-if="!isConferenceListEmpty"
        @click="createConference"
        ><b-icon icon="plus"></b-icon> Add Conference</b-button
      >
    </div>
    <EmptyConference v-if="isConferenceListEmpty" />
    <div v-else class="list-grid">
      <b-card
        class="list-card"
        v-for="conference in conferences"
        :key="conference.id"
        @click="viewConference(conference.id)"
      >
        <h5 class="list-title">{{ conference.name }}</h5>
        <div class="list-description">
          {{ conference.description || "-" }}
        </div>
        <div class="privacy-status">
          <!-- TODO: Change this to date with calendar icon -->
          <b-icon icon="lock" class="mr-1" />Private
        </div>
      </b-card>
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
