<template>
  <div>
    <create-article-button></create-article-button>
    <article-list></article-list>
  </div>
</template>

<script>
import CreateArticleButton from "../components/articles/CreateArticleButton.vue";
import ArticleList from "../components/articles/ArticleList.vue";
export default {
  name: "ArticlePage",
  components: {ArticleList, CreateArticleButton}
}
</script>

<style scoped>

</style>