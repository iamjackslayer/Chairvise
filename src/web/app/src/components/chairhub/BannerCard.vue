<template>
  <b-col fluid class="banner-card">
    <h6 class="card--title">{{ title }}</h6>
    <h6 class="card--figure">
      {{ figure === -1 ? "??" : displayFigure }}
    </h6>
    <h6 class="card--subfigure my-0">
      {{ subfigure === -1 ? "??" : subfigure }}
    </h6>
    <h6 class="card--subtitle my-0">{{ subtitle }}</h6>
  </b-col>
</template>
<script>
export default {
  name: "BannerCard",
  props: {
    title: String,
    figure: Number,
    subfigure: Number,
    subtitle: String
  },
  data() {
    return {
      displayFigure: 0
    };
  },
  mounted() {
    this.animateDisplayFigure();
  },
  methods: {
    animateDisplayFigure: function() {
      clearInterval(this.interval);

      if (this.figure == this.displayFigure) {
        return;
      }

      this.interval = window.setInterval(
        function() {
          if (this.displayFigure != this.figure) {
            var change = (this.figure - this.displayFigure) / 10;

            change = change >= 0 ? Math.ceil(change) : Math.floor(change);

            this.displayFigure = this.displayFigure + change;
          }
        }.bind(this),
        100
      );
    }
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
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .card--subfigure {
    font-size: 1rem;
    color: $gray-700;
    overflow: hidden;
    text-overflow: ellipsis;
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
