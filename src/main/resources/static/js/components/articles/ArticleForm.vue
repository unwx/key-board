<template>
  <div class="text" @keyup.enter="save">
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
      if (this.title.length <= 30 && this.title.length >= 5)
        this.cleanSpaceSpamTitle();
      else this.error = "length error."

      if (this.text.length < 15 || this.text.length > 5000 || this.title.length < 5 || this.title.length > 30 || !(/^([ a-zA-Zа-ёА-ЯЁїЇіІ1-9]+)$/.test(this.title))){
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
    },
    cleanSpaceSpamTitle(){
      let spaceCounter = 0;
      let startIndex = -1;
      let FLAG = false;
      let length = this.title.length;
      let title = this.title

      for (let i = 0; i < length; i++) {

        if (title[i] === ' '){
          spaceCounter++;
        } else if (spaceCounter === 1) {
          spaceCounter--;
        } else if (spaceCounter > 1) {
          FLAG = true;
        }

        if (spaceCounter === 2){
          startIndex = i - spaceCounter + 1;
        }

        if (spaceCounter > 1 && FLAG){
          title = title.substr(0, startIndex) + title.substr(startIndex + spaceCounter - 1, title.length);
          FLAG = false;
          length = title.length;
          i = i - spaceCounter;
          spaceCounter = 0;
          startIndex = -1;
        }
      }
      this.title = title
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