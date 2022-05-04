package com.tendcloud.tenddata;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public final class gMessageHandler extends Handler {
    gMessageHandler(Looper looper) {
        super(looper);
    }

    public void handleMessage(Message message) {
        TCAgent.aHandleMessage(message);
    }
}
