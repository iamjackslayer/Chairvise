<template>
  <div>
    <div class="empty-image-container">
      <img class="empty-image" src="@/assets/404guide.svg" />
    </div>
    <div class="mt-2 empty-container">
      <div class="empty-title">404 Page Not Found</div>
      <div class="empty-description">
        Oops, we can't seem to find the page that you're looking for...
      </div>

      <b-button
        class="empty-action responsive-btn"
        variant="primary"
        @click="returnHome"
      >
        <b-icon icon="arrow-left-short"></b-icon> Return to Homepage
      </b-button>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    returnHome() {
      this.$router.push("/");
    }
  }
};
</script>
