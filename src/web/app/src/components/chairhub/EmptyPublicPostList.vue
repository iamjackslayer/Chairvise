<template>
  <b-container>
    <b-row>
      <section class="text-center w-100">
        <div class="icon-throbbing">
          <b-icon
            class="mail-icon"
            icon="envelope-open"
            font-scale="7"
          ></b-icon>
        </div>
        <h2 class="my-4">No public posts</h2>
        <b-button @click="createPresentation" class="btn-create"
          >Create a public post</b-button
        >
      </section>
    </b-row>
  </b-container>
</template>
<script>
export default {
  methods: {
    createPresentation() {
      this.$router.push("/presentation/create");
    }
  },
  mounted() {}
};
</script>
<style lang="scss" scoped>
section {
  padding: 1.5rem;
  border: 2.7px dashed $gray-600;
  border-radius: 1rem;
  .mail-icon {
    color: $gray-600;
  }
}
.btn-create {
  background-color: $gray-600;
  border-width: 0;
  padding: 0.6rem 1rem;
  &:hover {
    background-color: $gray-500;
  }
}
.icon-throbbing {
  animation: 0.7s ease-in-out 0s infinite alternate throbbing;
}
</style>
<style lang="scss">
@keyframes throbbing {
  from {
    transform: scale(0.9);
  }
  to {
    transform: scale(1);
  }
}
</style>
