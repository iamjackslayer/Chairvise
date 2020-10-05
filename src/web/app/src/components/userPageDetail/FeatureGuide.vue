<template>
  <div class="accordion" role="tablist">
    <b-card no-body>
      <b-card-header
        v-b-toggle.feature1
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Word cloud for all submissions keywords
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="feature1" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/wordCloudSubmissions.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.feature2
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Submission rank author/country/organization
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="feature2" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/SubmissionRankViz.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.feature3
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Accepted submission organization rank
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="feature3" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/AcceptedSubmissionOrgRank.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.feature4
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Review Score Distribution/weighted evaluation score statistic summary
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="feature4" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/ReviewScoreDistributionViz.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.feature5
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Reviewer Expertise Level Statistic Summary
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="feature5" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/ReviewerExpertiseLevelSummary.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.feature6
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Number of Review Assignment based on Confidence Level using Scatter
        Chart
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="feature6" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/scatterChart.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.feature7
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Co-authorship
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="feature7" role="tabpanel">
        <b-card-body>
          <ul>
            <li>
              <p>Graph Network</p>
              <img
                class="img-fluid"
                src="@/assets/userGuide/graphNetworkCoAuthorViz.png"
              />
            </li>
            <li>
              <p>Dependency Wheel</p>
              <img
                class="img-fluid"
                src="@/assets/userGuide/dependencyWheelCoAuthorViz.png"
              />
            </li>
            <li>
              <p>Radar Chart</p>
              <img
                class="img-fluid"
                src="@/assets/userGuide/radarChartCoAuthorViz.png"
              />
            </li>
          </ul>
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.feature8
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Enable data persistence
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="feature8" role="tabpanel">
        <b-card-body></b-card-body>
      </b-collapse>
    </b-card>
  </div>
</template>

<script>
export default {
  name: "FeatureGuide",
  data() {
    return {
      activeNames: []
    };
  }
};
</script>

<style lang="scss" scoped></style>
