<template>
  <div class="page">
    <v-container class="text">
      <h1 class="title">{{ article.title }}</h1>
      <hr>
      <div class="code">
        <markdown-display :text="text"></markdown-display>
      </div>
      <p class="date"> {{ article.creationDate }} </p>
    </v-container>
  </div>
</template>
<!-- TODO: css styles. -->
<script>
import {mapGetters} from 'vuex'
import MarkdownDisplay from "../module/MarkdownDisplay.vue";

export default {
  name: "ArticleFullPage",
  components: {MarkdownDisplay},
  computed: mapGetters(['sortedArticles']),
  data() {
    return {
      article: -1,
      text: '',
      test: "<code class=\"language-html\">int main() {}</code>"
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
      this.text = this.article.text
    }
  }
}
</script>

<style scoped>
.page {
  font-family: Andale Mono, monospace;
}

h1 {
  margin-top: 10px;
  text-align: center;
}

.title {
  margin-top: 25px;
  height: auto;
  text-align: center;
  font-size: 25px;
}

hr {
  width: 70%;
  margin: 0 auto;
}

.text {
  font-size: 18px;
  padding-top: 5px;
  padding-bottom: 5px;
  white-space: pre-wrap;
}

.code >>> code {
  background-color: #f4f4f4;
  border: 1px solid #ddd;
  border-left: 3px solid #88aaff;
  page-break-inside: avoid;
  font-family: monospace;
  font-size: 13px;
  line-height: 1.6;
  margin: 5px 0;
  max-width: 100%;
  overflow: auto;
  padding: 17px 25px;
  display: block;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.date {
  text-align: right;
  margin-bottom: 25px;
  margin-top: 25px;
}
</style>