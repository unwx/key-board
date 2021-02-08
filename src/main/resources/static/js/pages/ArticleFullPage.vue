<template>
  <div>
    <p style="font-size: 50px"> {{$route.params.link}} </p>
    <p> {{article.title}} </p>
    <p> {{article.text}} </p>
    <p> {{article.link}} </p>
    <p> {{article.creationDate}} </p>
    <div v-html="text"></div>
  </div>
</template>
<!-- TODO: css styles. -->
<script>
import { mapGetters } from 'vuex'
import marked from 'marked';
import DOMPurify from 'dompurify'

export default {
  name: "ArticleFullPage",
  computed: mapGetters(['sortedArticles']),
  data (){
    return {
      article: -1,
      text: '',
    }
  },
  created () {
    this.findOne();
    this.markdown();
  },
  methods: {
    findOne() {
      const link = this.$route.params.link;
      for (let i = 0; i < this.sortedArticles.length; i++){
        if (link === this.sortedArticles[i].link)
          this.article = this.sortedArticles[i];
      }
      if (this.article === -1){
        this.$router.replace("/error/404")
      }
    },
    markdown () {
      this.text = DOMPurify.sanitize(marked(this.article.text),
          {ALLOWED_TAGS:
                ['h1', 'h2', 'h3', 'h4', 'h5',
                    'em', 'strong', 'ol', 'li', 'code', 'a'], ALLOWED_ATTR: ['class']})
    }
  }
}
</script>

<style scoped>

</style>