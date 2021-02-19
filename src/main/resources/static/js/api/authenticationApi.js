import 'regenerator-runtime/runtime'
import axios from "axios";

const apiURI = "/api/auth"

export default {
    login: usernamePassword => axios.post(apiURI + "/login", usernamePassword).catch(function (error){
        throw error
    }),
    registration: user =>  axios.post(apiURI + "/registration", user).catch(function (error) {
        throw error
    }),
    refresh: refreshToken => axios.post(apiURI + "/refresh", {},
        {headers: {Authorization:"Bearer_" + refreshToken}}
        ).catch(function (error) {
        throw error
    }),
}