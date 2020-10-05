<template>
  <div class="accordion" role="tablist">
    <b-card no-body>
      <b-card-header
        v-b-toggle.1
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Word cloud for all submissions keywords
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="1" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/wordCloudSubmissions.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.2
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Submission rank author/country/organization
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="2" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/SubmissionRankViz.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.3
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Accepted submission organization rank
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="3" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/AcceptedSubmissionOrgRank.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.4
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Review Score Distribution/weighted evaluation score statistic summary
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="4" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/ReviewScoreDistributionViz.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.5
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Reviewer Expertise Level Statistic Summary
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="5" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/ReviewerExpertiseLevelSummary.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.6
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Number of Review Assignment based on Confidence Level using Scatter
        Chart
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="6" role="tabpanel">
        <b-card-body>
          <img src="@/assets/userGuide/scatterChart.png" />
        </b-card-body>
      </b-collapse>
    </b-card>

    <b-card no-body>
      <b-card-header
        v-b-toggle.7
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Co-authorship
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="7" role="tabpanel">
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
        v-b-toggle.8
        header-tag="header"
        class="py-2 px-3 guide-header"
        role="tab"
      >
        Enable data persistence
        <b-icon class="collapsible-icon" icon="chevron-down"></b-icon>
      </b-card-header>
      <b-collapse id="8" role="tabpanel">
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
