import axios from "axios";
import { deepCopy } from "@/common/utility";

export default {
  state: {
    commentList: [],

    commentListStatus: {
      isLoading: false,
      isApiError: false,
      apiErrorMsg: ""
    },

    commentForm: {
      id: "",
      comment: "",
      createdDate: "",
      lastUpdatedDate: "",
      userIdentifier: "",
      presentationId: ""
    },

    commentFormStatus: {
      isLoading: false,
      isApiError: false,
      apiErrorMsg: ""
    },

    isSaveSuccess: false
  },
  mutations: {
    setCommentListLoading(state, payload) {
      if (payload) {
        state.commentListStatus.isApiError = false;
      }
      state.commentListStatus.isLoading = payload;
    },

    setCommentListApiError(state, payload) {
      state.commentListStatus.isApiError = true;
      state.commentListStatus.apiErrorMsg = payload;
    },

    setCommentList(state, payload) {
      state.commentList = payload;
    },

    setCommentFormLoading(state, payload) {
      if (payload) {
        state.commentFormStatus.isApiError = false;
      }
      state.commentFormStatus.isLoading = payload;
    },

    setCommentFormApiError(state, payload) {
      state.commentFormStatus.isApiError = true;
      state.commentFormStatus.apiErrorMsg = payload;
    },

    setCommentForm(state, payload) {
      state.commentForm = payload;
    },

    setCommentFormField(state, { field, value }) {
      state.commentForm[field] = value;
    },

    addToCommentList(state, payload) {
      state.commentList.push(payload);
    },

    setSaveSuccess(state, success) {
      state.isSaveSuccess = success;
    }
  },
  actions: {
    async getCommentListForPresentation({ commit }, presentationId) {
      commit("setCommentListLoading", true);
      axios
        .get(`/api/presentations/${presentationId}/comments`)
        .then(response => {
          commit("setCommentList", response.data);
        })
        .catch(e => {
          commit("setCommentListApiError", e.toString());
        })
        .finally(() => {
          commit("setCommentListLoading", false);
        });
    },

    async addCommentForPresentation({ commit, state }, presentationId) {
      commit("setCommentFormLoading", true);
      axios
        .post(
          `/api/presentations/${presentationId}/comments`,
          state.commentForm
        )
        .then(response => {
          commit("addToCommentList", deepCopy(response.data));
          // commit("setConferenceForm", deepCopy(response.data));
          commit("setSaveSuccess", true);
        })
        .catch(e => {
          commit("setCommentFormApiError", e.toString());
        })
        .finally(() => {
          commit("setCommentFormLoading", false);
        });
    }
  }
};
