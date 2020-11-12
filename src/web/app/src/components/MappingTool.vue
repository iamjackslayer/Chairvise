<template>
  <div>
    <b-card class="mb-3" title="Data Preview" v-if="previewData">
      <div class="data-preview">
        <span
          v-for="(line, index) in previewData"
          :key="index"
          class="d-block"
          >{{ line.toString() }}</span
        >
      </div>
    </b-card>
    <b-row>
      <b-col xs="12" lg="6">
        <b-card>
          <!-- db fields -->
          <div class="db-tags">
            <h5>Database fields</h5>
            <transition-group name="tags-group" tag="div">
              <div
                class="tag"
                v-for="(item, idx) in dbList.fieldMetaDataList"
                v-bind:key="idx"
                v-bind:class="[
                  idx === selectedDBTag ? 'active' : '',
                  mappedDBTag.includes(idx) ? 'hidden' : ''
                ]"
                v-on:click="dbTagClicked(idx)"
              >
                {{ item.name }}
              </div>
            </transition-group>
          </div>
          <!-- end of db fields -->

          <!-- imported tags -->
          <div class="import-tags mt-4">
            <h5>Imported data fields</h5>
            <transition-group name="tags-group" tag="div">
              <div
                class="tag"
                v-for="(item, idx) in importList"
                v-bind:key="idx"
                v-bind:class="[
                  idx === selectedImportTag ? 'active' : '',
                  mappedImportTag.includes(idx) ? 'hidden' : ''
                ]"
                v-on:click="importTagClicked(idx)"
              >
                {{ item }}
              </div>
            </transition-group>
          </div>
          <!-- end of imported tags -->

          <!-- button group -->
          <div class="mt-4">
            <b-button @click="uploadClicked" variant="primary">
              Submit
            </b-button>
            <b-button class="ml-2" @click="backClicked" variant="secondary">
              Back
            </b-button>
          </div>
          <!-- end of button group -->
        </b-card>
      </b-col>

      <b-col class="mt-3 mt-lg-0" xs="12" lg="6">
        <b-card>
          <h5>Mapping</h5>
          <transition-group name="map-group" tag="div">
            <div
              class="pair-tag"
              v-for="(item, index) in mappedPairs"
              v-bind:key="index"
            >
              <b-badge variant="info">{{
                dbList.fieldMetaDataList[item[0]].type
              }}</b-badge>
              <span class="pair-info">
                {{ dbList.fieldMetaDataList[item[0]].name }}
                <b-icon
                  icon="caret-right-square-fill"
                  variant="primary"
                  class="mx-2"
                />
                {{ importList[item[1]] }}
              </span>
              <b-icon
                icon="x-square-fill"
                @click="removeMapClicked(index)"
                class="remove-mapping"
              />
            </div>
          </transition-group>
          <transition name="fade" mode="out-in">
            <div class="no-map-info" v-show="mappedPairs.length === 0">
              No mapping has been specified.
            </div>
          </transition>
        </b-card>
      </b-col>

      <!-- dialogs -->
      <b-modal
        title="Confirm"
        centered
        :visible.sync="hasSubmitted"
        @ok="submitMapping"
        @hidden="hasSubmitted = false"
        @cancel="hasSubmitted = false"
      >
        <span
          >After submission, your will not be able to modify your mapping. Are
          you sure that the columns are correctly mapped?</span
        >
      </b-modal>
      <b-modal
        title="Success"
        centered
        :visible.sync="uploadSuccess"
        ok-only
        @ok="closeSuccess"
        @hidden="closeSuccess"
      >
        <span
          >You have successfully imported data with the specified column
          mapping.</span
        >
      </b-modal>
      <!-- end of dialogs -->
    </b-row>
  </div>
</template>

<script>
import { deepCopy, filterPredefinedMap } from "@/common/utility";

