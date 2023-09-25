<template>
  <div id="questionSubmitView">
    <h2>题目提交记录</h2>
    <a-form :model="searchParams" layout="inline">
      <a-form-item
        field="questionId"
        label="题目编号："
        style="min-width: 240px"
      >
        <a-input
          v-model="searchParams.questionId"
          placeholder="请输入题目编号"
        />
      </a-form-item>
      <a-form-item field="userId" label="用户：" style="min-width: 240px">
        <a-input-tag
          v-model="searchParams.userId"
          placeholder="请输入提交用户ID"
        />
      </a-form-item>
      <a-form-item field="language" label="语言：">
        <a-select
          v-model="searchParams.language"
          placeholder="Please select ..."
          allow-clear
        >
          <a-option value="java">Java</a-option>
          <a-option value="Cpp">Cpp</a-option>
          <a-option value="Go">Go</a-option>
        </a-select>
      </a-form-item>
      <a-form-item field="status" label="状态：">
        <a-select
          v-model="searchParams.status"
          placeholder="Please select ..."
          allow-clear
        >
          <a-option value="0">等待中</a-option>
          <a-option value="1">判题中</a-option>
          <a-option value="2">成功</a-option>
          <a-option value="3">失败</a-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">查询</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
    <a-table
      :ref="tableRef"
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total: total,
      }"
      @page-change="onPageChange"
    >
      <template #userName="{ record }">
        {{ record.userVO.userName }}
      </template>
      <template #codeLength="{ record }">
        {{ getByteCount(record.code) }} Bytes
      </template>
      <template #time="{ record }"> {{ record.judgeInfo.time }} ms</template>
      <template #memory="{ record }">
        {{ record.judgeInfo.memory }} kb
      </template>
      <template #submitTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #status="{ record }">
        <a-tag v-if="record.status === 0" color="orange">等待中</a-tag>
        <a-tag v-if="record.status === 1" color="blue">判题中</a-tag>
        <a-tag v-if="record.status === 2" color="green">成功</a-tag>
        <a-tag v-if="record.status === 3" color="red">失败</a-tag>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watchEffect } from "vue";
import {
  Question,
  QuestionSubmitControllerService,
  QuestionSubmitQueryRequest,
  UserControllerService,
} from "../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

const router = useRouter();
const show = ref(true);
const tableRef = ref();
const dataList = ref([]);
const total = ref(0);
const userName = ref();
/**
 * 控制分页显示大小 —— 一页有几个元素 & 当前是第几页
 */
const searchParams = ref<QuestionSubmitQueryRequest>({
  questionId: undefined,
  userId: undefined,
  status: undefined,
  language: "",
  pageSize: 8,
  current: 1,
});

/**
 * 加载数据
 */
const loadData = async () => {
  const res =
    await QuestionSubmitControllerService.listQuestionSubmitVoByPageUsingPost(
      searchParams.value
    );
  if (res.code === 0) {
    dataList.value = res.data.records;
    console.log(dataList.value);
    total.value = res.data.total;
  } else {
    message.error("加载失败：" + res.message);
  }
};

/**
 * 当页面初始化的时候，执行loadData 加载数据
 */
onMounted(() => {
  loadData();
});
/**
 * 监听watchEffect中包含的函数的参数的改变，一旦有所变化，就会重新执行这个函数
 * 此处就是监听searchParams的变化
 */
watchEffect(() => {
  loadData();
});
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};
/**
 * 跳转到做题页面
 * @param question
 */
const toQuestionPage = (question: Question) => {
  router.push({
    path: `/view/question/${question.id}`,
  });
};
/**
 * 搜索题目
 */
const doSubmit = () => {
  //这里需要重置搜索页号
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
  loadData();
};

const getByteCount = (code: string) => {
  const encoder = new TextEncoder();
  const encodedArray = encoder.encode(code);
  return encodedArray.length;
};

const columns = [
  {
    title: "提交编号",
    dataIndex: "id",
    // questionSubmit
  },
  {
    title: "用户",
    dataIndex: "userId",
    // questionSubmit
  },
  {
    title: "昵称",
    slotName: "userName",
    // user
  },
  {
    title: "题目编号",
    dataIndex: "questionId",
    // questionSubmit
    // todo
  },
  {
    title: "判题结果",
    slotName: "status",
    // questionSubmit
  },
  {
    title: "消耗内存",
    slotName: "memory",
    // questionSubmit
  },
  {
    title: "消耗时间",
    slotName: "time",
    // questionSubmit
  },
  {
    title: "代码语言",
    dataIndex: "language",
    // questionSubmit
  },
  {
    title: "代码长度",
    slotName: "codeLength",
  },
  {
    title: "提交时间",
    slotName: "submitTime",
    // questionSubmit
  },
];
</script>

<style scoped>
#questionSubmitView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
