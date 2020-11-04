<template>
  <b-container fluid class="mx-0 my-3">
    <h1 class="chairhub-heading d-none d-md-block">Chairhub</h1>
    <b-row class="banner mx-0">
      <b-col class="px-0">
        <h3 class="mb-3">This week</h3>
        <b-row align-h="around">
          <BannerCard
            title="updated"
            :figure="isLoggedIn ? totalUpdatedPastWeek.toString() : '??'"
            :subfigure="isLoggedIn ? totalUpdatedPerDay.toString() : '??'"
            subtitle="per day avg"
          />
          <BannerCard
            title="created"
            :figure="isLoggedIn ? totalCreatedPastWeek.toString() : '??'"
            :subfigure="isLoggedIn ? totalCreatedPerDay.toString() : '??'"
            subtitle="per day avg"
          />
          <BannerCard
            title="comments"
            :figure="isLoggedIn ? totalCommentsPastWeek.toString() : '??'"
            :subfigure="isLoggedIn ? totalCommentsPerDay.toString() : '??'"
            subtitle="per day avg"
          />
        </b-row>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import BannerCard from "./BannerCard";
export default {
  name: "Header",
  components: {
    BannerCard
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.userInfo.isLogin;
    },
    totalUpdatedPastWeek() {
      let all = this.$store.state.presentation.publicPresentationList;
      let filtered = all.filter(p => {
        let diff = new Date() - new Date(p.updatedDate);
        let daysAgo = diff / (60 * 60 * 24 * 1000);
        return daysAgo < 7;
      });
      return filtered.length;
    },
    totalUpdatedPerDay() {
      let res = this.totalUpdatedPastWeek / 7;
      if (res < 1) {
        return res.toFixed(2);
      } else if (res < 10) {
        return res.toFixed(1);
      } else {
        return res.toFixed(0);
      }
    },
    totalCommentsPastWeek() {
      let all = this.$store.state.comment.commentList;
      let filtered = all.filter(c => {
        let diff = new Date() - new Date(c.createdDate);
        let daysAgo = diff / (60 * 60 * 24 * 1000);
        return daysAgo < 7;
      });
      return filtered.length;
    },
    totalCommentsPerDay() {
      let res = this.totalCommentsPastWeek / 7;
      if (res < 1) {
        return res.toFixed(2);
      } else if (res < 10) {
        return res.toFixed(1);
      } else {
        return res.toFixed(0);
      }
    },
    totalCreatedPastWeek() {
      let all = this.$store.state.presentation.publicPresentationList;
      let filtered = all.filter(p => {
        let diff = new Date() - new Date(p.createdDate);
        let daysAgo = diff / (60 * 60 * 24 * 1000);
        return daysAgo < 7;
      });
      return filtered.length;
    },
    totalCreatedPerDay() {
      let res = this.totalCreatedPastWeek / 7;
      if (res < 1) {
        return res.toFixed(2);
      } else if (res < 10) {
        return res.toFixed(1);
      } else {
        return res.toFixed(0);
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.banner {
  transition: all 0.4s ease-in-out;
  background-color: $gray-200;
  border: 1.7px none $gray-600;
  border-radius: 0.7rem;
  padding: 1.7rem 2rem;
  &:hover {
    transition: all 0.6s ease-in-out;
    background-color: $gray-300;
  }
}
.chairhub-heading {
  font-weight: bold;
  font-size: 3.5rem;
  color: $gray-800;
}
</style>
