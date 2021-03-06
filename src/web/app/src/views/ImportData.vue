<template>
  <div>
    <div class="title-bar">
      <h1 class="title">Import Data</h1>
    </div>
    <b-alert v-if="!isLogin && !isAppLoading" show variant="danger">
      <b-icon
        class="alert-icon"
        icon="exclamation-circle-fill"
        variant="danger"
      />
      You need to login to view this page
    </b-alert>

    <div v-if="isLogin">
      <mapping-tool v-if="isReadyForMapping" ref="mapTool"></mapping-tool>
      <b-card v-else class="form">
        <div class="form-section">
          <div class="form-description">
            <h5>Record Information</h5>
            <p class="form-section-description">
              Information about records and stuff. EasyChair and SoftConf
              blablabla.
            </p>
          </div>
          <div class="form-container">
            <b-form-group label="Conference Type">
              <b-form-radio-group
                id="conference-type"
                name="conference-type"
                button-variant="outline-primary"
                v-model="formatType"
                :options="[
                  { text: 'EasyChair', value: '1' },
                  { text: 'SoftConf', value: '2' }
                ]"
                buttons
              ></b-form-radio-group>
            </b-form-group>

            <b-form-group label="Table Type">
              <b-form-radio-group
                id="table-type"
                name="table-type"
                button-variant="outline-primary"
                v-model="tableType"
                buttons
              >
                <template v-for="(schema, idx) in dbSchemas">
                  <b-form-radio :value="idx" :key="schema.name">
                    {{ schema.name }}
                  </b-form-radio>
                </template>
              </b-form-radio-group>
            </b-form-group>
          </div>
        </div>

        <div class="form-section" v-if="isReadyForChoosing">
          <div class="form-description">
            <h5>Mapping Information</h5>
            <p class="form-section-description">
              Mapping information and stuff. wth does has header mean? explain
              what it means. Also what is predefined mapping come on. This is
              also optional btw.
            </p>
          </div>
          <div class="form-container">
            <b-form-group label="Has Header">
              <b-form-checkbox v-model="hasHeader" switch size="lg" />
            </b-form-group>
            <b-form-group label="Predefined Mapping">
              <b-form-checkbox v-model="hasPredefined" switch size="lg" />
            </b-form-group>
          </div>
        </div>
        <div class="form-section" v-if="isReadyForChoosing">
          <div class="form-description">
            <h5>Conference Information</h5>
            <p class="form-section-description">
              If the input conference is an existing conference, current record
              will be replaced based on record type. If the input conference is
              a new conference, current record will be created based on record
              type.
            </p>
          </div>
          <div class="form-container">
            <b-form-group label="Conference">
              <!-- TODO: Add check for when user has no "version" and add link to create new conference -->
              <b-form-select v-model="conferenceName" :options="querySearch()"
                ><template v-slot:first>
                  <b-form-select-option :value="null" disabled
                    >Please select an option</b-form-select-option
                  >
                </template></b-form-select
              >
            </b-form-group>

            <b-form-group label="Upload" v-if="isReadyForUpload">
              <b-form-file
                v-model="file"
                placeholder="Choose a file or drop it here..."
                drop-placeholder="Drop file here..."
              ></b-form-file>
            </b-form-group>
          </div>
        </div>
      </b-card>
    </div>
  </div>
</template>

<script>
import MappingTool from "@/components/MappingTool.vue";
import Papa from "papaparse";
import {
  REVIEW_DATE_DAY_FIELD,
  REVIEW_DATE_TIME_FIELD,
  REVIEW_TABLE_ID
} from "@/common/const";
import { deepCopy } from "@/common/utility";
import PredefinedMappings from "@/store/data/predefinedMapping";
import moment from "moment";

