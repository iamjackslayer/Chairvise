<template>
  <div class="page-container">
    <b-row>
      <b-col>
        <b-breadcrumb>
          <b-breadcrumb-item to="/conference">My Conferences</b-breadcrumb-item>
          <b-breadcrumb-item active>Conference Details</b-breadcrumb-item>
        </b-breadcrumb>
      </b-col>
    </b-row>
    <b-row>
      <b-col xs="12" lg="4" class="record-count">
        <b-card header="Author Records">
          <h1>{{ getAuthorRecord }}</h1>
        </b-card>
      </b-col>
      <b-col xs="12" lg="4" class="record-count">
        <b-card header="Submission Records">
          <h1>{{ getSubmissionRecord }}</h1>
        </b-card>
      </b-col>
      <b-col xs="12" lg="4" class="record-count">
        <b-card header="Review Records">
          <h1>{{ getReviewRecord }}</h1>
        </b-card>
      </b-col>
    </b-row>
    <b-row class="mt-3">
      <b-col>
        <b-card class="details-with-form">
          <div slot="header">
            <span>Conference Details</span>
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
              <div v-if="!isInEditMode">{{ conferenceFormName }}</div>
              <b-form-input
                id="name"
                name="name"
                v-if="isInEditMode"
                v-model="conferenceFormName"
              />
            </b-form-group>

            <b-form-group label="Description" label-for="description">
              <div v-if="!isInEditMode">
                {{ conferenceFormDescription || "-" }}
              </div>
              <b-form-textarea
                id="description"
                name="description"
                v-if="isInEditMode"
                v-model="conferenceFormDescription"
              />
            </b-form-group>

            <b-form-group label="Date" label-for="date">
              <div v-if="!isInEditMode" id="conference-date">
                {{ dayjs(conferenceFormDate) }}
              </div>
              <b-form-datepicker
                id="date"
                name="date"
                v-if="isInEditMode"
                v-model="conferenceFormDate"
              />
            </b-form-group>
          </b-form>
          <b-button
            variant="outline-danger"
            v-if="!isNewConference && isLogin"
            @click="deleteConference()"
          >
            <b-icon icon="trash-fill" aria-hidden="true"></b-icon>
            Delete Conference
          </b-button>
        </b-card>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import dayjs from "dayjs";
import { ID_NEW_CONFERENCE } from "@/common/const";

export default {
  name: "ConferenceBrief",
  props: {
    id: String
  },
  mounted() {
    this.updateConferenceForm();
  },
  watch: {
    id() {
      this.updateConferenceForm();
    }
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },
    getAuthorRecord() {
      return this.$store.state.conference.conferenceForm.numAuthorRecord;
    },
    getReviewRecord() {
      return this.$store.state.conference.conferenceForm.numReviewRecord;
    },
    getSubmissionRecord() {
      return this.$store.state.conference.conferenceForm.numSubmissionRecord;
    },
    // conferenceForm() {
    //   // TODO: Review the date time formatting
    //   // var tzoffset = new Date().getTimezoneOffset() * 60000; //offset in milliseconds
    //   // var date = new Date(new Date(this.conferenceFormDate) - tzoffset)
    //   //   .toISOString()
    //   //   .slice(0, -1);
    //   return {
    //     name: this.conferenceFormName,
    //     creatorIdentifier: this.conferenceFormCreatorIdentifier,
    //     description: this.conferenceFormDescription,
    //     date: date
    //   };
    // },
    conferenceFormName: {
      get() {
        return this.$store.state.conference.conferenceForm.name;
      },
      set(value) {
        this.$store.commit("setConferenceFormField", {
          field: "name",
          value
        });
      }
    },
    conferenceFormCreatorIdentifier() {
      return this.$store.state.conference.conferenceForm.creatorIdentifier;
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
      return this.isEditing || this.isNewConference;
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
  data() {
    return {
      isEditing: false,
      rules: {
        name: [
          {
            required: true,
            message: "Please enter conference name",
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
    dayjs(date) {
      return dayjs(date).format("dddd, MMMM D, YYYY");
    },
    changeEditMode(isEditing) {
      if (isEditing === false) {
        this.updateConferenceForm();
      }
      this.isEditing = isEditing;
    },
    deleteConference() {
      this.$store.dispatch("deleteConference", this.id).then(() => {
        if (this.isError) {
          return;
        }
        this.$router.replace({
          name: "conference",
          params: {
            id: ID_NEW_CONFERENCE
          }
        });
        this.isEditing = false;
      });
    },
    updateConferenceForm() {
      if (this.$v) {
        this.$v.$reset();
      }
      this.$store.commit("resetConferenceForm");
      if (this.id !== ID_NEW_CONFERENCE) {
        this.$store.dispatch("getConference", this.id);
      }
    }
  },

  components: {}
};
</script>

<style lang="scss" scoped>
.record-count:not(:first-child) {
  margin-top: 1rem;
  @include media-breakpoint-up(lg) {
    margin-top: 0;
  }
}
</style>
