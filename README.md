# OJ

尽量每天更新 & 每次学习需要先解决前一天留下来的问题
新增需求：一键变灰？  
哈哈哈哈哈哈，地狱需求/doge

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

#### 前端初始化：

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
6.

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
3.  await store.dispatch("user/getLoginUser");  
    await 是干嘛的
4.  next(`/user/login?redirect=${to.fullPath}`);
   这里跳转后重定向，是什么意思 & 语法
5. 

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
         // 加 await 是为了等用户登录成功之后，再执行后续的代码
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
    name: "用户",
    component: UserLayout,
    children: [
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
    meta: {
        hideInMenu: true,
    },
   },
   ```
在app.vue中将UserLayout 和BasicLayout 做出区分