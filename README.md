# OJ

from yupi

#### 项目技术：

前端: Vue3、Arco Design 组件库、手撸项目模板、在线代码编辑器、在线文档浏览  
Java 进程控制、Java安全管理器、部分JVM知识点  
虚拟机 (云服务器) 、Docker (代码沙箱实现)  
Spring Cloud 微服务、消息队列、多种设计模式

## Day1 2023.9.11

一些小概念：

1. 任务调度：不是直接拒绝，应该是告诉用户要等多久轮到自己执行
2. 要学会制作时序图、分层架构图
3. 代码沙箱：让程序跑在一个隔离的环境下，不对外界的其他程序造成影响（保证每个用户的程序运行在独立的环境中）——coding的过程中可以先把它当成黑盒...

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

#### 项目通用布局：(于layouts文件夹中)

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