package com.tendcloud.tenddata;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class cMessageHandler implements Runnable {
    private static final int a = 1;
    private static final int b = 20480;
    private static final int c = 10000;
    private static final HandlerThread dHandlerThread = new HandlerThread("ProcessingThread");
    private static cMessageHandler eInstance = null;
    private static Handler fMessageHandler = null;
    private int gPostErrors = 0;
    private boolean h = false;
    private long iLastDataPostTime;
    private int j = 0;

    static {
        dHandlerThread.start();
    }

    private cMessageHandler() {
    }

    static synchronized cMessageHandler a() {
        cMessageHandler cVar;
        synchronized (cMessageHandler.class) {
            if (eInstance == null) {
                eRunAsynchronously();
            }
            cVar = eInstance;
        }
        return cVar;
    }

    private void aClearData(ahDeviceAndPackageInfo ahDeviceAndPackageInfo) {
        Log.d("TalkingData", "Send Success, Clear Data");
        qDatabaseManager.aCreateDatabase(TCAgent.cGetContext());
        List<agMessageContainer> list = ahDeviceAndPackageInfo.eMessages;

        qDatabaseManager.bDeleteActivitiesWhereIdIsAtMost(ahDeviceAndPackageInfo.fMaxActivityId);
        qDatabaseManager.cDeleteAppEventsWhereIdIsAtMost(ahDeviceAndPackageInfo.gMaxAppEventId);
        qDatabaseManager.dDeleteErrorReportWhereIdIsAtMost(ahDeviceAndPackageInfo.hMadErrorReportId);

        for (agMessageContainer agVar : list) {
            switch (agVar.aDeviceInformationsOrSmthOrErrorMessage) {
                case 1:
                    TCAgent.b(false);
                    break;
                case 2:
                    bDataClass bVar = agVar.b;
                    if (bVar.cInt != 1) {
                        if (bVar.cInt == 3) {
                            qDatabaseManager.bDeleteSessionOfId(bVar.aSessionActivityEventId);
                            qDatabaseManager.cDeleteActivityOfId(bVar.aSessionActivityEventId);
                            qDatabaseManager.dDeleteAppEventOfId(bVar.aSessionActivityEventId);
                        }
                    } else {
                        qDatabaseManager.aSetSessionLaunched(bVar.aSessionActivityEventId);
                    }
                    break;
            }
        }

        qDatabaseManager.bCloseDatabase();
        if (ahDeviceAndPackageInfo.iAllApplicationsHashNames != null) {
            TCAgent.a();
        }
    }

    static synchronized Handler bGetMessageHandler() {
        Handler handler;
        synchronized (cMessageHandler.class) {
            if (fMessageHandler == null) {
                eRunAsynchronously();
            }

            handler = fMessageHandler;
        }

        return handler;
    }

    private static void eRunAsynchronously() {
        if (eInstance == null) {
            eInstance = new cMessageHandler();
            new Thread(eInstance).start();
        }

        if (fMessageHandler == null) {
            fMessageHandler = new gMessageHandler(dHandlerThread.getLooper());
        }
    }

    private synchronized boolean fPostDeviceAndPackageInfo() throws IOException {
        boolean isPostOk = false;
        synchronized (this) {
            if (TCAgent.cGetContext() != null && SystemClock.elapsedRealtime() - this.iLastDataPostTime >= 10000) {
                ahDeviceAndPackageInfo deviceAndPackageInfo = g();
                if (deviceAndPackageInfo == null) {
                    isPostOk = true;
                } else if (this.gPostErrors > 1) {
                    this.h = false;
                    bGetMessageHandler().hasMessages(5);
                    bGetMessageHandler().removeMessages(5);
                    TCAgent.aSendDelayedMessage(5, 300000);
                    this.gPostErrors = 0;
                } else {
                    Log.d("TalkingData", "Post data to server...");
                    isPostOk = oHttpPostService.aPostArchived(deviceAndPackageInfo);
                    Log.d("TalkingData", "server return success:" + isPostOk);
                    if (isPostOk) {
                        this.gPostErrors = 0;
                        aClearData(deviceAndPackageInfo);
                        TCAgent.fSetLastDataClearTime(System.currentTimeMillis());
                    } else {
                        this.gPostErrors++;
                        this.h = true;
                    }
                    
                    this.iLastDataPostTime = SystemClock.elapsedRealtime();
                }
            }
        }

        return isPostOk;
    }

    private ahDeviceAndPackageInfo g() throws IOException {
        int length;
        boolean z;
        ahDeviceAndPackageInfo ahDeviceAndPackageInfo = new ahDeviceAndPackageInfo();
        ahDeviceAndPackageInfo.aDeviceId = uDeviceUtils.eGetDeviceId();
        ahDeviceAndPackageInfo.b = TCAgent.d();
        ahDeviceAndPackageInfo.cPackageInformations = TCAgent.nBuildPackageInformations();
        ahDeviceAndPackageInfo.dNetworkAndLocalistaionInfo = TCAgent.oBuildNetworkAndLocalistaionInfo();
        ahDeviceAndPackageInfo.iAllApplicationsHashNames = TCAgent.fAllApplicationsHashNames;
        length = ahDeviceAndPackageInfo.aCalculateLength() + 3;
        if (TCAgent.e()) {
            agMessageContainer agVar = new agMessageContainer();
            agVar.aDeviceInformationsOrSmthOrErrorMessage = 1;
            agVar.cDeviceInformations = uDeviceUtils.EbuildDeviceInformations();
            ahDeviceAndPackageInfo.eMessages.add(agVar);
            length += agVar.cDeviceInformations.aCalculateLength() + pSerializationUtils.cTypeLength(agVar.aDeviceInformationsOrSmthOrErrorMessage);
            z = true;
        } else {
            z = false;
        }

        qDatabaseManager.aCreateDatabase(TCAgent.cGetContext());
        ahDeviceAndPackageInfo.hMadErrorReportId = qDatabaseManager.e(rErrorReportTable.eERROR_REPORT);
        List<bDataClass> d2 = qDatabaseManager.d();
        ArrayList<bDataClass> arrayList = new ArrayList<>();
        Iterator it = d2.iterator();
        int i3 = length;
        int i4 = 0;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            bDataClass bVar = (bDataClass) it.next();
            i4++;
            bVar.hActivityList = qDatabaseManager.a(bVar.aSessionActivityEventId, ahDeviceAndPackageInfo.fMaxActivityId);
            bVar.iEventList = qDatabaseManager.b(bVar.aSessionActivityEventId, ahDeviceAndPackageInfo.gMaxAppEventId);
            agMessageContainer agVar2 = new agMessageContainer();
            agVar2.aDeviceInformationsOrSmthOrErrorMessage = 2;
            agVar2.b = bVar;
            int a3 = bVar.aCalculateLength();
            if (a3 + i3 > b && i4 != 1) {
                this.h = true;
                break;
            }
            i3 += a3;
            arrayList.add(bVar);
            if (bVar.cInt != 2 || bVar.hActivityList.size() != 0 || bVar.iEventList.size() != 0) {
                ahDeviceAndPackageInfo.eMessages.add(agVar2);
            }
        }

        ahDeviceAndPackageInfo.fMaxActivityId = qDatabaseManager.a(arrayList);
        ahDeviceAndPackageInfo.gMaxAppEventId = qDatabaseManager.b(arrayList);
        if (ahDeviceAndPackageInfo.hMadErrorReportId > 0) {
            ahDeviceAndPackageInfo.eMessages.addAll(qDatabaseManager.eGetAllErrorMessages(ahDeviceAndPackageInfo.hMadErrorReportId));
        }

        qDatabaseManager.bCloseDatabase();

        if (z || ahDeviceAndPackageInfo.eMessages.size() != 0) {
            return ahDeviceAndPackageInfo;
        }

        return null;
    }

    /* access modifiers changed from: package-private */
    public synchronized void cCheckWifiConnection() {
        if (!uDeviceUtils.aIsInternetConnected()) {
            Log.w("TalkingData", "network is disabled.");
        } else if (TCAgent.jConnectionInfo == 0 && TCAgent.gWifiConnected && !uDeviceUtils.bIsWifiConnected()) {
            Log.w("TalkingData", "wifi is not connected.");
        } else if (this.j <= 1) {
            this.j++;
            notify();
        }
    }

    public boolean d() {
        if (TCAgent.cGetContext() == null) {
            return false;
        }

        qDatabaseManager.aCreateDatabase(TCAgent.cGetContext());
        List<bDataClass> d2 = qDatabaseManager.d();
        qDatabaseManager.bCloseDatabase();
        return d2.size() > 0;
    }

    public void run() {
        try {
            synchronized (this) {
                while (true) {
                    if (this.j == 0 || !uDeviceUtils.aIsInternetConnected()) {
                        try {
                            wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                    try {
                        this.j--;
                        if (fPostDeviceAndPackageInfo()) {
                        }
                        if (this.h) {
                            TCAgent.aSendDelayedMessage(5, 10000);
                            this.h = false;
                        }
                    } catch (IOException e3) {
                    }
                }
            }
        } catch (Throwable th) {
        }
    }
}
