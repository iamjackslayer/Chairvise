<template>
  <div>
    <div class="title-bar">
      <h1 class="title">User Guide</h1>
    </div>
    <b-card no-body>
      <b-tabs :tab-position="tabPosition" card>
        <b-tab title="Overview" active>
          <OverviewGuide />
        </b-tab>
        <b-tab title="Features">
          <FeatureGuide />
        </b-tab>
        <b-tab title="Instructions">
          <InstructionsGuide />
        </b-tab>
        <b-tab title="FAQ">
          <FAQGuide />
        </b-tab>
      </b-tabs>
    </b-card>
  </div>
</template>

<script>
import OverviewGuide from "@/components/userPageDetail/OverviewGuide.vue";
import FeatureGuide from "@/components/userPageDetail/FeatureGuide.vue";
import InstructionsGuide from "@/components/userPageDetail/InstructionsGuide.vue";
import FAQGuide from "@/components/userPageDetail/FAQGuide.vue";

export default {
  name: "userGuide",
  components: {
    OverviewGuide,
    FeatureGuide,
    InstructionsGuide,
    FAQGuide
  },
  data() {
    return {
      tabPosition: "top"
    };
  }
};
</script>
<style lang="scss" scoped>
.display-card > .tabs > .tab-content > .card-body {
  padding: 2rem;
}
</style>
