<template>
  <div class="text">
    <v-container class="container">

      <div v-if="error !== null" class="error-message">
        {{error}}
      </div>

      <v-text-field
          class="item-title"
          placeholder="Введите заголовок"
          v-model="title"
      ></v-text-field>

      <hr>

      <v-textarea
          class="item-body"
          placeholder="о чем хотите поделиться?"
          rows="13"
          v-model="text"
      ></v-textarea>

      <v-btn class="button" @click="save">Опубликовать</v-btn>
    </v-container>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
export default {

  name: "ArticleForm",
  data () {
    return {
      title: '',
      text: '',
      error: null,
    }
  },
  methods: {
    ...mapActions(['addArticleAction']),
    save () {
      if (this.text.length < 15 || this.text.length > 5000 || this.title.length < 5 || this.title.length > 30){
        this.error = "length error."
      }
      else {
        const article = {
          title: this.title,
          text: this.text,
        }
        this.addArticleAction(article);
        this.title = '';
        this.text = '';
        this.$router.push("/");
      }
    }
  }

}
</script>

<style scoped>
.container {
  width: 1000px;
  margin: 25px auto;
  border-radius: 15px;
  border-width: 0;
}

.item-title {
  width: 1000px;
  margin: 25px auto;
  border-radius: 15px;
  border-width: 0;
}

.item-body {
  width: 1000px;
  margin: 25px auto;
  border-radius: 15px;
  border-width: 1px;
  border-color: black;
}

.text {
  font-size: 17px;
  font-family: Andale Mono, monospace;
}
/*375*/
.button {
  width: 500px;
  margin: 10px 250px;
}

hr {
  width: 15%;
  margin: auto;
}

.error-message {
  width: 1000px;
  margin: 25px auto;
  color: #ff1400;
  font-size: 30px;
  text-align: center;
}
</style>