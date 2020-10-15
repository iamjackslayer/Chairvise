<template>
  <div>
    <EmptyPublicPostList v-if="isPublicPostListEmpty" />
    <PublicPostList v-if="!fetchingPublicPostList" />
  </div>
</template>

<script>
import PublicPostList from "@/components/chairhub/PublicPostList.vue";
import EmptyPublicPostList from "@/components/chairhub/EmptyPublicPostList.vue";

export default {
  name: "ChairhubHome",
  data() {
    return {};
  },
  watch: {},
  computed: {
    fetchingPublicPostList() {
      return this.$store.state.presentation.publicPresentationListStatus
        .isLoading;
    },
    isPublicPostListEmpty() {
      return this.$store.state.presentation.publicPresentationList.length === 0;
    }
  },
  components: {
    PublicPostList,
    EmptyPublicPostList
  },
  methods: {},
  mounted() {
    // Get all public presentations
    this.$store.dispatch("getPublicPresentationList");
    // TODO: fill the ConferenceList table
  }
};
</script>

<style scoped></style>
