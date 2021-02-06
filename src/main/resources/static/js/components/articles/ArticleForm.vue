<template>
  <div class="text">
    <v-container class="container">
      <v-text-field
          class="item-title"
          placeholder="Введите заголовок"
      ></v-text-field>

      <hr>

      <v-textarea
          class="item-body"
          placeholder="о чем хотите поделиться?"
          rows="13"
      ></v-textarea>

      <v-btn class="button">Опубликовать</v-btn>
    </v-container>
  </div>
</template>

<script>
export default {
  name: "ArticleForm"
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
</style>