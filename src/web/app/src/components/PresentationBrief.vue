<template>
  <div>
    <b-breadcrumb>
      <b-breadcrumb-item to="/presentation">My Presentations</b-breadcrumb-item>
      <b-breadcrumb-item active>Presentation Details</b-breadcrumb-item>
    </b-breadcrumb>
    <b-card>
      <div slot="header" class="presentation-header">
        <span> Presentation Details </span>
        <b-button-toolbar class="presentation-toolbar" v-if="!isInEditMode">
          <b-button-group class="mr-2">
            <b-button
              title="Download PDF"
              variant="outline-secondary"
              @click="downloadPDF()"
            >
              <b-icon icon="file-earmark-text-fill" aria-hidden="true" />
            </b-button>
            <b-button
              title="Download Powerpoint"
              variant="outline-secondary"
              @click="downloadPPTX()"
            >
              <b-icon icon="file-earmark-slides-fill" aria-hidden="true" />
            </b-button>
          </b-button-group>
          <b-button-group>
            <b-button
              title="Share"
              variant="primary"
              @click="openAccessControlPanel()"
              v-if="isLogin && isPresentationEditable"
            >
              <b-icon icon="share-fill" aria-hidden="true"></b-icon>
            </b-button>
            <b-button
              title="Edit"
              variant="primary"
              @click="changeEditMode(true)"
              v-if="!isInEditMode && isPresentationEditable"
            >
              <b-icon icon="pencil-square" aria-hidden="true"></b-icon>
            </b-button>
            <b-button
              title="Delete"
              variant="primary"
              @click="deletePresentation()"
              v-if="!isNewPresentation && isLogin && isPresentationEditable"
            >
              <b-icon icon="trash-fill" aria-hidden="true"></b-icon>
            </b-button>
          </b-button-group>
        </b-button-toolbar>
      </div>
      <b-form @submit.stop.prevent>
        <b-overlay :show="isLoading" no-wrap />
        <b-alert v-if="isError" show variant="danger" class="mb-4">
          <b-icon
            class="alert-icon"
            icon="exclamation-circle-fill"
            variant="danger"
          />
          {{ apiErrorMsg }}
        </b-alert>

        <b-form-group label="Name" label-for="name">
          <div v-if="!isInEditMode">{{ presentationFormName }}</div>
          <b-form-input
            id="name"
            name="name"
            v-if="isInEditMode"
            v-model="presentationFormName"
            :state="validateState('name')"
            aria-describedby="name-live-feedback"
          />
          <b-form-invalid-feedback id="name-live-feedback"
            >This is a required field and must be at least 3 characters.
          </b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Access Control" label-for="access-control">
          <b-badge variant="info">
            Created by {{ presentationForm.creatorIdentifier }}
          </b-badge>
        </b-form-group>

        <b-form-group label="Description" label-for="description">
          <div v-if="!isInEditMode">
            {{ presentationFormDescription || "-" }}
          </div>
          <b-form-textarea
            id="description"
            name="description"
            v-if="isInEditMode"
            :state="validateState('description')"
            v-model="presentationFormDescription"
          />
        </b-form-group>
        <div class="mt-4" v-if="isInEditMode">
          <b-button @click="addPresentation()" variant="primary">
            Submit
          </b-button>
          <b-button
            class="ml-2"
            @click="changeEditMode(false)"
            variant="secondary"
          >
            Cancel
          </b-button>
        </div>
      </b-form>
    </b-card>

    <b-modal
      size="xl"
      title="Share with other users"
      centered
      :visible.sync="isAccessControlDialogVisible"
      hide-footer
      @hidden="isAccessControlDialogVisible = false"
    >
      <access-control-panel :presentationId="id"></access-control-panel>
    </b-modal>
  </div>
</template>

<script>
import { required, minLength } from "vuelidate/lib/validators";
import AccessControlPanel from "@/components/AccessControlPanel";
import { downloadPDF } from "@/store/helpers/pdfDownloader";
import { downloadPPTX } from "@/store/helpers/pptxDownloader";
import {
  AccessLevel,
  ID_NEW_PRESENTATION,
  SPECIAL_IDENTIFIER_PUBLIC
} from "@/common/const";
import { deepCopy } from "@/common/utility";

