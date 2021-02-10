import Vue from 'vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'vuetify/dist/vuetify.min.css'

import VueHighlightJS from 'vue-highlight.js';
import hljs from 'highlight.js'
import 'vue-highlight.js/lib/allLanguages'

import router from 'router/router'
import store from "store/store";
import HomeV from "./pages/HomeV.vue";
Vue.use(Vuetify)
Vue.use(VueHighlightJS)
new Vue({
    vuetify : new Vuetify(),
    store,
    router,
    el: '#app',
    render: a => a(HomeV),
})

