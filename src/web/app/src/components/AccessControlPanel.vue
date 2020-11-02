<template>
  <div>
    <b-overlay :show="isAccessControlPanelLoading" no-wrap />
    <b-form-group label="Shareable Link">
      <b-input-group prepend="Anyone with this link">
        <b-form-input id="name" name="name" v-model="currentUrl" disabled />
        <template v-slot:append>
          <b-form-select
            v-model="publicAccessLevel"
            :options="[
              { value: 'OFF', text: 'Cannot Access' },
              { value: 'CAN_READ', text: 'Can View' },
              { value: 'CAN_WRITE', text: 'Can Edit' }
            ]"
          ></b-form-select>
        </template>
      </b-input-group>
    </b-form-group>
    <b-form-group label="Access Control List">
      <b-alert
        v-if="isAccessControlListApiError"
        variant="danger"
        class="mb-4"
        show
      >
        <b-icon
          class="alert-icon"
          icon="exclamation-circle-fill"
          variant="danger"
        />
        {{ accessControlListApiErrorMsg }}
      </b-alert>
      <b-table
        outlined
        :fields="[
          { key: 'userIdentifier', label: 'Email' },
          'accessLevel',
          'actions'
        ]"
        :items="accessControlList"
      >
        <template v-slot:cell(accessLevel)="data">
          <b-form-select
            :value="data.value"
            @change="updateAccessControl(data.item, $event)"
            :options="[
              { value: 'CAN_READ', text: 'Can View' },
              { value: 'CAN_WRITE', text: 'Can Edit' }
            ]"
          ></b-form-select>
        </template>
        <template v-slot:cell(actions)="row">
          <b-button
            variant="outline-danger"
            @click="deleteAccessControl(row.item)"
          >
            <b-icon icon="trash-fill" aria-hidden="true"></b-icon>
          </b-button>
        </template>
      </b-table>
    </b-form-group>
    <b-form-group label="Add New Access Control">
      <b-alert
        v-if="isAccessControlFormApiError"
        variant="danger"
        class="mb-4"
        show
      >
        <b-icon
          class="alert-icon"
          icon="exclamation-circle-fill"
          variant="danger"
        />
        {{ accessControlFormApiErrorMsg }}
      </b-alert>
      <b-form @submit.stop.prevent="addAccessControl">
        <b-form-group label="Email address" label-for="userIdentifier">
          <b-form-input
            id="userIdentifier"
            name="userIdentifier"
            v-model="accessControlFormUserIdentifier"
            :state="validateState('userIdentifier')"
            aria-describedby="userIdentifier-live-feedback"
          />
          <b-form-invalid-feedback id="userIdentifier-live-feedback">
            This is a required field and should be a valid email format.
          </b-form-invalid-feedback>
          <!-- TODO: Add custom validation to check if permission already exists -->
        </b-form-group>
        <b-form-group label="Permissions" label-for="accessLevel">
          <b-form-select
            id="accessLevel"
            name="accessLevel"
            :state="validateState('accessLevel')"
            v-model="accessControlFormAccessLevel"
            :options="[
              { value: 'CAN_READ', text: 'View' },
              { value: 'CAN_WRITE', text: 'Edit' }
            ]"
            aria-describedby="accessLevel-live-feedback"
          ></b-form-select>
          <b-form-invalid-feedback id="accessLevel-live-feedback">
            This is a required field.
          </b-form-invalid-feedback>
        </b-form-group>

        <b-button class="responsive-btn" type="submit" variant="primary">
          Submit
        </b-button>
      </b-form>
    </b-form-group>
  </div>
</template>

<script>
import { required, email } from "vuelidate/lib/validators";
import { ID_NEW_PRESENTATION, SPECIAL_IDENTIFIER_PUBLIC } from "@/common/const";

