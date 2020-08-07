# android_library

#### 介绍
一些自己封装的库
使用方法参考DEMO
这里只封装SDK接口，具体使用方法参考官方文档

#### 软件架构
接SDK的时候需要针对各SDK写对应的接口调用方法，切换来切换去容易出错，抽了点时间封装成库方便使用


#### 安装教程

1.  直接引用对应的jar即可
2.  具体参考demo

#### 使用说明
重新编译
1.  在setting.gradle中启用对应的模块
2.  把不需要的模块注释
3.  打开Terminal面板
4.  执行 gradlew+对应模块中build.gradle的task方法即可
5.  如：要重新编译quicksdk只需在Terminal中执行gradlew buildQuick
6.  重新编译后在对应module下的build/libs/就会生成对应的jar包

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

