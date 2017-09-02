<template lang="pug">
  .container
    .header.clearfix
      nav
        ul.nav.nav-pills.float-right
          li.nav-item(v-for="item in conf.menu", v-if="item.visible()")
            router-link(v-bind:to="item.to", active-class="active").nav-link {{item.label}}
      h3.text-muted {{conf.title}}
    router-view
    Toaster
    footer.footer
      p &copy; {{conf.author}}
</template>

<script>
  import { mapState } from 'vuex'
  import Toaster from './components/Toaster'
  export default {
    name: 'app',
    components: {
      Toaster
    },
    data () {
      return {
      }
    },
    computed: {
      ...mapState({
        auth: ({account}) => account.auth
      }),
      conf () {
        return this.$store.state.conf
      }
    }
  }
</script>

<style lang="sass" src="./assets/scss/main.scss"></style>
