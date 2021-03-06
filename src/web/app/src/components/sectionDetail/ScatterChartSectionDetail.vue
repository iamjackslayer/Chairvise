<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :conference="conference"
    :extraFormItemsRules="extraFormItemsRules"
    @update-visualisation="updateVisualisation"
  >
    <scatter-chart :chart-data="chartData" :options="options"></scatter-chart>

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

      <b-form-group label="Colorful Bar" v-if="slotProps.isInAdvancedMode">
        <b-form-checkbox
          v-model="slotProps.extraData.isColorfulBar"
          switch
          size="lg"
        />
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
import ScatterChart from "@/components/sectionDetail/chart/ScatterChart.vue";
import BasicSectionDetail from "@/components/sectionDetail/BasicSectionDetail.vue";
import { generateBorderColor, generateBackgroundColor } from "@/common/color";

export default {
  name: "ScatterChartSectionDetail",

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

      var xyArray = [];
      for (var i = 0; i < this.partialResult.length; i++) {
        xyArray.push({
          x: this.partialResult[i][extraData.xAxisFieldName],
          y: this.partialResult[i][extraData.yAxisFieldName]
        });
      }
      // process y axis
      this.dataset = {
        borderWidth: 1,
        label: extraData.dataSetLabel,
        data: xyArray,
        pointRadius: 5,
        pointBackgroundColor: generateBackgroundColor(
          this.partialResult.length
        ),
        pointBorderColor: generateBorderColor(this.partialResult.length)
      };
      // generate color
      if (extraData.isColorfulBar) {
        this.dataset.pointBackgroundColor = generateBackgroundColor(
          this.partialResult.length
        );
        this.dataset.pointBorderColor = generateBorderColor(
          this.partialResult.length
        );
      } else {
        // choose a color in random
        this.dataset.pointBackgroundColor = generateBackgroundColor(
          this.partialResult.length
        )[this.partialResult.length - 1];
        this.dataset.pointBorderColor = generateBorderColor(
          this.partialResult.length
        )[this.partialResult.length - 1];
      }

      // to display more data
      let toolTipLabelCallback = tooltipItems => {
        return extraData.dataSetLabel + ": " + tooltipItems.yLabel;
      };
      let toolTipTitleCallback = tooltipItems => {
        let currentIndex = tooltipItems[0].index;
        return extraData.fieldsShownInToolTips.map(
          f => `${f.label}: ${this.partialResult[currentIndex][f.field]}`
        );
      };

      // process tooltip callback
      this.options = {
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true
              },
              gridLines: {
                display: true
              }
            }
          ],
          xAxes: [
            {
              gridLines: {
                display: false
              },
              ticks: {
                beginAtZero: true,
                stepSize: 1
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
        tooltips: {
          mode: "index",
          callbacks: {
            title: toolTipTitleCallback,
            label: toolTipLabelCallback
          }
        },
        plugins: {
          datalabels: {
            anchor: "end",
            align: "end",
            // show both value and percentage
            formatter: () => {
              return ``;
            }
          }
        }
      };
    },

    addTooltip(tooltips) {
      tooltips.push({
        label: "",
        field: ""
      });
    },

    removeTooltip(tooltips, tooltipToRemove) {
      let index = tooltips.indexOf(tooltipToRemove);
      tooltips.splice(index, 1);
    }
  },

  components: {
    BasicSectionDetail,
    ScatterChart
  }
};
</script>

<style scoped></style>
