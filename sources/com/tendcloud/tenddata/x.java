package com.tendcloud.tenddata;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import java.util.Map;


public final class x implements Runnable {
    final /* synthetic */ String aEventId;
    final /* synthetic */ String bEventLabel;
    final /* synthetic */ Map<String, Object> cParameterMap;

    x(String str, String str2, Map<String, Object> map) {
        this.aEventId = str;
        this.bEventLabel = str2;
        this.cParameterMap = map;
    }

    public void run() {
        try {
            if ((TCAgent.H) && !TextUtils.isEmpty(TCAgent.M)) {
                aAppEventEntity aVar = new aAppEventEntity();
                aVar.aEventId = this.aEventId;
                aVar.bEventLabel = this.bEventLabel;
                aVar.gParameterMap = this.cParameterMap;
                Handler handler = cMessageHandler.bGetMessageHandler();
                handler.sendMessage(Message.obtain(handler, 3, aVar));
            }
        } catch (Throwable ignored) {
        }
    }
}
