import Vue from 'vue'
import VueResource from 'vue-resource'
import store from '../store'

const API_ROOT = '/api'

Vue.use(VueResource)

Vue.http.interceptors.push((request, next) => {
  const auth = store.state.account.auth
  if (auth.check()) {
    const accessToken = auth.jwtToken
    Vue.http.headers.common.Authorization = `Bearer ${accessToken}`
  } else {
    delete Vue.http.headers.common.Authorization
  }
  next()
})

export const UserResource = Vue.resource(API_ROOT + '/users{/id}')
export const AccountResource = Vue.resource(API_ROOT + '/account', {}, {
  login: {method: 'POST', url: API_ROOT + '/account/login'},
  register: {method: 'POST', url: API_ROOT + '/account/register'}
})

