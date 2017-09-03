import api from '../../api'
import {
  SUCCESS_INIT_EDIT_USER,
  SUCCESS_INIT_LIST_USERS,
  SUCCESS_UPDATE_USER,
  SUCCESS_ADD_USER,
  SUCCESS_DELETE_USER
} from '../types'

const state = {
  list: {
    users: null,
    pagination: null,
    page: 1,
    size: 1
  },
  edit: {
    user: null
  }
}

const actions = {
  initEditUser ({ commit }, id) {
    return api.getUser(id).then(response => {
      commit(SUCCESS_INIT_EDIT_USER, {
        user: response.body.data
      })
    })
  },
  initListUsers ({ commit }, {page}) {
    api.getUsers({page: page, size: state.list.size}).then(response => {
      commit(SUCCESS_INIT_LIST_USERS, {
        users: response.body.data,
        pagination: response.body.pagination
      })
    })
  },
  addUser (store, data) {
    return api.addUser(data).then(response => {
      store.commit(SUCCESS_ADD_USER, { user: response.body.data })
    })
  },
  deleteUser (store, id) {
    return api.deleteUser(id).then(() => {
      store.commit(SUCCESS_DELETE_USER, { id: id })
    })
  },
  updateUser (store, { id, user }) {
    return api.editUser(id, user).then(response => {
      store.commit(SUCCESS_UPDATE_USER, { id: id, user: response.body.data })
    })
  }
}

const mutations = {
  [SUCCESS_INIT_EDIT_USER] (state, {user}) {
    state.edit.user = user
  },
  [SUCCESS_UPDATE_USER] (state, {user}) {
    state.edit.user = user
  },
  [SUCCESS_INIT_LIST_USERS] (state, {users, pagination}) {
    state.list.users = users
    state.list.pagination = pagination
  },
  [SUCCESS_ADD_USER] (state, payload) {
  },
  [SUCCESS_DELETE_USER] (state, {id}) {
  }
}

export default {
  state,
  actions,
  mutations
}
