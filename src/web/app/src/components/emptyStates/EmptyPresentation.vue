<template>
  <div>
    <div class="empty-image-container">
      <img class="empty-image" src="@/assets/emptyguide.svg" />
    </div>
    <div class="mt-2 empty-container">
      <div class="empty-title">Create a Presentation</div>
      <div class="empty-description">
        A presentation easily allows you to create useful charts generated from
        a conference's data and can be shared with other users.
      </div>

      <b-button
        class="empty-action responsive-btn"
        variant="primary"
        @click="createPresentation"
      >
        <b-icon icon="plus"></b-icon> Add New Presentation
      </b-button>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    createPresentation() {
      this.$router.push("/presentation/create");
    }
  }
};
</script>

<style lang="scss" scoped></style>
