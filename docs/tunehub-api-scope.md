# TuneHub API 接口清单与错误码映射

## 基本信息
- Base URL: https://music-dl.sayqz.com
- Version: 1.0.0
- 更新日期: 2026-02-03
- 时区：统计类 API 使用 UTC+8（北京时间）

## 支持平台（source）
- netease（网易云音乐）
- kuwo（酷我音乐）
- qq（QQ 音乐）

## 鉴权与限流策略（基于现有文档）
1. 公共接口
   - 文档未要求 API Key 或 Token。
   - `/api`、`/status`、`/health`、`/stats` 为公开接口（如后续增加鉴权需补充）。
2. 认证相关接口
   - 登录/注册需先获取图形验证码 `/auth/captcha`。
   - 登录/注册使用 `/auth/public-key` 获取 RSA 公钥并按文档规则加密密码。
   - 登录成功返回 `token` 与 `expires_in`，但文档未声明需要 token 的受保护接口。
3. 反爬/防盗链（主要针对 `type=url`）
   - `type=url` 返回 302，真实资源 URL 带短时效签名（默认 60 秒）。
   - 支持 Referer / User-Agent 白名单；非法请求返回 403。
   - 默认限流：单 IP 60 req/min；触发风控返回 429 或 403，响应体含 `risk_reason`。

## 接口清单

### 1) 音乐内容与搜索（/api）
- 获取歌曲基本信息
  - GET `/api/?source={source}&id={id}&type=info`
  - 响应字段：`code`、`message`、`data.name`、`data.artist`、`data.album`、`data.url`、`data.pic`、`data.lrc`、`timestamp`
- 获取音乐文件链接
  - GET `/api/?source={source}&id={id}&type=url&br={128k|320k|flac|flac24bit}`
  - 行为：`302 Redirect` 到真实音频 URL；可能返回 `X-Source-Switch` 头
- 获取专辑封面
  - GET `/api/?source={source}&id={id}&type=pic`
  - 行为：`302 Redirect` 到图片 URL
- 获取歌词（LRC）
  - GET `/api/?source={source}&id={id}&type=lrc`
  - 行为：`text/plain` LRC 内容
- 搜索歌曲
  - GET `/api/?source={source}&type=search&keyword={keyword}&limit={20}`
  - 响应字段：`data.keyword`、`data.total`、`data.results[].id/name/artist/album/url/platform`
- 聚合搜索
  - GET `/api/?type=aggregateSearch&keyword={keyword}`
  - 响应字段：`data.keyword`、`data.results[].id/name/artist/platform`
- 获取歌单详情
  - GET `/api/?source={source}&id={id}&type=playlist`
  - 响应字段：`data.list[].id/name/types[]`、`data.info.name/author`
- 获取排行榜列表
  - GET `/api/?source={source}&type=toplists`
  - 响应字段：`data.list[].id/name/updateFrequency`
- 获取排行榜歌曲
  - GET `/api/?source={source}&id={id}&type=toplist`
  - 响应字段：`data.list[].id/name`、`data.source`

### 2) 系统健康与监控
- 服务状态
  - GET `/status`
  - 响应字段：`data.status`、`data.platforms`
- 健康检查
  - GET `/health`
  - 响应字段：`data.status`

### 3) 统计分析（/stats）
- 获取统计数据
  - GET `/stats?period={today}&groupBy={platform}`
  - 响应字段：`data.period`、`data.overall`、`data.breakdown[]`、`data.qps`
- 获取统计摘要
  - GET `/stats/summary`
  - 响应字段：`data.today`、`data.week`、`data.top_platforms_today[]`
- 平台统计概览
  - GET `/stats/platforms?period={today}`
  - 响应字段：`data.platforms.{platform}.total_calls/success_rate`
- QPS 统计
  - GET `/stats/qps?period={today}`
  - 响应字段：`data.qps.avg_qps/peak_qps/hourly_data[]`
- 趋势数据
  - GET `/stats/trends?period={week}`
  - 响应字段：`data.trends[].date/total_calls/success_rate`
- 请求类型统计
  - GET `/stats/types?period={today}`
  - 响应字段：`data.requestTypes.{type}.total_calls/success_rate`

### 4) 认证与安全（/auth）
- 获取图形验证码
  - GET `/auth/captcha`
  - 响应字段：`data.captcha_id`、`data.image`、`data.expires_in`
- 获取登录加密公钥
  - GET `/auth/public-key`
  - 响应字段：`data.key_id`、`data.public_key`、`data.expires_in`
- 用户注册
  - POST `/auth/register`
  - 请求字段：`username`、`password`（RSA-OAEP-SHA256 加密）`key_id`、`captcha_id`、`captcha_code`、`nonce`、`timestamp`
  - 响应字段：`data.user_id`
- 用户登录
  - POST `/auth/login`
  - 请求字段：`username`、`password`、`key_id`、`captcha_id`、`captcha_code`、`nonce`、`timestamp`
  - 响应字段：`data.token`、`data.expires_in`

## 错误码映射表（基于现有文档）
| code | 场景 | 处理建议 | 备注 |
|---|---|---|---|
| 200 | 通用成功 | 正常解析 data | JSON 响应常见字段 `code/message/data` |
| 302 | `type=url` 或 `type=pic` 成功 | 跟随重定向获取资源 | 302 Header 可能包含 `X-Source-Switch` |
| 403 | 反爬或白名单校验失败 | 提示访问受限，检查 Referer/UA | 文档注明可能返回 403 |
| 429 | 触发限流/风控 | 指数退避重试或提示用户稍后重试 | 响应体含 `risk_reason` |
| 其它 | 未文档化错误 | 统一错误提示并记录日志 | 需与服务端确认 |

## 备注
- 自动换源：`type=url` 失败会尝试其它平台，响应头返回 `X-Source-Switch`。
- 如后续新增鉴权或接口字段变更，请同步更新本清单与错误码映射。