export default {
  name: "AccessControlPanel",

  props: {
    presentationId: {
      type: String,
      required: true
    }
  },

  watch: {
    presentationId: {
      immediate: true,
      handler: "fetchAccessControlList"
    }
  },

  data() {
    return {
      accessControlFormRule: {
        userIdentifier: [
          {
            validator: (rule, value, callback) => {
              if (
                this.accessControlList.some(ele => ele.userIdentifier === value)
              ) {
                callback(
                  new Error("There is existent access control for the user")
                );
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ]
      },
      currentUrl: ""
    };
  },

  beforeUpdate() {
    this.currentUrl = window.location.href;
  },

  computed: {
    isAccessControlPanelLoading() {
      return (
        this.$store.state.accessControl.accessControlListStatus.isLoading ||
        this.$store.state.accessControl.accessControlFormStatus.isLoading
      );
    },

    isAccessControlListApiError() {
      return this.$store.state.accessControl.accessControlListStatus.isApiError;
    },

    accessControlListApiErrorMsg() {
      return this.$store.state.accessControl.accessControlListStatus
        .apiErrorMsg;
    },

    isAccessControlFormApiError() {
      return this.$store.state.accessControl.accessControlFormStatus.isApiError;
    },

    accessControlFormApiErrorMsg() {
      return this.$store.state.accessControl.accessControlFormStatus
        .apiErrorMsg;
    },

    accessControlList() {
      // filter out public access control in the ACL
      return this.$store.state.accessControl.accessControlList.filter(
        ac => ac.userIdentifier !== SPECIAL_IDENTIFIER_PUBLIC
      );
    },

    publicAccessLevel: {
      get: function() {
        let publicAccessLevelControl = this.$store.state.accessControl.accessControlList.find(
          ac => ac.userIdentifier === SPECIAL_IDENTIFIER_PUBLIC
        );
        if (publicAccessLevelControl === undefined) {
          return "OFF";
        }
        return publicAccessLevelControl.accessLevel;
      },
      set: function(accessLevel) {
        let publicAccessControl = this.$store.state.accessControl.accessControlList.find(
          ac => ac.userIdentifier === SPECIAL_IDENTIFIER_PUBLIC
        );

        // delete
        if (accessLevel === "OFF" && publicAccessControl !== undefined) {
          this.$store.dispatch("deleteAccessControl", {
            presentationId: this.presentationId,
            id: publicAccessControl.id
          });
          return;
        }

        if (publicAccessControl === undefined) {
          // create if not exist
          this.$store.dispatch("addAccessControl", {
            presentationId: this.presentationId,
            userIdentifier: SPECIAL_IDENTIFIER_PUBLIC,
            accessLevel
          });
        } else {
          // update if exist
          this.$store.dispatch("updateAccessControl", {
            presentationId: this.presentationId,
            id: publicAccessControl.id,
            accessLevel
          });
        }
      }
    },

    accessControlForm() {
      return {
        userIdentifier: this.accessControlFormUserIdentifier,
        accessLevel: this.accessControlFormAccessLevel
      };
    },

    accessControlFormUserIdentifier: {
      get() {
        return this.$store.state.accessControl.accessControlForm.userIdentifier;
      },
      set(value) {
        this.$store.commit("setAccessControlFormField", {
          field: "userIdentifier",
          value
        });
      }
    },
    accessControlFormAccessLevel: {
      get() {
        return this.$store.state.accessControl.accessControlForm.accessLevel;
      },
      set(value) {
        this.$store.commit("setAccessControlFormField", {
          field: "accessLevel",
          value
        });
      }
    }
  },
  validations: {
    accessControlForm: {
      userIdentifier: {
        required,
        email
      },
      // TODO: Add custom validation to check if email already has permissions
      accessLevel: {
        required,
        alphaNum: true
      }
    }
  },
  methods: {
    fetchAccessControlList() {
      if (this.presentationId === ID_NEW_PRESENTATION) {
        return;
      }
      this.$store.dispatch("fetchAccessControlList", this.presentationId);
    },

    updateAccessControl({ id }, $event) {
      this.$store.dispatch("updateAccessControl", {
        presentationId: this.presentationId,
        id,
        accessLevel: $event
      });
    },
    deleteAccessControl({ id }) {
      this.$store.dispatch("deleteAccessControl", {
        presentationId: this.presentationId,
        id
      });
    },
    addAccessControl() {
      this.$v.accessControlForm.$touch();
      if (this.$v.accessControlForm.$anyError) {
        return;
      }

      this.$store
        .dispatch("addAccessControl", {
          presentationId: this.presentationId,
          userIdentifier: this.accessControlFormUserIdentifier,
          accessLevel: this.accessControlFormAccessLevel
        })
        .then(() => {
          this.accessControlFormUserIdentifier = "";
          this.accessControlFormAccessLevel = "";

          this.$v.$reset();
        });
    },
    validateState(name) {
      const { $dirty, $error } = this.$v.accessControlForm[name];
      return $dirty ? !$error : null;
    }
  }
};
</script>

<style scoped>
.errorAlert {
  margin-bottom: 15px;
}
</style>
