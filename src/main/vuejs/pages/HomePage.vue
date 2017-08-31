<template>
  <div>
    <div class="jumbotron">
      <h1 class="display-4">Vue.js & Spring Boot starter</h1>
      <p class="lead">This is starter project using Vue.js and Spring Boot. This project demostrates different aspects
        of using Vue.js as frontend and Spring Boot as backend and webserver.</p>
      <p><a class="btn btn-lg btn-success" href="#" role="button">Github project</a></p>
    </div>
    <div class="row marketing">
      <div class="col-lg-12">
        <h1>Features</h1>
        <hr />
      </div>
      <div class="col-lg-6">
        <ul>
          <li>Spring Boot</li>
          <li>Vue.js</li>
          <li>Maven</li>
          <li>Hot module replacement</li>
          <li>vue-router</li>
          <li>vuex</li>
          <li>bootstrap 4</li>
          <li>scss</li>
        </ul>
      </div>
      <div class="col-lg-6">
        <h5>Installation</h5>
        <code>npm install</code>
        <br /><br />
        <h5>Dev mode</h5>
        <code>mvn spring-boot:run -Dspring.profiles.active=dev</code>
        <code>npm run dev</code>
        <br /><br />
        <h5>Production build and run</h5>
        <code>mvn spring-boot:run</code>
      </div>
    </div>
  </div>
</template>

<script>
export default {
}
</script>
