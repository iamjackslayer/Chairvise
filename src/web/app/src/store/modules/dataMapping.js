import axios from "axios";
import { processMapping } from "@/store/helpers/processor.js";

export default {
  state: {
    hasDBSchemaSet: false,
    hasFileUploaded: false,
    hasFormatTypeSpecified: false,
    hasTableTypeSelected: false,
    hasConferenceNameSpecified: false,
    hasHeaderSpecified: false,
    hasPredefinedSpecified: false,
    hasPredefinedSwitchSpecified: false, // new
    hasMappingFinished: false,
    isUploadSuccess: false,
    data: {
      dbSchema: null,
      uploadedData: [],
      uploadedLabel: [],
      mappingResult: [],
      processedResult: [],
      formatType: null,
      tableType: null,
      isNewConference: null,
      conferenceName: null,
      hasHeader: null,
      hasPredefined: null, // new
      predefinedMapping: null,
      predefinedMappingId: null
    },
    error: []
  },

  mutations: {
    setUploadSuccess(state, success) {
      state.isUploadSuccess = success;
    },

    setUploadedFile(state, data) {
      state.data.uploadedLabel = data[0];
      state.data.uploadedData = data;
      state.hasFileUploaded = true;
    },

    clearUploadedFile(state) {
      state.data.uploadedLabel = [];
      state.data.uploadedData = [];
      state.hasFileUploaded = false;
    },

    setDBSchema(state, dbSchema) {
      state.data.dbSchema = dbSchema;
      state.hasDBSchemaSet = true;
    },

    clearDBSchema(state) {
      state.data.dbSchema = [];
      state.hasDBSchemaSet = false;
    },

    setFormatType(state, formatType) {
      state.data.formatType = formatType;
      state.hasFormatTypeSpecified = true;
    },

    clearFormatType(state) {
      state.data.formatType = null;
      state.hasFormatTypeSpecified = false;
    },

    setTableType(state, selected) {
      state.data.tableType = selected;
      state.hasTableTypeSelected = true;
    },

    clearTableType(state) {
      state.data.tableType = null;
      state.hasTableTypeSelected = false;
    },

    setConferenceName(state, selected) {
      state.data.conferenceName = selected;
      state.hasConferenceNameSpecified = true;
    },

    clearConferenceName(state) {
      state.data.conferenceName = null;
      state.hasConferenceNameSpecified = false;
    },

    setIsNewConference(state, selected) {
      state.data.isNewConference = selected;
    },

    clearIsNewConference(state) {
      state.data.isNewConference = null;
    },

    setHasHeader(state, hasHeader) {
      state.data.hasHeader = hasHeader;
      state.hasHeaderSpecified = true;
    },

    clearHasHeader(state) {
      // Changing this to false by default since it's optional
      state.data.hasHeader = false;
      // Shouldn't need this variable since it's an optional field
      state.hasHeaderSpecified = false;
    },

    setPredefinedMapping(state, payload) {
      state.data.predefinedMapping = payload.mapping;
      state.data.predefinedMappingId = payload.id;
      state.hasPredefinedSpecified = true;
    },

    clearPredefinedMapping(state) {
      state.data.predefinedMapping = null;
      state.data.predefinedMappingId = null;
      state.hasPredefinedSpecified = false;
    },

    // TODO: Review difference between predefined mapping and predefined switch
    setPredefinedSwitch(state, hasPredefined) {
      state.data.hasPredefined = hasPredefined;
      state.hasPredefinedSwitchSpecified = true;
    },

    clearPredefinedSwitch(state) {
      // Changing this to false by default since it's optional
      state.data.hasPredefined = null;
      // Shouldn't need this variable since it's optional field
      state.hasPredefinedSwitchSpecified = false;
    },

    setMapping(state, payload) {
      try {
        state.error = [];
        state.data.mappingResult = payload.map;
        state.mappingFinished = true;
        state.data.processedResult = processMapping(
          payload.map,
          state.data.uploadedData,
          state.data.dbSchema,
          state.data.hasHeader
        );
      } catch (err) {
        state.error.push(err);
        state.mappingFinished = false;
        state.data.mappingResult = [];
        state.data.processedResult = [];
      }
    },

    clearMapping(state) {
      state.data.mappingResult = [];
      state.data.processedResult = [];
      state.mappingFinished = false;
    },

    setDataMappingError(state, err) {
      state.error.push(err);
    },

    clearError(state) {
      state.error = [];
    }
  },

  actions: {
    async persistMappingNewConference({ commit, state }) {
      commit("setPageLoadingStatus", true);
      let endpoint;
      let fnKeyTable;
      switch (state.data.tableType) {
        case 0:
          endpoint = "author";
          fnKeyTable = "AuthorRecord";
          break;
        case 1:
          endpoint = "review";
          fnKeyTable = "ReviewRecord";
          break;
        case 2:
          endpoint = "submission";
          fnKeyTable = "SubmissionRecord";
          break;
      }
      var fnKeyEntry = {};
      fnKeyEntry.conferenceName = state.data.conferenceName;
      fnKeyEntry.recordType = fnKeyTable;

      // add conference to end
      for (var i = 0; i < state.data.processedResult.length; i++) {
        var row = state.data.processedResult[i];
        row.conferenceName = state.data.conferenceName;
      }

      // concurrent POST data and POST conference requests
      axios
        .all([
          postTable(endpoint, state.data.processedResult)
          // postConference(fnKeyEntry)
        ])
        .then(
          axios.spread(() => {
            commit("setPageLoadingStatus", false);
            commit("setUploadSuccess", true);
          })
        )
        .catch(
          axios.spread(function(dataErr, verErr) {
            commit("setPageLoadingStatus", false);
            commit("setUploadSuccess", false);
            commit("setDataMappingError", dataErr);
            commit("setDataMappingError", verErr);
          })
        );
    },

    async persistMappingOldConference({ commit, state }) {
      commit("setPageLoadingStatus", true);
      let endpoint;
      switch (state.data.tableType) {
        case 0:
          endpoint = "author";
          break;
        case 1:
          endpoint = "review";
          break;
        case 2:
          endpoint = "submission";
          break;
      }
      // add conference to end
      for (var i = 0; i < state.data.processedResult.length; i++) {
        var row = state.data.processedResult[i];
        row.conferenceName = state.data.conferenceName;
      }
      //console.log(state.data.processedResult);
      await axios
        .post("/api/record/" + endpoint, state.data.processedResult)
        .then(() => {
          commit("setPageLoadingStatus", false);
          commit("setUploadSuccess", true);
        })
        .catch(e => {
          commit("setPageLoadingStatus", false);
          commit("setUploadSuccess", false);
          commit("setDataMappingError", e.toString());
        });
    }
  }
};
// function postConference(fnKeyEntry) {
//   return axios.post("/api/conference", fnKeyEntry);
// }

function postTable(endpoint, processedResult) {
  return axios.post("/api/record/" + endpoint, processedResult);
}
