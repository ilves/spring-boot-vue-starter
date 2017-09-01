<template>
  <form @submit.prevent="submit()" novalidate>
    <input type="email" name="email" placeholder="E-mail" v-model="params.email" />
    <input type="password" name="password" placeholder="Password" v-model="params.password" />
    <button class="btn btn-outline-primary" type="submit">Login</button>
  </form>
</template>

<script>
  import { mapState, mapActions } from 'vuex'
  export default {
    data () {
      return {
        params: {
          email: '',
          password: ''
        }
      }
    },
    computed: {
      ...mapState({
        authenticated: ({account}) => account.auth.check()
      })
    },
    methods: {
      ...mapActions([
        'showMsg',
        'showErrors'
      ]),
      submit () {
        this.$store.dispatch('accountLoginSubmit', this.params).then(() => {
          this.showMsg({content: 'Login successful. Welcome back!', type: 'success'})
        }).catch((response) => {
          this.showErrors(response.body.error)
        })
      },
      redirect () {
        if (this.authenticated) {
          const redirectUrl = this.$route.query.redirect_url || '/'
          this.$router.push(redirectUrl)
        }
      }
    },
    watch: {
      authenticated () {
        this.redirect()
      }
    },
    created () {
      this.redirect()
    }
  }
</script>
