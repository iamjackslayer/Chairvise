<template>
  <div class="emptySection">
    <b-row>
      <b-col>
        <div class="icon">
          <img src="@/assets/businessman.png" />
        </div>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <p>
          <i>
            No conference found.
          </i>
        </p>
        <p>
          Let's get started by adding a new conference.
        </p>
        <b-button variant="primary" @click="createConference">
          <b-icon icon="plus" font-scale="1"></b-icon>Add New
          Conference</b-button
        >
      </b-col>
    </b-row>
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

<style scoped>
.icon {
  margin-left: 04.5rem;
  margin-bottom: 0.25rem;
}
.emptySection {
  text-align: center;
  vertical-align: middle;
}
</style>
