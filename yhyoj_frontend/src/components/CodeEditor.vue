<template>
  <div id="code-editor" ref="codeEditorRef" style="min-height: 400px" />
  <!--  <a-button @click="fillValue">填充值</a-button>-->
</template>

<script setup lang="ts">
import * as monaco from "monaco-editor";
import { onMounted, ref, toRaw, defineProps, withDefaults } from "vue";

interface Props {
  value: string;
  handleChange: (v: string) => void;
}

const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  handleChange: (v: string) => {
    console.log();
  },
});
const codeEditorRef = ref();
const codeEditor = ref();
const value = ref("hello world");

// const fillValue = () => {
//   if (!codeEditor.value) {
//     return;
//   }
//   toRaw(codeEditor.value).setValue("新的值");
// };
onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }

  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    value: value.value,
    language: "java",
    automaticLayout: true,
    minimap: {
      enabled: true,
    },
    // lineNumbers: "off",
    // roundedSelection: false,
    // scrollBeyondLastLine: false,
    readOnly: false,
    theme: "vs-dark",
  });

  codeEditor.value.onDidChangeModelContent(() => {
    console.log("目前的内容为：", toRaw(codeEditor.value).getValue());
  });
});
// Hover on each property to see its docs!
</script>
<style scoped></style>
