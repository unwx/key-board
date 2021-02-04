import Vue from 'vue'
import HomeV from "./pages/HomeV.vue";
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'vuetify/dist/vuetify.min.css'
Vue.use(Vuetify)

new Vue({
    vuetify : new Vuetify(),
    el: '#app',
    render: a => a(HomeV)
})