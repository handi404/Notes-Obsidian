```vue
<template>
  <h1>Reaction Timer</h1>
  <button @click="start" :disabled="isPlaying">play</button>
  <Block v-if="isPlaying" :delay="delay" @en="endGame" />
  <Results v-if="showResult" :result="result" />
</template>

<script>
import Block from "./components/Block.vue";
import Results from "./components/Results.vue";
export default {
  components: {
    Block,
    Results,
  },
  data() {
    return {
      isPlaying: false,
      delay: null,
      result: null,
      showResult: false,
    };
  },
  methods: {
    start() {
      this.isPlaying = true;
      this.delay = 2000 + Math.random() * 5000;
      this.showResult = false;
    },
    endGame(reactionTime) {
      this.isPlaying = false;
      this.result = reactionTime;
      this.showResult = true;
    },
  },
};
</script>

<style></style>

```