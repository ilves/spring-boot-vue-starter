<template lang="pug">
  div
    h1 Users
      router-link.btn.btn-success.float-right(to="/users/new") Add new user
    p.lead
      | This page displays all the uses in the system.
    table.table.table-bordered
      thead.thead-default
        tr
          th ID
          th Name
          th Email
          th Actions
      tr(v-for="(user,index) in users" v-bind:key="index")
        td {{user.id}}
        td {{user.name}}
        td {{user.email}}
        td.text-right
          router-link.btn.btn-outline-primary.btn-sm.mr-2(v-bind:to="{ name:'editUser', params: { id: user.id }}") Edit
          a.btn.btn-sm.btn-outline-danger(href="#", @click="delUser(user, $event)") Delete
    Loader(v-bind:isLoading="users===null")
    Paginate(v-if="pagination!==null", v-bind:pageCount="pages", v-bind:clickHandler="goToPage", v-bind:initialPage="page-1")
</template>

<script>
  import { mapState, mapActions } from 'vuex'
  import Loader from '../../components/Loader'
  import Paginate from '../../components/Paginate'
  export default {
    components: {
      Loader, Paginate
    },
    computed: {
      ...mapState({
        users: ({users}) => users.list.users,
        pagination: ({users}) => users.list.pagination,
        defaultPage: ({users}) => users.list.page
      }),
      pages () {
        if (this.pagination === null) {
          return 0
        }
        return Math.ceil(this.pagination.totalCount / this.pagination.limit)
      },
      page () {
        let page = this.$route.params.page
        return page === undefined ? this.defaultPage : parseInt(page)
      }
    },
    methods: {
      ...mapActions([
        'initListUsers', 'deleteUser'
      ]),
      delUser (user, $e) {
        $e.preventDefault()
        this.deleteUser(user.id).then(() => {
          this.initListUsers({page: this.page})
        })
      },
      goToPage (page, e) {
        e.preventDefault()
        this.$router.push({name: 'users', params: { page }})
        this.initListUsers({page: page})
      }
    },
    created () {
      this.initListUsers({page: this.page})
    }
  }
</script>
