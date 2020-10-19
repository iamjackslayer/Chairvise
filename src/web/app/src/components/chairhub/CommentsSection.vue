<template>
  <div>
    <h1 class="chairhub-heading d-none d-md-block">Comments</h1>
    <b-form @submit="addComments">
      <b-form-textarea
          id="textarea"
          v-model="commentText"
          placeholder="Enter something..."
          rows="3"
          max-rows="6"
      ></b-form-textarea>
      <div class="d-flex justify-content-end mb-3">
        <b-button type="submit" :disabled=isCommentTextEmpty>Submit comment</b-button>
      </div>
    </b-form>

    <b-list-group>
      <b-list-group-item v-for="comment in commentList" v-bind:key="comment">
        <Comment :comment="comment.comment" :user="comment.user_identifier"></Comment>
      </b-list-group-item>
    </b-list-group>
  </div>
</template>

<script>
import Comment from "@/components/chairhub/Comment.vue";

export default {
  name: "Comments",
  data() {
    return {
      commentText: "",
      commentList: [{
        "user_identifier": "testUser",
        "comment": "testComment",
      },{
        "user_identifier": "testUser",
        "comment": "testComment",
      }],
    };
  },
  components: {
    Comment
  },
  watch: {},
  computed: {
    isCommentTextEmpty() {
      return (this.commentText.trim() == "");
    }
  },
  methods: {
    addComments(evt) {
      // Prevent auto refresh of page
      evt.preventDefault();

      console.log("add comment");
      console.log(this.commentText);
      //TODO: call API to add comments here
    }
  },
  mounted() {
  }
};
</script>

<style>

</style>