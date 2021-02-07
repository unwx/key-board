<template>
  <div>
    <p style="font-size: 50px"> {{$route.params.link}} </p>
    <p> {{article.title}} </p>
    <p> {{article.text}} </p>
    <p> {{article.link}} </p>
    <p> {{article.creationDate}} </p>
  </div>
</template>
<!-- TODO: css styles. -->
<script>
import { mapGetters } from 'vuex'

export default {
  name: "ArticleFullPage",
  computed: mapGetters(['sortedArticles']),
  data (){
    return {
      article: -1,
    }
  },
  created () {
    this.findOne();
  },
  methods: {
    findOne() {
      const link = this.$route.params.link;
      for (let i = 0; i < this.sortedArticles.length; i++){
        console.log(`is ${this.$route.params.link} === ${link}`)
        if (link === this.sortedArticles[i].link)
          this.article = this.sortedArticles[i];
      }
      console.log(this.article)
      if (this.article === -1){
        this.$router.push("/error/404")
      }
    },
  }
}
</script>

<style scoped>

</style>