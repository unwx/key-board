import Vue from 'vue'
import VueRouter from 'vue-router'

import ArticleCreatePage from "../pages/ArticleCreatePage.vue";
import ArticlesPage from "../pages/ArticlesPage.vue";
import HttpNotFound from "../errors/HttpNotFound.vue";
import ArticleFullPage from "../pages/ArticleFullPage.vue";

Vue.use(VueRouter)

const routes = [
    { path: '/article/create', component: ArticleCreatePage},
    { path: '/article/:link', component: ArticleFullPage},
    { path: '/', component: ArticlesPage},
    { path: '/**', component: HttpNotFound},
]

export default new VueRouter({
    mode: 'history',
    routes
})