import 'regenerator-runtime/runtime'
import axios from "axios";

const apiURI = "/api/user"

export default {
    getAvatar: params => axios.get(apiURI + "/avatar/" + params.avatarName, {
        headers:
            {Authorization: "Bearer_" + params.accessToken}
    }).catch(function (error) {
        throw error
    }),
    uploadAvatar: params => axios.post(apiURI + "/change/avatar", {params: {avatar: params.avatar}},
        {
            headers:
                {'Content-Type': 'multipart/form-data', Authorization: "Bearer_" + params.accessToken}

        }).catch(function (error) {
        throw error
    }),
}