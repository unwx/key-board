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
      <div v-if="isSigned" class="text-title" @click="goSignIn">
        <v-btn
            outlined
            color="#33b4ff"
        >
          Sign in
        </v-btn>
      </div>
      <div v-else class="text-title" @click="goProfile">Profile</div>
    </v-app-bar>
  </div>
</template>

<script>
import cookies from "vue-cookies";
export default {
  name: "appBar",
  data() {
    return {
      isSigned: null,
    }
  },
  methods: {
    goHome() {
      const homePath = '/';
      if (this.$route.path !== homePath) {
        this.$router.push(homePath);
      }
    },
    goSignIn() {
      const signInPath = '/sign-in';
      if (this.$route.path !== signInPath) {
        this.$router.push(signInPath);
      }
    },
    goProfile() {
      const profilePath = '/profile';
      if (this.$route.path !== profilePath) {
        this.$router.push(profilePath);
      }
    }
  },
  beforeCreate() {
      const user = cookies.get("user");
      this.isSigned = user === null;
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