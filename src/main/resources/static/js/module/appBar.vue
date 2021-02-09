<template>
  <div>
    <v-app-bar
        color="#f7f5f5"
        hide-on-scroll
        elevation="3"
    >
        <div @click="goHome" class="toolbar-title">< Key - ? ></div>

      <v-spacer></v-spacer>
      <div class="text-title" @click="goHome">Q & A</div>
      <div class="text-title" @click="goHome">About-us</div>
      <div class="text-title" @click="goHome">Profile</div>
      <v-btn icon>
        <v-icon>logout</v-icon>
      </v-btn>
    </v-app-bar>
  </div>
</template>

<script>
export default {
  name: "appBar",
  methods: {
    goHome() {
      const homePath = '/';
      if (this.$route.path !== homePath) {
        this.$router.push("/");
      }
    }
  }
}
</script>

<style scoped>

.toolbar-title {
  font-size: 28px;
  font-family: FreeMono, monospace;
  font-weight: 570;
  padding-left: 20px;
  text-decoration: none;
  color: black;
  cursor: pointer;
}

.text-title {
  font-family: Andale Mono, monospace;
  font-size: 23px;
  padding-left: 35px;
  padding-right: 35px;
  font-weight: bold;
  color: black;
  text-decoration-width: 3px;
  text-decoration-color: black;
  cursor: pointer;
}

</style>