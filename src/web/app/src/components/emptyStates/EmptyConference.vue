<template>
  <div>
    <div class="empty-image-container">
      <img class="empty-image" src="@/assets/emptyguide2.svg" />
    </div>
    <div class="mt-2 empty-container">
      <div class="empty-title">Create a Conference</div>
      <div class="empty-description">
        A conference easily allows you to manage and keep track of conference
        data for the specified conference.
      </div>

      <b-button
        class="empty-action responsive-btn"
        variant="primary"
        @click="createConference"
      >
        <b-icon icon="plus"></b-icon> Add New Conference
      </b-button>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    createConference() {
      this.$router.push("/conference/add");
    }
  }
};
</script>
