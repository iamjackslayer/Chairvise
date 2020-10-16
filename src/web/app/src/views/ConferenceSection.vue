<template>
  <div>
    <conference-brief :id="id" />
  </div>
</template>

<script>
import { ID_NEW_CONFERENCE } from "@/common/const";
import ConferenceBrief from "@/components/ConferenceBrief.vue";

export default {
  props: {
    id: String
  },
  data() {
    return {
      selectedNewSection: "",
      conferenceId: ""
    };
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },

    isNewConference() {
      return this.conferenceId === ID_NEW_CONFERENCE;
    }
  },
  components: {
    ConferenceBrief
  },
  mounted() {
    this.conferenceId = this.$route.params.id;
  },
  methods: {}
};
</script>
