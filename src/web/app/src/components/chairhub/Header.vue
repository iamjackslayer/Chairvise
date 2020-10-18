<template>
  <b-container fluid class="mx-0 my-3">
    <h1 class="chairhub-heading d-none d-md-block">Chairhub</h1>
    <b-row class="banner mx-0">
      <b-col class="px-0">
        <h3 class="mb-3">This week</h3>
        <b-row align-h="around">
          <BannerCard
            title="public posts"
            :figure="totalPublicPostsPastWeek.toString()"
            subfigure="32"
            subtitle="per day avg"
          />
          <BannerCard
            title="comments"
            :figure="totalCommentsPastWeek.toString()"
            subfigure="24"
            subtitle="per day avg"
          />
          <BannerCard
            title="active users"
            :figure="totalActiveUsersPastWeek.toString()"
            subfigure="2123"
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
    totalPublicPostsPastWeek() {
      return 212;
    },
    totalCommentsPastWeek() {
      return 3219;
    },
    totalActiveUsersPastWeek() {
      return 21;
    }
  }
};
</script>
<style lang="scss" scoped>
.banner {
  background-color: $gray-200;
  border: 1.7px none $gray-600;
  border-radius: 0.7rem;
  padding: 1.7rem 2rem;
}
.chairhub-heading {
  font-weight: bold;
  font-size: 3.5rem;
  color: $gray-800;
}
</style>
