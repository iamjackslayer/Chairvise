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

    <div class="page">
      <b-card
        class="shadow p-3 mb-5 bg-white rounded"
        v-if="isConferenceListEmpty"
      >
        <EmptyConference />
      </b-card>
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
            <b-icon icon="lock" class="mr-1" />Private
          </div>
        </b-card>
      </div>
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
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.page {
  padding: 20px;
}
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
</style>
