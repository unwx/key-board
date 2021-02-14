<template>
  <div>
    <v-container>

      <div v-if="error !== null" class="error-message">
        {{ error }}
      </div>
      <div v-if="this.apiError !== null">
        <p class="error-message">{{ apiError.message }}</p>
      </div>

      <h5 class="title">Sign in</h5>
      <div class="form">
        <v-text-field
            placeholder="login"
            outlined
            color="#75c6d1"
            v-model="login"
        ></v-text-field>
        <v-text-field
            placeholder="password"
            outlined
            color="#75c6d1"
            v-model="password"
        ></v-text-field>
        <v-btn
            class="mx-auto button"
            depressed
            color="#59d3e3"
            @click="signIn"
        >
          Sign in
        </v-btn>
      </div>
      <h4 class="text">are you not registered yet?
        <v-btn
            class="mx-auto button"
            depressed
            color="#59d3e3"
            @click="goRegistration"
        >
          Registration
        </v-btn>
      </h4>
    </v-container>
  </div>
</template>

<script>
import {mapState, mapActions} from 'vuex'

export default {
  name: "SignIn",
  computed: mapState(['apiError']),
  data() {
    return {
      login: '',
      password: '',
      error: null,
    }
  },
  methods: {
    ...mapActions(['loginAction', 'clearErrorsAction']),
    goRegistration() {
      const registrationPath = "/registration"
      if (this.$route.path !== registrationPath) {
        this.$router.push(registrationPath);
      }
    },
    async signIn() {
      await this.clearErrorsAction();
      if (!this.validation()) {
        const user = {
          username: this.login,
          password: this.password,
        }
        await this.loginAction(user);
        this.login = '';
        this.password = '';
        if (this.apiError === null)
          await this.$router.push("/");
      }
    },
    validation() {
      let EF = false;
      if (this.login.length <= 1 || this.login.length >= 15) {
        this.error = "login length should be more than 1 and less than 15"
        EF = true;
      }
      if (this.password.length < 8 || this.password.length > 30) {
        this.error = "password max length = 30; min length = 8";
        EF = true;
      }
      return EF;
    },

  }
}
</script>

<style scoped>

.form {
  width: 500px;
  padding: 20px;
  background-color: #f2fbfc;
  margin: 20px auto;
  border-radius: 15px;
  border-style: solid;
  border-color: #55aab5;
  border-width: 1px;
}

.title {
  font-size: 35px;
  font-family: Andale Mono, monospace;
  color: #76d2de;
  margin-top: 60px;
  text-align: center;
}

.button {
  color: white;
}

.text {
  font-size: 15px;
  font-family: Andale Mono, monospace;
  color: #76d2de;
  margin-top: 60px;
  text-align: center;
}
.error-message {
  width: 1000px;
  margin: 25px auto;
  color: #ff1400;
  font-size: 30px;
  text-align: center;
}
</style>