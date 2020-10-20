<template>
  <div>
    <h1 class="chairhub-heading d-none d-md-block">Comments</h1>
    <b-form @submit="addComment">
      <b-form-textarea
        id="textarea"
        v-model="commentFormComment"
        placeholder="Enter something..."
        rows="3"
        max-rows="6"
      ></b-form-textarea>
      <div class="d-flex justify-content-end mb-3">
        <b-button type="submit" :disabled="isCommentTextEmpty"
          >Submit comment</b-button
        >
      </div>
    </b-form>

    <b-list-group>
      <b-list-group-item v-for="comment in commentList" v-bind:key="comment.id">
        <Comment
          :comment="comment.comment"
          :user="comment.userIdentifier"
        ></Comment>
      </b-list-group-item>
    </b-list-group>
  </div>
</template>

<script>
import Comment from "@/components/chairhub/Comment.vue";

export default {
  props: {
    presentationId: String
  },
  name: "Comments",
  data() {
    return {
      commentText: ""
    };
  },
  components: {
    Comment
  },
  watch: {},
  computed: {
    isCommentTextEmpty() {
      return this.$store.state.comment.commentForm.comment.trim() == "";
    },
    commentList() {
      return this.$store.state.comment.commentList;
    },
    commentFormComment: {
      get() {
        return this.$store.state.comment.commentForm.comment;
      },
      set(value) {
        this.$store.commit("setCommentFormField", {
          field: "comment",
          value
        });
      }
    }
  },
  methods: {
    addComment(evt) {
      // Prevent auto refresh of page
      evt.preventDefault();

      this.$store.dispatch("addCommentForPresentation", this.presentationId);
    }
  },
  mounted() {}
};
</script>

<style></style>
