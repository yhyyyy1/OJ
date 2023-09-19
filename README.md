# OJ

尽量每天更新 & 一端时间的学习后解决前一段时间留下来的问题——沉淀/doge  
新增需求：一键变灰？  
哈哈哈哈哈哈，地狱需求/doge

### 系统功能梳理

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

### 项目技术：

* 前端：
  > Vue3、Arco Design 组件库、手撸项目模板 (✔)  
  > 在线代码编辑器、在线文档浏览
* 后端：
  > Java 进程控制、Java安全管理器、部分JVM知识点
* 服务器：
  > 虚拟机 (云服务器) 、Docker (代码沙箱实现)  
  Spring Cloud 微服务、消息队列、多种设计模式

## Day1 2023.9.11

一些小概念：

1. 任务调度：不是直接拒绝，应该是告诉用户要等多久轮到自己执行
2. 要学会制作时序图、分层架构图
3. 代码沙箱：让程序跑在一个隔离的环境下，不对外界的其他程序造成影响（保证每个用户的程序运行在独立的环境中）——coding的过程中可以先把它当成黑盒...

框架的结构(src部分)：

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

### 前端初始化：

1. 正常的node环境 & vue-cli脚手架
2. vue create yhyoj_frontend——创建自己的前端项目

#### 组件库：

yarn add --dev @arco-design/web-vue——引入组件库arco design(使用非常方便)

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

### 项目通用布局：(于layouts文件夹中)

详见layouts/BasicLayout.vue，下面是学到的东西

###### 无code的部分：

1. 每个组件要给出一个id（coding习惯）
2. 比较大块的内容，用div框起来
3. 布局需要先把上中下的内容安排好，然后再写内容

###### 含code的部分：

1. ```vue
    <style scoped></style> 
   <!--这个样式将仅适用于包含该<style>标签的元素及其子元素，而不会影响到全局的样式。-->
    ```
2. ```js
    const router = useRouter();
    const selectedKeys = ref([route.path]);
    
    router.afterEach((to, from, failure) => {
        selectedKeys.value = [to.path];
        <!--表示跳转路由后，执行函数中的内容-->
    });
   <!--执行路由后，同步更新菜单栏的高亮状态-->
   <!--整体的执行：点击菜单栏 => 跳转并更新路由 => 更新路由后，同步更新菜单栏的高亮状态-->
    ```

#### 动态路由配置方法：

1. 提取通用路由文件，将得到的新文件import进index.ts中
2. 菜单组件读取路由，动态渲染菜单项
3. 绑定跳转事件
4. 同步路由到菜单项

#### 全局状态管理：

所有页面全局共享的变量，而不是仅仅局限于某一个页面中
一般为User & LoginUser

state: 存储的状态信息，比如用户信息  
mutation (尽量同步) : 定义了对变量进行增删改 (更新) 的方法  
actions (支持异步): 执行异步操作，并且触发 mutation 的更改 (actions 调用 mutation)  
modules (模块): 把 个大的 state (全局变量) 划分为多个小模块，比如 user 专门存用户的状态信息

#### 权限管理:

能够直接以一套通用的机制,去定义哪个页面需要哪些权限  
(路由中定义页面所需要的权限)

1. 在路由配置文件，定义某个路由的访问权限
2. 在全局页面组件 app.vue 中，绑定一个全局路由监听。每次访问页面时，根据用户要访问页面的路由信息先判断用户是否有对应的访问权限
3. 如果有，跳转到原页面，如果没有，拦截或跳转到 401 鉴权或登录页  
   下面是一个利用beforeEach获取路由信息,进而判断的方法

```js
router.beforeEach((to, from, next) => {
    if (to.meta?.access === "canAdmin") {
        if (store.state.user.loginUser?.role !== "admin") {
            next("/noAuth");
            return;
        }
    }
    next();
});
```

## Day2 2023.9.12

### question:

