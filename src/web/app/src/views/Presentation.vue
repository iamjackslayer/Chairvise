<template>
  <div>
    <div class="title-bar">
      <h1 class="title">Presentations</h1>
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
      <EmptyPresentation v-if="isPresentationListEmpty" />

      <div class="list-grid">
        <b-card
          class="list-card shadow-sm rounded-lg"
          v-for="presentation in presentations"
          :key="presentation.id"
          @click="viewPresentation(presentation.id)"
        >
          <h5 class="list-title">{{ presentation.name }}</h5>
          <div class="list-description">
            {{ presentation.description || "-" }}
          </div>
          <div class="privacy-status">
            <template v-if="presentation.isPublic">
              <b-icon icon="people" class="mr-1" /> Public
            </template>
            <template v-else>
              <b-icon icon="lock" class="mr-1" /> Private
            </template>
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
  name: "Presentation",
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
      this.$router.push("/presentation/create");
    },
    loadPresentations() {
      this.show = true;
    },
    // TODO: Remove the infinite scroll stuff.
    loadMorePresentation() {
      this.count += 5;
    },
    viewPresentation(id) {
      this.$router.push("/presentation/" + id);
    }
  },
  mounted() {
    this.$store.dispatch("getPresentationList");
    this.loadPresentations();
  }
};
</script>

<style lang="scss" scoped></style>
