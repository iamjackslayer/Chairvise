<template>
  <div>
    <b-overlay :show="isLoadingDBMetaData || isLoadingSectionList">
      <div v-if="!isNewPresentation">
        <div v-if="isLogin">
          <b-card v-if="!isSectionListEmpty" class="mt-4">
            <div slot="header">
              <span> Select Version </span>
            </div>
            <!-- TODO: Add link the create conference if no conferences -->
            <b-form-select
              v-model="presentationFormVersion"
              :options="versions"
            >
              <template v-slot:first>
                <b-form-select-option :value="null" disabled>
                  Please select an option
                </b-form-select-option>
              </template>
            </b-form-select>
          </b-card>
          <b-card class="mt-4">
            <div slot="header">
              <span> Add Section </span>
            </div>
            <b-form-select v-model="selectedNewSection">
              <b-form-select-option :value="null" disabled>
                Please select an option
              </b-form-select-option>
              <b-form-select-option-group
                v-for="group in predefinedSections"
                :key="group.label"
                :label="group.label"
              >
                <b-form-select-option
                  v-for="item in group.options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </b-form-select-option-group>
            </b-form-select>
            <b-button class="mt-2" variant="primary" @click="addNewSection">
              <b-icon icon="plus" />Add New Section
            </b-button>
          </b-card>
        </div>
        <b-alert v-if="isSectionListApiError" show variant="danger">
          <b-icon
            class="alert-icon"
            icon="exclamation-circle-fill"
            variant="danger"
          />
          {{ sectionListApiErrorMsg }}
        </b-alert>
        <b-card class="mt-4">
          <abstract-section-detail
            class="presentation-section"
            v-for="section in sectionList"
            :sectionDetail="section"
            :key="section.id"
            :presentationId="presentationId"
            :version="presentationFormVersion"
          />
          <EmptySection v-if="isSectionListEmpty" />
        </b-card>
      </div>
    </b-overlay>
  </div>
</template>

<script>
import AbstractSectionDetail from "@/components/AbstractSectionDetail.vue";
import { ID_NEW_PRESENTATION } from "@/common/const";
import PredefinedQueries from "@/store/data/predefinedQueries";
import EmptySection from "@/components/emptyStates/EmptySection.vue";

export default {
  props: {
    presentationId: String
  },
  watch: {
    presentationId: "fetchSectionList",
    presentationFormVersion() {
      this.updateVersion();
    }
  },
  data() {
    return {
      selectedNewSection: null,
      presentationFormVersion: null
    };
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },

    isPresentationEditable() {
      return this.$store.state.presentation.isPresentationEditable;
    },

    predefinedSections() {
      let sectionOptionsGroup = {};
      // grouping the predefined queries
      for (let key in PredefinedQueries) {
        if (!PredefinedQueries.hasOwnProperty(key)) {
          continue;
        }
        let groupName = PredefinedQueries[key].group;
        if (sectionOptionsGroup[groupName] === undefined) {
          sectionOptionsGroup[groupName] = [];
        }
        sectionOptionsGroup[groupName].push({
          value: key,
          label: PredefinedQueries[key].name
        });
      }

      // generate to format that element ui requires
      let sectionOptions = [];
      for (let groupName in sectionOptionsGroup) {
        if (!sectionOptionsGroup.hasOwnProperty(groupName)) {
          continue;
        }
        sectionOptions.push({
          label: groupName,
          options: sectionOptionsGroup[groupName]
        });
      }
      return sectionOptions;
    },

    isNewPresentation() {
      return this.presentationId === ID_NEW_PRESENTATION;
    },

    sectionList() {
      return this.$store.state.section.sectionList;
    },
    isSectionListEmpty() {
      return this.$store.state.section.sectionList.length <= 0;
    },
    isLoadingSectionList() {
      return this.$store.state.section.sectionListStatus.isLoading;
    },
    isSectionListApiError() {
      return this.$store.state.section.sectionListStatus.isApiError;
    },
    sectionListApiErrorMsg() {
      return this.$store.state.section.sectionListStatus.apiErrorMsg;
    },
    isLoadingDBMetaData() {
      return this.$store.state.dbMetaData.entitiesStatus.isLoading;
    },
    versions() {
      let list = Array.from(
        new Set(
          this.$store.state.presentation.versionList.map(v => v.versionId)
        )
      );
      this.setDefaultValueForVersionList(list[0]);
      return list;
    }
  },
  components: {
    AbstractSectionDetail,
    EmptySection
  },
  mounted() {
    this.fetchSectionList();
    this.$store.dispatch("fetchDBMetaDataEntities");
    this.$store.dispatch("getVersionList");
  },
  methods: {
    updateVersion() {
      var value = this.presentationFormVersion;
      if (value === undefined) {
        value = this.versions[0];
      }
      this.$store.commit("setPresentationFormField", {
        field: "version",
        value
      });
    },

    setDefaultValueForVersionList(value) {
      this.presentationFormVersion = value;
    },

    fetchSectionList() {
      if (this.isNewPresentation) {
        this.$store.commit("clearSectionList");
      } else {
        this.$store.dispatch("fetchSectionList", this.presentationId);
      }
    },

    addNewSection() {
      if (this.selectedNewSection.length === 0) {
        this.$notify.error({
          title: "Error",
          message: "Please select a section to add into presentation."
        });
        return;
      }
      this.$store
        .dispatch("addSectionDetail", {
          presentationId: this.presentationId,
          selectedNewSection: this.selectedNewSection,
          dataSet: this.$store.state.userInfo.userEmail
        })
        .then(() => {
          this.selectedNewSection = "";
        });
    }
  }
};
</script>

<style lang="scss" scoped></style>