1. min-height是什么？vh是什么意思？| position是什么元素？
2. :wrap是换行？怎么记忆
3. style="margin-bottom: 16px" 这是什么意思呢？
4. v-for 和 v-if的渲染优先级问题，**v-for 比 v-if 有更高的优先级**  
   意味着在同一元素上使用 v-for 和 v-if 时，v-for 的循环渲染会在 v-if 条件判断之前执行。  
   **所以尽量不要两者一起使用（推荐在js中先过滤）**
5. const loginUser = store.state.user.loginUser; 这是什么意思呢？

### 项目通用布局（接上）

#### 全局权限管理（new），不再依赖于app.vue，根据权限控制菜单的显隐

新建一个access目录用于定义权限

1. 定义权限，见accessEnum.ts（可知类似枚举类，进而确定权限定义的规范）
   ```js
      const AccessEnum = {
         NOT_LOGIN: "notLogin",
         USER: "user",
         ADMIN: "admin",
       };
   ```
2. 定义一个公用的权限校验函数，见checkAccess.ts
   ```js
   const checkAccess = (loginUser: any, needAccess = AccessEnum.NOT_LOGIN) => {
   //获取当前登录用户具有的权限，如果没有loginUser，默认未登录
    const loginUserAccess = loginUser?.userRole ?? AccessEnum.NOT_LOGIN;
    if (needAccess === AccessEnum.NOT_LOGIN) {
        return true;
    }
   //如果需要用户登录才能访问
    if (needAccess === AccessEnum.USER) {
   // 只要登录就可以了
        if (loginUserAccess === AccessEnum.NOT_LOGIN) 
            return false;
    }
    if (needAccess === AccessEnum.ADMIN) {
        if (loginUserAccess !== AccessEnum.ADMIN)
            return false;
    }
    return true;
   //影响不大，属于少写代码了
   };
   ```
3. 修改GlobalHeader，特别是使用了计算属性，可以保证在更新的时候，会同步更新对页面的渲染
   ```ts
      const visibleRoutes = computed(() => {
        return routes.filter((item, idex) => {
            if (item.meta?.hideInMenu) {
                return false;
            }
            if (
                !checkAccess(store.state.user.loginUser, item?.meta?.access as string)
            ) {
                return false;
            }
            return true;
        });
      });
   ```
4. **特殊提醒：一个js文件想要被其他模块使用，需要导出**，如下
   ```js
   export default checkAccess;
   export default AccessEnum;
   ```

### 全局项目入口（但是没有实现）

app.vue中，见doInit & onMounted

## 前端初始化到此为止！下面是后端

后端主要用的是 《后端模板》，详见本人 springboot-init项目

### 使用方法：

1. 改项目名称  
   ctrl + shift + F 进行全局搜索，找到springboot-init  
   ctrl + shift + R 进行全局替换，换为自己的项目名称（本项目后端为yhyoj_backend）
2. 改包名  
   ctrl + shift + F 进行全局搜索，找到包名（springbootinit）  
   ctrl + shift + R 进行全局替换，换为自己的包名（本项目后端为yhyoj）
3. 找到未替换的包名（一般为目录名）  
   shift + F6 ， 将包名替换为上一步的包名
4. 并且按照定义设计数据库(可以直接执行sql/create_table.sql，改名即可)
5. 找到 yhyoj_backend(项目名称)/src/main/resources/application.yml 修改配置文件  
   数据库部分，按照自己的本地数据库进行更改，替换之前的my_db  
   端口号，尽量不要和自己的其他项目的重复
6. **启动！！！！一定要启动main中的MainApplication文件**
   血泪史，没看清，自己瞎搞了半天，发现是启动错文件了，淦！！！！！
7. 接下来启动http://localhost:8121(定义的端口号)/api/doc.html
8. 日志部分没有完善哦~

## Day3 2023.9.13

### question:

1. aop切面？ 有什么用呢
2. ```ts
    commit("updateUser", {
        ...state.loginUser,
        userRole: AccessEnum.NOT_LOGIN,
    });
    ```
   这里的...是什么意思？
