import {
  SHOW_MSG,
  HIDE_MSG
} from '../types'

const state = {
  message: {
    type: '',
    content: '',
    title: ''
  }
}

const actions = {
  showErrors ({commit}, errors) {
    errors.forEach(e => {
      commit(SHOW_MSG, {content: e.message, type: 'error'})
    })
  },
  showMsg ({commit}, {content, type = 'error'}) {
    commit(SHOW_MSG, {content: content, type: type})
  },
  hideMsg ({commit}) {
    commit(HIDE_MSG)
  }
}

const mutations = {
  [SHOW_MSG] (state, action) {
    state.message = {...action}
  },
  [HIDE_MSG] (state, action) {
    state.message = {
      type: '',
      content: '',
      title: ''
    }
  }
}

export default {
  state,
  mutations,
  actions
}
