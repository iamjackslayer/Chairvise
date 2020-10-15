<template>
  <div>
    <div class="title-bar">
      <h1 class="title">My Conferences</h1>
      <b-button class="title-action" variant="primary" @click="createConference"
        ><b-icon icon="plus"></b-icon> Add New Conference</b-button
      >
    </div>

    <b-card>
      <FullCalendar :options="calendarOptions" />
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
      return {
        themeSystem: "bootstrap",
        plugins: [dayGridPlugin, timeGridPlugin, listPlugin, interactionPlugin],
        // initialView: "dayMonthView",
        events: this.conferences,
        eventClick: this.eventSelected,
        selectable: true,
        headerToolbar: {
          left: "today prev,next",
          center: "title",
          right: "dayGridMonth timeGridWeek timeGridDay listYear"
        }
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
    loadConferences() {
      this.show = true;
    },
    viewConference(id) {
      this.$router.push("/conference/" + id);
    },
    eventSelected({ event }) {
      this.viewConference(event.id);
    }
  },
  mounted() {
    this.$store.dispatch("getConferenceList");
    this.loadConferences();
  }
};
</script>

<style lang="scss" scoped></style>