3. await store.dispatch("user/getLoginUser");  
   await 是干嘛的
4. next(`/user/login?redirect=${to.fullPath}`);
   这里跳转后重定向，是什么意思 & 语法

### 后端模板讲解：

1. 先阅读 README.md
2. sql/create_table.sql 定义了数据库的初始化建库建表语句
3. sql/post_es_mapping.json 帖子表在 ES 中的建表语句
4. aop：用于全局权限校验、全局日志记录
5. common：万用的类，比如通用响应类
6. config：用于接收 application.yml 中的参数，初始化一些客户端的配置类（比如对象存储客户端）
7. constant：定义常量
8. controller：接受请求
9. esdao：类似 mybatis 的 mapper，用于操作 ES
10. exception：异常处理相关
11. job：任务相关（定时任务、单次任务）
12. manager：服务层（一般是定义一些公用的服务、对接第三方 API 等）
13. mapper：mybatis 的数据访问层，用于操作数据库
14. model：数据模型、实体类、包装类、枚举值
15. service：服务层，用于编写业务逻辑
16. utils：工具类，各种各样公用的方法
17. wxmp：公众号相关的包
18. test：单元测试
19. MainApplication：项目启动入口
20. Dockerfile：用于构建 Docker 镜像

### 前后端联调：( 接口！)

前端发送请求，调用后端接口；接口/请求，连接前后端

现在！使用**openAPI**自动生成！如下：

```
npm install openapi-typescript-codegen --save-dev
openapi --input http://localhost:8121/api/v2/api-docs --output ./generated --client axios
```

就获得了接口的文件（在前端获取），不用再自己一点点写啦！（如果有更新就再运行上面的指令就行了）

(如果无法运行，请进行全局安装openapi，```npm install -g openapi-typescript-codegen```)

**如果想要自定义参数**

1) 修改generated/core/OpenAPI.ts文件中，对应的参数
    ```ts
    export const OpenAPI: OpenAPIConfig = {
        BASE: 'http://localhost:3000/api',
        VERSION: '2.0',
        WITH_CREDENTIALS: false,
        CREDENTIALS: 'include',
        TOKEN: undefined,
        USERNAME: undefined,
        PASSWORD: undefined,
        HEADERS: undefined,
        ENCODE_PATH: undefined,
    };
   ```
2) 直接定义axios 请求库的全局参数，如全局请求响应拦截器
    ```ts
    //new 了一个yhyoj_frontend/src/plugins/axios.ts
    // Add a request interceptor
    import axios from "axios";
    
    axios.interceptors.request.use(
        function (config) {
        // Do something before request is sent
            return config;
        },
        function (error) {
        // Do something with request error
            return Promise.reject(error);
        }
    );
    
    // Add a response interceptor
    axios.interceptors.response.use(
        function (response) {
            console.log("响应", response);
            // Any status code that lie within the range of 2xx cause this function to trigger
            // Do something with response data
            return response;
        },
        function (error) {
            // Any status codes that falls outside the range of 2xx cause this function to trigger
            // Do something with response error
            return Promise.reject(error);
        }
    );
    ```
   并且在mian.js中配置axios.ts文件```import "@/plugins/axios";```

## 后端初始化到此为止！下面是主要业务的实现

简单的增删改查就在store/user.ts中实现，全局状态管理是有它的用途滴

### 用户登录功能

1. 自动登录
    * 编写获取远程登录用户信息的代码
      ```ts
      actions: {
         async getLoginUser({ commit, state }, payload) {
            //从远程获取登录信息
            const res = await UserControllerService.getLoginUserUsingGet();
            if (res.code === 0) {
               commit("updateUser", res.data);
            } else {
               commit("updateUser", {
                  ...state.loginUser,
                  userRole: AccessEnum.NOT_LOGIN,
               });
            }
            // 触发了mutation的更改
         },
      },
      ```
    * 在哪里触发 getLoginUser 的执行？应当在一个全局的位置  
      路由拦截(✔，之前在app.vue中有写过，现在移到access/index.ts中) 、全局页面入口app.vue、全局通用布局、

