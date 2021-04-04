## 01 What This

在低延时的应用系统中，需要尽力避免 IO 操作，因此一种做法是将 IO 数据缓存到内存中，服务方按照数据的时效性进行刷新，业务方按需获取内存值即可。

## 02 Feature

> 本项目依赖于 SpringBoot，目前已在 SpringBoot 2.0 + 版本上测试通过。

- [x] SpringBoot 自动配置，无需增加配置文件
- [x] 支持保留被删除的键
- [ ] 支持多种刷新策略
  - [x] 定时刷新
  - [x] 单次刷新
  - [x] 懒加载式刷新
  - [ ] 事件驱动刷新
- [x] 支持观察者模式，缓存发生变化时回调

## 03 Quick Start

> 该项目目前用户较小，因此没有 deploy 到公有仓库中，使用者需采用本地 install 方式。

**STEP1 Clone**

```bash
git clone https://github.com/jitwxs/datakeeper.git
```

**STEP2 Install**

```
cd datakeeper/datakeeper

mvn clean install -Dmaven.test.skip=true
```

出现 `BUILD SUCCESS` 即可

**STEP3 Dependency In Pom**

```xml
<dependency>
    <groupId>com.github.jitwxs</groupId>
    <artifactId>datakeeper</artifactId>
    <version>${datakeeper.version}</version>
</dependency>
```

## 04 Example

您可运行 `datakeeper-sample` 项目，该项目为您展示了如何使用 `datakeeper`。

