import Vuex from 'vuex'
import Vue from 'vue'
import conf from '../config'
import users from './modules/users'
import account from './modules/account'
import msg from './modules/msg'
import * as actions from './actions'
import * as getters from './getters'

const debug = process.env.NODE_ENV !== 'production'
Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    a: 1,
    conf: conf
  },
  actions,
  getters,
  modules: {
    account,
    users,
    msg
  },
  strict: debug
})

console.log('olen' + store)

if (module.hot) {
  module.hot.accept([
    './getters',
    './actions',
    './modules/users',
    './modules/account',
    './modules/msg'
  ], () => {
    store.hotUpdate({
      getters: require('./getters').default,
      actions: require('./actions').default,
      modules: {
        account: require('./modules/account').default,
        users: require('./modules/users').default,
        msg: require('./modules/msg').default
      }
    })
  })
}

export default store
