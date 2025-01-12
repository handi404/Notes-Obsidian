```vue
<template>
  <p>Result time - {{ result }} ms</p>
  <p class="rank">{{ rank }}</p>
</template>

<script>
export default {
  props: ["result"],
  data() {
    return {
      rank: null,
    };
  },
  mounted() {
    if (this.result < 250) {
      this.rank = "So fast!";
    } else if (this.result < 500) {
      this.rank = "Good!";
    } else if (this.result < 1000) {
      this.rank = "Not bad!";
    } else {
      this.rank = "Too slow!";
    }
  },
};
</script>

<style scoped>
.rank {
  font-size: 20px;
  color: #0faf87;
}
</style>

```