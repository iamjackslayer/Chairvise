<template>
  <div class="page">
    <h1>Features</h1>
    <b-row class="feature-list" cols="1" cols-lg="3">
      <b-col>
        <b-card class="feature">
          <b-icon
            class="h1 rounded-circle"
            variant="primary"
            icon="cloud-arrow-up-fill"
          ></b-icon>
          <h4>Upload conference data</h4>
          <p>
            Easily import data in various supported formats like SoftConf and
            EasyChair.
          </p>
        </b-card>
      </b-col>
      <b-col>
        <b-card class="feature">
          <b-icon
            class="h1 rounded-circle"
            variant="primary"
            icon="file-image-fill"
          ></b-icon>
          <h4>Visualise your data</h4>
          <p>
            Create presentations filled with charts that make sense of the data
            you have.
          </p>
        </b-card>
      </b-col>
      <b-col>
        <b-card class="feature">
          <b-icon
            class="h1 rounded-circle"
            variant="primary"
            icon="people-fill"
          ></b-icon>
          <h4>Share data with others</h4>
          <p>
            Share presentations either publicly through ChairHub or privately
            with your peers.
          </p>
        </b-card>
      </b-col>
    </b-row>
  </div>
</template>

<script>
export default {
  name: "FeatureListDetail",
  props: {
    msg: String
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.page {
  margin-top: 3rem;
}

.col > .bcard {
  background-color: transparent;
  border: none;
}

.feature-list {
  margin-top: 1rem;
}

.feature {
  img {
    height: 100px;
  }

  h4 {
    margin-top: 1rem;
  }

  p {
    color: $gray-600;
  }
}

.card {
  background-color: $gray-200;
  border-radius: 0.3rem;
  border-style: none;
}
</style>
