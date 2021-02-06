<template>
  <div>
    <app-bar></app-bar>
    <article-list></article-list>
    <hr>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <hr>
    <article-form></article-form>
  </div>
</template>
<script>
import AppBar from "../module/appBar.vue";
import ArticleList from "../components/articles/ArticleList.vue";
import ArticleForm from "../components/articles/ArticleForm.vue";

export default {
  name: "HomeV.vue",
  components: {
    ArticleForm,
    ArticleList,
    AppBar
  }
}
</script>

<style scoped>
</style>