<template>
  <div v-if="conference !== undefined && typeof conference != 'undefined'">
    <div v-if="sectionDetail.type === WORD_CLOUD">
      <word-cloud-section-detail
        :sectionDetail="sectionDetail"
        :presentationId="presentationId"
        :conference="conference"
      />
    </div>
    <div v-else-if="sectionDetail.type === BAR_CHART">
      <bar-chart-section-detail
        :sectionDetail="sectionDetail"
        :presentationId="presentationId"
        :conference="conference"
      />
    </div>
    <div v-else-if="sectionDetail.type === PIE_CHART">
      <pie-chart-section-detail
        :sectionDetail="sectionDetail"
        :presentationId="presentationId"
        :conference="conference"
      />
    </div>
    <div v-else-if="sectionDetail.type === LINE_CHART">
      <line-chart-section-detail
        :sectionDetail="sectionDetail"
        :presentationId="presentationId"
        :conference="conference"
      />
    </div>
    <div v-else-if="sectionDetail.type === STATS">
      <stats-section-detail
        :sectionDetail="sectionDetail"
        :presentationId="presentationId"
        :conference="conference"
      />
    </div>
    <div v-else-if="sectionDetail.type === SCATTER_CHART">
      <scatter-chart-section-detail
        :sectionDetail="sectionDetail"
        :presentationId="presentationId"
        :conference="conference"
      />
    </div>
    <div v-else-if="sectionDetail.type === GRAPH_NETWORK">
      <graph-network-section-detail
        :sectionDetail="sectionDetail"
        :presentationId="presentationId"
        :conference="conference"
      />
    </div>
    <div v-else-if="sectionDetail.type === RADAR_CHART">
      <radar-chart-section-detail
        :sectionDetail="sectionDetail"
        :presentationId="presentationId"
        :conference="conference"
      />
    </div>
    <div v-else-if="sectionDetail.type === DEPENDENCY_CHART">
      <dependency-chart-section-detail
        :sectionDetail="sectionDetail"
        :presentationId="presentationId"
        :conference="conference"
      />
    </div>
    <div v-else>
      <b-alert show variant="danger">
        <b-icon
          class="alert-icon"
          icon="exclamation-circle-fill"
          variant="danger"
        />
        Unexpected Section Detail Type: {{ sectionDetail.type }}
      </b-alert>
    </div>
  </div>
  <div v-else>
    <b-alert show variant="danger">
      <b-icon
        class="alert-icon"
        icon="exclamation-circle-fill"
        variant="danger"
      />
      Unexpected Conference
    </b-alert>
  </div>
</template>

<script>
import WordCloudSectionDetail from "@/components/sectionDetail/WordCloudSectionDetail.vue";
import BarChartSectionDetail from "@/components/sectionDetail/BarChartSectionDetail.vue";
import PieChartSectionDetail from "@/components/sectionDetail/PieChartSectionDetail.vue";
import LineChartSectionDetail from "@/components/sectionDetail/LineChartSectionDetail.vue";
import StatsSectionDetail from "@/components/sectionDetail/StatsSectionDetail.vue";
import ScatterChartSectionDetail from "@/components/sectionDetail/ScatterChartSectionDetail.vue";
import GraphNetworkSectionDetail from "@/components/sectionDetail/GraphNetworkSectionDetail.vue";
import RadarChartSectionDetail from "@/components/sectionDetail/RadarChartSectionDetail.vue";
import DependencyChartSectionDetail from "@/components/sectionDetail/DependencyChartSectionDetail.vue";
import {
  SECTION_TYPE_BAR_CHART,
  SECTION_TYPE_LINE_CHART,
  SECTION_TYPE_PIE_CHART,
  SECTION_TYPE_STATS,
  SECTION_TYPE_WORD_CLOUD,
  SECTION_TYPE_SCATTER_CHART,
  SECTION_TYPE_GRAPH_NETWORK,
  SECTION_TYPE_RADAR_CHART,
  SECTION_TYPE_DEPENDENCY_CHART
} from "@/common/const";

export default {
  props: {
    sectionDetail: {
      type: Object,
      required: true
    },
    presentationId: {
      type: String,
      required: true
    },
    conference: {
      type: String
    }
  },
  data() {
    return {
      // constant
      WORD_CLOUD: SECTION_TYPE_WORD_CLOUD,
      BAR_CHART: SECTION_TYPE_BAR_CHART,
      PIE_CHART: SECTION_TYPE_PIE_CHART,
      LINE_CHART: SECTION_TYPE_LINE_CHART,
      STATS: SECTION_TYPE_STATS,
      SCATTER_CHART: SECTION_TYPE_SCATTER_CHART,
      GRAPH_NETWORK: SECTION_TYPE_GRAPH_NETWORK,
      RADAR_CHART: SECTION_TYPE_RADAR_CHART,
      DEPENDENCY_CHART: SECTION_TYPE_DEPENDENCY_CHART
    };
  },
  components: {
    WordCloudSectionDetail,
    BarChartSectionDetail,
    PieChartSectionDetail,
    LineChartSectionDetail,
    StatsSectionDetail,
    ScatterChartSectionDetail,
    GraphNetworkSectionDetail,
    RadarChartSectionDetail,
    DependencyChartSectionDetail
  }
};
</script>

<style scoped>
.sectionDetail {
  margin-bottom: 30px;
}
</style>
