<template lang="pug">
  include ./../assets/mixins.pug
  form(v-if="user!==null" novalidate)
    +formGroup
      +colInput("text", "name", "user.name", "Name", "'required|min:3'")
    +formGroup
      +colInput("text", "email", "user.email", "Email", "'required|email'")
    +formGroup
      +colInput("password", "password", "user.password", "Password", "{rules:{required:formType!='UPDATE', min:6}}")
    +formGroup(v-if="formType!='CREATE'")
      +colInput("password", "passwordRepeat", "user.passwordRepeat", "Repeat password", "")
    +formGroup
      .col-sm-12
        button.btn.btn-primary(@click="submit" v-bind:class="{disabled:isLoading}")
          span(v-if="!isLoading") Save
          span(v-else="") Loading...
</template>

<script>
  import Loader from './Loader'
  export default {
    components: {
      Loader
    },
    props: {
      isLoading: {
        type: Boolean,
        required: false,
        default: false
      },
      formType: {
        default: 'CREATE'
      },
      user: {
        required: true
      },
      handleSubmit: {
        type: Function,
        required: true
      }
    },
    methods: {
      submit (e) {
        e.preventDefault()
        if (this.isLoading) {
          return
        }
        this.$validator.validateAll().then(result => {
          if (result) {
            this.handleSubmit(this.errors)
          }
        }).catch(() => {
        })
      }
    }
  }
</script>
