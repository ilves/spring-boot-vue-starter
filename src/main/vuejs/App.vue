<template>
  <div class="container">
    <div class="header clearfix">
      <nav>
        <ul class="nav nav-pills float-right">
          <li class="nav-item">
            <router-link to="/home" active-class="active" class="nav-link">Home</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" active-class="active" to="/users">Users</router-link>
          </li>
          <li v-if="!auth.check()" class="nav-item">
            <router-link to="/login" active-class="active" class="nav-link">Login</router-link>
          </li>
          <li v-if="auth.check()" class="nav-item">
            <router-link to="/logout" active-class="active" class="nav-link">Logout</router-link>
          </li>
        </ul>
      </nav>
      <h3 class="text-muted">{{conf.title}}</h3>
    </div>
    <router-view></router-view>
    <Toaster></Toaster>
    <footer class="footer">
      <p>&copy; {{conf.author}}</p>
    </footer>
  </div>
</template>

<script>
  import { mapState } from 'vuex'
  import Toaster from './components/Toaster'
  export default {
    name: 'app',
    components: {
      Toaster
    },
    data () {
      return {
      }
    },
    computed: {
      ...mapState({
        auth: ({account}) => account.auth
      }),
      conf () {
        return this.$store.state.conf
      }
    }
  }
</script>

<style lang="sass" src="./assets/scss/main.scss"></style>
