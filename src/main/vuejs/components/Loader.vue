<template>
  <div v-if="isLoading">Loading...</div>
</template>

<script>
  export default {
    props: {
      isLoading: {
        type: Boolean,
        required: true
      }
    }
  }
</script>
