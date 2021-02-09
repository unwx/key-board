<template>
  <div class="page">
    <v-container>
      <div class="border">
        <h1>{{ article.title }}</h1>
      </div>
      <hr>
      <div class="test">
        <div v-html="text" class="text"></div>
      </div>
      <p class="date"> {{ article.creationDate }} </p>
    </v-container>
  </div>
</template>
<!-- TODO: css styles. -->
<script>
import {mapGetters} from 'vuex'
import marked from 'marked';
import DOMPurify from 'dompurify'

export default {
  name: "ArticleFullPage",
  computed: mapGetters(['sortedArticles']),
  data() {
    return {
      article: -1,
      text: '',
      test: "<strong>hi</strong>"
    }
  },
  created() {
    this.findOne();
    this.markdown();
  },
  methods: {
    findOne() {
      const link = this.$route.params.link;
      for (let i = 0; i < this.sortedArticles.length; i++) {
        if (link === this.sortedArticles[i].link)
          this.article = this.sortedArticles[i];
      }
      if (this.article === -1) {
        this.$router.replace("/error/404")
      }
    },
    markdown() {
      this.text = DOMPurify.sanitize(marked(this.article.text),
          {
            ALLOWED_TAGS:
                ['h2', 'h3', 'h4', 'h5',
                  'em', 'strong', 'ol', 'li', 'code', 'a'], ALLOWED_ATTR: ['href']
          })
    }
  }
}
</script>

<style scoped>
.page {
  font-family: Andale Mono, monospace;
}

h1 {
  margin-top: 25px;
  margin-bottom: 25px;
  text-align: center;
}

.border {
  margin: auto;
  width: 600px;
  border-style: solid;
  border-color: black;
  padding: 10px;
  border-radius: 25px;
  border-width: 1px;
}

hr {
  width: 70%;
  margin: 25px auto;
}

.text {
  font-size: 21px;
  padding-top: 5px;
  padding-bottom: 5px;
}

.test >>> code{
  font-size: 30px;
}

strong {
  font-size: 500px;
}

.date {
  text-align: right;
  margin-bottom: 25px;
  margin-top: 25px;
}
</style>