export default {
  name: "ImportData",
  data() {
    return {
      predefinedMappings: PredefinedMappings,
      file: null
    };
  },
  watch: {
    file(val) {
      this.fileUploadHandler(val);
    }
  },
  beforeCreate() {
    this.$store.dispatch("fetchDBMetaDataEntities");
    this.$store.dispatch("getConferenceList");
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },
    isAppLoading: function() {
      return (
        this.$store.state.isPageLoading ||
        this.$store.state.dbMetaData.entitiesStatus.isLoading
      );
    },
    dbSchemas: function() {
      return this.$store.state.dbMetaData.entities;
    },
    formatType: {
      get: function() {
        return this.$store.state.dataMapping.data.formatType;
      },
      set: function(newValue) {
        this.$store.commit("setFormatType", newValue);
      }
    },
    tableType: {
      get: function() {
        return this.$store.state.dataMapping.data.tableType;
      },
      set: function(newValue) {
        let dbSchema = deepCopy(this.dbSchemas[newValue]);
        if (newValue === REVIEW_TABLE_ID) {
          dbSchema.fieldMetaDataList.push(REVIEW_DATE_DAY_FIELD);
          dbSchema.fieldMetaDataList.push(REVIEW_DATE_TIME_FIELD);
        }
        this.$store.commit("setTableType", newValue);
        this.$store.commit("setDBSchema", dbSchema);
      }
    },
    conferenceName: {
      get: function() {
        return this.$store.state.dataMapping.data.conferenceName;
      },
      set: function(newValue) {
        this.$store.commit("setConferenceName", newValue);
      }
    },
    conferenceId: {
      get: function() {
        return this.$store.state.dataMapping.data.conferenceId;
      },
      set: function(newValue) {
        this.$store.commit("setConferenceId", newValue);
      }
    },
    hasHeader: {
      get: function() {
        return this.$store.state.dataMapping.data.hasHeader;
      },
      set: function(newValue) {
        this.$store.commit("setHasHeader", newValue);
      }
    },
    hasPredefined: {
      get: function() {
        return this.$store.state.dataMapping.data.hasPredefined;
      },
      set: function(newValue) {
        this.$store.commit("setPredefinedSwitch", newValue);
      }
    },
    predefinedMappingId: {
      // TODO: Review this. This is never used.
      get: function() {
        return this.$store.state.dataMapping.data.predefinedMappingId;
      },
      set: function(newValue) {
        this.$store.commit("setPredefinedMapping", {
          id: newValue,
          mapping: PredefinedMappings[newValue].mapping
        });
      }
    },
    isReadyForMapping: function() {
      return (
        this.$store.state.dataMapping.hasFileUploaded &&
        this.$store.state.dataMapping.hasFormatTypeSpecified &&
        this.$store.state.dataMapping.hasTableTypeSelected &&
        // this.$store.state.dataMapping.hasHeaderSpecified &&
        // this.$store.state.dataMapping.hasPredefinedSpecified &&
        this.$store.state.dataMapping.hasConferenceNameSpecified
      );
    },
    uploaded: function() {
      return this.$store.state.dataMapping.hasFileUploaded;
    },
    isReadyForUpload: function() {
      return (
        this.$store.state.dataMapping.hasFormatTypeSpecified &&
        this.$store.state.dataMapping.hasTableTypeSelected &&
        // this.$store.state.dataMapping.hasHeaderSpecified &&
        // this.$store.state.dataMapping.hasPredefinedSwitchSpecified &&
        this.$store.state.dataMapping.hasConferenceNameSpecified
      );
    },
    isReadyForChoosing: function() {
      return this.$store.state.dataMapping.hasTableTypeSelected;
    }
  },
  methods: {
    querySearch(queryString) {
      // TODO: Fix this for new database changes
      // convert to array of string
      var links = this.$store.state.presentation.conferenceList.map(
        v => v.name
      );
      // function to remove duplicate from array of string
      let reduceFunction = links =>
        links.filter((v, i) => links.indexOf(v) === i);
      links = reduceFunction(links);
      // links = links.map(v => {
      //   return { value: v };
      // });
      var results = queryString
        ? links.filter(this.createFilter(queryString))
        : links;

      return results;
      // cb(results);
    },
    createFilter(queryString) {
      return link => {
        return (
          link.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        );
      };
    },
    fileUploadHandler: function(file) {
      // show loading and go parsing
      this.$store.commit("setPageLoadingStatus", true);

      // if conferenceList is empty
      // console.log(this.$store.state.presentation.conferenceList);
      // filter by "AuthorRecord" "ReviewRecord" "SubmissionRecord"
      if (!this.$store.state.presentation.conferenceList) {
        this.$store.commit("setIsNewConference", false);
      } else {
        // if tabletype 0 author elif 1 review elif 2 sub
        // filter by "AuthorRecord" "ReviewRecord" "SubmissionRecord"
        var verList;
        switch (this.$store.state.dataMapping.data.tableType) {
          case 0:
            verList = this.$store.state.presentation.conferenceList
              .filter(v => v.recordType === "AuthorRecord")
              .map(v => v.conferenceName);
            break;
          case 1:
            verList = this.$store.state.presentation.conferenceList
              .filter(v => v.recordType === "ReviewRecord")
              .map(v => v.conferenceName);
            break;
          case 2:
            verList = this.$store.state.presentation.conferenceList
              .filter(v => v.recordType === "SubmissionRecord")
              .map(v => v.conferenceName);
            break;
          default:
        }
        this.$store.commit(
          "setIsNewConference",
          !verList.includes(this.$store.state.dataMapping.data.conferenceName)
        );
      }

      /*  tabletype 1	  1 - review 
            tabletype 0	  2 - author
            tabletype 2	  3 - sub */
      // map sub to sub// rev to rev // author to author if predefined mapping specified
      if (this.$store.state.dataMapping.data.hasPredefined) {
        switch (this.$store.state.dataMapping.data.tableType) {
          case 0:
            this.$store.commit("setPredefinedMapping", {
              id: 2,
              mapping: PredefinedMappings[2].mapping
            });
            break;
          case 1:
            this.$store.commit("setPredefinedMapping", {
              id: 1,
              mapping: PredefinedMappings[1].mapping
            });
            break;
          case 2:
            this.$store.commit("setPredefinedMapping", {
              id: 3,
              mapping: PredefinedMappings[3].mapping
            });
            break;
          default:
        }
      } else {
        this.$store.commit("setPredefinedMapping", {
          id: 0,
          mapping: PredefinedMappings[0].mapping
        });
      }

      Papa.parse(file, {
        // ignoring empty lines in csv file
        skipEmptyLines: true,
        complete: function(result) {
          var res = result;
          var res2 = res.data;
          var verId = this.$store.state.dataMapping.data.conferenceName;

          //author file preprocessing
          if (this.$store.state.dataMapping.data.tableType == "0") {
            var authorres = [];
            //ACL file preprocessing //Softconf
            if (this.$store.state.dataMapping.data.formatType == "2") {
              authorres.push([
                "submission #",
                "first name",
                "last name",
                "email",
                "country",
                "organization",
                "Web page",
                "person #",
                "corresponding?"
              ]);
              // for each row of data, manipulate temporary array element[]
              // then push to true array res2[] for parsing
              for (var i = 1; i < res2.length; i++) {
                var x = res2[i];
                //console.log(x);
                var k = 0,
                  j = 14,
                  element = [],
                  corr = "",
                  country = "";
                while (x[j] != "") {
                  if (x[j] == x[65] && x[j + 1] == x[66]) {
                    corr = "yes";
                    country = x[78];
                  } else {
                    corr = "no";
                    country = "";
                  }
                  element[k] = [
                    x[0],
                    x[j],
                    x[j + 1],
                    x[j + 2],
                    country,
                    x[j + 3],
                    "",
                    "",
                    corr,
                    verId
                  ];
                  authorres.push(element[k]);
                  k += 1;
                  j = 14 + k * 5;
                }
                //var element1=[x[0],x[14],x[15],x[16],"",x[17],"",""];
              }
              res2 = authorres;
              //console.log(authorres)
            }

            //author anonymization - Both formats
            var convertstring = require("convert-string");
            for (var m = 1; m < res2.length; m++) {
              var conv1 = convertstring.stringToBytes(res2[m][1]);
              var conv2 = convertstring.stringToBytes(res2[m][2]);
              var firstname = "";
              var lastname = "";
              for (var a = 0; a < conv1.length; a++) {
                firstname = firstname.concat(String(conv1[a] + 18));
              }
              for (var w = 0; w < conv2.length; w++) {
                lastname = lastname.concat(String(conv2[w] + 18));
              }
              res2[m][1] = firstname;
              res2[m][2] = lastname;
            }
            //console.log(res2);
          }

          //review file preprocessing
          else if (this.$store.state.dataMapping.data.tableType == "1") {
            //Softconf
            if (this.$store.state.dataMapping.data.formatType == "2") {
              var reviewres = [];
              reviewres.push([
                "Review Id",
                "Submission Id",
                "Num Review Assignment",
                "Reviewer Name",
                "Expertise Level",
                "Review Comment",
                "Confidence Level",
                "Overall Evaluation Score",
                "Column 9",
                "Column 10",
                "Column 11",
                "Column 12",
                "Day of the Review Date",
                "Time of the Review Date",
                "Has Recommended for the Best Paper"
              ]);

              for (var q = 1; q < res2.length; q++) {
                var z = res2[q];
                z[32] = "confidence: " + z[32];
                //console.log(typeof(z[7]));
                //var str=z[7].toString();
                var date_time = z[7].split(" ");
                //console.log(date_time);
                var date = date_time[0];
                var time =
                  date_time[1].split(":")[0] + ":" + date_time[1].split(":")[1];
                //console.log(date,time);
                element = [
                  "",
                  z[0],
                  "",
                  "",
                  "",
                  z[38],
                  z[32],
                  z[31],
                  "",
                  "",
                  "",
                  "",
                  date,
                  time,
                  "",
                  verId
                ];
                reviewres.push(element);
              }
              res2 = reviewres;
              //console.log(reviewres);
            }

            //author anonymization - JCDL
            // Easy Chair
            else if (this.$store.state.dataMapping.data.formatType == "1") {
              var convert_string = require("convert-string");
              for (var index = 1; index < res2.length; index++) {
                var convert = convert_string.stringToBytes(res2[index][3]);
                var name = "";
                for (var idx = 0; idx < convert.length; idx++) {
                  name = name.concat(String(convert[idx] + 18));
                }
                res2[index][3] = name;
              }
            }
          }

          //ACL submission file processing
          else if (this.$store.state.dataMapping.data.tableType == "2") {
            if (this.$store.state.dataMapping.data.formatType == "2") {
              var submissionres = [];
              submissionres.push([
                "#",
                "track #",
                "track name",
                "title",
                "authors",
                "submitted",
                "last updated",
                "form fields",
                "keywords",
                "decision",
                "notified",
                "reviews sent",
                "abstract"
              ]);

              for (var l = 1; l < res2.length; l++) {
                var y = res2[l];
                var dt = moment(y[10], "D MMM YYYY HH:mm:ss").format(
                  "YYYY-M-D H:m"
                );
                if (y[6].includes("Reject")) {
                  y[6] = "reject";
                } else {
                  y[6] = "accept";
                }
                //console.log(x);
                element = [
                  y[0],
                  "",
                  y[4],
                  y[2],
                  y[3],
                  dt,
                  dt,
                  "",
                  y[13],
                  y[6],
                  "",
                  "",
                  y[9],
                  verId
                ];
                submissionres.push(element);
              }
              res2 = submissionres;
            }
          }

          if (this.$store.state.dataMapping.data.formatType == "1") {
            var tempCSV = [];
            //author
            if (this.$store.state.dataMapping.data.tableType == "0") {
              tempCSV.push([
                "submission #",
                "first name",
                "last name",
                "email",
                "country",
                "organization",
                "Web page",
                "person #",
                "corresponding?"
              ]);
            }
            //review
            else if (this.$store.state.dataMapping.data.tableType == "1") {
              tempCSV.push([
                "Review Id",
                "Submission Id",
                "Num Review Assignment",
                "Reviewer Name",
                "Expertise Level",
                "Review Comment",
                "Confidence Level",
                "Overall Evaluation Score",
                "Column 9",
                "Column 10",
                "Column 11",
                "Column 12",
                "Day of the Review Date",
                "Time of the Review Date",
                "Has Recommended for the Best Paper"
              ]);
            }
            //submission
            else if (this.$store.state.dataMapping.data.tableType == "2") {
              tempCSV.push([
                "#",
                "track #",
                "track name",
                "title",
                "authors",
                "submitted",
                "last updated",
                "form fields",
                "keywords",
                "decision",
                "notified",
                "reviews sent",
                "abstract"
              ]);
            }
            // for each row of data, manipulate temporary array element[]
            // then push to true array res2[] for parsing
            var csvRow = [];
            for (var rowNum = 1; rowNum < res2.length; rowNum++) {
              csvRow = res2[rowNum];
              //csvRow.push(verId);
              tempCSV.push(csvRow);
            }
            res2 = tempCSV;
          }
          //console.log(res2);
          this.$store.commit("setUploadedFile", res2);
          this.$store.commit("setPageLoadingStatus", false);
        }.bind(this)
      });
    }
  },
  components: {
    MappingTool
  }
};
</script>

<style scoped></style>
