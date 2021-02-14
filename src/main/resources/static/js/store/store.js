import Vue from 'vue'
import Vuex from 'vuex'

import articleApi from "../api/articleApi";
import authenticationApi from "../api/authenticationApi";
import cookies from 'vue-cookies'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        articles: frontendData.articles,
        apiError: null,
        clientError: null,
    },
    getters: {
        sortedArticles: state => (state.articles || []).sort((a, b) => -(a.id - b.id))
    },
    mutations: {
        handleApiError(state, response) {
            state.apiError = response;
        },
        /**
         *
         * @param state
         * @param code:
         * 10: accessTokenNotFound
         * 11: refreshTokenNotFound
         */
        handleClientError(state, code) {
            state.clientError = code
        },
        cleanErrorState(state) {
            state.apiError = null;
            state.clientError = null;
        },
        addArticleMutation(state, article) {
            state.articles = [
                article,
                ...state.articles
            ]
        },
        loginMutation(state, userWithTokens) {
            let user = userWithTokens.user;
            let accessToken = user.accessToken;
            let refreshToken = user.refreshToken;
            cookies.set('user', {username: user.username, email: user.email}, '1y');
            cookies.set('accessToken', accessToken, '1h');
            cookies.set('refreshToken', refreshToken, '1y');
        },
        registrationMutation(state, userWithTokens) {
            let user = userWithTokens.user;
            let accessToken = user.accessToken;
            let refreshToken = user.refreshToken;
            cookies.set('user', {username: user.username, email: user.email}, '1y');
            cookies.set('accessToken', accessToken, '1h');
            cookies.set('refreshToken', refreshToken, '1y');
        },
        refreshTokensMutation(state, tokens) {
            cookies.set('accessToken', tokens.accessToken, '1h');
            cookies.set('refreshToken', tokens.refreshToken, '1y');
        }
    },
    actions: {
        async clearErrorsAction({commit}) {
            commit('cleanErrorState')
        },
        async addArticleAction({commit, state}, article) {
            let errFlag = false;
            let accessToken = cookies.get("accessToken")
            if (accessToken !== null) {
                const result = await articleApi.add({article, accessToken}).catch(function (error) {
                    if (error.response) {
                        commit('handleApiError', error.response.data)
                        if (state.apiError === "Access Denied") {
                            errFlag = true;
                            state.refreshTokensAction(commit);
                        }
                    }
                })

                if (!errFlag) {
                    const data = await result.data;
                    commit('addArticleMutation', data);
                }
            } else {
                commit('handleClientError', 10);
            }
        },

        async loginAction({commit, state}, usernamePassword) {
            let errFlag = false;
            const result = await authenticationApi.login(usernamePassword).catch(function (error) {
                if (error.response) {
                    commit('handleApiError', error.response.data)
                    errFlag = true;
                }
            })

            if (!errFlag) {
                const data = await result.data;
                commit('loginMutation', data);
            }
        },
        async registrationAction({commit, state}, user) {
            let errFlag = false;
            const result = await authenticationApi.registration(user).catch(function (error) {
                if (error.response) {
                    commit('handleApiError', error.response.data)
                    errFlag = true;
                }
            })

            if (!errFlag) {
                const data = await result.data;
                commit('registrationMutation', data);
            }
        },
        async refreshTokensAction({commit}) {
            let errFlag = false;
            let refreshToken = cookies.get("refreshToken")
            if (refreshToken !== null) {
                const result = await authenticationApi.refresh(refreshToken).catch(function (error) {
                    if (error.response) {
                        commit('handleClientError', 11);
                        errFlag = true;
                    }
                })

                if (!errFlag) {
                    const data = await result.data;
                    commit('refreshTokensMutation', data);
                }
            } else {
                commit('handleClientError', 11);
            }
        }
    }
})