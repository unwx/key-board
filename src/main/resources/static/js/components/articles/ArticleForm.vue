<template>
  <div class="text">
    <v-container>
      <v-text-field
          class="input mx-3"
          placeholder="Введите заголовок"
      ></v-text-field>

      <v-textarea
          class="input mx-3"
          placeholder="о чем хотите поделиться?"
      ></v-textarea>

      <v-btn class="input">Опубликовать</v-btn>
    </v-container>
  </div>
</template>

<script>
export default {
  name: "ArticleForm"
}
</script>

<style scoped>
.text {
  font-size: 17px;
  font-family: Andale Mono, monospace;
}
.input {
  width: 800px;
  align-items: center;
  padding-top: 20px;
  padding-bottom: 20px;
}
</style>