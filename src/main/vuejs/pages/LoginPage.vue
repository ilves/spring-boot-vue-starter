<template lang="pug">
  include ./../assets/mixins.pug
  div
    h1 Login
    p.lead
      | Here you can login to the site. For demo admin user use <b>admin@admin.ee</b> and password <b>admin</b>.
      | For regular user use <b>user@user.ee</b> and password <b>user</b>.
    form(novalidate)
      +formGroup
        +colInput("text", "email", "params.email", "Email", "'required|email'")
      +formGroup
        +colInput("password", "password", "params.password", "Password", "'required|min:3'")
      +formGroup
        .col-sm-12
          LoadingButton(:isLoading="isLoading", label="Login", :type="{'btn-primary':true}", :click="submit")
</template>

<script>
  import { mapState, mapActions } from 'vuex'
  import LoadingButton from '../components/LoadingButton'

  export default {
    components: {
      LoadingButton
    },
    data () {
      return {
        params: {
          email: '',
          password: ''
        },
        isLoading: false
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
        this.$validator.validateAll().then(result => {
          if (!result) return
          this.$store.dispatch('accountLoginSubmit', this.params).then(() => {
            this.showMsg({content: 'Login successful. Welcome back!', type: 'success'})
          }).catch((response) => {
            this.showErrors(response.body.error)
          })
        }).catch(() => {})
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
