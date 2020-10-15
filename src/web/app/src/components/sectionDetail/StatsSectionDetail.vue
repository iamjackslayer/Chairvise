<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :conference="conference"
    :extraFormItemsRules="{}"
    @update-visualisation="updateVisualisation"
  >
    <b-table
      outlined
      striped
      hover
      :fields="[
        'type',
        'value',
        { key: 'numIds', label: 'Reviewers' },
        { key: 'reviewer', label: 'Corresponding Reviewer ID' }
      ]"
      :items="tableData"
    >
    </b-table>
    <template slot="extraFormItems" slot-scope="slotProps">
      <b-form-group label="Type" v-if="slotProps.isInAdvancedMode">
        <!-- TODO: Review if need reviewer value -->
        <b-form-select
          v-model="slotProps.extraData.types"
          multiple
          :select-size="4"
          :options="[
            { value: 'min', text: 'Min', reviewer: 'min' },
            { value: 'max', text: 'Max', reviewer: 'max' },
            { value: 'sum', text: 'Sum', reviewer: 'sum' },
            { value: 'avg', text: 'Average', reviewer: 'avg' },
            { value: 'median', text: 'Median', reviewer: 'median' },
            { value: 'std', text: 'Standard Deviation', reviewer: 'std' }
          ]"
        >
        </b-form-select>
      </b-form-group>
    </template>
  </basic-section-detail>
</template>

<script>
import {
  max,
  mean,
  median,
  min,
  standardDeviation,
  sum
} from "simple-statistics";
import BasicSectionDetail from "@/components/sectionDetail/BasicSectionDetail.vue";

export default {
  name: "StatsSectionDetail",

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
      tableData: []
    };
  },

  computed: {
    hasData() {
      return this.tableData.length !== 0;
    }
  },

  methods: {
    updateVisualisation({ result, selections, extraData }) {
      this.tableData = [];
      if (result.length === 0) {
        return;
      }
      let data = result.map(r => r[selections[1].rename]);

      extraData.types.forEach(t => {
        var rIds = [];
        var rId;
        var stringBuilder;
        switch (t) {
          case "min":
            stringBuilder = "";
            rIds = result
              .filter(r => r[selections[1].rename] == min(data))
              .map(r => r[selections[0].rename]);

            for (rId of rIds) {
              stringBuilder = stringBuilder + rId + ", ";
            }
            stringBuilder = stringBuilder.substring(
              0,
              stringBuilder.length - 2
            );

            this.tableData.push({
              type: "Min",
              value: min(data),
              numIds: rIds.length,
              reviewer: stringBuilder
            });
            break;
          case "max":
            stringBuilder = "";
            rIds = result
              .filter(r => r[selections[1].rename] == max(data))
              .map(r => r[selections[0].rename]);
            for (rId of rIds) {
              stringBuilder = stringBuilder + rId + ", ";
            }
            stringBuilder = stringBuilder.substring(
              0,
              stringBuilder.length - 2
            );
            this.tableData.push({
              type: "Max",
              value: max(data),
              numIds: rIds.length,
              reviewer: stringBuilder
            });
            break;
          case "sum":
            this.tableData.push({
              type: "Sum",
              value: sum(data),
              numIds: result.length,
              reviewer: "-"
            });
            break;
          case "avg":
            this.tableData.push({
              type: "Mean",
              value: mean(data).toFixed(2),
              numIds: result.length,
              reviewer: "-"
            });
            break;
          case "median":
            this.tableData.push({
              type: "Median",
              value: median(data),
              numIds: result.length,
              reviewer: "-"
            });
            break;
          case "std":
            this.tableData.push({
              type: "ST.DEV",
              value: standardDeviation(data).toFixed(2),
              numIds: result.length,
              reviewer: "-"
            });
            break;
        }
      });
    }
  },

  components: {
    BasicSectionDetail
  }
};
</script>

<style scoped>
.hovered-text {
  color: #606266;
}
</style>
