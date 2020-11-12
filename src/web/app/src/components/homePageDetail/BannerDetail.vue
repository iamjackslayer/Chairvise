<template>
  <div class="banner">
    <div class="hero-image">
      <img alt="ChairVise Image" src="@/assets/chair2.png" />
    </div>
    <b-jumbotron
      class="jumbo"
      header="ChairVisE"
      lead="The conference data visualisation system"
    >
    </b-jumbotron>
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

.banner {
  display: flex;
  flex-direction: column;

  @include media-breakpoint-up(lg) {
    flex-direction: row;
  }
}

.jumbotron {
  h1 {
    font-size: 2.5rem;
    @include media-breakpoint-up(lg) {
      font-size: 4.5rem;
    }
  }
}

.hero-image {
  border-radius: 0.3rem;
  padding: 2rem;
  background-color: $indigo-100;
  margin: 0 0 1rem 0;

  @include media-breakpoint-up(lg) {
    margin: 0 1rem 0 0;
  }
}

.jumbo {
  flex: 2;
  margin: 0;
}
</style>
