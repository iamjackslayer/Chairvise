<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :conference="conference"
    :edit-form-involved-records-rule="editFormInvolvedRecordsRule"
    :extraFormItemsRules="extraFormItemsRules"
    @update-visualisation="updateVisualisation"
  >
    <word-cloud :data="words"> </word-cloud>

    <template
      slot="extraFormItems"
      v-if="slotProps.isInAdvancedMode"
      slot-scope="slotProps"
    >
      <b-form-group label="Delimiter to Generate Word">
        <b-form-select
          multiple
          v-model="slotProps.extraData.delimiters"
          :select-size="3"
        >
          <b-form-select-option label="\r" value="\r" />
          <b-form-select-option label="\n" value="\n" />
          <b-form-select-option label="Space" value="\s" />
        </b-form-select>
        <b-form-invalid-feedback
          :state="slotProps.vuelidate.editForm.extraData.delimiters.required"
        >
          Please specify at least one delimiter
        </b-form-invalid-feedback>
      </b-form-group>

      <b-form-group label="Word to Ignore">
        <b-form-tags
          input-id="ignored-word"
          v-model="slotProps.extraData.ignoreWords"
          placeholder="Add word..."
        ></b-form-tags>
      </b-form-group>
    </template>
  </basic-section-detail>
</template>

<script>
import { required } from "vuelidate/lib/validators";
import WordCloud from "@/components/sectionDetail/chart/WordCloud.vue";
import BasicSectionDetail from "@/components/sectionDetail/BasicSectionDetail.vue";

const mustBeOneValue = value => value.length == 1;

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
      type: String,
      required: true
    }
  },

  data() {
    return {
      editFormInvolvedRecordsRule: { mustBeOneValue },
      extraFormItemsRules: {
        delimiters: { required }
      },

      // word cloud related field
      words: []
    };
  },

  computed: {
    hasData() {
      return this.words.length !== 0;
    }
  },

  methods: {
    updateVisualisation({ result, selections, extraData }) {
      let fieldName = selections[0].rename;
      let wordsCount = {};
      let delimiterRegex = new RegExp(extraData.delimiters.join("|"), "g");
      // will only require at least one selection
      // count the occurrence of word
      result.forEach(r => {
        r[fieldName]
          .split(delimiterRegex)
          .filter(w => !extraData.ignoreWords.includes(w.toLowerCase())) // filter ignoreWords
          .forEach(w => {
            // skip empty string
            if (w.length === 0) {
              return;
            }
            // normalized word e.g. 'digital world' -> `Digital World`
            let normalizedW = this.capitalizeFirstWord(w);
            // put in the count map
            if (wordsCount.hasOwnProperty(normalizedW)) {
              wordsCount[normalizedW]++;
            } else {
              wordsCount[normalizedW] = 1;
            }
          });
      });
      // generate format as VueWordCloud required
      let words = [];
      for (let word in wordsCount) {
        if (wordsCount.hasOwnProperty(word)) {
          words.push([word, wordsCount[word]]);
        }
      }
      // sort and keep the first twenty words
      words.sort((a, b) => {
        return b[1] - a[1];
      });
      words = words.slice(0, 20);
      this.words = words;
    },

    capitalizeFirstWord(str) {
      return str.replace(/\b\w/g, l => l.toUpperCase());
    }
  },

  components: {
    WordCloud,
    BasicSectionDetail
  }
};
</script>
