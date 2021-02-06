import Vue from 'vue'
import Vuex from 'vuex'

import articleApi from "../api/articleApi";

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        articles: frontendData.articles,
        URI: URI,
    },
    getters: {
        sortedArticles: state => (state.articles || []).sort((a, b) => -(a.id - b.id))
    },
    mutations: {
        addArticleMutation(state, article) {
            state.articles = [
                article,
                ...state.articles
            ]
        }
    },
    actions: {
        async addArticleAction({commit, state}, article){
            const result = await articleApi.add(article);
            const response = await result.data;
            commit('addArticleMutation', response);
        },
    }
})