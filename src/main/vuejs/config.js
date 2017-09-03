import store from './store'

export default {
  title: 'Vue-Spring Starter',
  author: 'Taavi Ilves (golive.ee)',
  menu: [{
    to: '/home',
    label: 'Home',
    visible: () => true
  }, {
    to: '/users',
    label: 'Users',
    visible: () => true
  }, {
    to: '/login',
    label: 'Login',
    visible: () => !store.state.account.auth.check()
  }, {
    to: '/logout',
    label: 'Logout',
    visible: () => store.state.account.auth.check()
  }, {
    to: '/register',
    label: 'Register',
    visible: () => !store.state.account.auth.check()
  }]
}
