<template>
  <div>
    <div class="title-bar">
      <h1 class="title">Add New Presentation</h1>
    </div>

    <b-alert v-if="isNewPresentation && !isLogin" show variant="danger">
      <b-icon
        class="alert-icon"
        icon="exclamation-circle-fill"
        variant="danger"
      />
      Please login to create new presentation
    </b-alert>
    <b-card v-else class="form">
      <b-form @submit.stop.prevent="uploadClicked">
        <div class="form-section">
          <div class="form-description">
            <h5>Basic Information</h5>
            <p class="form-section-description">
              Having an up-to-date email address attached to your acount is a
              great step toward improved account security.
            </p>
          </div>
          <div class="form-container">
            <b-form-group label="Name" label-for="name">
              <b-form-input
                id="name"
                name="name"
                v-model="presentationFormName"
                :state="validateState('name')"
                aria-describedby="name-live-feedback"
              />
              <b-form-invalid-feedback id="name-live-feedback"
                >This is a required field and must be at least 3 characters.
              </b-form-invalid-feedback>
            </b-form-group>

            <b-form-group label="Description" label-for="description">
              <b-form-textarea
                id="description"
                name="description"
                rows="3"
                max-rows="6"
                :state="validateState('description')"
                v-model="presentationFormDescription"
              />
            </b-form-group>

            <b-button type="submit" class="submit-btn" variant="primary"
              >Submit</b-button
            >
          </div>
        </div>
      </b-form>
    </b-card>

    <!-- dialogs -->
    <b-modal
      title="Confirm"
      centered
      :visible.sync="hasSubmitted"
      @ok="addPresentation"
      @hidden="hasSubmitted = false"
      @cancel="hasSubmitted = false"
    >
      <span> Are you sure that the conference details are correct?</span>
    </b-modal>
    <b-modal
      title="Success"
      centered
      :visible.sync="saveSuccess"
      ok-only
      @ok="closeSuccess"
      @hidden="closeSuccess"
    >
      <span>You have successfully added a new conference</span>
    </b-modal>
    <!-- end of dialogs -->
  </div>
</template>

<script>
import { required, minLength } from "vuelidate/lib/validators";
import {
  AccessLevel,
  ID_NEW_PRESENTATION,
  SPECIAL_IDENTIFIER_PUBLIC
} from "@/common/const";

export default {
  name: "PresentationBrief",
  props: {
    id: String
  },
  watch: {
    id() {
      this.updatePresentationForm();
    }
  },
  mounted() {
    this.updatePresentationForm();
    this.$store.dispatch("getVersionList");
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },
    presentationForm() {
      return {
        name: this.presentationFormName,
        creatorIdentifier: this.presentationFormCreatorIdentifier,
        description: this.presentationFormDescription
      };
    },
    presentationFormCreatorIdentifier() {
      return this.$store.state.presentation.presentationForm.creatorIdentifier;
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
    saveSuccess() {
      return this.$store.state.presentation.isSaveSuccess;
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
      hasSubmitted: false,
      test: true,
      rules: {
        name: [
          {
            required: true,
            message: "Please enter presentation name",
            trigger: "blur"
          },
          {
            min: 3,
            message: "The length should be more than 3 character",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    addPresentation() {
      this.hasSubmitted = false;
      this.$store.dispatch("savePresentation").then(() => {
        if (this.isNewPresentation && !this.isLogin) {
          return;
        }
      });
    },
    updatePresentationForm() {
      if (this.$v) {
        this.$v.$reset;
      }
      this.$store.commit("resetPresentationForm");
      if (this.id !== ID_NEW_PRESENTATION) {
        // TODO: Review when this is used
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
      } else {
        this.$store.dispatch("getVersionList");
      }
    },
    uploadClicked() {
      this.$v.presentationForm.$touch();
      if (this.$v.presentationForm.$anyError) {
        return;
      }

      this.hasSubmitted = true;
    },
    closeSuccess() {
      this.$store.commit("setSaveSuccess", false);
      this.$router.push({
        name: "analyze"
      });
    },
    validateState(name) {
      const { $dirty, $error } = this.$v.presentationForm[name];
      return $dirty ? !$error : null;
    }
  },
  components: {}
};
</script>

<style lang="scss" scoped></style>
