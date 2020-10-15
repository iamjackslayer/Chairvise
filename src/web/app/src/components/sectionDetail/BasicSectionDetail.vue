<template>
  <div>
    <b-row v-loading="sectionDetail.status.isLoading">
      <b-form
        status-icon
        ref="editForm"
        label-position="left"
        :model="editForm"
        label-width="170px"
        :rules="editFormRule"
      >
        <div class="title" v-if="!isEditing">
          {{ sectionDetail.title }}
          <b-button
            variant="primary"
            @click="changeEditMode(true)"
            v-if="isPresentationEditable"
            icon="el-icon-edit"
          >
            Edit
          </b-button>
          <b-button
            variant="danger"
            icon="el-icon-delete"
            @click="deleteSectionDetail"
            v-if="isPresentationEditable"
          >
            Delete
          </b-button>
        </div>
        <div class="title" v-else>
          <b-input v-model="editForm.title"></b-input>
        </div>
        <b-alert
          v-if="sectionDetail.status.isApiError"
          :title="sectionDetail.status.apiErrorMsg"
          variant="danger"
          class="errorMessage"
        >
          <b-icon
            class="alert-icon"
            icon="exclamation-circle-fill"
            variant="danger"
          />
          {{ sectionDetail.status.apiErrorMsgDetail }}
        </b-alert>
        <b-alert
          v-if="!this.hasData"
          title="No Data to display"
          variant="secondary"
          class="noDataToDisplay"
        >
          No Data to display
        </b-alert>
        <slot v-else></slot>
        <div v-if="!isEditing" class="description">
          {{ editForm.description }}
        </div>
        <div v-if="isEditing">
          <b-form-item>
            <b-row>
              <label>Advanced Editing Mode</label>
              <b-form-checkbox v-model="isInAdvancedMode" switch>
              </b-form-checkbox>
            </b-row>
          </b-form-item>

          <b-form-item
            v-if="isInAdvancedMode"
            v-for="(selection, index) in editForm.selections"
            :key="'s' + index"
            :prop="'selections.' + index"
            :rules="editFormSelectionsRule"
          >
            <b-row>
              <label>Selection {{ index }}</label>
              <b-form-input
                v-model="selection.expression"
                id="selection expression"
                placeholder="Expression"
                style="width: 30%"
              ></b-form-input
              >&nbsp;
              <b-form-input
                v-model="selection.rename"
                placeholder="Rename Field"
                style="width: 20%"
              ></b-form-input
              >&nbsp;
              <b-button
                variant="danger"
                icon="el-icon-delete"
                @click="removeSelection(selection)"
                >delete</b-button
              >
            </b-row>
          </b-form-item>

          <b-form-item
            label="Record Involved"
            prop="involvedRecords"
            v-if="isInAdvancedMode"
            key="involvedRecords"
          >
            <b-form-select
              v-model="editForm.involvedRecords"
              multiple
              placeholder="Please select"
              filterable
              allow-create
            >
              <b-form-select-option
                v-for="option in involvedRecordsOptions"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              >
              </b-form-select-option>
            </b-form-select>
          </b-form-item>

          <b-form-item
            v-if="isInAdvancedMode"
            v-for="(joiner, index) in editForm.joiners"
            :label="'Joiner ' + index"
            :key="'j' + index"
            :prop="'joiners.' + index"
            :rules="editFormJoinersRule"
          >
            On
            <b-form-select placeholder="Left" v-model="joiner.left">
              <b-form-select-option-group
                v-for="group in joinersFieldOptions"
                :key="group.label"
                :label="group.label"
              >
                <b-form-select-option
                  v-for="item in group.options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </b-form-select-option>
              </b-form-select-option-group>
            </b-form-select>
            Equals
            <b-form-select placeholder="Right" v-model="joiner.right">
              <b-form-select-option-group
                v-for="group in joinersFieldOptions"
                :key="group.label"
                :label="group.label"
              >
                <b-form-select-option
                  v-for="item in group.options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </b-form-select-option>
              </b-form-select-option-group> </b-form-select
            >&nbsp;
            <b-button
              variant="danger"
              icon="el-icon-delete"
              @click="removeJoiner(joiner)"
            ></b-button>
          </b-form-item>

          <b-form-item
            v-for="(filter, index) in editForm.filters"
            :label="'Filter ' + index"
            :key="'f' + index"
            :prop="'filters.' + index"
            :rules="editFormFiltersRule"
          >
            <b-form-select
              placeholder="Field"
              v-model="filter.field"
              filterable
              allow-create
            >
              <b-form-select-option-group
                v-for="group in filtersFieldOptions"
                :key="group.label"
                :label="group.label"
              >
                <b-form-select-option
                  v-for="item in group.options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </b-form-select-option>
              </b-form-select-option-group> </b-form-select
            >&nbsp;
            <b-form-select v-model="filter.comparator" style="width: 80px">
              <b-form-select-option label=">" value=">" />
              <b-form-select-option label="=" value="=" />
              <b-form-select-option label="<" value="<" /> </b-form-select
            >&nbsp;
            <b-form-input
              v-model="filter.value"
              placeholder="Value"
              style="width: 200px"
            ></b-form-input
            >&nbsp;
            <b-button
              variant="danger"
              icon="el-icon-delete"
              @click="removeFilter(filter)"
              >Delete</b-button
            >
          </b-form-item>

          <b-form-item label="Description for the section">
            <b-form-input
              type="textarea"
              :autosize="{ minRows: 4 }"
              placeholder="Please enter description (Leave empty to hide the description part)"
              v-model="editForm.description"
            >
            </b-form-input>
          </b-form-item>

          <b-form-item
            label="Group (Aggregation)"
            prop="groupers"
            v-if="isInAdvancedMode"
            key="groupers"
          >
            <b-form-select
              placeholder="Groupers"
              v-model="editForm.groupers"
              style="width: 100%"
              multiple
              filterable
              allow-create
            >
              <b-form-select-option-group
                v-for="group in groupersFieldOptions"
                :key="group.label"
                :label="group.label"
              >
                <b-form-select-option
                  v-for="item in group.options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </b-form-select-option>
              </b-form-select-option-group> </b-form-select
            >&nbsp;
          </b-form-item>

          <b-form-item
            v-if="isInAdvancedMode"
            v-for="(sorter, index) in editForm.sorters"
            :label="'Sorting ' + index"
            :key="'sort' + index"
            :prop="'sorters.' + index"
            :rules="editFormSortersRule"
          >
            <b-form-input
              v-model="sorter.field"
              placeholder="Field to Sort"
              style="width: 300px"
            ></b-form-input
            >&nbsp;
            <b-form-select
              v-model="sorter.order"
              style="width: 80px"
              placeholder="Order"
            >
              <el-option label="Big to Small" value="DESC" />
              <el-option label="Small to Big" value="ASC" /> </b-form-select
            >&nbsp;
            <el-button
              type="danger"
              icon="el-icon-delete"
              @click="removeSorter(sorter)"
            ></el-button>
          </b-form-item>

          <slot
            name="extraFormItems"
            :editForm="editForm"
            :extraData="editForm.extraData"
            :isInAdvancedMode="isInAdvancedMode"
          ></slot>
          <b-form-item>
            <b-button
              type="success"
              icon="el-icon-plus"
              plain
              @click="addSelection"
              v-if="isInAdvancedMode"
              >Add selection</b-button
            >
            <b-button
              type="success"
              icon="el-icon-plus"
              plain
              @click="addJoiner"
              v-if="isInAdvancedMode"
              >Add joiner</b-button
            >
            <b-button
              variant="success"
              icon="el-icon-plus"
              plain
              @click="addFilter"
              >Add filter</b-button
            >
            <b-button
              variant="success"
              icon="el-icon-plus"
              plain
              @click="addSorter"
              v-if="isInAdvancedMode"
              >Add sorting</b-button
            >
          </b-form-item>
          <b-form-item>
            <b-button
              type="primary"
              icon="el-icon-view"
              @click="previewAnalysisResult('editForm')"
              plain
              >Preview</b-button
            >
            <b-button
              type="success"
              icon="el-icon-check"
              @click="saveSectionDetail('editForm')"
              >Save</b-button
            >
            <b-button icon="el-icon-close" @click="cancelEditing"
              >Cancel</b-button
            >
          </b-form-item>
        </div>
      </b-form>
    </b-row>
  </div>