#### 全局权限管理优化

1. 新建 access\index.ts 文件，把原有的路由拦截、权限校验逻辑放在独立的文件中  
   优势：只要不引入、就不会开启、不会对项目有影响
2. 编写权限管理和自动登录逻辑  
   如果没登陆过，自动登录：
   ```ts
   // // 如果之前没登陆过，自动登录
   if (!loginUser || !loginUser.userRole) {
   // 加 await 是为了等用户登录成功之后，再执行后续的代码
        await store.dispatch("user/getLoginUser");
   }
   ```
3. 最终函数
   ```ts
   router.beforeEach(async (to, from, next) => {
      console.log("登陆用户信息", store.state.user.loginUser);
      const loginUser = store.state.user.loginUser;
      //如果之前没登陆过，自动登录
      if (!loginUser || !loginUser.userRole) {
         // 加 await 是为了等用户登录成功之后，再执行后续的代码，异步变同步
         await store.dispatch("user/getLoginUser");
      }
      //上面是希望用户尽量登录，
      //因为可能存在依旧没有登录的情况，所以需要下面的判断。。。非常的严
      const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN;
      // 要跳转的页面必需要登陆
      if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
         // 如果没登陆，跳转到登录页面
         if (!loginUser || !loginUser.userRole) {
            next(`/user/login?redirect=${to.fullPath}`);
            return;
         }
         // 如果已经登陆了，但是权限不足，那么跳转到无权限页面
         if (!checkAccess(loginUser, needAccess)) {
            next("/noAuth");
            return;
         }
      }
      next();
   });
   
   ```

#### User页面开发：

包括登录和注册
因为登录页面不需要通用模板的导航栏，所以就自己再开发一套喽，实现多套布局

##### 实现多套布局：

在route.ts中，添加user的对应路由

   ```ts
   {
    path: "/user",
        name
:
    "用户",
        component
:
    UserLayout,
        children
:
    [
        {
            path: "/user/login",
            name: "用户登录",
            component: UserLoginView,
        },
        {
            path: "/user/register",
            name: "用户注册",
            component: UserRegisterView,
        },
    ],
        meta
:
    {
        hideInMenu: true,
    }
,
}
,
   ```

在app.vue中将UserLayout 和BasicLayout 做出区分

## Day4 2023.9.14

沉淀，解决之前的问题

##### copy from Day2

1. min-height是什么？vh是什么意思？| position是什么元素？
   > min-height是一种CSS属性，用于指定元素的最小高度，   
   > vh是CSS 中的一个单位，代表视口高度（Viewport Height）；一般有 min-height = 100vh
   表示元素的高度就是浏览器窗口的可见高度  
   > position 是CSS属性。它用于定义HTML元素在页面上的定位方式；position: sticky表示将当前元素黏附到特定位置上
2. :wrap是换行？怎么记忆
   > 记住吧，参数：true——启用文本或内容的自动换行；false——禁用文本或内容的自动换行
3. style="margin-bottom: 16px" 这是什么意思呢？
   > 定义该元素的底部边距（margin）为16像素；其实不只是页面底部，包括当前元素与下一个元素之间的边距
4. v-for 和 v-if的渲染优先级问题
   > **v-for 比 v-if 有更高的优先级**  
   意味着在同一元素上使用 v-for 和 v-if 时，v-for 的循环渲染会在 v-if 条件判断之前执行。  
   **所以尽量不要两者一起使用（推荐在js中先过滤）**
5. const loginUser = store.state.user.loginUser; 这是什么意思呢？
   > 前面肯定有const store = useStore();
   > 这个就像数据结构一样，一层一层的 state

##### copy from Day3

1. aop切面？ 有什么用呢
   > 在后端
   aop：用于全局权限校验、全局日志记录
2. ```ts
    commit("updateUser", {
        ...state.loginUser,
        userRole: AccessEnum.NOT_LOGIN,
    });
    ```
   这里的...是什么意思？
   > 经过查资料 ...是保证单向的数据传递，确保状态的不可变性和可预测性。。。。。
