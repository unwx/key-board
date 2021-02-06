<template>
  <div>
    <div v-for="article in sortedArticles">
      <article-item
          :article="article"
      ></article-item>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import ArticleItem from "./ArticleItem.vue";

export default {
  name: "ArticleList",
  components: {ArticleItem},
  computed: mapGetters(['sortedArticles']),
  methods: {},
}
</script>

<style scoped>
</style>