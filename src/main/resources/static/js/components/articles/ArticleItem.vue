<template>
    <div class="article">
        <div class="title">{{article.title}}</div>
        <div class="text">{{article.text}}</div>
        <div class="date">{{article.creationDate}}</div>
    </div>
</template>

<script>
export default {

  name: "ArticleItem",
  props: ['article']
}
</script>

<style scoped>
.article {
  width: 1000px;
  padding-top: 25px;
  padding-bottom: 25px;
  background-color: #f7f7f7;
  margin: 25px auto;
  border-radius: 50px;
  border-style: solid;
  border-color: black;
  border-width: 1px;
}
.title {
  width: 700px;
  font-family: Andale Mono, monospace;
  font-size: 35px;
  text-align: center;
  margin: auto auto 30px;
  padding: 10px 20px;
  height: 50px;
  word-wrap: break-word;
}
.text {
  font-family: monospace;
  font-size: 15px;
  margin: auto auto 15px;
  max-width: 800px;
  text-align: left;
  word-wrap: break-word;
}
.date {
  font-family: Andale Mono, monospace;
  font-size: 12px;
  text-align: right;
  padding-right: 15px;
  padding-left: 15px;
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>