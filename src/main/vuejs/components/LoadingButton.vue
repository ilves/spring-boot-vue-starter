<template lang="pug">
  button.btn(v-bind:class="clazz", @click="submit")
    span(v-if="!isLoading") {{label}}
    span(v-else="") {{loadingLabel}}
</template>

<script>
  export default {
    props: {
      isLoading: {
        type: Boolean,
        required: true
      },
      label: {
        type: String,
        default: 'Submit'
      },
      loadingLabel: {
        type: String,
        default: 'Loading...'
      },
      type: {
        default: {}
      },
      click: {
        default: () => true
      }
    },
    computed: {
      clazz () {
        return Object.assign({}, this.type, {disabled: this.isLoading})
      }
    },
    methods: {
      submit (e) {
        e.preventDefault()
        this.click()
      }
    }
  }
</script>
