import {UserResource, AccountResource} from './resources'

export default {
  accountLogin: function (params) {
    return AccountResource.login(params)
  },
  getUser: function (id) {
    return UserResource.get({id: id})
  },
  getUsers: function (options) {
    return UserResource.get({id: '', ...options})
  },
  addUser: function (data) {
    return UserResource.save(data)
  },
  deleteUser: function (id) {
    return UserResource.remove({id: id})
  },
  editUser: function (id, data) {
    return UserResource.update({id: id}, data)
  }
}
