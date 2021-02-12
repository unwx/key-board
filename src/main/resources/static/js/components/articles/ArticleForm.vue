<template>
  <div class="text">
    <section>
      <v-container class="container">

        <div v-if="error !== null" class="error-message">
          {{ error }}
        </div>
        <div v-if="apiError !== null">
          <p class="error-message">{{ apiError.message }}</p>
        </div>

        <v-text-field
            class="item-title"
            placeholder="Введите заголовок"
            v-model="title"
        ></v-text-field>

        <v-btn
            id="test"
            color="#edfffc"
            fab
            x-small
        >
          <v-icon>code</v-icon>
        </v-btn>
        <hr>

        <v-textarea
            class="item-body"
            placeholder="о чем хотите поделиться?"
            rows="13"
            v-model="text"
        ></v-textarea>

        <v-btn class="button" @click="save">Опубликовать</v-btn>
      </v-container>
    </section>

    <aside>
      <v-container class="container">
        <div class="hint-section">
          <table class="hints">
            <li>## <h2 style="display: inline">header2</h2></li>
            <li>##### <h5 style="display: inline">header5</h5></li>
            <li>**<strong>bold</strong>**</li>
            <li>*<em>Italicized</em>*</li>
            <li>list: -, *, +, 1...n;</li>
            <li>```your code.```</li>
          </table>
        </div>
      </v-container>
    </aside>

  </div>
</template>

<script>
import {mapActions, mapState} from 'vuex'

export default {
  name: "ArticleForm",
  computed: mapState(['apiError']),
  data() {
    return {
      title: '',
      text: '',
      error: null,
    }
  },
  methods: {
    ...mapActions(['addArticleAction']),
    async save() {
      if (this.title.length <= 30 && this.title.length >= 5)
        this.cleanSpaceSpamTitle();
      else this.error = "title length must be >= 5 and <= 30"

      if (this.text.length < 15 || this.text.length > 5000 || this.title.length < 5 || this.title.length > 30 || !(/^([ a-zA-Zа-ёА-ЯЁїЇіІ1-9]+)$/.test(this.title))) {
        this.error = "pattern error;"
      } else {
        const article = {
          title: this.title,
          text: this.text,
        }
        await this.addArticleAction(article);
        this.title = '';
        this.text = '';
        if (this.apiError === null) {
          await this.$router.push("/");
        }
      }
    },
    cleanSpaceSpamTitle() {
      let spaceCounter = 0;
      let startIndex = -1;
      let FLAG = false;
      let length = this.title.length;
      let title = this.title

      for (let i = 0; i < length; i++) {

        if (title[i] === ' ') {
          spaceCounter++;
        } else if (spaceCounter === 1) {
          spaceCounter--;
        } else if (spaceCounter > 1) {
          FLAG = true;
        }

        if (spaceCounter === 2) {
          startIndex = i - spaceCounter + 1;
        }

        if (spaceCounter > 1 && FLAG) {
          title = title.substr(0, startIndex) + title.substr(startIndex + spaceCounter - 1, title.length);
          FLAG = false;
          length = title.length;
          i = i - spaceCounter;
          spaceCounter = 0;
          startIndex = -1;
        }
      }
      this.title = title
    },
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

section {
  float: left;
  margin: 0 1.5%;
  width: 63%;
}
aside {
  float: right;
  margin: 0 1.5%;
  width: 30%;
}
.hints {
  width: auto;
  margin-top: 150px;
  margin-left: 50px;
}
.hint-section {
  background-color: #f0faff;
  width: 350px;
  margin-left: 15px;
  border-radius: 15px;
  border-width: 1px;
  border-color: #c4e8c3;
}
li {
  padding-top: 10px;
  padding-bottom: 10px;
  list-style-type: circle;
  margin-bottom: 15px;
  margin-top: 15px;
  width: 300px;
}
</style>