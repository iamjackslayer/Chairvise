<template>
  <b-overlay :show="sectionDetail.status.isLoading">
    <b-form>
      <!-- :rules="editFormRule"  -->
      <div class="title mb-2" v-if="!isEditing">
        <span class="title-text">{{ sectionDetail.title }}</span>
        <div class="toolbar ml-lg-auto">
          <b-button
            variant="primary"
            class="ml-lg-2 mr-lg-2 responsive-btn"
            @click="changeEditMode(true)"
            v-if="isPresentationEditable"
          >
            <b-icon icon="pencil-square" aria-hidden="true" /> Edit
          </b-button>
          <b-button
            class="responsive-btn"
            variant="outline-danger"
            @click="deleteSectionDetail"
            v-if="isPresentationEditable"
          >
            <b-icon icon="trash-fill" aria-hidden="true" /> Delete
          </b-button>
        </div>
      </div>
      <div class="title mb-2" v-else>
        <b-form-input v-model="editForm.title"></b-form-input>
      </div>
      <b-alert
        show
        variant="danger"
        class="mt-3"
        v-if="sectionDetail.status.isApiError"
      >
        <b-icon
          class="alert-icon"
          icon="exclamation-circle-fill"
          variant="danger"
        />
        <span>{{ sectionDetail.status.apiErrorMsg }}</span>
        <span class="d-block">{{
          sectionDetail.status.apiErrorMsgDetail
        }}</span>
      </b-alert>
      <b-alert show v-if="!this.hasData" class="mt-3" variant="primary">
        No data to display.
      </b-alert>
      <slot v-else></slot>
      <div v-if="!isEditing" class="description">
        <p>{{ editForm.description }}</p>
      </div>
      <div v-if="isEditing">
        <b-form-group label="Editing Mode">
          <b-form-checkbox v-model="isInAdvancedMode" switch>
            Advanced
          </b-form-checkbox>
        </b-form-group>

        <!-- TODO: Add validation and remove prop -->
        <div v-if="isInAdvancedMode">
          <b-form-group
            v-for="(v, index) in $v.editForm.selections.$each.$iter"
            :label="'Selection ' + index"
            :key="'s' + index"
          >
            <!-- :rules="editFormSelectionsRule" -->
            <b-input
              class="d-inline-block mr-2"
              v-model="v.expression.$model"
              placeholder="Expression"
              style="width: 300px"
            ></b-input>
            <b-input
              class="d-inline-block mr-2"
              v-model="v.rename.$model"
              placeholder="Rename Field"
              style="width: 200px"
            ></b-input>
            <b-button
              variant="outline-danger"
              @click="removeSelection(selection)"
            >
              <b-icon icon="trash-fill" aria-hidden="true" />
            </b-button>
            <b-form-invalid-feedback
              :state="v.expression.required && v.rename.required"
            >
              Please specify all field for the selection
            </b-form-invalid-feedback>
          </b-form-group>
        </div>

        <!-- TODO: Add validation and remove prop, key -->
        <b-form-group
          label="Record Involved"
          prop="involvedRecords"
          v-if="isInAdvancedMode"
          key="involvedRecords"
        >
          <v-select
            v-model="editForm.involvedRecords"
            taggable
            multiple
            label="label"
            :options="involvedRecordsOptions"
            :create-option="option => ({ label: option, value: option })"
            :reduce="option => option.value"
          />
          <b-form-invalid-feedback
            :state="$v.editForm.involvedRecords.required"
          >
            There must be one record involved
          </b-form-invalid-feedback>

          <b-form-invalid-feedback
            :state="$v.editForm.involvedRecords.mustBeOneValue"
          >
            There must be only one record involved
          </b-form-invalid-feedback>
        </b-form-group>

        <!-- TODO: Add validation and remove prop, key -->
        <div v-if="isInAdvancedMode">
          <b-form-group
            v-for="(joiner, index) in editForm.joiners"
            :label="'Joiner ' + index"
            :key="'j' + index"
            :prop="'joiners.' + index"
            :rules="editFormJoinersRule"
          >
            On
            <b-form-select
              placeholder="Left"
              class="d-inline-block w-auto"
              v-model="joiner.left"
            >
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
            <b-form-select
              placeholder="Right"
              class="d-inline-block w-auto mr-2"
              v-model="joiner.right"
            >
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
            <b-button variant="outline-danger" @click="removeJoiner(joiner)">
              <b-icon icon="trash-fill" aria-hidden="true" />
            </b-button>
          </b-form-group>
        </div>

        <!-- This appears in basic -->
        <!-- TODO: Add validation and remove prop, key -->
        <b-form-group
          v-for="(filter, index) in editForm.filters"
          :label="'Filter ' + index"
          :key="'f' + index"
          :prop="'filters.' + index"
          :rules="editFormFiltersRule"
        >
          <!-- TODO: bug in inter-author collaboration and other grpahs -->
          <!-- Could append filter.field to filtersFieldOptions if none exist or if not in fieldOptions -->
          <!-- Also filter.comparator -->
          <b-form-select
            placeholder="Field"
            v-model="filter.field"
            class="w-auto"
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
            </b-form-select-option-group>
          </b-form-select>
          <b-form-select
            v-model="filter.comparator"
            class="mx-2"
            style="width: 80px"
          >
            <b-form-select-option label=">" value=">" />
            <b-form-select-option label="=" value="=" />
            <b-form-select-option label="<" value="<" />
          </b-form-select>
          <b-form-input
            class="d-inline-block mr-2 align-middle"
            v-model="filter.value"
            placeholder="Value"
            style="width: 200px"
          ></b-form-input>
          <b-button variant="outline-danger" @click="removeFilter(filter)">
            <b-icon icon="trash-fill" aria-hidden="true" />
          </b-button>
        </b-form-group>

        <!-- This appears in basic -->
        <b-form-group label="Description">
          <b-form-textarea
            rows="3"
            max-rows="6"
            placeholder="Please enter description (Leave empty to hide the description part)"
            v-model="editForm.description"
          />
        </b-form-group>

        <!-- TODO: Add validation and remove prop, key -->
        <b-form-group
          label="Group (Aggregation)"
          prop="groupers"
          v-if="isInAdvancedMode"
          key="groupers"
        >
          <!-- TODO: bug in inter-organisation collaboration and other graphs, unsupported group aggregation -->
          <!-- Could append editForm.groupers to groupersFieldOptions if none exist or if not in fieldOptions -->
          <b-form-select
            placeholder="Groupers"
            v-model="editForm.groupers"
            multiple
            :select-size="5"
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
            </b-form-select-option-group>
          </b-form-select>
        </b-form-group>

        <!-- TODO: Add validation and remove prop, key -->
        <div v-if="isInAdvancedMode">
          <b-form-group
            v-for="(sorter, index) in editForm.sorters"
            :label="'Sorting ' + index"
            :key="'sort' + index"
            :prop="'sorters.' + index"
            :rules="editFormSortersRule"
          >
            <b-form-input
              v-model="sorter.field"
              class="d-inline-block mr-2 align-middle"
              placeholder="Field to Sort"
              style="width: 300px;"
            ></b-form-input>
            <b-form-select
              v-model="sorter.order"
              class="d-inline-block mr-2"
              placeholder="Order"
              style="width: 150px"
            >
              <b-form-select-option label="Descending" value="DESC" />
              <b-form-select-option label="Ascending" value="ASC" />
            </b-form-select>
            <b-button variant="outline-danger" @click="removeSorter(sorter)">
              <b-icon icon="trash-fill" aria-hidden="true" />
            </b-button>
          </b-form-group>
        </div>

        <!-- TODO: Review if this appears only in advanced mode  -->
        <slot
          name="extraFormItems"
          :editForm="editForm"
          :extraData="editForm.extraData"
          :isInAdvancedMode="isInAdvancedMode"
        ></slot>

        <b-form-group>
          <b-button
            variant="outline-success"
            class="mr-2"
            @click="addSelection"
            v-if="isInAdvancedMode"
          >
            <b-icon icon="plus" /> Add selection
          </b-button>
          <b-button
            variant="outline-success"
            class="mr-2"
            @click="addJoiner"
            v-if="isInAdvancedMode"
          >
            <b-icon icon="plus" /> Add joiner
          </b-button>
          <b-button variant="outline-success" class="mr-2" @click="addFilter">
            <b-icon icon="plus" />Add filter
          </b-button>
          <b-button
            variant="outline-success"
            class="mr-2"
            @click="addSorter"
            v-if="isInAdvancedMode"
          >
            <b-icon icon="plus" />Add sorting
          </b-button>
        </b-form-group>

        <!-- TODO: Fix preview to load -->
        <b-form-group>
          <b-button
            variant="outline-primary"
            icon="el-icon-view"
            class="mr-2"
            @click="previewAnalysisResult('editForm')"
          >
            <b-icon icon="eye-fill" /> Preview
          </b-button>
          <b-button
            variant="primary "
            icon="el-icon-check"
            class="mr-2"
            @click="saveSectionDetail('editForm')"
          >
            Save
          </b-button>
          <b-button icon="el-icon-close" @click="cancelEditing">
            Cancel
          </b-button>
        </b-form-group>
      </div>
    </b-form>
    <pre class="debug">{{ $v }}</pre>
    <!-- <el-form
      status-icon
      ref="editForm"
      label-position="left"
      :model="editForm"
      label-width="170px"
      :rules="editFormRule"
    >
      <div v-if="isEditing">
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-view"
            @click="previewAnalysisResult('editForm')"
            plain
          >
            Preview
          </el-button>
          <el-button
            type="success"
            icon="el-icon-check"
            @click="saveSectionDetail('editForm')"
          >
            Save
          </el-button>
          <el-button icon="el-icon-close" @click="cancelEditing">
            Cancel
          </el-button>
        </el-form-item>
      </div>
    </el-form> -->
  </b-overlay>
