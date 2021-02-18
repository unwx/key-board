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
      </div>
      <div v-else>{{errorMessage}}</div>
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
    }
  },
  methods: {
    ...mapActions(['getUserAction'])

  },
   beforeMount() {
    this.getUserAction().then(response => {
      this.user = response;
    });
  }
}
</script>

<style scoped>

</style>