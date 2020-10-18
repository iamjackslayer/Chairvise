<template>
  <b-col fluid class="banner-card">
    <h6 class="card--title">{{ title }}</h6>
    <h6 class="card--figure">{{ figure }}</h6>
    <h6 class="card--subfigure my-0">{{ subfigure }}</h6>
    <h6 class="card--subtitle my-0">{{ subtitle }}</h6>
  </b-col>
</template>
<script>
export default {
  name: "BannerCard",
  props: {
    title: String,
    figure: String,
    subfigure: String,
    subtitle: String
  }
};
</script>
<style lang="scss" scoped>
.banner-card {
  background-color: white;
  border: 1.7px solid $gray-400;
  border-radius: 0.7rem;
  padding: 1rem;
  margin: 0.4rem 0.8rem;
  min-width: 9rem;
  max-width: 30rem;
  .card--title {
    font-size: 0.9rem;
    font-weight: bold;
    text-transform: uppercase;
    color: $gray-600;
  }
  .card--figure {
    font-size: 1.9rem;
    font-weight: bold;
  }
  .card--subfigure {
    font-size: 1rem;
    color: $gray-700;
  }
  .card--subtitle {
    font-size: 1rem;
    color: $gray-700;
  }
  h6 {
    text-align: center;
    line-height: 1.7;
  }
}
</style>
