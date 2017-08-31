<template>
  <div>
    Loading...
  </div>
</template>

<script>
  import { mapState, mapActions } from 'vuex'
  export default {
    computed: {
      ...mapState({
        authenticated: ({account}) => account.auth.check()
      })
    },
    methods: {
      ...mapActions([
        'showMsg'
      ])
    },
    watch: {
      authenticated (isAuthenticated) {
        if (!isAuthenticated) {
          this.$router.push('/home')
        }
      }
    },
    mounted () {
      this.$store.dispatch('accountLogoutSubmit', this.params).then(x => {
        this.showMsg({content: 'Bye! See you again', type: 'success'})
      })
    }
  }
</script>
