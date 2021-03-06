<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :conference="conference"
    :extraFormItemsRules="extraFormItemsRules"
    @update-visualisation="updateVisualisation"
  >
    <dependency-chart :chart-data="chartData"></dependency-chart>

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

      <b-form-group
        label="xAxis Field Name 2"
        v-if="slotProps.isInAdvancedMode"
      >
        <b-form-select v-model="slotProps.extraData.xAxisFieldName2">
          <b-form-select-option
            v-for="selection in slotProps.editForm.selections"
            :key="selection.rename"
            :label="selection.rename"
            :value="selection.rename"
          ></b-form-select-option>
        </b-form-select>
        <b-form-invalid-feedback
          :state="
            slotProps.vuelidate.editForm.extraData.xAxisFieldName2.required
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

      <!-- TODO: Review. This is bugged, not linked with any of the fields -->
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
import DependencyChart from "@/components/sectionDetail/chart/DependencyChart.vue";
import BasicSectionDetail from "@/components/sectionDetail/BasicSectionDetail.vue";
import { generateBorderColor, generateBackgroundColor } from "@/common/color";

export default {
  name: "DependencyChartSectionDetail",

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
        xAxisFieldName2: { required },
        yAxisFieldName: { required }
      },

      labels: [],
      labels2: [],
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
        labels2: this.labels2,
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
      this.labels2 = this.partialResult.map(
        record => record[extraData.xAxisFieldName2]
      );

      // process y axis
      this.dataset = {
        borderWidth: 1,
        label: extraData.dataSetLabel,
        data: this.partialResult.map(
          record => record[extraData.yAxisFieldName]
        ),
        backgroundColor: generateBackgroundColor(this.partialResult.length),
        borderColor: generateBorderColor(this.partialResult.length)
      };

      // generate color
      if (extraData.isColorfulBar) {
        this.dataset.backgroundColor = generateBackgroundColor(
          this.partialResult.length
        );
        this.dataset.borderColor = generateBorderColor(
          this.partialResult.length
        );
      } else {
        // choose a color in random
        this.dataset.backgroundColor = generateBackgroundColor(
          this.partialResult.length
        )[this.partialResult.length - 1];
        this.dataset.borderColor = generateBorderColor(
          this.partialResult.length
        )[this.partialResult.length - 1];
      }

      // to display more data
      let toolTipFooterCallback = tooltipItems => {
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
                autoSkip: false
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
          callbacks: {
            footer: toolTipFooterCallback
          }
        },
        plugins: {
          datalabels: {
            anchor: "end",
            align: "end"
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
    DependencyChart
  }
};
</script>

<style scoped></style>
