import Vue from 'vue'
import Router from 'vue-router'

import store from '../store'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      component: require('../pages/HomePage.vue'),
      meta: {
        title: 'Homepage'
      }
    },
    {
      path: '/login',
      component: require('../pages/LoginPage.vue'),
      meta: {
        title: 'Login'
      }
    },
    {
      path: '/logout',
      component: require('../pages/LogoutView.vue'),
      meta: {
        title: 'Logout'
      }
    },
    {
      path: '/users/new',
      component: require('../pages/users/NewUserPage.vue'),
      meta: {
        title: 'New User',
        requiresAuth: true,
        requiresAdmin: true
      }
    },
    {
      path: '/users/:page?',
      name: 'users',
      component: require('../pages/users/UserPage.vue'),
      meta: {
        title: 'Users',
        requiresAuth: true,
        requiresAdmin: true
      }
    },
    {
      path: '/users/edit/:id',
      name: 'editUser',
      component: require('../pages/users/EditUserPage.vue'),
      meta: {
        title: 'Edit user',
        requiresAuth: true,
        requiresAdmin: true
      }
    }
  ]
})

router.afterEach((route) => {
  document.title = `${route.meta.title} - Starter`
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const auth = store.state.account.auth
    if (!auth.check()) {
      next({
        path: '/login',
        query: { redirect_url: to.fullPath }
      })
      store.dispatch('showMsg', 'Please login to continue')
      return
    } else if (to.meta.requiresAdmin && !auth.isAdmin) {
      next({
        path: '/'
      })
      store.dispatch('showMsg', 'Sorry, you don\'t have enough permissions')
      return
    }
  }
  next()
})

export default router
