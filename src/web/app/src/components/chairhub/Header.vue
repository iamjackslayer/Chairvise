<template>
  <b-container fluid class="mx-0 my-3">
    <h1 class="chairhub-heading d-none d-md-block">Chairhub</h1>
    <b-row class="banner mx-0">
      <b-col class="px-0">
        <h3 class="mb-3">This week</h3>
        <b-row align-h="around">
          <BannerCard
            title="public posts"
            :figure="isLoggedIn ? totalPublicPostsPastWeek.toString() : '??'"
            :subfigure="isLoggedIn ? totalPublicPostsPerDay.toString() : '??'"
            subtitle="per day avg"
          />
          <BannerCard
            title="comments"
            :figure="isLoggedIn ? totalCommentsPastWeek.toString() : '??'"
            :subfigure="isLoggedIn ? totalCommentsPerDay.toString() : '??'"
            subtitle="per day avg"
          />
          <BannerCard
            title="active users"
            :figure="isLoggedIn ? totalActiveUsersPastWeek.toString() : '??'"
            :subfigure="isLoggedIn ? totalActiveUsersPerDay.toString() : '??'"
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
    totalPublicPostsPastWeek() {
      return 212;
    },
    totalPublicPostsPerDay() {
      return 1;
    },
    totalCommentsPastWeek() {
      return 3219;
    },
    totalCommentsPerDay() {
      return 2;
    },
    totalActiveUsersPastWeek() {
      return 21;
    },
    totalActiveUsersPerDay() {
      return 1;
    }
  }
};
</script>
<style lang="scss" scoped>
.banner {
  transition: all 0.5s ease-in-out;
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
