package com.tendcloud.tenddata;

import android.app.Activity;
import android.os.Message;

final class adActivityStarter implements Runnable {
    final /* synthetic */ int aMessageWhat;
    final /* synthetic */ String bMessageObject;
    final /* synthetic */ Activity cActivity;

    adActivityStarter(int i, String str, Activity activity) {
        this.aMessageWhat = i;
        this.bMessageObject = str;
        this.cActivity = activity;
    }

    public void run() {
        try {
            if (!TCAgent.g()) {
                TCAgent.initReadManifestTags(this.cActivity);
            }
            if (TCAgent.r() != null) {
                cMessageHandler.bGetMessageHandler().sendMessage(Message.obtain(cMessageHandler.bGetMessageHandler(), this.aMessageWhat, this.bMessageObject));
            }
        } catch (Throwable ignored) {
        }
    }
}
