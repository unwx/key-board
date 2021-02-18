import 'regenerator-runtime/runtime'
import axios from "axios";

const apiURI = "/api/user"

export default {
    getAvatar: params => axios.get(apiURI + "/avatar/" + params.avatarName, {headers:
            {Authorization: "Bearer_" + params.accessToken}}).catch(function (error) {
        throw error
    })
}