<template>
  <div>
    <!-- The button alignment is off abit -->
    <h1 class="alignLeft">My Conferences</h1>
    <b-button class="alignRight" variant="primary" @click="createConference"
      ><b-icon icon="plus" font-scale="1"></b-icon>Add New Conference</b-button
    >
    <br />
    <br />
    <hr />
    <zoom-center-transition :duration="500" :delay="100">
      <b-card v-show="show">
        <FullCalendar
          :events="conferences"
          ref="fullCalendar"
          defaultView="month"
          :config="config"
          @event-selected="eventSelected"
        />
      </b-card>
    </zoom-center-transition>
  </div>
</template>

<script>
import { ZoomCenterTransition } from "vue2-transitions";
import { FullCalendar } from "vue-full-calendar";
import ConferenceBrief from "@/components/ConferenceBrief.vue";

export default {
  name: "ViewConferences",
  props: {
    id: String
  },
  data() {
    return {
      show: false,
      config: {
        height: 500,
        editable: false
      }
    };
  },
  watch: {
    isError() {
      if (!this.isError) {
        return;
      }
      this.$notify.error({
        title: "Conference list API request fail",
        message: this.$store.state.conference.conferenceListStatus.apiErrorMsg,
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
      return this.$store.state.conference.conferenceListStatus.isLoading;
    },
    conferences() {
      let list = this.$store.state.conference.conferenceList;
      var tzoffset = new Date().getTimezoneOffset() * 60000; //offset in milliseconds
      let newList = [];
      list.forEach(element => {
        var temp = new Date(element.date);
        var date = new Date(temp - tzoffset).toISOString().slice(0, -1);
        newList.push({
          id: element.id,
          title: element.name,
          start: date,
          allDay: false
        });
      });
      return newList;
    },
    isError() {
      return this.$store.state.conference.conferenceListStatus.isApiError;
    }
  },
  components: {
    ZoomCenterTransition,
    FullCalendar,
    ConferenceBrief
  },
  methods: {
    createConference() {
      this.$router.push("/conference/add");
    },
    loadConferences() {
      this.show = true;
    },
    viewConference(id) {
      this.$router.push("/conference/" + id);
    },
    eventSelected(event) {
      this.viewConference(event.id);
    }
  },
  mounted() {
    this.$store.dispatch("getConferenceList");
    this.loadConferences();
  }
};
</script>

<style scoped>
.alignLeft {
  float: left;
  display: inline-block;
  margin: 0;
  margin-top: 5;
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
.conferenceCard {
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
.conferenceCard .button {
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
.right {
  float: right;
}
</style>
