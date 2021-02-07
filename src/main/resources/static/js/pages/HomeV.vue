<template>
  <div>
    <app-bar></app-bar>
    <router-view></router-view>
  </div>
</template>
<script>

import AppBar from "../module/appBar.vue";
import ArticleList from "../components/articles/ArticleList.vue";
import ArticleForm from "../components/articles/ArticleForm.vue";
import CreateArticleButton from "../components/articles/CreateArticleButton.vue";
import ArticlesPage from "./ArticlesPage.vue";
import ArticleCreatePage from "./ArticleCreatePage.vue";

export default {
  name: "HomeV.vue",
  components: {
    ArticleCreatePage,
    ArticlesPage,
    CreateArticleButton,
    ArticleForm,
    ArticleList,
    AppBar
  },
}
</script>

<style scoped>
</style>