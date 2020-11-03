<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :conference="conference"
    :edit-form-selections-rule="editFormSelectionsRule"
    :edit-form-involved-records-rule="editFormInvolvedRecordsRule"
    :edit-form-filters-rule="editFormFiltersRule"
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
      editFormSelectionsRule: {
        $each: {
          expression: {
            required
          },
          rename: {
            required
          }
        }
      },
      editFormInvolvedRecordsRule: { mustBeOneValue },
      editFormFiltersRule: [
        {
          validator: (rule, value, callback) => {
            if (
              value.field.length === 0 ||
              value.comparator.length === 0 ||
              value.value.length === 0
            ) {
              return callback(new Error("Please specify all fields"));
            }
            callback();
          },
          trigger: "blur"
        }
      ],

      extraFormItemsRules: {
        delimiters: [
          {
            validator: (rule, value, callback) => {
              if (value.length === 0) {
                return callback(
                  new Error("Please specify at least one delimiter")
                );
              }
              callback();
            },
            trigger: "blur"
          }
        ]
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
