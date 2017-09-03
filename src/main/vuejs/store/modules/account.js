import api from '../../api'
import {
  ACCOUNT_AUTH_STATUS_CHANGED,
  SUCCESS_ACCOUNT_REGISTER
} from '../types'

const AUTH = 'auth'

const localStorage = global.localStorage
const JSON = global.JSON
const USER = JSON.parse(localStorage.getItem(AUTH))

const state = {
  auth: {
    check () {
      return this.id !== null && this.id !== 0
    },
    jwtToken: USER !== null ? USER.jwtToken : null,
    id: USER !== null ? USER.id : null,
    user: USER !== null ? USER.user : null,
    isAdmin: USER !== null ? USER.admin : null
  }
}

const actions = {
  accountLoginSubmit (store, params) {
    return api.accountLogin(params).then((response) => {
      store.commit(ACCOUNT_AUTH_STATUS_CHANGED, response.body)
    })
  },
  accountLogoutSubmit (store) {
    return new Promise((resolve, reject) => {
      store.commit(ACCOUNT_AUTH_STATUS_CHANGED, {data: null})
      resolve(true)
    })
  },
  accountRegister (store, data) {
    return api.accountRegister(data).then(response => {
      store.commit(SUCCESS_ACCOUNT_REGISTER, { user: response.body.data })
    })
  }
}

const mutations = {
  [ACCOUNT_AUTH_STATUS_CHANGED] (state, {data}) {
    if (data === null) {
      state.auth.jwtToken = null
      state.auth.id = null
      state.auth.user = null
      state.auth.isAdmin = false
      localStorage.removeItem(AUTH)
    } else {
      state.auth.jwtToken = data.jwtToken
      state.auth.id = data.id
      state.auth.user = data.user
      state.auth.isAdmin = data.admin
      localStorage.setItem(AUTH, JSON.stringify(data))
    }
  },
  [SUCCESS_ACCOUNT_REGISTER] (state, payload) {
  }
}

export default {
  state,
  actions,
  mutations
}