export default {
  name: "PresentationBrief",
  props: {
    id: String
  },
  mounted() {
    this.updatePresentationForm();
  },
  watch: {
    id() {
      this.updatePresentationForm();
    }
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },
    isPresentationEditable() {
      return this.$store.state.presentation.isPresentationEditable;
    },

    presentationForm() {
      return {
        name: this.presentationFormName,
        creatorIdentifier: this.presentationFormCreatorIdentifier,
        description: this.presentationFormDescription
      };
    },
    presentationFormName: {
      get() {
        return this.$store.state.presentation.presentationForm.name;
      },
      set(value) {
        this.$store.commit("setPresentationFormField", {
          field: "name",
          value
        });
      }
    },
    presentationFormCreatorIdentifier() {
      return this.$store.state.presentation.presentationForm.creatorIdentifier;
    },
    presentationFormDescription: {
      get() {
        return this.$store.state.presentation.presentationForm.description;
      },
      set(value) {
        this.$store.commit("setPresentationFormField", {
          field: "description",
          value
        });
      }
    },
    isNewPresentation() {
      return this.id === ID_NEW_PRESENTATION;
    },
    isInEditMode() {
      return this.isEditing || this.isNewPresentation;
    },
    isLoading() {
      return this.$store.state.presentation.presentationFormStatus.isLoading;
    },
    isError() {
      return this.$store.state.presentation.presentationFormStatus.isApiError;
    },
    apiErrorMsg() {
      return this.$store.state.presentation.presentationFormStatus.apiErrorMsg;
    }
  },
  validations: {
    presentationForm: {
      name: {
        required,
        minLength: minLength(3)
      },
      description: {
        alphaNum: true
      }
    }
  },
  data() {
    return {
      isEditing: false,
      isAccessControlDialogVisible: false
    };
  },
  methods: {
    changeEditMode(isEditing) {
      if (isEditing === false) {
        this.updatePresentationForm();
      }
      this.isEditing = isEditing;
    },

    openAccessControlPanel() {
      this.isAccessControlDialogVisible = true;
    },

    addPresentation() {
      this.$v.presentationForm.$touch();
      if (this.$v.presentationForm.$anyError) {
        return;
      }

      if (this.isNewPresentation) {
        // add
        this.$store.dispatch("savePresentation").then(() => {
          if (this.isError) {
            return;
          }
          // redirect to the newly added presentation
          this.$router.push({
            name: "presentation",
            params: {
              id: this.$store.state.presentation.presentationForm.id
            }
          });
        });
      } else {
        // edit
        this.$store.dispatch("updatePresentation").then(() => {
          if (this.isError) {
            return;
          }
          this.$v.$reset();
          this.isEditing = false;
        });
      }
    },
    deletePresentation() {
      this.$store.dispatch("deletePresentation", this.id).then(() => {
        if (this.isError) {
          return;
        }
        this.$router.replace({
          name: "presentation",
          params: {
            id: ID_NEW_PRESENTATION
          }
        });
        this.isEditing = false;
      });
    },
    updatePresentationForm() {
      if (this.$refs["presentationForm"]) {
        this.$refs["presentationForm"].clearValidate();
      }
      this.$store.commit("resetPresentationForm");
      if (this.id !== ID_NEW_PRESENTATION) {
        this.$store.dispatch("getPresentation", this.id).then(() => {
          // check writable or not
          this.$store.dispatch("fetchAccessControlList", this.id).then(() => {
            let currentUser = this.$store.state.userInfo.userEmail;
            let accessControlList = this.$store.state.accessControl
              .accessControlList;
            let isPresentationEditable =
              currentUser === this.presentationFormCreatorIdentifier ||
              accessControlList.some(
                acl =>
                  acl.userIdentifier === currentUser &&
                  acl.accessLevel === AccessLevel.CAN_WRITE
              ) ||
              accessControlList.some(
                acl =>
                  acl.userIdentifier === SPECIAL_IDENTIFIER_PUBLIC &&
                  acl.accessLevel === AccessLevel.CAN_WRITE
              );
            this.$store.commit(
              "setIsPresentationEditable",
              isPresentationEditable
            );
          });
        });
      }
    },
    downloadPDF() {
      window.scrollTo(0, 0);
      let vm = this;
      let wasPresentationEditable = deepCopy(vm.isPresentationEditable);
      vm.$store.commit("setIsPresentationEditable", false);
      vm.$store.commit("setPageLoadingStatus", true);

      this.$nextTick(() => {
        downloadPDF(vm.presentationFormName).then(() => {
          vm.$store.commit(
            "setIsPresentationEditable",
            wasPresentationEditable
          );
          vm.$store.commit("setPageLoadingStatus", false);
        });
      });
    },
    downloadPPTX() {
      window.scrollTo(0, 0);
      let vm = this;
      let wasPresentationEditable = deepCopy(vm.isPresentationEditable);
      vm.$store.commit("setIsPresentationEditable", false);
      vm.$store.commit("setPageLoadingStatus", true);

      this.$nextTick(() => {
        downloadPPTX(vm.presentationFormName).then(() => {
          vm.$store.commit(
            "setIsPresentationEditable",
            wasPresentationEditable
          );
          vm.$store.commit("setPageLoadingStatus", false);
        });
      });
    },
    validateState(name) {
      const { $dirty, $error } = this.$v.presentationForm[name];
      return $dirty ? !$error : null;
    }
  },

  components: {
    AccessControlPanel
  }
};
</script>

<style lang="scss" scoped>
.presentation-header {
  display: flex;
  flex-direction: column;

  @include media-breakpoint-up(lg) {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
}

.presentation-toolbar {
  margin-top: 0.5rem;

  @include media-breakpoint-up(lg) {
    margin-top: 0;
  }
}
</style>
