<template>
  <div class="button">
    <div class="my-2" >
        <v-btn
            color="#0d122e"
            fab
            x-large
            dark
            @click="createArticle"
        >
          <v-icon>create</v-icon>
        </v-btn>
    </div>
  </div>
</template>

<script>
export default {
  name: "CreateArticleButton",
  methods: {
    createArticle() {
        this.$router.push({path: '/article/create'})
    }
  }
}
</script>

<style scoped>
.button {
  width: max-content;
  position: relative;
  left: 50%;
  transform: translate(-50%, 0);
  margin-top: 25px;
  margin-bottom: 25px;
}
</style>