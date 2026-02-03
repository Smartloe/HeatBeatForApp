# TuneHub Android 架构与模块划分

## 架构选择
- 采用 **MVVM + 单向数据流（MVI 风格）**：
  - UI 只渲染 `UiState` 并派发 `UiEvent`。
  - ViewModel 处理事件、调用 UseCase、输出新的 `UiState`。
  - Repository 负责数据来源聚合（远端/本地），并提供稳定接口。

## 分层职责
- **data**：远端/本地数据源、DTO/Entity、Repository 实现
- **domain**：业务模型与 UseCase（保持纯 Kotlin、无平台依赖）
- **ui**：Compose UI、导航、ViewModel、UiState/UiEvent
- **core**：通用模型、设计系统、工具类（后续扩展）

## 核心状态流与事件流
```
UiEvent -> ViewModel -> UseCase -> Repository -> DataSource
                     <- UiState  <- Result/Model <-
```
- `UiEvent`：用户操作或生命周期事件（点击/搜索/切换播放）
- `UiState`：页面可渲染状态（加载/错误/数据）
- `UiEffect`（可选）：一次性事件（Toast/导航）

## 包结构（初始）
```
app/src/main/java/com/tunehub/app/
  core/
    model/
  data/
  domain/
  ui/
    navigation/
    screens/
    state/
```

## 导航骨架约定
- 采用 **Compose Navigation** 作为导航框架。
- 定义 `AppRoute` 作为统一路由入口。
- `AppNavGraph` 负责集中式路由注册与参数管理。
- 主页采用底部导航：Home / Search / Player / Playlist / Profile / Settings。

## 未来扩展
- 数据层加入缓存策略（Room/DataStore）与错误处理统一化。
- UI 层拆分为 feature modules（如 search/player/playlist）。
