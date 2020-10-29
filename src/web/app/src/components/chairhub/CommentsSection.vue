<template>
  <b-container class="comments-section">
    <h3 class="cs-heading d-none d-md-block">Create a comment</h3>
    <b-form @submit="onCreateComment">
      <b-form-textarea
        id="textarea"
        v-model="commentFormComment"
        placeholder="Enter something..."
        rows="3"
        max-rows="6"
      ></b-form-textarea>
      <div class="d-flex justify-content-end py-3">
        <b-button type="submit" :disabled="isCommentTextEmpty" variant="create">Create</b-button>
      </div>
    </b-form>
    <h3 class="cs-list-heading" v-if="commentList.length > 0">Comments</h3>
    <b-list-group>
      <Comment
        v-for="comment in commentList"
        :key="comment.id"
        class="my-1 comment-item"
        :id="comment.id"
        :presentationId="presentationId"
        :comment="comment.comment"
        :creator="comment.userIdentifier"
        :createdDate="comment.createdDate"
        :updatedDate="comment.updatedDate"
      ></Comment>
    </b-list-group>
  </b-container>
</template>

<script>
import Comment from "@/components/chairhub/Comment.vue";

export default {
  props: {
    presentationId: String
  },
  name: "Comments",
  data() {
    return { scrolling: false };
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
    fetchComments() {
      this.$store.dispatch(
        "getCommentListForPresentation",
        this.presentationId
      );
    },
    clearTextField() {
      this.commentFormComment = "";
    },
    async addComment(evt) {
      // Prevent auto refresh of page
      evt.preventDefault();

      this.$store
        .dispatch(
          "addCommentForPresentation",
          this.$store.state.presentation.presentationForm.id
        )
        .then(() => {
          this.$store.dispatch("clearCommentField");
        });
    },
    async onCreateComment(evt) {
      evt.preventDefault();
      await this.addComment(evt);
      // console.log(document.body.scrollHeight);
      this.scrolling = true;
    },
    scrollToLastComment() {
      const comments = document.getElementsByClassName("comment-item");
      const len = comments.length;
      comments[len - 1].scrollIntoView({
        behavior: "smooth",
        block: "end"
      });
    }
  },
  mounted() {
    // fetch all comments for this presentation
    this.fetchComments();
  },
  updated() {
    this.scrollToLastComment();
  }
};
</script>

<style lang="scss" scoped>
.animate-show {
  opacity: 1;
}
.list-enter {
  opacity: 0;
  transform: scale(0);
}
.list-leave-active {
  transition: all 0.4s ease;
}
.list-enter-active {
  transition: all 0.4s ease;
}
.list-leave-to {
  opacity: 0;
  transform: scale(0);
}
.cs-heading {
  color: $gray-700;
  font-size: 1.5rem;
}
.cs-list-heading {
  color: $gray-700;
  font-size: 1.4rem;
}
.comment-item {
}
.comments-section {
  margin: 20px 0 0 0;
  padding: 0;
  width: 100%;
  max-width: 100%;
}
.btn-create {
  @include button-variant($gray-700, #fff, lighten($gray-700, 5%), $gray-700);
}
</style>
