// import axios from "axios";
import 'regenerator-runtime/runtime'
import axios from "axios";

const apiURI = "/api/article"

export default {
    add: article => axios.post(apiURI, article).catch(function (error){
         throw error
    })
    // TODO : UPDATE;
    // TODO : REMOVE;
}