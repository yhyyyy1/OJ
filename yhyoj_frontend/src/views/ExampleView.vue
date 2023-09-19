<template>
  <div class="exampleView">
    <MdEditor :value="mdValue" :handle-change="onMdChange" />
    <CodeEditor :value="codeValue" :handle-change="onCodeChange" />
  </div>
</template>

<script setup lang="ts">
import MdEditor from "@/components/MdEditor.vue";
import { ref } from "vue";
import CodeEditor from "@/components/CodeEditor.vue"; // @ is an alias to /src

const mdValue = ref();
const onMdChange = (v: string) => {
  mdValue.value = v;
};

const codeValue = ref();
const onCodeChange = (v: string) => {
  codeValue.value = v;
};
</script>
