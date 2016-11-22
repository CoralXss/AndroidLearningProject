-----为整个Project配置同意的sdk版本-----
1. 在根目录下创建 config.gradle如下：
// Gradle统一依赖管理
ext{
    ANDROID_COMPILE_SDK_VERSION=24
    ANDROID_BUILD_TOOLS_VERSION="24.0.1"
    ANDROID_BUILD_MIN_SDK_VERSION=15
    ANDROID_TARGET_SDK_VERSION=24

    APPCOMPAT_VERSION="24.1.1"
}
2. 在项目的build.gradle文件开头中添加：
apply from: "config.gradle"
3. 在app或者其它module的build.gradle中做如下配置：
android {
    compileSdkVersion ANDROID_COMPILE_SDK_VERSION
    buildToolsVersion ANDROID_BUILD_TOOLS_VERSION

    lintOptions {
        abortOnError false
    }

    defaultConfig {
        applicationId "com.xss.mobile"
        minSdkVersion ANDROID_BUILD_MIN_SDK_VERSION
        targetSdkVersion ANDROID_TARGET_SDK_VERSION
        versionCode 1
        versionName "1.0"
        multiDexEnabled = true
    }
}
dependencies {
    compile "com.android.support:recyclerview-v7:${APPCOMPAT_VERSION}"
}


