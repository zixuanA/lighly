package com.mredrock.cyxbs.freshman

import cn.leancloud.AVLogger
import cn.leancloud.AVOSCloud
import com.mredrock.cyxbs.common.BaseApp


class MyApplication : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG)
        AVOSCloud.initialize(this, "Mo02OzQtFuQ5q0Nc8Ux55YeJ-gzGzoHsz", "aDSQq66b9hik7Fxlc6OiOyK7")
    }
}