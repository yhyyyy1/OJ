# OJ——YhyOJ系统

#### 系统功能梳理

1. 用户模块
    1. 注册
    2. 登录
2. 题目模块
    1. 创建题目-admin
    2. 删除题目-admin
    3. 修改题目-admin
    4. 搜索题目-user
    5. 在线做题——题目详情页
3. 判题模块
    1. 提交判题——结果正确or错误
    2. 错误处理——内存溢出、安全性、超时
    3. **代码沙箱**——安全沙箱
    4. 开放接口

#### 项目技术：

* 前端：
  > Vue3、Arco Design 组件库、手撸项目模板 (✔)  
  > 在线代码编辑器、在线文档编辑 (✔)
* 后端：
  > Java 进程控制
  > Java安全管理器
  > 部分JVM知识点
* 服务器：
  > 虚拟机 (云服务器)
  > Docker (代码沙箱实现)  
  > Spring Cloud 微服务、消息队列、多种设计模式

# 前端

前端框架的结构(src部分)：

1. **assets 文件夹**：这通常用于**存放项目中的静态资源**，如图像、字体、样式表、音频文件等。这些资源可以在前端应用程序中使用，并通过相对路径引用
2. **components 文件夹**：这是用于**存放可重用组件**的地方。组件是前端应用程序的构建块，它们可以包括界面元素、小部件和功能模块。将组件放在这个文件夹中有助于保持代码的组织结构和可维护性。
3. **layouts 文件夹**：这个文件夹通常用于**存放应用程序的不同布局或页面模板**。在一些前端框架中，你可以创建多个不同的布局，然后根据需要将它们应用于不同的页面。
4. **router 文件夹**：在使用前端路由器的应用程序中，这个文件夹通常**包含路由配置和路由器相关的代码**
   。前端路由器用于管理应用程序的导航和URL路由。
5. **store 文件夹**：如果你使用状态管理库（如Vuex、Redux等），这个文件夹通常用于**存放应用程序的状态管理代码**
   。状态管理库有助于管理应用程序的全局状态和数据。
6. **views 文件夹**：这个文件夹通常**包含应用程序的不同视图或页面**。**每个视图对应着一个特定的路由或页面**
   ，它们通常包含与用户界面相关的代码和逻辑。
7. **access 文件夹**：全局权限校验

## 前端初始化
1. 检查正常的node环境 & vue-cli脚手架
2. vue create yhyoj_frontend——创建自己的前端项目
3. 安装组件库  
    常见的组件库 arco design 和 ant design（前端项目需要常用组件库，极其方便）  
    yarn add --dev @arco-design/web-vue——引入组件库arco design

    ```ts
    import {createApp} from 'vue'
    import ArcoVue from '@arco-design/web-vue';
    import App from './App.vue';
    import '@arco-design/web-vue/dist/arco.css';

    const app = createApp(App);
    app.use(ArcoVue);
    app.mount('#app');
    //完整引入（写进main.ts）
    ```
4. 关注App.vue——全局入口
    ```ts
    <template>
      <div id="app">
        <template v-if="route.path.startsWith('/user')">
          <!--看此处，有个if，表明如果有以/user开头的需要直接渲染页面，而不使用BasicLayout-->
          <router-view />
        </template>
        <template v-else>
          <BasicLayout />
        </template>
      </div>
    </template>

    <style>
    #app {
    }
    </style>

    <script setup lang="ts">
    import BasicLayout from "@/layouts/BasicLayout.vue";
    import { onMounted } from "vue";
    import { useRoute } from "vue-router";

    const route = useRoute();
    /**
    * 全局初始化函数，有全局单次调用的代码，都可以写在这里
    */
    const doInit = () => {
      console.log("hello 欢迎来到我的项目，你好~~~~~~~");
    };

    onMounted(() => {
      doInit();
    });
    </script>
    ```

### layouts 文件夹 —— 页面布局 & 模板管理

#### 1. layouts/BasicLayout.vue
基础布局——应用了components中的GlobalHeader组件；`<router-view />`表示布局的content内容是根据路由配置和url显示的view，
```ts
<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer"
        >一个简单的开发中的OJ平台 By Yhy 联系我yinhongyang546@gmail.com
      </a-layout-footer>
    </a-layout>
  </div>
</template>
```
#### 2. layouts/UserLayout.vue
针对用户的Login、Info、Register、Update的页面的样式（和BasicLayout较为相像）

### components 文件夹 —— 组件管理

#### 1. components/GlobalHeader.vue
全局的头部样式——平台Logo、菜单、用户状态  
主要的需求：
1. 隐藏几种不需要的页面（）
#### 2. components/CodeEditor.vue

#### 3. components/MdEditor.vue

#### 4. components/MdViewer.vue


### store 文件夹 —— 状态管理

#### 1. store/index.ts

#### 2. store/user.ts


### access 文件夹 —— 权限管理

#### 1. access/index.ts

#### 2. access/accessEnum.ts

#### 3. access/checkAccess.ts


### router 文件夹 —— 路由管理

#### 1. router/index.ts

#### 2. router/routes.ts


### views 文件夹 —— 页面内容管理
分为主要页面、User页面、Question页面
#### 1.



# 后端




# 代码沙箱
