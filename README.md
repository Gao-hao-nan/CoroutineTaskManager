# CoroutineTaskManager Demo

一个使用 **Kotlin 协程 + ViewModel + StateFlow** 编写的异步任务调度示例项目，不依赖任何网络框架（如 Retrofit / OkHttp），使用原生 `HttpURLConnection` 实现网络请求。

---

## ✨ 特性 Features

- ✅ 使用 `CoroutineTaskManager` 封装异步任务与取消逻辑
- ✅ 每个任务都有独立的 `taskId` 可追踪和取消
- ✅ 利用 `StateFlow` 实时更新 UI 状态
- ✅ 原生网络请求（`HttpURLConnection`），无需依赖第三方库
- ✅ 自动提取 JSON 中所有 `"title"` 字段返回
- ✅ 简洁的状态 UI（Loading / Success / Error）

---

## 🧠 项目结构概览

```plaintext
├── MainActivity.kt             // UI 层，监听状态，展示数据
├── MainViewModel.kt           // ViewModel 层，发起网络任务
├── CoroutineTaskManager.kt    // 通用任务调度器，管理任务状态
├── UiState.kt                 // 封装任务状态：Loading / Success / Error
├── activity_main.xml          // 简单布局按钮 + 状态展示