</template>

<script>
import { deepCopy } from "@/common/utility";

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
    version: {
      type: String,
      required: true
    },
    hasData: {
      type: Boolean,
      required: true
    },
    extraFormItemsRules: {
      type: Object,
      required: false
    },
    editFormSelectionsRule: {
      type: Array,
      required: false,
      default: () => []
    },
    editFormInvolvedRecordsRule: {
      type: Array,
      required: false,
      default: () => []
    },
    editFormFiltersRule: {
      type: Array,
      required: false,
      default: () => []
    },
    editFormJoinersRule: {
      type: Array,
      required: false,
      default: () => []
    },
    editFormGroupersRule: {
      type: Array,
      required: false,
      default: () => []
    },
    editFormSortersRule: {
      type: Array,
      required: false,
      default: () => []
    }
  },
  watch: {
    version() {
      if (this.isEditing == true) {
        this.previewAnalysisResult("editForm");
      } else {
        this.sendAnalysisRequest();
      }
    }
  },
  created() {
    this.syncDataWithProps();
    this.sendAnalysisRequest();
  },

  data() {
    return {
      isInAdvancedMode: false,
      isEditing: false,

      editForm: {
        title: "",
        description: "",
        dataSet: "",
        selections: [],
        involvedRecords: [],
        filters: [],
        joiners: [],
        groupers: [],
        sorters: [],
        extraData: {}
      },

      editFormRule: {
        involvedRecords: this.editFormInvolvedRecordsRule,
        groupers: this.editFormGroupersRule,
        extraData: this.extraFormItemsRules
      }
    };
  },

  computed: {
    involvedRecordsOptions() {
      return this.$store.state.dbMetaData.entities.map(entity => ({
        label: entity.name,
        value: entity.tableName
      }));
    },
    editFormInvolvedRecords() {
      return this.editForm.involvedRecords.map(r => ({
        name: r,
        customized: !this.$store.state.dbMetaData.entities.some(
          e => e.tableName === r
        )
      }));
    },
    filtersFieldOptions() {
      return this.$store.state.dbMetaData.entities
        .filter(entity =>
          this.editForm.involvedRecords.includes(entity.tableName)
        )
        .map(entity => ({
          label: entity.name,
          options: entity.fieldMetaDataList.map(field => ({
            label: field.name,
            value: field.fieldName
          }))
        }));
    },
    joinersFieldOptions() {
      return this.filtersFieldOptions;
    },
    groupersFieldOptions() {
      return this.filtersFieldOptions;
    },
    isPresentationEditable() {
      return this.$store.state.presentation.isPresentationEditable;
    }
  },

  methods: {
    changeEditMode(isEditing) {
      this.isEditing = isEditing;
    },

    cancelEditing() {
      this.isEditing = false;
      this.syncDataWithProps();
      this.sendAnalysisRequest();
    },

    syncDataWithProps() {
      this.editForm.title = this.sectionDetail.title;
      this.editForm.description = this.sectionDetail.description;
      this.editForm.dataSet = this.sectionDetail.dataSet;
      this.editForm.selections = deepCopy(this.sectionDetail.selections); // deep copy
      this.editForm.involvedRecords = this.sectionDetail.involvedRecords.map(
        r => r.name
      );
      this.editForm.filters = this.sectionDetail.filters.map(f =>
        Object.assign({}, f)
      );
      this.editForm.joiners = this.sectionDetail.joiners.map(f =>
        Object.assign({}, f)
      );
      this.editForm.groupers = this.sectionDetail.groupers.map(r => r.field);
      this.editForm.sorters = deepCopy(this.sectionDetail.sorters); // deep copy
      this.editForm.extraData = deepCopy(this.sectionDetail.extraData); // deep copy
    },

    addSelection() {
      this.editForm.selections.push({
        expression: "",
        rename: ""
      });
    },

    removeSelection(selection) {
      let index = this.editForm.selections.indexOf(selection);
      this.editForm.selections.splice(index, 1);
    },

    addJoiner() {
      this.editForm.joiners.push({
        left: "",
        right: ""
      });
    },

    removeJoiner(joiner) {
      let index = this.editForm.joiners.indexOf(joiner);
      this.editForm.joiners.splice(index, 1);
    },

    addFilter() {
      this.editForm.filters.push({
        field: "",
        comparator: "=",
        value: ""
      });
    },

    removeFilter(filter) {
      let index = this.editForm.filters.indexOf(filter);
      this.editForm.filters.splice(index, 1);
    },

    addSorter() {
      this.editForm.sorters.push({
        field: "",
        order: ""
      });
    },

    removeSorter(sorter) {
      let index = this.editForm.sorters.indexOf(sorter);
      this.editForm.sorters.splice(index, 1);
    },

    saveSectionDetail(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$store
            .dispatch("saveSectionDetail", {
              id: this.sectionDetail.id,
              presentationId: this.presentationId,
              title: this.editForm.title,
              description: this.editForm.description,
              dataSet: this.sectionDetail.dataSet,
              selections: this.editForm.selections,
              involvedRecords: deepCopy(this.editFormInvolvedRecords),
              filters: this.editForm.filters.map(f => Object.assign({}, f)),
              joiners: this.editForm.joiners.map(j => Object.assign({}, j)),
              groupers: this.editForm.groupers.map(g => ({ field: g })),
              sorters: this.editForm.sorters.map(s => Object.assign({}, s)),
              extraData: this.editForm.extraData
            })
            .then(() => {
              // only update when there is no error in saving
              if (this.sectionDetail.status.isApiError) {
                return;
              }
              this.isEditing = false;
              this.sendAnalysisRequest();
            });
          return true;
        } else {
          return false;
        }
      });
    },

    deleteSectionDetail() {
      this.$store.dispatch("deleteSectionDetail", {
        presentationId: this.presentationId,
        id: this.sectionDetail.id
      });
    },

    previewAnalysisResult(formName) {
      this.$refs[formName].validate(valid => {
        if (!valid) {
          return false;
        }

        this.$store
          .dispatch("sendPreviewAnalysisRequest", {
            presentationId: this.presentationId,
            id: this.sectionDetail.id,
            dataSet: this.sectionDetail.dataSet,
            selections: this.editForm.selections,
            involvedRecords: this.editFormInvolvedRecords,
            filters: this.editForm.filters,
            joiners: this.editForm.joiners.map(j => Object.assign({}, j)),
            groupers: this.editForm.groupers.map(g => ({ field: g })),
            sorters: this.editForm.sorters.map(s => Object.assign({}, s)),
            versionId: this.version
          })
          .then(() => {
            this.$emit("update-visualisation", {
              selections: this.editForm.selections,
              involvedRecords: this.editFormInvolvedRecords,
              filters: this.editForm.filters.map(f => Object.assign({}, f)),
              joiners: this.editForm.joiners.map(j => Object.assign({}, j)),
              groupers: this.editForm.groupers.map(g => ({ field: g })),
              sorters: this.editForm.sorters.map(s => Object.assign({}, s)),
              result: this.sectionDetail.previewResult,
              extraData: this.editForm.extraData
            });
          });
      });
    },

    sendAnalysisRequest() {
      this.$store
        .dispatch("sendAnalysisRequest", {
          id: this.sectionDetail.id,
          presentationId: this.presentationId,
          version: this.version
        })
        .then(() => {
          this.$emit("update-visualisation", {
            presentationId: this.presentationId,
            selections: this.sectionDetail.selections,
            involvedRecords: this.sectionDetail.involvedRecords,
            filters: this.sectionDetail.filters,
            joiners: this.sectionDetail.joiners,
            result: this.sectionDetail.result,
            groupers: this.sectionDetail.groupers,
            sorters: this.sectionDetail.sorters,
            extraData: this.sectionDetail.extraData,
            versionId: this.version
          });
        });
    }
  }
};
</script>

<style scoped>
.title {
  font-size: 20px;
  text-align: center;
  margin-bottom: 10px;
  margin-top: 10px;
}

.description {
  margin-top: 20px;
  padding-left: 50px;
  padding-right: 50px;
}

.noDataToDisplay {
  margin-top: 10px;
  margin-bottom: 10px;
}

.errorMessage {
  margin-top: 10px;
}
</style>
