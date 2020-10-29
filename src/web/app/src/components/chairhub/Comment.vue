<template>
  <div class="comment-container">
    <b-row class="mb-2 mx-0">
      <h6 class="comment-heading col-10" v-text="creator"></h6>
      <b-button
        v-if="canDelete"
        @click="showDeleteConfirmation"
        variant="outline-delete"
        size="sm"
        class="mx-0 ml-auto"
      >
        <b-icon icon="trash-fill"></b-icon>
      </b-button>
    </b-row>
    <b-row v-if="!editMode" class="mx-0 my-2">
      <p class="fade" v-text="currentComment"></p>
    </b-row>
    <b-row v-if="editMode" class="mx-0">
      <b-form-textarea
        class="px-1 py-0"
        v-model="currentComment"
      ></b-form-textarea>
    </b-row>
    <b-row class="comment-footer">
      <b-button
        v-if="!editMode && canEdit"
        @click="enterEditMode"
        variant="outline-edit"
        size="sm"
        class="mr-1"
      >
        Edit
      </b-button>
      <b-button
        v-if="editMode"
        @click="exitEditMode"
        variant="outline-cancel"
        size="sm"
        class="mr-1"
      >
        Cancel
      </b-button>
      <b-button
        v-if="editMode"
        @click="saveComment(currentComment)"
        variant="outline-secondary"
        size="sm"
      >
        Save
      </b-button>
      <small class="ml-auto text-secondary">3 days ago</small>
    </b-row>
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
      currentComment: "",
      originalComment: "",
      editMode: false
    };
  },
  watch: {},
  computed: {
    canDelete() {
      return this.$store.state.userInfo.userEmail === this.creator;
    },
    canEdit() {
      return this.$store.state.userInfo.userEmail === this.creator;
    }
  },
  methods: {
    enterEditMode() {
      this.editMode = true;
    },
    exitEditMode() {
      this.editMode = false;
      this.currentComment = this.originalComment;
    },
    saveComment(editedComment) {
      this.$store
        .dispatch("editCommentForPresentation", {
          presentationId: this.presentationId,
          id: this.id,
          editedComment
        })
        .then(() => {
          this.exitEditMode();
          this.originalComment = editedComment;
          this.currentComment = editedComment;
        });
    },
    deleteComment() {
      this.$store.dispatch("deleteCommentForPresentation", {
        presentationId: this.presentationId,
        id: this.id
      });
    },
    showDeleteConfirmation() {
      this.$bvModal
        .msgBoxConfirm("Delete comment?", {
          title: "Please Confirm",
          size: "sm",
          buttonSize: "md",
          okVariant: "delete",
          okTitle: "Yes",
          cancelTitle: "No",
          footerClass: "p-2",
          centered: true
        })
        .then(value => {
          if (value) this.deleteComment();
        });
    }
  },
  mounted() {
    this.originalComment = this.comment;
    this.currentComment = this.comment;
  }
};
</script>
<style lang="scss" scoped>
.fade {
  animation: fadeinout 1s ease-in-out 0s 1 forwards;
  @keyframes fadeinout {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
}
</style>
<style lang="scss">
.comment-container {
  transition: 0.4s all ease-out;
  background-color: $white;
  border: 1px solid $gray-400;
  border-radius: 5px;
  padding: 0.9rem 1rem;
}
.comment-heading {
  padding: 0;
  margin: 1px 0 1px 0;
  color: $teal-600;
  font-size: 0.9rem;
  font-weight: 600;
}
.comment-body {
}
.comment-footer {
  margin: 0.5rem 0 0 0;
  text-align: center;
}
.btn-delete {
  @include button-variant(
    $red-500,
    $red-500,
    darken($red-500, 7.5%),
    lighten($red-500, 5%),
    $red-500
  );
}
.btn-outline-delete {
  @include button-outline-variant(
    $red-500,
    #fff,
    lighten($red-500, 5%),
    $red-500
  );
}
.btn-outline-save {
  @include button-outline-variant(
    $indigo-600,
    #fff,
    lighten($indigo-600, 5%),
    $indigo-600
  );
}
.btn-outline-edit {
  @include button-outline-variant(
    $indigo-600,
    #fff,
    lighten($indigo-600, 5%),
    $indigo-600
  );
}
.btn-outline-cancel {
  @include button-outline-variant(
    $indigo-600,
    #fff,
    lighten($indigo-600, 5%),
    $indigo-600
  );
}
</style>
