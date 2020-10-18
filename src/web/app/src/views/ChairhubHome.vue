<template>
  <div class="px-lg-3">
    <Header />
    <EmptyPublicPostList v-if="isPublicPostListEmpty" />
    <PublicPostList v-else />
  </div>
</template>

<script>
import Header from "@/components/chairhub/Header.vue";
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
    Header,
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
