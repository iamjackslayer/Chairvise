<template>
  <div class="d-flex w-100 justify-content-between">
    <div class="d-flex flex-column justify-content-between">
      <h5 class="mb-1" v-text="user"></h5>
      <p class="text-secondary" v-text="comment"></p>
    </div>
    <small>3 days ago</small>
  </div>
</template>

<script>
export default {
  props: {
    user: String,
    comment: String
  },
  name: "Comments",
  data() {
    return {
      commentText: "",
      commentList: []
    };
  },
  watch: {},
  computed: {},
  methods: {},
  mounted() {}
};
</script>

<style></style>
