package com.tendcloud.tenddata;

final class aaThrowableThread implements Runnable {
    final /* synthetic */ Throwable a;

    aaThrowableThread(Throwable th) {
        this.a = th;
    }

    public void run() {
        try {
            if (TCAgent.q() && TCAgent.r() != null) {
                TCAgent.a(this.a);
            }
        } catch (Throwable ignored) {
        }
    }
}

