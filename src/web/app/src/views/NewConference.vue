<template>
  <div>
    <div class="title-bar">
      <h1 class="title">Add Conference</h1>
    </div>
    <b-alert
      v-if="isNewConference && !isLogin"
      show
      variant="danger"
      class="mb-2"
    >
      <b-icon
        class="alert-icon"
        icon="exclamation-circle-fill"
        variant="danger"
      />
      Please login to create new conference
    </b-alert>
    <b-card v-else class="form">
      <b-overlay :show="isLoading" no-wrap />

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
                v-model="conferenceFormName"
                :state="validateState('name')"
                aria-describedby="name-live-feedback"
              />
              <b-form-invalid-feedback id="name-live-feedback"
                >This is a required field and must be at least 3
                characters.</b-form-invalid-feedback
              >
            </b-form-group>

            <b-form-group label="Description" label-for="description">
              <b-form-textarea
                id="description"
                name="description"
                rows="3"
                max-rows="6"
                :state="validateState('description')"
                v-model="conferenceFormDescription"
              />
            </b-form-group>

            <b-form-group label="Date" label-for="date">
              <b-form-datepicker
                id="date"
                name="date"
                v-model="conferenceFormDate"
                :state="validateState('date')"
                aria-describedby="date-live-feedback"
              />

              <b-form-invalid-feedback id="date-live-feedback">
                This is a required field.
              </b-form-invalid-feedback>
            </b-form-group>

            <b-button type="submit" class="responsive-btn" variant="primary">
              Submit
            </b-button>
          </div>
        </div>
      </b-form>
    </b-card>

    <!-- <pre class="debug">{{ $v }}</pre> -->
    <!-- dialogs -->
    <b-modal
      title="Confirm"
      centered
      :visible.sync="hasSubmitted"
      @ok="addConference"
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
import { ID_NEW_CONFERENCE } from "../common/const";
export default {
  name: "AddConference",
  props: {
    id: String
  },
  watch: {
    id() {
      this.updateConferenceForm();
    }
  },
  mounted() {
    this.updateConferenceForm();
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },
    conferenceForm() {
      return {
        name: this.conferenceFormName,
        description: this.conferenceFormDescription,
        date: this.conferenceFormDate
      };
    },
    conferenceFormName: {
      get() {
        return this.$store.state.conference.conferenceForm.name;
      },
      set(value) {
        this.$store.commit("setConferenceFormField", {
          field: "name",
          value
        });
        // TODO: Add this.$v.name.$touch()
        // To get on touch validation
      }
    },
    conferenceFormDescription: {
      get() {
        return this.$store.state.conference.conferenceForm.description;
      },
      set(value) {
        this.$store.commit("setConferenceFormField", {
          field: "description",
          value
        });
      }
    },
    conferenceFormDate: {
      get() {
        return this.$store.state.conference.conferenceForm.date;
      },
      set(value) {
        this.$store.commit("setConferenceFormField", {
          field: "date",
          value
        });
      }
    },
    isNewConference() {
      return this.id === ID_NEW_CONFERENCE;
    },
    isInEditMode() {
      return this.isEditing || this.isNewPresentation;
    },
    saveSuccess() {
      return this.$store.state.conference.isSaveSuccess;
    },
    isLoading() {
      return this.$store.state.conference.conferenceFormStatus.isLoading;
    },
    isError() {
      return this.$store.state.conference.conferenceFormStatus.isApiError;
    },
    apiErrorMsg() {
      return this.$store.state.conference.conferenceFormStatus.apiErrorMsg;
    }
  },
  methods: {
    addConference() {
      this.hasSubmitted = false;
      this.$store.dispatch("saveConference").then(() => {
        if (this.isNewConference && !this.isLogin) {
          return;
        }
      });
    },
    updateConferenceForm() {
      if (this.$v) {
        this.$v.$reset();
      }
      this.$store.commit("resetConferenceForm");
    },
    uploadClicked() {
      this.$v.conferenceForm.$touch();
      if (this.$v.conferenceForm.$anyError) {
        return;
      }

      this.hasSubmitted = true;
    },
    closeSuccess() {
      this.$store.commit("setSaveSuccess", false);
      this.$router.push({
        name: "conference"
      });
    },
    validateState(name) {
      const { $dirty, $error } = this.$v.conferenceForm[name];
      return $dirty ? !$error : null;
    }
  },
  validations: {
    conferenceForm: {
      name: {
        required,
        minLength: minLength(3)
      },
      description: {
        alphaNum: true
      },
      date: {
        required
      }
    }
  },
  data() {
    return {
      hasSubmitted: false
    };
  }
};
</script>

<style lang="scss" scoped>
// .temp {
//   display: block;
//   background-color: $gray-200;
//   color: $red-600;
//   padding: 1rem;
//   max-width: 35em;
// }
</style>
