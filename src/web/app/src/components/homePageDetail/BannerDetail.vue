<template>
  <div>
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
    <div class="page">
      <b-card
        class="shadow p-3 mb-5 bg-white rounded"
        v-if="isPresentationListEmpty"
      >
        <!-- TODO: Replace with openpeeps image -->
      </b-card>
      <div class="presentation-grid">
        <b-card
          class="presentation-card"
          v-for="conference in conferences"
          :key="conference.id"
          @click="viewConference(conference.id)"
        >
          <h5 class="presentation-title">{{ conference.name }}</h5>
          <div class="presentation-description">
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
    this.$store.dispatch("getConferenceList");
    this.loadConferences();
  },
  computed: {
    isLoading() {
      return this.$store.state.conference.conferenceListStatus.isLoading;
    },
    conferences() {
      return this.$store.state.conference.conferenceList;
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
@supports (display: grid) {
  .presentation-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    // TODO: Change to one column for mobile
    grid-gap: 1.5rem;
  }
}
.row {
  margin: 20px;
}
.presentation-card {
  cursor: pointer;
}

.presentation-card:hover {
  background-color: $indigo-100;
}

.presentation-card > .card-body {
  display: flex;
  flex-direction: column;
}
.page {
  padding: 20px;
}
.presentation-description {
  font-size: 0.95rem;
  letter-spacing: -0.025em;
  color: $gray-600;
  // clamp to 3 lines (restricted to webkit browsers)
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.privacy-status {
  margin-top: auto;
  font-size: 0.95rem;
  color: $indigo-600;
  align-self: flex-end;
}

.presentation-title {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
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