export default {
  name: "MappingTool",
  data() {
    return {
      // currently selected database tag and imported tag
      selectedDBTag: -1,
      selectedImportTag: -1,

      // ordered list of tags that have been
      // mapped with their data type details
      mappedDBTag: filterPredefinedMap(
        deepCopy(
          this.$store.state.dataMapping.data.predefinedMapping.dbTagIndices
        ),
        this.$store.state.dataMapping.data.dbSchema.fieldMetaDataList
      ),
      mappedImportTag: filterPredefinedMap(
        deepCopy(
          this.$store.state.dataMapping.data.predefinedMapping
            .importedTagIndices
        ),
        this.$store.state.dataMapping.data.uploadedLabel
      ),

      hasSubmitted: false,
      tableType: ""
    };
  },
  computed: {
    dbList: function() {
      return this.$store.state.dataMapping.data.dbSchema;
    },
    // a list of size k * 2, k is the number of mapped pairs
    // the mapped pairs are indexes.
    mappedPairs: function() {
      let temp = this.mappedImportTag;
      return this.mappedDBTag.map(function(e, i) {
        return [e, temp[i]];
      });
    },
    previewData() {
      return this.$store.state.dataMapping.data.uploadedData.slice(0, 3);
    },
    // generates imported tags.
    // if initially no tag, just display column number
    importList: function() {
      if (this.$store.state.dataMapping.data.hasHeader) {
        return this.$store.state.dataMapping.data.uploadedLabel;
      }
      let lst = [];
      for (
        let i = 0;
        i < this.$store.state.dataMapping.data.uploadedLabel.length;
        i++
      ) {
        lst.push("Column " + (i + 1));
      }
      return lst;
    },

    // gets errors
    errors: function() {
      return this.$store.state.dataMapping.error;
    },

    // whether upload is successful
    uploadSuccess: function() {
      return this.$store.state.dataMapping.isUploadSuccess;
    }
  },

  // display errors
  watch: {
    errors(newValue) {
      if (newValue.length > 0) {
        this.$bvToast.toast(newValue.join("\n"), {
          title: "Error",
          variant: "danger",
          solid: true,
          autoHideDelay: 0
        });
      }
    }
  },
  methods: {
    dbTagClicked: function(idx) {
      if (idx === this.selectedDBTag) {
        this.selectedDBTag = -1;
        return;
      }
      this.selectedDBTag = idx;
      if (this.selectedImportTag !== -1 && this.selectedDBTag !== -1) {
        this.mappedDBTag.push(this.selectedDBTag);
        this.mappedImportTag.push(this.selectedImportTag);
        this.selectedDBTag = -1;
        this.selectedImportTag = -1;
      }
    },
    importTagClicked: function(idx) {
      if (idx === this.selectedImportTag) {
        this.selectedImportTag = -1;
        return;
      }
      this.selectedImportTag = idx;
      if (this.selectedImportTag !== -1 && this.selectedDBTag !== -1) {
        this.mappedDBTag.push(this.selectedDBTag);
        this.mappedImportTag.push(this.selectedImportTag);
        this.selectedDBTag = -1;
        this.selectedImportTag = -1;
      }
    },
    removeMapClicked: function(idx) {
      this.mappedDBTag.splice(idx, 1);
      this.mappedImportTag.splice(idx, 1);
    },
    backClicked: function() {
      this.$store.commit("clearDBSchema");
      this.$store.commit("clearUploadedFile");
      this.$store.commit("clearFormatType");
      this.$store.commit("clearTableType");
      this.$store.commit("clearHasHeader");
      this.$store.commit("clearMapping");
      this.$store.commit("clearPredefinedMapping");
      this.$store.commit("clearConferenceName");
      this.$store.commit("clearPredefinedSwitch");
      this.$store.commit("clearIsNewConference");
    },
    uploadClicked: function() {
      let map = deepCopy(this.mappedPairs);
      this.$store.commit("setMapping", { map: map });
      if (this.errors.length === 0) {
        this.hasSubmitted = true;
      }
    },
    submitMapping: function() {
      this.hasSubmitted = false;
      if (this.$store.state.dataMapping.data.isNewConference) {
        this.$store.dispatch("persistMappingNewConference");
      } else {
        this.$store.dispatch("persistMappingOldConference");
      }
    },
    closeSuccess: function() {
      this.$store.commit("setUploadSuccess", false);
      this.$store.commit("clearDBSchema");
      this.$store.commit("clearUploadedFile");
      this.$store.commit("clearFormatType");
      this.$store.commit("clearTableType");
      this.$store.commit("clearHasHeader");
      this.$store.commit("clearMapping");
      this.$store.commit("clearError");
      this.$store.commit("clearPredefinedMapping");
      this.$store.commit("clearConferenceName");
      this.$store.commit("clearPredefinedSwitch");
      this.$store.commit("clearIsNewConference");
      this.$store.dispatch("getConferenceList");
    }
  },
  mounted() {},
  updated() {}
};
</script>

<style lang="scss" scoped>
@keyframes pulse {
  from {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }

  50% {
    -webkit-transform: scale3d(1.1, 1.1, 1.1);
    transform: scale3d(1.1, 1.1, 1.1);
  }

  to {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter, .fade-leave-to
    /* .component-fade-leave-active below conference 2.1.8 */
 {
  opacity: 0;
}

.map-container {
  display: flex;
  flex-direction: row;
}

.db-tags {
  min-height: 90px;
}

.import-tags {
  min-height: 90px;
}

.tag:first-child {
  margin-left: 0px;
}

.tag {
  display: inline-block;
  /* height: 20px; */
  margin-right: 10px;
  margin-top: 5px;
  padding: 7px 14px;
  background-color: #ffffff;
  border: 1px solid $indigo-600;
  color: $indigo-600;
  font-size: 14px;
  cursor: pointer;
  opacity: 1;
  z-index: 1;
  transition: opacity 0.2s, transform 0.3s, background-color 0.2s;
  border-radius: 5px;
  max-width: 400px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  /* box-shadow: 0 2px 4px -1px rgba(0,0,0,.2), 0 4px 5px 0 rgba(0,0,0,.14), 0 1px 10px 0 rgba(0,0,0,.12); */
}

.tag.active {
  animation: pulse 1s infinite;
  background-color: $indigo-600;
  color: #ffffff;
  transition: 0.3s;
}

.tag.hidden {
  position: absolute;
  opacity: 0;
  z-index: -1;
  transition: 0.2s;
}

.tag:hover {
  background-color: $indigo-600;
  color: #ffffff;
  transition: 0.2s;
}

.pair-tag {
  display: flex;
  align-items: center;
  padding: 1rem 0;
  color: $gray-900;
  font-weight: 600;
}

.pair-tag:not(:first-child) {
  border-top: 1px solid $gray-400;
}

.pair-tag .pair-info {
  margin-left: 10px;
  font-size: 14px;
  display: inline;
}

.pair-tag .remove-mapping {
  cursor: pointer;
  margin-left: auto;
}

.pair-tag .remove-mapping:hover {
  color: $red-600;
}

.no-map-info {
  color: #777;
  font-weight: 300;
}

.data-preview {
  font-family: "Consolas", "Lucida Console", monospace;
  background-color: $gray-100;
  font-weight: 600;
  padding: 1rem;
}

.data-preview > span:not(:first-child) {
  margin-top: 1rem;
}
</style>
