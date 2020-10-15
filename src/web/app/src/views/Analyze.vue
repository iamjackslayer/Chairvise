<template>
  <div>
    <div class="title-bar">
      <h1 class="title">My Presentations</h1>
      <b-button
        class="title-action"
        variant="primary"
        v-if="!isPresentationListEmpty"
        @click="createPresentation"
      >
        <b-icon icon="plus"></b-icon>
        Add New Presentation
      </b-button>
    </div>
    <div>
      <b-card
        class="shadow p-3 mb-5 bg-white rounded"
        v-if="isPresentationListEmpty"
      >
        <!-- TODO: Replace with openpeeps image -->
        <EmptyPresentation />
      </b-card>
      <div class="presentation-grid">
        <b-card
          class="presentation-card p-2 shadow-sm rounded-lg"
          v-for="presentation in presentations"
          :key="presentation.id"
          @click="viewPresentation(presentation.id)"
        >
          <h5 class="presentation-title">{{ presentation.name }}</h5>
          <div class="presentation-description">
            {{ presentation.description || "-" }}
          </div>
          <div class="privacy-status">
            <b-icon icon="lock" class="mr-1" />Private
          </div>
        </b-card>
      </div>
    </div>
  </div>
</template>

<script>
import { ZoomCenterTransition } from "vue2-transitions";
import EmptyPresentation from "@/components/emptyStates/EmptyPresentation.vue";

export default {
  name: "Analyze",
  props: {
    id: String
  },
  data() {
    return {
      show: false,
      count: 0
    };
  },
  watch: {
    isError() {
      if (!this.isError) {
        return;
      }
      this.$bvToast.toast(
        this.$store.state.presentation.presentationListStatus.apiErrorMsg ||
          "-",
        {
          title: "Presentation list API request fail",
          variant: "danger",
          solid: true,
          autoHideDelay: 0
        }
      );
    }
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },
    isAppLoading() {
      return this.$store.state.isPageLoading;
    },
    isLoading() {
      return this.$store.state.presentation.presentationListStatus.isLoading;
    },
    presentations() {
      return this.$store.state.presentation.presentationList;
    },
    isPresentationListEmpty() {
      return this.$store.state.presentation.presentationList.length <= 0;
    },
    isError() {
      return this.$store.state.presentation.presentationListStatus.isApiError;
    }
  },
  components: {
    ZoomCenterTransition,
    EmptyPresentation
  },
  methods: {
    createPresentation() {
      this.$router.push("/analyze/create");
    },
    loadPresentations() {
      this.show = true;
    },
    // TODO: Remove the infinite scroll stuff.
    loadMorePresentation() {
      this.count += 5;
    },
    viewPresentation(id) {
      this.$router.push("/analyze/" + id);
    }
  },
  mounted() {
    this.$store.dispatch("getPresentationList");
    this.loadPresentations();
  }
};
</script>

<style lang="scss" scoped>
@supports (display: grid) {
  .presentation-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    // TODO: Change to one column for mobile
    grid-gap: 1.5rem;
  }
}

.presentation-card {
  cursor: pointer;
  height: 200px;
}

.presentation-card:hover {
  background-color: $indigo-100;
}

.presentation-card > .card-body {
  display: flex;
  flex-direction: column;
}

.presentation-description {
  font-size: 0.95rem;
  letter-spacing: -0.025em;
  color: $gray-600;
  // clamp to 3 lines (restricted to webkit browsers)
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.privacy-status {
  margin-top: auto;
  font-size: 0.95rem;
  color: $indigo-600;
  align-self: flex-end;
}

.presentation-title {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
