<template>
  <div class="p-4">
    <h2>Welcome to ChairVise</h2>
    <p>
      This project is designed to enable conference program chairpersons to
      visualize and share conference submission statistics. By parsing
      information in different formats, we aim to assist user to obtain the most
      value out of the information uploaded. We also support sharing and
      exporting of such visualization.
    </p>
    <p>We have provided 4 different panels for our user guide:</p>
    <ol>
      <li>Overview</li>
      <li>Features - To describe the features that our system can provide</li>
      <li>
        Instructions - To provide a detailed instructions on how to use
        ChairVise features
      </li>
      <li>FAQ - To list out commonly asked questions by user</li>
    </ol>
  </div>
</template>

<script>
export default {
  name: "OverviewGuide",
  props: {
    msg: String
  }
};
</script>

<style lang="scss" scoped>
h2 {
  font-weight: 700;
  margin-bottom: 1rem;
}

h3 {
  font-weight: normal;
}

img {
  width: 100%;
}
</style>