3. await store.dispatch("user/getLoginUser");  
   await 是干嘛的——异步变同步
   > await 关键字用于等待一个异步操作的完成，使异步代码看起来像同步代码一样执行。  
   > 表示等待 store.dispatch("user/getLoginUser") 这个异步操作完成后再继续执行下面的代码，从而实现了异步变同步的效果。
4. next(`/user/login?redirect=${to.fullPath}`);
   这里跳转后重定向，&是什么意思
   > 就是之前mybatis中的参数空缺，使用to.fullPath填充

##### create from codeFiles

1. 关于 store.dispatch 的作用：
   > dispatch：分发；之后跟一个方法如user/getLoginUser & 传递的参数  
   > 实现调用store中actions部分中的函数  
   > 实现了异步操作
2. 一定要注意store中的状态变量，调用方法：  
   store.state.user（某个store文件存储的状态）.某个状态  
   使用store.dispatch 调用actions 中的函数  
   使用store.commit 调用 mutations 中的函数

## Day5 2023.9.15

关于router.push的参数 replace的认识

```ts
await router.push({
    path: "/user/login",
    replace: true,
});
```

replace 表示用于导航时控制路由的替代方式（默认为false）  
当设置为true时，它将替代当前路由历史中的当前路由，而不会在历史中创建新的路由记录。  
这意味着用户在浏览器的后退按钮或前进按钮上点击时，不会返回到前一个路由，而是直接跳转到新的路由。

## Day6&7 2023.9.17 & 18

### 库表设计

1. 用户模块(就用后端给的就行)
2. 题目表
    1. 题目标题
    2. 题目内容（存放题目的介绍、输入输出提示、描述、具体的详情）
    3. 题目标签（json 数组字符串）：栈/队列/链表，简单/中等/困难
    4. 题目答案：管理员or用户设置的参考答案
    5. 提交数、ac数
    6. 判题相关：judgeConfig——时间限制、内存限制，judgeCase——输入用例、输出用例 （judgeConfig属于json 对象，包括：
       ```ts
       {
           "timeLimit": 1000,
           "memoryLimit": 1000,
           "stackLimit": 1000
        }
   JSON的优点：便于扩展，只需要改变对象内部的字段，而不用修改数据库表）
   总体题目表为：
   ```
   -- 题目表
   create table if not exists question
   (
   id         bigint auto_increment comment 'id' primary key,
   title      varchar(512)                       null comment '标题',
   content    text                               null comment '内容',
   tags       varchar(1024)                      null comment '标签列表（json 数组）',
   answer     text                               null comment '题目答案',
   submitNum  int  default 0 not null comment '题目提交数',
   acceptedNum  int  default 0 not null comment '题目通过数',
   judgeCase text null comment '判题用例（json 数组）',
   judgeConfig text null comment '判题配置（json 对象）',
   thumbNum   int      default 0                 not null comment '点赞数',
   favourNum  int      default 0                 not null comment '收藏数',
   userId     bigint                             not null comment '创建用户 id',
   createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
   updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
   isDelete   tinyint  default 0                 not null comment '是否删除',
   index idx_userId (userId)
   ) comment '题目' collate = utf8mb4_unicode_ci;
   ```
3. 题目提交表
    1. 提交用户id：userId
    2. 题目 id：questionId
    3. 语言：language
    4. 用户的代码：code
    5. 判题状态：status（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
    6. 判题信息（判题过程中得到的一些信息，比如程序的失败原因、程序执行消耗的时间、空间）：
       其中判题信息有：
        * Accepted 成功
        * Wrong Answer 答案错误
        * Compile Error 编译错误
        * Memory Limit Exceeded 内存溢出
        * Time Limit Exceeded 超时
        * Presentation Error 展示错误
        * Output Limit Exceeded 输出溢出
        * Waiting 等待中
        * Dangerous Operation 危险操作
        * Runtime Error 运行错误（用户程序的问题）
        * System Error 系统错误（做系统人的问题）  
          judgeInfo（json 对象 包括的参数：
       ```
          {
               "message": "程序执行信息",
               "time": 1000, // 程序执行时间，单位为 ms
               "memory": 1000, // 程序执行内存，单位为 kb
          }
   总体题目提交表为：
    ```
   -- 题目提交表
   create table if not exists question_submit
   (
   id         bigint auto_increment comment 'id' primary key,
   language   varchar(128)                       not null comment '编程语言',
   code       text                               not null comment '用户代码',
   judgeInfo  text                               null comment '判题信息（json 对象）',
   status     int      default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
   questionId bigint                             not null comment '题目 id',
   userId     bigint                             not null comment '创建用户 id',
   createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
   updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
   isDelete   tinyint  default 0                 not null comment '是否删除',
   index idx_questionId (questionId),
   index idx_userId (userId)
   ) comment '题目提交';
    ```

