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
      save () {
        this.addUser(this.user).then(() => {
          this.$router.push('/users')
        }).catch((response) => {
          this.showMsg(response.body.error[0].message)
        })
      }
    }
  }
</script>
