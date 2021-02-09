<template>
  <div>
    <div v-html="test"></div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      test: "<strong>hi</strong>"
    }
  },
}
</script>

<style scoped>
strong {
  font-size: 500px;
}
</style>