<template lang="pug">
  div
    h1 Register
    p.lead  On this page you can register new account. Please fill the form and press save button.
    UserForm(:user="user", :handleSubmit="save", formType="REGISTER")
</template>

<script>
  import { mapActions } from 'vuex'
  import UserForm from '../components/UserForm'
  export default {
    components: {
      UserForm
    },
    data () {
      return {
        user: {
          name: '',
          email: '',
          password: '',
          passwordRepeat: ''
        }
      }
    },
    methods: {
      ...mapActions([
        'accountRegister', 'showMsg'
      ]),
      save (errorBag) {
        this.accountRegister(this.user).then(() => {
          this.$router.push('/login')
          this.showMsg({content: 'Account created. Please login!', type: 'success'})
        }).catch(({body}) => {
          this.showMsg({content: body.error[0].message})
          body.error.forEach(e => {
            errorBag.add(e.field, e.message, 'server')
          })
        })
      }
    }
  }
</script>
