import Vue from 'vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'vuetify/dist/vuetify.min.css'

import store from "store/store";
import HomeV from "./pages/HomeV.vue";
Vue.use(Vuetify)

new Vue({
    vuetify : new Vuetify(),
    store,
    el: '#app',
    render: a => a(HomeV),

})