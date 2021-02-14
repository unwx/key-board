import 'regenerator-runtime/runtime'
import axios from "axios";

const apiURI = "/api/article"

export default {
    add: params =>  axios.post(apiURI, params.article, {headers:
            {Authorization: "Bearer_" + params.accessToken}
    }).catch(function (error) {
        throw error
    })
    // TODO : UPDATE;
    // TODO : REMOVE;

}