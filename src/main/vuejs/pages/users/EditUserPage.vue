<template>
  <div>
    <h1>Edit user</h1>
    <UserForm :user="editableUser" :handleSubmit="save" formType="UPDATE" :isLoading="isLoading"></UserForm>
  </div>
</template>

<script>
  import { mapActions, mapState } from 'vuex'
  import UserForm from '../../components/UserForm'
  export default {
    components: {
      UserForm
    },
    data () {
      return {
        editableUser: null,
        isSaving: false
      }
    },
    computed: {
      ...mapState({
        user: ({users}) => users.edit.user
      }),
      isLoading () {
        return this.editableUser === null || this.isSaving
      }
    },
    watch: {
      user (user) {
        this.editableUser = Object.assign({}, user)
      }
    },
    methods: {
      ...mapActions([
        'initEditUser',
        'updateUser',
        'showMsg',
        'showErrors'
      ]),
      save () {
        this.isSaving = true
        this.updateUser({id: this.user.id, user: this.editableUser}).then(() => {
          this.$router.push('/users')
          this.isSaving = false
        }).catch(x => {
          this.isSaving = false
        })
      }
    },
    created () {
      this.initEditUser(this.$route.params.id).catch((response) => {
        this.showErrors(response.body.error)
        this.$router.push('/users')
      })
    }
  }
</script>
