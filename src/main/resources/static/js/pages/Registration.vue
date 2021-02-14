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
        <v-text-field
            placeholder="email"
            outlined
            color="#75c6d1"
            v-model="email"
        ></v-text-field>
        <v-btn
            class="mx-auto button"
            depressed
            color="#59d3e3"
            @click="register"
        >
          Register
        </v-btn>
      </div>
    </v-container>
  </div>
</template>

<script>
import {mapState, mapActions} from 'vuex'

export default {
  name: "Registration",
  computed: mapState(['apiError']),
  data() {
    return {
      login: '',
      password: '',
      email: '',
      error: null,
    }
  },
  methods: {
    ...mapActions(['registrationAction', 'clearErrorsAction']),
    goRegistration() {
      const registrationPath = "/registration"
      if (this.$route.path !== registrationPath) {
        this.$router.push(registrationPath);
      }
    },
    async register() {
      await this.clearErrorsAction();
      if (!this.validation()) {
        const user = {
          username: this.login,
          password: this.password,
          email: this.email
        }
        await this.registrationAction(user);
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
      const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      if (!re.test(this.email.toLowerCase())) {
        this.error = "incorrect email"
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
.error-message {
  width: 1000px;
  margin: 25px auto;
  color: #ff1400;
  font-size: 30px;
  text-align: center;
}
</style>