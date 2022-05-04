package com.tendcloud.tenddata;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public final class TCAgent {
    private static final int A;
    private static final int B;
    private static long C = 0;
    private static final int D;
    private static final int E;
    private static final int F;
    private static final int G;
    private static volatile boolean HisInitialized = i;
    private static String ITD_CHANNEL_IDvalue = TAG;
    private static boolean J = i;
    private static Context context;
    private static String LTD_APP_IDvalue;
    private static String MSessionActivityEventId;
    private static long NInsertedActivityId;
    private static boolean O = i;
    private static int P = 0;
    private static boolean QAreLifecycleCallbacksRegistered = i;
    protected static final int a;
    protected static final int b;
    protected static final int c;
    protected static final int d;
    protected static final int e;
    static Long[][] fAllApplicationsHashNames;
    static boolean gWifiConnected;
    static final String TAG;
    static final boolean i;
    static int jConnectionInfo = 0;
    private static final String k;
    private static final String l;
    private static final String mActivityIdPreferenceKey;
    private static final String nActivityNameKey;
    private static final String oActivityStartTimePreferenceKey;
    private static final String pSessionStartTimeKey;
    private static final String q;
    private static final String rActivityStartTimeKey;
    private static final String sLastActivityDurationUpdateKey;
    private static final String tLastDataClearTimeKey;
    private static final String uCurrentDayPreferenceKey;
    private static final String vManifestTD_APP_ID;
    private static final String wManifestTD_CHANNEL_ID;
    private static final long x;
    private static final int y;
    private static final int z;
    private final static int MILLISECONDS_PER_DAY = 86400000;

    static {
        pSetDefaultUncaughtExceptionHandler();
    }

    private static String aGetValueOfKeyFromBundle(Bundle bundle, String str) {
        for (String str2 : bundle.keySet()) {
            if (str2.equalsIgnoreCase(str)) {
                return String.valueOf(bundle.get(str));
            }
        }
        return "";
    }

    static void a() {
        uSetCurrentDayPreference();
        fAllApplicationsHashNames = null;
    }

    public static void a(int i2) {
        jConnectionInfo = i2;
    }

    public static void aSendDelayedMessage(int code, long delay) {
        Message obtain = Message.obtain(cMessageHandler.bGetMessageHandler(), code);
        Handler b2 = cMessageHandler.bGetMessageHandler();
        b2.removeMessages(code);
        b2.sendMessageDelayed(obtain, delay);
    }

    static void aSetActivityStartTime(long j2) {
        aSetPreference(oActivityStartTimePreferenceKey, j2);
    }

    private static void aStartActivity(Activity activity, String messageObject, int messageWhat) {
        kAsyncRunner.aRun(new adActivityStarter(messageWhat, messageObject, activity));
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void aHandleMessage(android.os.Message r8) {
        /*
        // Method dump skipped, instructions count: 186
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tendcloud.tenddata.TCAgent.a(android.os.Message):void");
    }

    static void aTryToClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable ignored) {
            }
        }
    }

    static void aSetSessionId(String str) {
        aSetPreference(mActivityIdPreferenceKey, str);
    }

    private static void aSetPreference(String key, long value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putLong(key, value).commit();
    }

    private static void aSetPreference(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(key, value).commit();
    }

    private static void aAddCause(StringBuilder sb, StackTraceElement[] stackTraceElementArr, Throwable th, int i2) {
        int i3 = 50;
        StackTraceElement[] stackTrace = th.getStackTrace();
        int length = stackTrace.length - 1;
        int length2 = stackTraceElementArr.length - 1;
        while (length >= 0 && length2 >= 0 && stackTrace[length].equals(stackTraceElementArr[length2])) {
            length2--;
            length--;
        }
        if (length <= 50) {
            i3 = length;
        }
        sb.append("Caused by : ").append(th).append("\r\n");
        for (int i4 = 0; i4 <= i3; i4++) {
            sb.append("\t").append(stackTrace[i4]).append("\r\n");
        }
        if (i2 < 5 && th.getCause() != null) {
            aAddCause(sb, stackTrace, th, i2 + 1);
        }
    }

    public static void a(boolean z2) {
        gWifiConnected = z2;
    }

    private static long bGetPreference(String key, long defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, defaultValue);
    }

    private static String bGetPreference(String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    static void bSetSessionStartTime(long j2) {
        aSetPreference(pSessionStartTimeKey, j2);
    }

    static void bSetActivityName(String str) {
        aSetPreference(nActivityNameKey, str);
    }

    public static void bSendMessageAboutThrowable(Throwable th) {
        if (HisInitialized) {
            aAppEventEntity aVar = new aAppEventEntity();
            aVar.cTime = System.currentTimeMillis();
            aVar.dMessage = cBuildErrorMessage(th);
            while (th.getCause() != null) {
                th = th.getCause();
            }
            StackTraceElement[] stackTrace = th.getStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append(th.getClass().getName()).append(":");
            int i2 = 0;
            while (i2 < 3 && i2 < stackTrace.length) {
                sb.append(stackTrace[i2].toString()).append(":");
                i2++;
            }
            aVar.eStackTrace = uDeviceUtils.bGetMd5DigestedBytes(sb.toString());
            cMessageHandler.bGetMessageHandler().sendMessage(Message.obtain(cMessageHandler.bGetMessageHandler(), 4, aVar));
        }
    }

    static void b(boolean z2) {
        aSetPreference(l, z2 ? 1 : 0);
    }

    public static boolean bIsDayUpdated() {
        if (System.currentTimeMillis() / MILLISECONDS_PER_DAY != bGetPreference(uCurrentDayPreferenceKey, 0)) {
            return true;
        }

        return i;
    }

    private static boolean bRegisterActivity(Message message) {
        boolean z2;
        String activityName = (String) message.obj;

        long elapsedRealtime = SystemClock.elapsedRealtime();
        long startTime = System.currentTimeMillis();

        long h2 = hGetActivityStartTime();
        long l2 = lGetLastActivityDuration();
        long j2 = Math.max(l2, h2);
        long j3 = j2 - h2;

        if (j3 < 500) {
            j3 = -1000;
        }

        String refer = gGetActivityName();
        if (startTime - j2 > 30000) {
            if (!TextUtils.isEmpty(MSessionActivityEventId)) {
                qDatabaseManager.a(MSessionActivityEventId, ((int) j3) / y);
            }

            MSessionActivityEventId = UUID.randomUUID().toString();
            aSetSessionId(MSessionActivityEventId);
            aSetActivityStartTime(startTime);
            long sessionStartTime = iGetSessionStartTime();
            long interval = startTime - sessionStartTime;
            if (0 == sessionStartTime) {
                interval = 0;
            }
            bSetSessionStartTime(startTime);
            qDatabaseManager.aInsertSession(MSessionActivityEventId, startTime, interval, uDeviceUtils.aIsInternetConnected() ? 1 : -1);
            refer = "";
            z2 = true;
        } else {
            z2 = false;
        }
        dSetActivityStartTime(startTime);
        bSetActivityName(activityName);
        NInsertedActivityId = qDatabaseManager.aInsertActivity(MSessionActivityEventId, activityName, startTime, 0, refer, elapsedRealtime);
        return z2;
    }

    protected static Context cGetContext() {
        return context;
    }

    private static String cBuildErrorMessage(Throwable th) {
        int i2 = 50;

        StringBuilder sb = new StringBuilder();
        sb.append(th.toString());
        sb.append("\r\n");

        StackTraceElement[] stackTrace = th.getStackTrace();

        if (stackTrace.length <= 50) {
            i2 = stackTrace.length;
        }

        for (int i3 = 0; i3 < i2; i3++) {
            sb.append("\t" + stackTrace[i3] + "\r\n");
        }

        Throwable cause = th.getCause();
        if (cause != null) {
            aAddCause(sb, stackTrace, cause, 1);
        }

        return sb.toString();
    }

    static void cSetTimePreference(long j2) {
        aSetPreference(q, j2);
    }

    private static void cUpdateActivityDuration(Message message) {
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (NInsertedActivityId != -1) {
            qDatabaseManager.aUpdateActivityDuration(NInsertedActivityId, elapsedRealtime);
        }
        eLastActivityDurationUpdate(currentTimeMillis);
    }

    public static String d() {
        return LTD_APP_IDvalue;
    }

    static void dSetActivityStartTime(long j2) {
        aSetPreference(rActivityStartTimeKey, j2);
    }

    private static void dInsertAppEvent(Message message) throws Throwable {
        aAppEventEntity aVar = (aAppEventEntity) message.obj;
        qDatabaseManager.aInsertAppEvent(MSessionActivityEventId, aVar.aEventId, aVar.bEventLabel, aVar.fOccurrenceTime, aVar.gParameterMap);
    }

    static void eLastActivityDurationUpdate(long j2) {
        aSetPreference(sLastActivityDurationUpdateKey, j2);
    }

    static boolean e() {
        if (bGetPreference(l, 1) != 0) {
            return true;
        }
        return i;
    }

    static String fGetActivityId() {
        return bGetPreference(mActivityIdPreferenceKey, (String) null);
    }

    static void fSetLastDataClearTime(long j2) {
        aSetPreference(tLastDataClearTimeKey, j2);
    }

    static String gGetActivityName() {
        return bGetPreference(nActivityNameKey, "");
    }

    public static synchronized String getDeviceId(Context context) {
        String deviceId;
        synchronized (TCAgent.class) {
            uDeviceUtils.aContext = context.getApplicationContext();
            deviceId = uDeviceUtils.eGetDeviceId();
        }

        return deviceId;
    }

    static long hGetActivityStartTime() {
        return bGetPreference(oActivityStartTimePreferenceKey, 0);
    }

    static long iGetSessionStartTime() {
        return bGetPreference(pSessionStartTimeKey, 0);
    }

    public static void initReadManifestTags(Context context) {
        TCAgent.context = context.getApplicationContext();
        try {
            Bundle bundle = TCAgent.context.getPackageManager().getApplicationInfo(TCAgent.context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            String TD_APP_ID = aGetValueOfKeyFromBundle(bundle, vManifestTD_APP_ID);
            String TD_CHANNEL_ID = aGetValueOfKeyFromBundle(bundle, wManifestTD_CHANNEL_ID);
            if (TextUtils.isEmpty(TD_APP_ID)) {
                Log.e(TAG, "TD_APP_ID not found in AndroidManifest.xml!");
                return;
            }
            Log.i(TAG, "TD_APP_ID in AndroidManifest.xml is:" + TD_APP_ID + ".");
            Log.i(TAG, "TD_CHANNEL_ID in AndroidManifest.xml is:" + TD_CHANNEL_ID + ".");
            if (TD_CHANNEL_ID == null) {
                TD_CHANNEL_ID = TAG;
            }
            init(context, TD_APP_ID, TD_CHANNEL_ID);
        } catch (Throwable th) {
            Log.e(TAG, "Failed to load meta-data", th);
        }
    }

    public static synchronized void init(Context context, String TD_APP_ID, String TD_CHANNEL_ID) {
        synchronized (TCAgent.class) {
            if (!HisInitialized) {
                TCAgent.context = context.getApplicationContext();
                uDeviceUtils.aContext = TCAgent.context;
                if (!uDeviceUtils.aPermissionCheck("android.permission.INTERNET")) {
                    Log.e(TAG, "stop working...application do not have permission to send data, you must add <uses-permission android:name=\"android.permission.INTERNET\"/> to your AndroidManifest.xml.");
                } else {
                    LTD_APP_IDvalue = TD_APP_ID;
                    ITD_CHANNEL_IDvalue = TD_CHANNEL_ID;
                    if (Build.VERSION.SDK_INT >= 14 && (context instanceof Activity)) {
                        ((Activity) context).getApplication().registerActivityLifecycleCallbacks(new aeActivityLifecycleCallbacks());
                        QAreLifecycleCallbacksRegistered = true;
                    }

                    kAsyncRunner.aRun(new ac());
                }
                HisInitialized = true;
            }
        }
    }

    static long jGetTimePreference() {
        return bGetPreference(q, 0);
    }

    static long kGetActivityStartTime() {
        return bGetPreference(rActivityStartTimeKey, 0);
    }

    static long lGetLastActivityDuration() {
        return bGetPreference(sLastActivityDurationUpdateKey, 0);
    }

    static long mGetLastDataClearTime() {
        return bGetPreference(tLastDataClearTimeKey, 0);
    }

    static jPackageInformations nBuildPackageInformations() {
        if (context == null) {
            return null;
        }

        jPackageInformations jVar = new jPackageInformations();
        jVar.aPackageName = context.getPackageName();
        jVar.bPackageVersionName = uDeviceUtils.tGetPackageVersionName();
        jVar.cPackageVersionCode = uDeviceUtils.sGetPackageVersionCode();
        jVar.dTime = jGetTimePreference();
        jVar.eString = k;
        jVar.fString = ITD_CHANNEL_IDvalue;
        jVar.hPackageFirstInstallTime = uDeviceUtils.wGetPackageFirstInstallTime();
        jVar.iPackageLastUpdateTime = uDeviceUtils.xGetPackageLastUpdateTime();

        return jVar;
    }

    static yNetworkAndLocalisationInfo oBuildNetworkAndLocalistaionInfo() {
        Location location = null;
        if (context == null) {
            return null;
        }

        yNetworkAndLocalisationInfo yVar = new yNetworkAndLocalisationInfo();
        yVar.aDeviceDescription = uDeviceUtils.jGetDeviceDescription();
        yVar.bSdkVersion = uDeviceUtils.kGetVersionSDK();
        List<Location> lastKnownLocations = uDeviceUtils.rGetLastKnownLocationsAlsoRegisterAndUnregisterUpdates();
        StringBuilder locationsString = new StringBuilder();
        for (Location location2 : lastKnownLocations) {
            locationsString.append(location2.getLatitude()).append(',').append(location2.getLongitude()).append(',').append(location2.getAltitude()).append(',').append(location2.getTime()).append(',').append(location2.getAccuracy()).append(',').append(location2.getBearing()).append(',').append(location2.getSpeed()).append(',').append((short) location2.getProvider().hashCode()).append(':');
            if (location != null && location2.getTime() <= location.getTime()) {
                location2 = location;
            }
            location = location2;
        }

        fCoordinates fVar = new fCoordinates();
        if (location != null) {
            fVar.bLatitude = location.getLatitude();
            fVar.aLongitude = location.getLongitude();
        }

        yVar.cCoordinates = fVar;
        yVar.dCpuAbi = Build.CPU_ABI;
        yVar.eResolutionEquation = uDeviceUtils.CgetResolutionEquation();
        yVar.fCountry = uDeviceUtils.AgetCountry();
        yVar.gNetworkOperatorName = uDeviceUtils.pGetNetworkOperatorName();
        yVar.hLanguage = uDeviceUtils.BgetLanguage();
        yVar.i = ((TimeZone.getDefault().getRawOffset() / y) / 60) / 60;
        yVar.jAndroidVersion = "Android+" + Build.VERSION.RELEASE;
        yVar.kIsWifiConnected = uDeviceUtils.bIsWifiConnected() ? 0 : 1;
        yVar.lNetworkType = uDeviceUtils.DgetNetworkType();
        yVar.oNetworkOperator = uDeviceUtils.mGetNetworkOperator();
        yVar.nSimOperator = uDeviceUtils.nGetSimOperator();
        yVar.pLastKnownLocations = locationsString.toString();
        yVar.tWifiBssidRssiAndAccessPointsBssids = uDeviceUtils.iGetWifiBssidAndRssiAndAccessPointsBssids();
        vNetworkInfo q2NetworkInfo = uDeviceUtils.qBuildNetworkInfo();
        yVar.uGsmOrCdma = q2NetworkInfo.cGsmOrCdma;
        yVar.vCellOrStation = q2NetworkInfo.dCellOrStation;
        yVar.wLocationOrNetwork = q2NetworkInfo.eLocationOrNetwork;

        return yVar;
    }

    public static void onError(Context context, Throwable th) {
        if (th != null) {
            kAsyncRunner.aRun(new aaThrowableThread(th));
        }
    }

    public static void onEvent(Context context, String str) {
        onEvent(context, str, "");
    }

    public static void onEvent(Context context, String str, String str2) {
        onEvent(context, str, str2, null);
    }

    public static void onEvent(Context context, String str, String str2, Map map) {
        kAsyncRunner.aRun(new x(str, str2, map));
    }

    public static void onPageEnd(Activity activity, String messageObject) {
        aStartActivity(activity, messageObject, 2);
    }

    public static void onPageStart(Activity activity, String messageObject) {
        aStartActivity(activity, messageObject, 1);
    }

    public static void onPause(Activity activity) {
        if (!QAreLifecycleCallbacksRegistered) {
            onPageEnd(activity, activity.getLocalClassName());
        }
    }

    public static void onResume(Activity activity) {
        if (!QAreLifecycleCallbacksRegistered) {
            onPageStart(activity, activity.getLocalClassName());
        }
    }

    public static void pSetDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new iUncaughtExceptionHandler());
    }

    public static void setReportUncaughtExceptions(boolean z2) {
        J = z2;
    }

    private static void uSetCurrentDayPreference() {
        aSetPreference(uCurrentDayPreferenceKey, System.currentTimeMillis() / MILLISECONDS_PER_DAY);
    }
}
