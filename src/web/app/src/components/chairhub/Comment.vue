<template>
  <div class="d-flex w-100 justify-content-between">
    <div class="d-flex flex-column justify-content-between">
      <h5 class="mb-1" v-text="creator"></h5>
      <p class="text-secondary" v-text="comment"></p>
    </div>
    <b-button v-if="canDelete" @click="deleteComment" variant="outline-danger">
      <b-icon icon="trash-fill" variant="danger"></b-icon>
    </b-button>
    <small>3 days ago</small>
  </div>
</template>

<script>
export default {
  props: {
    id: String,
    presentationId: String,
    creator: String,
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
  computed: {
    canDelete() {
      return this.$store.state.userInfo.userEmail === this.creator;
    }
  },
  methods: {
    editComment(editedComment) {
      this.$store.dispatch("editCommentForPresentation", {
        presentationId: this.presentationId,
        id: this.id,
        editedComment
      });
    },
    deleteComment() {
      this.$store.dispatch("deleteCommentForPresentation", {
        presentationId: this.presentationId,
        id: this.id
      });
    }
  },
  mounted() {}
};
</script>

<style></style>
