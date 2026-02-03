# Release / APK 生成说明

## 构建 APK
1. Android Studio 打开项目并 Sync
2. 运行以下命令生成 Debug APK：
   - `./gradlew :app:assembleDebug`
3. 生成文件路径：
   - `app/build/outputs/apk/debug/app-debug.apk`

## 发布前检查
- 运行单元测试：`./gradlew test`
- 运行 UI 测试（如已配置）：`./gradlew connectedAndroidTest`
- 完成 `docs/qa-checklist.md`
