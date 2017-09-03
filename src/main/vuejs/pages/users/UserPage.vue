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
          th Last login
          th Actions
      tr(v-for="(user,index) in users", :key="index")
        td {{user.id}}
        td {{user.name}}
        td {{user.email}}
        td {{user.lastLogin ? new Date(user.lastLogin).toLocaleString() : '-'}}
        td.text-right
          router-link.btn.btn-outline-primary.btn-sm.mr-2(:to="{name:'editUser', params:{id:user.id}}") Edit
          a.btn.btn-sm.btn-outline-danger(href="#", @click="delUser(user, $event)") Delete
    Loader(v-bind:isLoading="users===null")
    | Leht on: {{currentPage}}
    Paginate(v-if="pagination!==null", :pageCount="pages", :clickHandler="goToPage", :page="currentPage-1")
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
        page: ({users}) => users.list.page
      }),
      pages () {
        if (this.pagination === null) {
          return 0
        }
        return Math.ceil(this.pagination.totalCount / this.pagination.limit)
      },
      currentPage () {
        let page = this.$route.params.page
        return page === undefined ? this.page : parseInt(page)
      }
    },
    watch: {
      users (val) {
        if (val.length <= 0 && this.pages > 0 && this.currentPage > 0) {
          this.goToPage(this.pages - 1)
        }
      },
      currentPage (page) {
        this.loadData(page)
      }
    },
    methods: {
      ...mapActions([
        'initListUsers', 'deleteUser', 'showMsg'
      ]),
      delUser (user, $e) {
        $e.preventDefault()
        this.deleteUser(user.id).then(() => {
          this.loadData(this.page)
        }).then(() => {
          this.showMsg({content: 'User deleted', 'type': 'success'})
        })
      },
      goToPage (page) {
        this.$router.push({name: 'users', params: { page: ++page }})
      },
      loadData (page) {
        this.initListUsers({page})
      }
    },
    created () {
      this.loadData(this.currentPage)
    }
  }
</script>
