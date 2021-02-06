import Vue from 'vue'
import VueRouter from 'vue-router'

import ArticleCreatePage from "../pages/ArticleCreatePage.vue";
import ArticlePage from "../pages/ArticlePage.vue";

Vue.use(VueRouter)

const routes = [
    { path: '/article/create', component: ArticleCreatePage},
    { path: '/', component: ArticlePage},

]

export default new VueRouter({
    mode: 'history',
    routes
})