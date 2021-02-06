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
import ArticlePage from "./ArticlePage.vue";
import ArticleCreatePage from "./ArticleCreatePage.vue";

export default {
  name: "HomeV.vue",
  components: {
    ArticleCreatePage,
    ArticlePage,
    CreateArticleButton,
    ArticleForm,
    ArticleList,
    AppBar
  },
  beforeMount() {
    this.$router.replace('/')
  }
}
</script>

<style scoped>
</style>