**索引**，什么情况加索引 & 选择什么字段加索引呢？  
根据实际业务情况 & sql语句来添加（上面就是根据题目ID 添加）  
要选择区分度很大的字段来加，如性别加索引就没必要了

### 后端接口开发

流程：

1. 根据功能设计库表（详见上一步）
2. **自动生成**对数据库基本的增删改查—— Mapper 和 Service 层的基本功能
    1. 安装 MyBatisX 插件
    2. 根据项目去调整生成配置（建议生成到独立的包 不要影响老的项目）
       ![](D:\Project\OJ\yhyoj_frontend\doc\myBatisX.png)
    3. 将生成的代码从生成包中移动到实际项目对应目录中
3. 编写 Controller 层，实现基本的增删改查和权限校验
    1. 在基础Controller中找业务相似的代码复制一下就可以（单表复制单表、关联表去复制关联表）
    2. 复制实体类相关的DTO、VO、枚举值字段（用于接收相关请求 or 业务间传递），复制之后调整其中字段
    3. dto中json为了方便，写成了list的形式，所以需要给json字段编写独立的类，如judgeCase、judgeConfig、judgeInfo
4. 去根据业务定制开发新的功能/编写新的代码
    1. 编写 QuestionVO 的json /对象转换工具类用同样的方法
    2. 编写 questionSubmit 提交类，这次参考 postThumb 相关文件
    3. 编写枚举类（判题状态 & 判题信息 & 编程语言）
    4. 代码写好后不要着急写前端、先通过Swagger验证

##### 经过测试发现的问题

1. 使用Lombok（一种 Java 库）中的@Data注解，用于自动生成一些简单的函数  
   如：get & set 、 equal 、 hashCode 、 toString
2. 为了防止用户艳照id顺序爬取题目，建议id的生成规则改成 ASSIGN_ID（非连续自增的——雪花算法？） 而不是从 1 开始自增

##### 纯原创接口——查询提交信息接口（重点是思路 & 过程）

**功能**：能够根据用户id or 题目id or 编程语言 or 题目状态，去查询提交记录  
**注意tips**：仅本人和管理员能看见自己（自交userId和登录ID相同）提交的代码  
**实现方案**：先查询，再根据userId进行脱敏

**业务前缀**，加与否？  
加业务前缀的好处，防止多个表都有类似的类，产生冲突；不加的前提，因为可能这个类是多个业务之间共享的，能够复用的。

**DTO**：（Data Transfer Object）数据传输对象， 即RPC 接口请求或传输出去的对象，用于展示层与服务层之间的数据传输对象。  
**VO**：视图对象，一般位于Controller层，用于展示视图。  
本项目定义的VO 类：作用是专门给前端返回对象，可以节约网络传输大小、或者**过滤字段（脱敏——主要的作用）**、保证安全性。  
比如 judgeCase、answer 字段，一定要删，不能直接给用户答案。（过滤）

数据脱敏——数据去隐私化

synchronized——同步的，表明加锁了；对应数据库中对事务的操作

