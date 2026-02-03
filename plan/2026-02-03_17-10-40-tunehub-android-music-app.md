---
mode: plan
cwd: /Users/chenxiray/projects/HeatBeatForApp
task: TuneHub Music App (Android) 计划
complexity: complex
planning_method: builtin
created_at: 2026-02-03T17:10:40+08:00
---

# Plan: TuneHub Music App (Android)

🎯 任务概述
开发一款基于 TuneHub API 的安卓音乐应用，采用 Kotlin + MVVM/MVI + Jetpack Compose，覆盖搜索、播放、歌词、歌单/榜单、登录注册等功能，并实现小清新简约风格与轻量动画。

📋 执行计划
1. 明确需求与 API 范围：核对 TuneHub 接口、鉴权方式、限流策略、字段含义与可用平台范围，形成接口清单与错误码映射。
2. 架构与模块划分：确定 MVVM/MVI 方案，划分 data/domain/ui 层，定义核心状态流与事件；建立包结构与导航骨架。
3. 工程初始化与依赖：引入 Compose、Retrofit、Coroutines、Coil/Glide、Room/DataStore、ExoPlayer，配置最小可跑样例与主题基线。
4. 网络层与仓库：实现 Retrofit 服务、DTO/Domain 映射、统一错误处理与限流提示（含 429/403），建立 Repository 与缓存策略。
5. 播放内核与媒体会话：接入 ExoPlayer，支持播放队列、循环模式、后台播放、通知栏控制与播放历史。
6. 核心页面与导航：实现 Home/Search/Player/Playlist/Profile/Settings 的 Compose 页面与路由，完成小清新视觉样式与轻量动画。
7. 歌词解析与同步：实现 LRC 解析、滚动高亮与拖动定位，和播放进度双向同步。
8. 用户认证与安全：实现验证码流程、RSA 登录、Token 存储与刷新，处理登出与异常态。
9. 本地存储与收藏：用 Room/DataStore 保存歌单收藏、播放历史、用户偏好与音质选择。
10. 质量保障与发布：补充单元/基础 UI 测试，检查性能与耗电，输出 APK 或仓库文档。

⚠️ 风险与注意事项
- TuneHub API 的稳定性、限流与字段变更可能影响功能，需要留出降级与换源提示。
- 后台播放与通知控制受 Android 版本与电量策略限制，需要适配与测试。
- 音乐内容版权合规与平台政策需明确，否则可能影响分发与上线。

📎 参考
- TuneHub API 文档与授权策略（/Users/chenxiray/projects/HeatBeatForApp/TuneHub API Documentation.md）
- Android 媒体播放指南（待确认版本范围）
