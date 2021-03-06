<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :conference="conference"
    :extraFormItemsRules="extraFormItemsRules"
    @update-visualisation="updateVisualisation"
  >
    <radar-chart :chart-data="chartData" :options="options"></radar-chart>

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
        <b-form-invalid-feedback
          :state="
            slotProps.vuelidate.editForm.extraData.xAxisFieldName.required
          "
        >
          There should be one field to map x axis
        </b-form-invalid-feedback>
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
        <b-form-invalid-feedback
          :state="
            slotProps.vuelidate.editForm.extraData.yAxisFieldName.required
          "
        >
          There should be one field to map y axis
        </b-form-invalid-feedback>
      </b-form-group>

      <b-form-group label="Legend Label Name" v-if="slotProps.isInAdvancedMode">
        <b-form-input v-model="slotProps.extraData.dataSetLabel">
        </b-form-input>
      </b-form-group>

      <b-form-group
        :label="
          'Num of result to display: ' +
            slotProps.extraData.numOfResultToDisplay
        "
        v-if="slotProps.isInAdvancedMode"
      >
        <b-form-input
          v-model="slotProps.extraData.numOfResultToDisplay"
          type="range"
          min="5"
          max="30"
        ></b-form-input>
      </b-form-group>

      <!-- TODO: Review. This is bugged, tooltip doesn't reflect in chart -->
      <div v-if="slotProps.isInAdvancedMode">
        <b-form-group
          v-for="(tooltip, index) in slotProps.extraData.fieldsShownInToolTips"
          :label="'Tooltips ' + index"
          :key="'tooltips' + index"
        >
          <b-form-select
            placeholder="Field"
            v-model="tooltip.field"
            class="w-auto mr-2"
          >
            <b-form-select-option
              v-for="selection in slotProps.editForm.selections"
              :key="selection.rename"
              :label="selection.rename"
              :value="selection.rename"
            >
            </b-form-select-option>
          </b-form-select>
          <b-form-input
            class="d-inline-block mr-2 align-middle"
            v-model="tooltip.label"
            placeholder="Label Name"
            style="width: 200px"
          ></b-form-input>
          <b-button
            variant="outline-danger"
            @click="
              removeTooltip(slotProps.extraData.fieldsShownInToolTips, tooltip)
            "
          >
            <b-icon icon="trash-fill" aria-hidden="true" />
          </b-button>
        </b-form-group>
      </div>

      <b-form-group v-if="slotProps.isInAdvancedMode">
        <b-button
          variant="primary"
          @click="addTooltip(slotProps.extraData.fieldsShownInToolTips)"
        >
          Add Tooltip
        </b-button>
      </b-form-group>
    </template>
  </basic-section-detail>
</template>

<script>
import { required } from "vuelidate/lib/validators";
import RadarChart from "@/components/sectionDetail/chart/RadarChart.vue";
import BasicSectionDetail from "@/components/sectionDetail/BasicSectionDetail.vue";
import { generateBorderColor, generateBackgroundColor } from "@/common/color";

export default {
  name: "RadarChartSectionDetail",

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
      extraFormItemsRules: {
        xAxisFieldName: { required },
        yAxisFieldName: { required }
      },

      labels: [],
      dataset: {},
      partialResult: [],
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
      this.partialResult = result.slice(0, extraData.numOfResultToDisplay);
      // process x axis
      this.labels = this.partialResult.map(
        record => record[extraData.xAxisFieldName]
      );

      // process y axis
      this.dataset = {
        borderWidth: 2,
        data: this.partialResult.map(
          record => record[extraData.yAxisFieldName]
        ),
        backgroundColor: "rgba(175, 175, 175, 0.3)",
        borderColor: "rgb(175, 175, 175, 0.6)",
        pointRadius: 4,
        pointBorderColor: generateBorderColor(this.partialResult.length),
        pointBackgroundColor: generateBackgroundColor(this.partialResult.length)
      };

      this.options = {
        legend: false,
        labels: {
          display: false
        },
        layout: {
          padding: 30
        },
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
          display: false
        },
        plugins: {
          datalabels: {
            backgroundColor: generateBackgroundColor(this.partialResult.length),
            anchor: "end",
            align: "end"
          }
        },
        scale: {
          ticks: {
            display: false
          },
          pointLabels: {
            callback: value => {
              if (value.toString().length > 24) {
                return value.toString().substr(0, 23) + "..."; //truncate
              } else {
                return value;
              }
            }
          }
        }
      };
    }
  },

  components: {
    BasicSectionDetail,
    RadarChart
  }
};
</script>
