package com.tendcloud.tenddata;

import android.util.Log;

public class iUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler a = Thread.getDefaultUncaughtExceptionHandler();

    iUncaughtExceptionHandler() {
    }

    public void uncaughtException(Thread thread, Throwable th) {
        if (TCAgent.J) {
            TCAgent.bSendMessageAboutThrowable(th);
            Log.w("TalkingData", "UncaughtException in Thread " + thread.getName(), th);
        }

        if (this.a != null) {
            this.a.uncaughtException(thread, th);
        }
    }
}
