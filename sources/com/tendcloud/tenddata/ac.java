package com.tendcloud.tenddata;

final class ac implements Runnable {
    ac() {
    }

    public void run() {
        TCAgent.c(TCAgent.fGetActivityId());
        if (TCAgent.jGetTimePreference() == 0) {
            TCAgent.cSetTimePreference(System.currentTimeMillis());
        }

        if (TCAgent.bIsDayUpdated()) {
            TCAgent.fAllApplicationsHashNames = uDeviceUtils.uGetAllApplicationsHashNames();
        }
        TCAgent.bSetSessionStartTime(0);
        TCAgent.aSendDelayedMessage(7, 1000);
    }
}
