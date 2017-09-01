<template>
  <div>
    <h1>Create new user</h1>
    <UserForm :user="user" :handleSubmit="save"></UserForm>
  </div>
</template>

<script>
  import { mapActions } from 'vuex'
  import UserForm from '../../components/UserForm'
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
        'addUser', 'showMsg'
      ]),
      save (errorBag) {
        this.addUser(this.user).then(() => {
          this.$router.push('/users')
        }).catch(({body}) => {
          this.showMsg({content: body.error[0].message})
          body.error.forEach(e => {
            errorBag.add(e.field, e.message, 'server')
          })
          console.log(errorBag.first('email'))
        })
      }
    }
  }
</script>