枚举类中如 WAITING("等待中", 0) 其中“等待中”->text，0->value  
这种的枚举方法可以通过getText & getValue获取这个枚举类附带的不同参数

类名 + allget：获取当前类的所有变量，用于一个个查看参数 并 对其进行操作

sqlfather——代码生成器，等学完设计模式，自己写一套

## Day8 2023.9.19

纯前端，主要的OJ网页设计

1. 用户注册页面
    * 注册
    * 登录
2. 创建题目页面(管理员)
3. 题目管理页面(管理员)
    * 查看(搜索)
    * 删除
    * 修改
    * 快捷创建
4. 题目列表页 (用户)
5. 题目详情页(在线做题页)
    * 判题状态的查看
6. 题目提交列表页
7. 提交统计 & 个人页面（之后拓展）

### 接入要用到的组件

开工之前要进行技术选型！  

本项目：要有一个在线文档编辑器——Markdown的 & 在线代码编辑器——用微软的  
so，要先把上述两个组件接入

###### 1. bytemd md编辑器

bytemd => https://github.com/bytedance/bytemd
> npm i @bytemd/vue-next

安装vue3的bytemd的包

> npm i @bytemd/plugin-gfm
> npm i @bytemd/plugin-highlight

安装markdown中支持GFM 和 高亮 的两个组件

新建一个api文件 "yhyoj_frontend/src/components/MdEditor.vue"  
同时要把MdEditor 当前输入的值暴露给父组件，便于父组件去使用，同时提高组件的同用性，需要定义属性，要把value 和 handleChange
事件交给父组件去管理

```vue

<template>
  <Editor :value="value" :plugins="plugins" @change="handleChange"/>
</template>

<script setup lang="ts">
  import gfm from "@bytemd/plugin-gfm";
  import highlight from "@bytemd/plugin-highlight";
  import {Editor, Viewer} from "@bytemd/vue-next";
  import {ref, withDefaults, defineProps} from "vue";

  /**
   * 定义组件属性的类型
   */
  interface Props {
    value: string;
    handleChange: (v: string) => void;
  }

  const plugins = [
    gfm(),
    highlight(),
    // Add more plugins here
  ];
  const props = withDefaults(defineProps<Props>(), {
    value: () => "",
    handleChange: (v: string) => {
      console.log(v);
    },
  });
</script>
<style scoped></style>
```

###### 2. Monaco Editor 代码编辑器

微软官方 => https://github.com/microsoft/monaco-editor  
官方整合教程 => https://github.com/microsoft/monaco-editor/blob/main/docs/integrate-esm.md

> npm install monaco-editor  
> npm install monaco-editor-webpack-plugin

在 vue.config 中添加 webpack 配置 yhyoj_frontend/vue.config.js

```ts
const {defineConfig} = require("@vue/cli-service");
const MonacoWebpackPlugin = require("monaco-editor-webpack-plugin");

module.exports = defineConfig({
    transpileDependencies: true,
    chainWebpack(config) {
        config.plugin("monaco").use(new MonacoWebpackPlugin({}));
    },
});
```

新建文件yhyoj_frontend/src/components/CodeEditor.vue(非终版，有东西是写死的)，对monaco editor进行配置

```vue
<
<template>
  <div id="code-editor" ref="codeEditorRef" style="min-height: 400px"/>
  <!--  <a-button @click="fillValue">填充值</a-button>-->
</template>

<script setup lang="ts">
  import * as monaco from "monaco-editor";
  import {onMounted, ref, toRaw, defineProps, withDefaults} from "vue";

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
```

配置参数 参考 => http://chart.zhenglinglu.cn/pages/2244bd/#%E5%9C%A8-vue-%E4%B8%AD%E4%BD%BF%E7%94%A8

### 页面开发

#### 创建题目页面

#### 题目管理页面

#### 更新页面

#### question

1. vue中父子组件之间传值 & 相互管理 的操作 interface Props {xxxx} & const props = withDefaults
2. 
