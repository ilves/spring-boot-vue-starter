<template>
  <div>
    <h1><router-link class="btn btn-primary float-right" to="/users/new">Add new user</router-link>Users</h1>
    <table class="table table-bordered">
      <thead class="thead-default">
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tr v-for="(user,index) in users" :key="index">
        <td>{{user.id}}</td>
        <td>{{user.name}}</td>
        <td class="text-right">
          <router-link class="btn btn-outline-primary btn-sm" :to="{ name:'editUser', params: { id: user.id }}">Edit</router-link>
          <a class="btn btn-sm btn-outline-danger" href="#" @click="delUser(user, $event)">Delete</a>
        </td>
      </tr>
    </table>
    <Loader :isLoading="users===null"></Loader>
    <Paginate v-if="pagination!==null" :pageCount="pages" :clickHandler="goToPage" :initialPage="page-1"></Paginate>
  </div>
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
