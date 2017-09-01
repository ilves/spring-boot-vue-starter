<template>
  <div>
    <form v-if="user!==null" novalidate>
      <div class="form-group row">
        <label for="name" class="col-sm-3 col-form-label">Name</label>
        <div class="col-sm-9">
          <input id="name" type="text" name="name" v-model="user.name" v-validate="'required|min:3'" class="form-control" placeholder="Name" />
          <span class="invalid-feedback" v-show="errors.has('name')">{{ errors.first('name') }}</span>
        </div>
      </div>
      <div class="form-group row">
        <label for="email" class="col-sm-3 col-form-label">E-mail</label>
        <div class="col-sm-9">
          <input id="email"
                 type="text"
                 name="email"
                 v-model="user.email"
                 v-validate="'required|email'"
                 class="form-control"
                 :class="{'is-invalid':errors.has('email')}"
                 placeholder="E-mail" />
          <div class="invalid-feedback" v-show="errors.has('email')">{{ errors.first('email') }}</div>
        </div>
      </div>
      <div class="form-group row">
        <label for="password" class="col-sm-3 col-form-label">Password</label>
        <div class="col-sm-9">
          <input
            id="password"
            type="password"
            name="password"
            v-model="user.password"
            v-validate="{rules:{required:formType=='CREATE', min:6}}"
            class="form-control"
            :class="{'is-invalid':errors.has('password')}"
            placeholder="Password"/>
          <span class="invalid-feedback" v-show="errors.has('password')">{{ errors.first('password') }}</span>
        </div>
      </div>
      <div v-if="formType=='UPDATE'" class="form-group row">
        <label for="password" class="col-sm-3 col-form-label">Password repeat</label>
        <div class="col-sm-9">
          <input id="password-repeat" type="password" name="password-repeat" v-model="user.passwordRepeat" v-validate="{rules:{required:formType=='CREATE', min:6}}" class="form-control" placeholder="Password repeat" />
          <span class="invalid-feedback" v-show="errors.has('password-repeat')">{{ errors.first('password-repeat') }}</span>
        </div>
      </div>
      <div class="form-group row">
        <div class="col-sm-9">
          <button @click="submit" class="btn btn-primary" :class="{disabled:isLoading}">
            <span v-if="!isLoading">Save</span>
            <span v-else="">Loading...</span>
          </button>
        </div>
      </div>
    </form>
  </div>
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
    watch: {
      errors () {
        console.log('muutus')
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
