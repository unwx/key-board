<template>
  <div>
    <v-container>
      <div v-if="errorMessage === ''">
        <v-avatar
            size="155px">
          <img
              :src="user.picture"
              alt="userPicture"
          >

        </v-avatar>
        <div class="nickname">{{ user.username }}</div>
        <div class="nickname">{{ user.email }}</div>
      </div>
      <div v-else>{{ errorMessage }}</div>

      <div class="large-12 medium-12 small-12 cell">
        <label>File
          <input type="file" id="file" ref="file" v-on:change="handleFileUpload()"/>
        </label>
        <button v-on:click="submitFile()">Submit</button>
      </div>
    </v-container>
  </div>
</template>

<script>
import {mapActions} from 'vuex'

export default {
  name: "UserProfile",
  data() {
    return {
      user: {},
      errorMessage: '',
      file: '',
    }
  },
  methods: {
    ...mapActions(['getUserAction', 'uploadAvatarAction']),
    handleFileUpload() {
      this.file = this.$refs.file.files[0];
    },
    submitFile(){
      this.user = this.uploadAvatarAction(this.file)
    },
  },
  beforeMount() {
    this.getUserAction().then(response => {
      this.user = response;
    });
  }
}
</script>

<style scoped>
.nickname {
  font-family: Andale Mono, monospace;
  font-size: 20px;
}
</style>