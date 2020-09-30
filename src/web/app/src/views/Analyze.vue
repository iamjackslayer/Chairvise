<template>
  <!-- need to check layout alignment after adding presentations -->
  <div>
    <h1 class="alignLeft">My Created Presentations</h1>
    <b-button
      class="alignRight"
      type="primary"
      v-if="!isPresentationListEmpty"
      @click="createPresentation"
      >Add New Presentation</b-button
    >
    <br />
    <br />
    <hr />
    <div class="infinite-list-wrapper">
      <b-card
        class="shadow p-3 mb-5 bg-white rounded"
        v-if="isPresentationListEmpty"
      >
        <EmptyPresentation />
      </b-card>
      <ul
        class="infinite-list"
        v-infinite-scroll="loadMorePresentation"
        infinite-scroll-disabled="disabled"
        v-loading="isLoading"
      >
        <li
          v-for="(presentation, index) in presentations"
          :key="presentation.id"
        >
          <zoom-center-transition :duration="500" :delay="100 * (index - 1)">
            <b-card class="shadow p-3 mb-5 bg-white rounded">
              <b-button
                type="text"
                class="presentationCard"
                v-show="show"
                @click="viewPresentation(presentation.id)"
              >
                <b-row>
                  <b-col class="presentation-id" :span="1">
                    <p>#{{ presentation.id }}</p>
                  </b-col>
                  <b-col :span="19" :offset="1">
                    <p>{{ presentation.name }}</p>
                  </b-col>
                  <b-col :span="19" :offset="1">
                    <p>{{ presentation.description }}</p>
                  </b-col>
                </b-row>
              </b-button>
            </b-card>
          </zoom-center-transition>
        </li>
      </ul>
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
      this.$notify.error({
        title: "Presentation list API request fail",
        message: this.$store.state.presentation.presentationListStatus
          .apiErrorMsg,
        duration: 0
      });
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

<style scoped>
.alignLeft {
  float: left;
  display: inline-block;
  margin: 0;
}
.alignRight {
  float: right;
  display: inline-block;
  margin: 0;
}
.background {
  background-color: transparent;
  border-style: hidden;
}
.presentationCard {
  width: 100%;
  height: 100%;
  margin-bottom: 16px;
  background-color: white;
  text-align: left;
  color: black;
  padding: 4px 16px;
}

.el-card__body {
}
.menuCard {
  width: 100%;
  height: 100%;
}
.infinite-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.presentationCard .button {
  color: black;
  text-align: left;
}
.presentation-image {
  text-align: center;
  vertical-align: middle;
  margin-top: 1rem;
}
.presentation-id {
  margin-top: 1.7rem;
}
</style>