</template>

<script>
// import { required } from "vuelidate/lib/validators";
import Multiselect from "vue-multiselect";
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
    conference: {
      type: String,
      required: true
    },
    hasData: {
      type: Boolean,
      required: true
    },
    editFormSelectionsRule: {
      type: Object,
      required: false,
      default: () => ({})
    },
    editFormInvolvedRecordsRule: {
      type: Object,
      required: false,
      default: () => ({})
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
    editFormSortersRule: {
      type: Array,
      required: false,
      default: () => []
    },
    editFormGroupersRule: {
      type: Array,
      required: false,
      default: () => []
    },
    extraFormItemsRules: {
      type: Object,
      required: false,
      default: () => {}
    }
  },
  watch: {
    conference() {
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
  components: { Multiselect },
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
      // unused
      editFormRule: {
        involvedRecords: this.editFormInvolvedRecordsRule,
        groupers: this.editFormGroupersRule,
        extraData: this.extraFormItemsRules
      },
      editFormRuleTest: {
        involvedRecords: { custom: this.editFormInvolvedRecordsRule },
        groupers: { custom: this.editFormGroupersRule },
        extraData: { custom: this.extraFormItemsRules }
      }
    };
  },
  validations() {
    return {
      editForm: {
        selections: this.editFormSelectionsRule,
        involvedRecords: this.editFormInvolvedRecordsRule
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

    saveSectionDetail() {
      this.$v.editForm.$touch();
      if (this.$v.editForm.$anyError) {
        return;
      }

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
            conferenceName: this.conference
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
          conference: this.conference
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
            conferenceName: this.conference
          });
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.title {
  display: flex;
  flex-direction: column;

  @include media-breakpoint-up(lg) {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
}

.title-text {
  font-size: 1.3rem;
}
</style>
