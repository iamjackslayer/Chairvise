<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :conference="conference"
    @update-visualisation="updateVisualisation"
  >
    <line-chart :chart-data="chartData" :options="options"></line-chart>

    <!-- TODO: Replace validation. -->
    <template slot="extraFormItems" slot-scope="slotProps">
      <b-form-group label="xAxis Field Name" v-if="slotProps.isInAdvancedMode">
        <b-form-select v-model="slotProps.extraData.xAxisFieldName">
          <b-form-select-option
            v-for="selection in slotProps.editForm.selections"
            :key="selection.rename"
            :label="selection.rename"
            :value="selection.rename"
          ></b-form-select-option>
        </b-form-select>
      </b-form-group>

      <b-form-group label="yAxis Field Name" v-if="slotProps.isInAdvancedMode">
        <b-form-select v-model="slotProps.extraData.yAxisFieldName">
          <b-form-select-option
            v-for="selection in slotProps.editForm.selections"
            :key="selection.rename"
            :label="selection.rename"
            :value="selection.rename"
          ></b-form-select-option>
        </b-form-select>
      </b-form-group>

      <b-form-group label="Legend Label Name" v-if="slotProps.isInAdvancedMode">
        <b-form-input
          v-model="slotProps.extraData.dataSetLabel"
          placeholder="Label Name"
        />
      </b-form-group>
    </template>
  </basic-section-detail>
</template>

<script>
import LineChart from "@/components/sectionDetail/chart/LineChart.vue";
import BasicSectionDetail from "@/components/sectionDetail/BasicSectionDetail.vue";
import { generateBorderColor, generateBackgroundColor } from "@/common/color";

export default {
  name: "LineChartSectionDetail",

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
      type: String,
      required: true
    }
  },

  data() {
    return {
      labels: [],
      dataset: {},
      options: {}
    };
  },

  computed: {
    hasData() {
      return this.labels.length !== 0;
    },

    chartData() {
      return {
        labels: this.labels,
        datasets: [this.dataset]
      };
    }
  },

  methods: {
    updateVisualisation({ result, extraData }) {
      // process x axis
      this.labels = result.map(record => record[extraData.xAxisFieldName]);

      // process y axis
      this.dataset = {
        borderWidth: 1,
        label: extraData.dataSetLabel,
        data: result.map(record => record[extraData.yAxisFieldName]),
        backgroundColor: generateBackgroundColor(2)[1],
        borderColor: generateBorderColor(2)[1]
      };

      this.options = {
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: false
              },
              gridLines: {
                display: true
              }
            }
          ],
          xAxes: [
            {
              ticks: {
                autoSkip: false
              },
              gridLines: {
                display: false
              }
            }
          ]
        },
        legend: {
          display: true,
          position: "bottom"
        },
        layout: {
          padding: {
            top: 30
          }
        },
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          datalabels: {
            anchor: "end",
            align: "end"
          }
        }
      };
    }
  },

  components: {
    BasicSectionDetail,
    LineChart
  }
};
</script>

<style scoped></style>
