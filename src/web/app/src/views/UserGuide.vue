<template>
<!-- indentation lel -->
  <div>
    <div>
          <b-tabs :tab-position="tabPosition" active>
            <b-tab title="Overview">
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
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
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
<style scoped></style>
