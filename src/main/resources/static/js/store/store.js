import Vue from 'vue'
import Vuex from 'vuex'

import articleApi from "../api/articleApi";

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        articles: frontendData.articles,
        apiError: null
    },
    getters: {
        sortedArticles: state => (state.articles || []).sort((a, b) => -(a.id - b.id))
    },
    mutations: {
        handleApiError(state, response) {
            state.apiError = response;
        },
        cleanErrorState(state) {
            state.apiError = null;
        },

        addArticleMutation(state, article) {
            state.articles = [
                article,
                ...state.articles
            ]
        },
    },
    actions: {
        async addArticleAction({commit, state}, article) {
            let errFlag = false;
            const result = await articleApi.add(article).catch(function (error) {
                if (error.response) {
                    commit('handleApiError', error.response.data)
                    errFlag = true;
                }})

            if (!errFlag){
                const data = await result.data;
                commit('addArticleMutation', data);
            }
        },
    }
})