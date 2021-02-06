import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        articles: frontendData.articles,
    },
    getters: {
        sortedArticles: state => (state.articles || []).sort((a, b) => -(a.id - b.id))
    }
})