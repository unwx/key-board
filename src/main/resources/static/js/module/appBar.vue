<template>
  <v-card class="overflow-hidden">
    <v-app-bar
        app
        absolute
        color="#f7f5f5"
        elevate-on-scroll
        scroll-target="#scrolling-techniques-7"
    >

      <v-toolbar-title class="toolbar-title"><b><</b> Key - ? <b>></b></v-toolbar-title>

      <v-spacer></v-spacer>
      <a class="text-title" href="#">{ Q & A }</a>
      <a class="text-title" href="#">About-us</a>
      <a class="text-title" href="#">Profile</a>
      <v-btn icon>
        <v-icon>logout</v-icon>
      </v-btn>
    </v-app-bar>
    <v-sheet
        id="scrolling-techniques-7"
        class="overflow-y-auto"

    >
      <v-container style="height: 1500px;">
      </v-container>
    </v-sheet>
  </v-card>
</template>

<script>
export default {
  name: "appBar"
}
</script>

<style scoped>

.toolbar-title {
  font-size: 28px;
  font-family: FreeMono, monospace;
  font-weight: 570;
  padding-left: 20px;
}

.text-title {
  font-family: Andale Mono, monospace;
  font-size: 23px;
  padding-left: 35px;
  padding-right: 35px;
  font-weight: bold;
  color: black;
  text-decoration: none;
}
</style>