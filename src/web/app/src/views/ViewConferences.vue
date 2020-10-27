<template>
  <div>
    <div class="title-bar">
      <h1 class="title">Calendar</h1>
      <b-button class="title-action" variant="primary" @click="createConference"
        ><b-icon icon="plus"></b-icon> Add Conference</b-button
      >
    </div>
    <b-card>
      <FullCalendar ref="fullCalendar" :options="calendarOptions" />
    </b-card>
  </div>
</template>

<script>
import ConferenceBrief from "@/components/ConferenceBrief.vue";
import FullCalendar from "@fullcalendar/vue";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import listPlugin from "@fullcalendar/list";

export default {
  name: "ViewConferences",
  props: {
    id: String
  },
  data() {
    return {
      isWindowLarge: this.checkLargeScreen()
    };
  },
  watch: {
    isError() {
      if (!this.isError) {
        return;
      }

      this.$bvToast.toast(
        this.$store.state.conference.conferenceListStatus.apiErrorMsg || "-",
        {
          title: "Conference list API request fail",
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
      return this.$store.state.conference.conferenceListStatus.isLoading;
    },

    isError() {
      return this.$store.state.conference.conferenceListStatus.isApiError;
    },
    conferences() {
      let list = this.$store.state.conference.conferenceList;
      // TODO: Review this timezone code, I think it can just be set to UTC
      var tzoffset = new Date().getTimezoneOffset() * 60000; //offset in milliseconds
      let newList = [];
      list.forEach(element => {
        var temp = new Date(element.date);
        var date = new Date(temp - tzoffset).toISOString().slice(0, -1);
        newList.push({
          id: element.id,
          title: element.name,
          start: date,
          allDay: true
        });
      });
      return newList;
    },
    calendarOptions() {
      const headerToolbarOptions = this.isWindowLarge
        ? {
            left: "today prev,next",
            center: "title",
            right: "dayGridMonth timeGridWeek timeGridDay listYear"
          }
        : {
            left: "title",
            center: "",
            right: "prev,next"
          };

      const view = this.isWindowLarge ? "dayGridMonth" : "listYear";

      return {
        themeSystem: "bootstrap",
        plugins: [dayGridPlugin, timeGridPlugin, listPlugin, interactionPlugin],
        initialView: view,
        events: this.conferences,
        eventClick: this.eventSelected,
        windowResize: this.handleResize,
        selectable: true,
        headerToolbar: headerToolbarOptions
      };
    }
  },
  components: {
    FullCalendar,
    ConferenceBrief
  },
  methods: {
    createConference() {
      this.$router.push("/conference/add");
    },
    viewConference(id) {
      this.$router.push("/conference/" + id);
    },
    eventSelected({ event }) {
      this.viewConference(event.id);
    },
    checkLargeScreen() {
      return window.innerWidth >= 992;
    },
    handleResize() {
      this.isWindowLarge = this.checkLargeScreen();
      const view = this.isWindowLarge ? "dayGridMonth" : "listYear";
      this.$refs.fullCalendar.getApi().changeView(view);
    }
  },
  mounted() {
    this.$store.dispatch("getConferenceList");
  }
};
</script>

<style lang="scss" scoped></style>
