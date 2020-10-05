<template>
  <div>
    <PublicPostList />
  </div>
</template>

<script>
import PublicPostList from "@/components/chairhub/PublicPostList.vue";
export default {
  name: "ChairhubHome",
  data() {
    return {};
  },
  watch: {},
  computed: {},
  components: {
    PublicPostList
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
