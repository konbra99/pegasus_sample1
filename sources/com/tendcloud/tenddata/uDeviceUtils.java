package com.tendcloud.tenddata;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpHost;

/* access modifiers changed from: package-private */
public final class uDeviceUtils {
    public static Context aContext = null;
    static String bAllSensorsHash = null;
    private static final String cPREF_DEVICEID_KEY = "pref.deviceid.key";
    private static TelephonyManager dTelephonyManager = null;
    private static final String eUtf8 = "UTF-8";
    private static final String fZeroMacAddress = "00:00:00:00:00:00";
    private static final Pattern gIntegerHexPattern = Pattern.compile("^([0-9A-F]{2}:){5}([0-9A-F]{2})$");
    private static String hDeviceId;

    uDeviceUtils() {
    }

    static String AgetCountry() {
        return Locale.getDefault().getCountry();
    }

    static String BgetLanguage() {
        return Locale.getDefault().getLanguage();
    }

    static String CgetResolutionEquation() {
        DisplayMetrics displayMetrics = aContext.getResources().getDisplayMetrics();
        return displayMetrics != null ? displayMetrics.widthPixels + "*" + displayMetrics.heightPixels + "*" + displayMetrics.density : "";
    }

    static String DgetNetworkType() {
        String[] types = {"UNKNOWN", "GPRS", "EDGE", "UMTS", vNetworkInfo.bCDMA, "EVDO_0", "EVDO_A", "1xRTT", "HSDPA", "HSUPA", "HSPA", "IDEN", "EVDO_B", "LTE", "EHRPD", "HSPAP"};
        if (!aPermissionCheck("android.permission.READ_PHONE_STATE")) {
            return "";
        }

        JinitializeTelephonyManager();

        int networkType = dTelephonyManager.getNetworkType();
        return (networkType < 0 || networkType >= types.length) ? types[0] : types[networkType];
    }

    static mDeviceInformations EbuildDeviceInformations() throws IOException {
        mDeviceInformations mVar = new mDeviceInformations();
        String[] cpuInfo = QgetCpuInfo();
        try {
            mVar.aProcessor = cpuInfo[0];
            mVar.bCpuVariant = Integer.parseInt(cpuInfo[1]);
            mVar.dHardware = cpuInfo[2];
            mVar.cCpuMaxFreq = Float.parseFloat(cpuInfo[3]);
        } catch (Exception ignored) {
        }

        int[] totalAndAvailableMemory = FgetTotalAndAvailableMemoryInKB();
        mVar.gTotalMemory = totalAndAvailableMemory[0];
        mVar.hAvailableMemory = totalAndAvailableMemory[1];

        int[] fileSystemSizes = GgetFileSystemSizes();
        mVar.iEnvironmentTotalSize = fileSystemSizes[0];
        mVar.jEnvironmentFreeSize = fileSystemSizes[1];
        mVar.kExternalTotalSize = fileSystemSizes[2];
        mVar.lExternalFreeSize = fileSystemSizes[3];

        mVar.mBatteryCapacity = HgetBatteryCapacityChyba();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) aContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        mVar.nDisplayWidth = ((float) displayMetrics.widthPixels) / displayMetrics.xdpi;
        mVar.oDisplayHeight = ((float) displayMetrics.heightPixels) / displayMetrics.ydpi;
        mVar.pDensityDpi = displayMetrics.densityDpi;
        mVar.qDisplayId = Build.DISPLAY;
        mVar.rGsmVersionBaseband = "unknown";
        try {
            mVar.rGsmVersionBaseband = (String) Class.forName("android.os.SystemProperties").getDeclaredMethod("get", String.class).invoke(null, "gsm.version.baseband");
        } catch (Exception ignored) {
        }

        String deviceId = fGetDeviceId();
        if (deviceId != null) {
            mVar.sDeviceId = deviceId;
        }

        String h2 = hGetWifiConnectionMacAddress();
        if (h2 != null) {
            mVar.tWifiConnectionMacAddress = h2;
        }

        aSetApnColumns(mVar);
        mVar.ySubscriberId = oGetSubscriberId();
        mVar.ASimSerialNumber = lGetSimSerialNumber();
        mVar.BAndroidId = gAndroidId();

        return mVar;
    }

    static int[] FgetTotalAndAvailableMemoryInKB() {
        int[] iArr = {0, 0};
        int[] iArr2 = {0, 0, 0, 0};

        try {
            FileReader fileReader = new FileReader("/proc/meminfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader, 1024);
            for (int i2 = 0; i2 < 4; i2++) {
                try {
                    iArr2[i2] = iToInt(bufferedReader.readLine());
                } catch (IOException e2) {
                    try {
                        bufferedReader.close();
                        fileReader.close();
                    } catch (IOException ignored) {
                    }
                } catch (Throwable th) {
                    try {
                        bufferedReader.close();
                        fileReader.close();
                    } catch (IOException ignored) {
                    }
                    throw th;
                }
            }

            iArr[0] = iArr2[0]; // MemTotal
            iArr[1] = iArr2[3] + iArr2[1] + iArr2[2]; // Cached + MemFree + Buffers
            try {
                bufferedReader.close();
                fileReader.close();
            } catch (IOException ignored) {
            }
        } catch (FileNotFoundException ignored) {
        }
        return iArr;
    }

    static int[] GgetFileSystemSizes() {
        StatFs environmentFileSystem = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        int availableBlocks = environmentFileSystem.getAvailableBlocks();
        StatFs externalStorageFileSystem = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        return new int[]{
                (environmentFileSystem.getBlockCount() * (environmentFileSystem.getBlockSize() / 512)) / 2,
                ((environmentFileSystem.getBlockSize() / 512) * availableBlocks) / 2,
                (externalStorageFileSystem.getBlockCount() * (externalStorageFileSystem.getBlockSize() / 512)) / 2,
                ((externalStorageFileSystem.getBlockSize() / 512) * externalStorageFileSystem.getAvailableBlocks()) / 2
        };
    }

    static int HgetBatteryCapacityChyba() {
        Matcher matcher = Pattern.compile("\\s*([0-9]+)").matcher(jReadFile("/sys/class/power_supply/battery/full_bat"));
        if (!matcher.find()) {
            return 0;
        }
        try {
            return Integer.parseInt(matcher.toMatchResult().group(0));
        } catch (Exception e2) {
            return 0;
        }
    }

    static String IgetAllSensorsHash() {
        if (bAllSensorsHash == null) {
            Sensor[] sensorArr = new Sensor[64];
            for (Sensor sensor : ((SensorManager) aContext.getSystemService(Context.SENSOR_SERVICE)).getSensorList(-1)) {
                if (sensor.getType() < sensorArr.length && sensor.getType() >= 0) {
                    sensorArr[sensor.getType()] = sensor;
                }
            }

            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i < sensorArr.length; i++) {
                if (sensorArr[i] != null) {
                    // (i.vendor-name-ver\n)*
                    stringBuffer
                            .append(i)
                            .append('.').append(sensorArr[i].getVendor())
                            .append('-').append(sensorArr[i].getName())
                            .append('-').append(sensorArr[i].getVersion()).append('\n');
                }
            }

            bAllSensorsHash = String.valueOf(stringBuffer.toString().hashCode());
        }

        return bAllSensorsHash;
    }

    private static synchronized void JinitializeTelephonyManager() {
        synchronized (uDeviceUtils.class) {
            if (dTelephonyManager == null) {
                dTelephonyManager = (TelephonyManager) aContext.getSystemService(Context.TELEPHONY_SERVICE);
            }
        }
    }

    private static String KgetExistingDeviceId() {
        String str;
        String preferredDeviceId = OgetPreferredDeviceId();
        String tmpTid = MreadTmpTid();
        String varTid = NreadVarTid();
        String LexternalTid = LreadExternalTid();
        String[] ids = {preferredDeviceId, tmpTid, varTid, LexternalTid};

        int length = ids.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                str = null;
                break;
            }

            str = ids[i];
            if (!cIsBlank(str)) {
                break;
            }

            i++;
        }

        if (cIsBlank(str)) {
            str = PgetMd5DigestedId();
        }

        if (cIsBlank(preferredDeviceId)) {
            hSetPreferredDeviceId(str);
        }

        if (cIsBlank(tmpTid)) {
            fCreateTmpTidFile(str);
        }

        if (cIsBlank(varTid)) {
            gCreateVarTidFile(str);
        }

        if (cIsBlank(LexternalTid)) {
            eCreateExternalStorageTidFile(str);
        }

        return str;
    }

    private static String LreadExternalTid() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return dReadUpTo128Bytes(Environment.getExternalStorageDirectory() + "/.tid" + IgetAllSensorsHash());
        }

        return null;
    }

    private static String MreadTmpTid() {
        return dReadUpTo128Bytes("/tmp/.tid");
    }

    private static String NreadVarTid() {
        return dReadUpTo128Bytes("/var/.tid");
    }

    private static String OgetPreferredDeviceId() {
        return PreferenceManager.getDefaultSharedPreferences(aContext).getString(cPREF_DEVICEID_KEY, null);
    }

    private static String PgetMd5DigestedId() {
        String idString = hGetWifiConnectionMacAddress();
        String type = "1";
        if (cIsBlank(idString)) {
            idString = fGetDeviceId();
            type = "0";
        }
        if (cIsBlank(idString)) {
            idString = UUID.randomUUID().toString();
            type = "2";
        }
        return type + bGetMd5DigestedBytes(idString);
    }

    private static String[] QgetCpuInfo() throws IOException {
        boolean z;
        String[] strArr = new String[4];
        for (int i = 0; i < 4; i++) {
            strArr[i] = "";
        }

        ArrayList<String> cpuInfo = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader, 1024);
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        cpuInfo.add(readLine);
                    } else {
                        break;
                    }
                } catch (IOException e4) {
                    try {
                        bufferedReader.close();
                        fileReader.close();
                    } catch (IOException ignored) {
                    }
                } catch (Throwable th) {
                    try {
                        bufferedReader.close();
                        fileReader.close();
                    } catch (IOException ignored) {
                    }
                    throw th;
                }
            }

            bufferedReader.close();
            fileReader.close();
            z = true;
        } catch (FileNotFoundException e7) {
            z = false;
        }

        String[] patterns = {"Processor\\s*:\\s*(.*)", "CPU\\s*variant\\s*:\\s*0x(.*)", "Hardware\\s*:\\s*(.*)"};
        if (z) {
            int size = cpuInfo.size();
            for (int pattern = 0; pattern < 3; pattern++) {
                Pattern compile = Pattern.compile(patterns[pattern]);

                for (int line = 0; line < size; line++) {
                    Matcher matcher = compile.matcher(cpuInfo.get(line));
                    if (matcher.find()) {
                        strArr[pattern] = matcher.toMatchResult().group(1);
                    }
                }
            }
        }

        strArr[3] = jReadFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
        return strArr;
    }

    private static String aGetColumnIfExists(Cursor cursor, String str) {
        return cursor.getString(cursor.getColumnIndex(str));
    }

    static String aCheckForSpecificVendors(String str, String str2) {
        String lowerCase = str.toLowerCase();
        if (lowerCase.startsWith("unknown") || lowerCase.startsWith("alps") || lowerCase.startsWith("android") || lowerCase.startsWith("sprd") || lowerCase.startsWith("spreadtrum") || lowerCase.startsWith("rockchip") || lowerCase.startsWith("wondermedia") || lowerCase.startsWith("mtk") || lowerCase.startsWith("mt65") || lowerCase.startsWith("nvidia") || lowerCase.startsWith("brcm") || lowerCase.startsWith("marvell") || str2.toLowerCase().contains(lowerCase)) {
            return null;
        }

        return str;
    }

    static void aSetApnColumns(mDeviceInformations mVar) {
        Cursor query;
        Uri parse = Uri.parse("content://telephony/carriers/preferapn");
        if (aContext.checkCallingOrSelfUriPermission(parse, Intent.FLAG_GRANT_READ_URI_PERMISSION) == PackageManager.PERMISSION_DENIED
                && (query = aContext.getContentResolver().query(parse, null, null, null, null)) != null
                && query.moveToFirst()) {
            try {
                mVar.uApn = aGetColumnIfExists(query, "apn");
                mVar.vMcc = aGetColumnIfExists(query, "mcc");
                mVar.wMnc = aGetColumnIfExists(query, "mnc");
                String proxy = aGetColumnIfExists(query, "proxy");

                if (proxy != null && !proxy.trim().equals("")) {
                    mVar.xProxyExists = true;
                }
            } finally {
                query.close();
            }
        }
    }

    public static boolean aIsInternetConnected() {
        if (!aPermissionCheck("android.permission.INTERNET")) {
            return false;
        }
        if (aPermissionCheck("android.permission.ACCESS_NETWORK_STATE")) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        try {
            Socket socket2 = new Socket(" .               ", 80);
            try {
                socket2.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return true;
        } catch (Exception e3) {
            return false;
        }
    }

    static boolean aPermissionCheck(String permission) {
        return aContext.checkCallingOrSelfPermission(permission) == 0;
    }

    static String bGetMd5DigestedBytes(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes(eUtf8));
            StringBuilder sb = new StringBuilder();
            for (byte b2 : digest) {
                sb.append(Integer.toHexString(b2 & 255));
            }
            return sb.toString();
        } catch (Exception e2) {
            return null;
        }
    }

    private static void bCreateReadableFile(String filepath, String source) {
        try {
            File file = new File(filepath);
            if (!file.exists() || file.length() != ((long) source.length())) {
                file.getParentFile().mkdirs();
                if (Build.VERSION.SDK_INT >= 9) {
                    abUtilities.aSetFileReadable(file);
                } else {
                    Runtime.getRuntime().exec("chmod 444 " + file.getAbsolutePath());
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(source.getBytes());
                fileOutputStream.close();
            }
        } catch (Exception ignored) {
        }
    }

    public static boolean bIsWifiConnected() {
        if (!aPermissionCheck("android.permission.ACCESS_NETWORK_STATE")) {
            return false;
        }

        NetworkInfo activeNetworkInfo = ((ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && 1 == activeNetworkInfo.getType() && activeNetworkInfo.isConnected();
    }

    public static boolean cDoesProxyExist() {
        return !cIsBlank(Proxy.getDefaultHost());
    }

    static boolean cIsBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    private static String dReadUpTo128Bytes(String file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] bArr = new byte[128];
            int read = fileInputStream.read(bArr);
            fileInputStream.close();
            return new String(bArr, 0, read);
        } catch (Exception e2) {
            return null;
        }
    }

    public static HttpHost dGetDefaultHttpHost() {
        return new HttpHost(Proxy.getDefaultHost(), Proxy.getDefaultPort());
    }

    public static synchronized String eGetDeviceId() {
        String str;
        synchronized (uDeviceUtils.class) {
            if (hDeviceId == null) {
                hDeviceId = KgetExistingDeviceId();
            }

            str = hDeviceId;
        }

        return str;
    }

    private static void eCreateExternalStorageTidFile(String str) {
        bCreateReadableFile(Environment.getExternalStorageDirectory() + "/.tid" + IgetAllSensorsHash(), str);
    }

    public static String fGetDeviceId() {
        if (!aPermissionCheck("android.permission.READ_PHONE_STATE")) {
            return "";
        }

        JinitializeTelephonyManager();
        return dTelephonyManager.getDeviceId();
    }

    private static void fCreateTmpTidFile(String str) {
        bCreateReadableFile("/tmp/.tid", str);
    }

    public static String gAndroidId() {
        return "android_id";
    }

    private static void gCreateVarTidFile(String str) {
        bCreateReadableFile("/var/.tid", str);
    }

    public static String hGetWifiConnectionMacAddress() {
        WifiInfo connectionInfo;
        if (aPermissionCheck("android.permission.ACCESS_WIFI_STATE")) {
            WifiManager wifiManager = (WifiManager) aContext.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled() && (connectionInfo = wifiManager.getConnectionInfo()) != null) {
                String trim = connectionInfo.getMacAddress().toUpperCase().trim();
                return (fZeroMacAddress.equals(trim) || !gIntegerHexPattern.matcher(trim).matches()) ? "" : trim;
            }
        }
        return "";
    }

    private static void hSetPreferredDeviceId(String str) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(aContext).edit();
        edit.putString(cPREF_DEVICEID_KEY, str);
        edit.commit();
    }

    private static int iToInt(String str) {
        String str2 = "";
        try {
            Matcher matcher = Pattern.compile("([0-9]+)").matcher(str);
            if (matcher.find()) {
                str2 = matcher.toMatchResult().group(0);
            }

            return Integer.parseInt(str2);
        } catch (Exception e2) {
            return 0;
        }
    }

    public static String iGetWifiBssidAndRssiAndAccessPointsBssids() {
        StringBuilder sb = new StringBuilder();

        if (aPermissionCheck("android.permission.ACCESS_WIFI_STATE")) {
            WifiManager wifiManager = (WifiManager) aContext.getSystemService(Context.WIFI_SERVICE);

            if (wifiManager.isWifiEnabled()) {
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                String bssId = null;

                if (!(connectionInfo == null || connectionInfo.getBSSID() == null)) {
                    bssId = connectionInfo.getBSSID();
                    sb.append(bssId).append('/').append(connectionInfo.getRssi()).append(";;");
                }

                List<ScanResult> scanResults = wifiManager.getScanResults();
                if (scanResults != null) {
                    for (ScanResult scanResult : scanResults) {
                        if (scanResult.BSSID != null && !scanResult.BSSID.equals(bssId)) {
                            sb.append(scanResult.BSSID).append('/').append(scanResult.level).append(';');
                        }
                    }
                }
            }
        }

        return sb.toString();
    }

    static String jGetDeviceDescription() {
        String model = Build.MODEL.trim();
        String blankIfSpecific = aCheckForSpecificVendors(Build.MANUFACTURER.trim(), model);

        if (cIsBlank(blankIfSpecific)) {
            blankIfSpecific = aCheckForSpecificVendors(Build.BRAND.trim(), model);
        }

        StringBuilder sb = new StringBuilder();
        if (blankIfSpecific == null) {
            blankIfSpecific = "";
        }

        return sb.append(blankIfSpecific).append(":").append(model).toString();
    }

    private static String jReadFile(String file) {
        StringBuilder str2 = new StringBuilder();

        try {
            FileReader fileReader = new FileReader(file);
            try {
                char[] cArr = new char[1024];
                BufferedReader bufferedReader = new BufferedReader(fileReader, 1024);
                while (true) {
                    int read = bufferedReader.read(cArr, 0, 1024);
                    if (-1 == read) {
                        break;
                    }
                    str2.append(new String(cArr, 0, read));
                }

                bufferedReader.close();
                fileReader.close();
            } catch (IOException ignored) {
            }
        } catch (FileNotFoundException ignored) {
        }

        return str2.toString();
    }

    private static long kHash(String str) {
        long j = 1125899906842597L;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            j = (j * 131) + ((long) str.charAt(i));
        }
        return j;
    }

    static String kGetVersionSDK() {
        return Build.VERSION.SDK;
    }

    static String lGetSimSerialNumber() {
        if (!aPermissionCheck("android.permission.READ_PHONE_STATE")) {
            return "";
        }

        JinitializeTelephonyManager();
        return dTelephonyManager.getSimSerialNumber();
    }

    static String mGetNetworkOperator() {
        if (!aPermissionCheck("android.permission.READ_PHONE_STATE")) {
            return "";
        }

        JinitializeTelephonyManager();
        return dTelephonyManager.getNetworkOperator();
    }

    static String nGetSimOperator() {
        if (!aPermissionCheck("android.permission.READ_PHONE_STATE")) {
            return "";
        }

        JinitializeTelephonyManager();
        return dTelephonyManager.getSimOperator();
    }

    static String oGetSubscriberId() {
        if (!aPermissionCheck("android.permission.READ_PHONE_STATE")) {
            return "";
        }

        JinitializeTelephonyManager();
        return dTelephonyManager.getSubscriberId();
    }

    static String pGetNetworkOperatorName() {
        if (!aPermissionCheck("android.permission.READ_PHONE_STATE")) {
            return "";
        }

        JinitializeTelephonyManager();
        return dTelephonyManager.getNetworkOperatorName();
    }

    public static vNetworkInfo qBuildNetworkInfo() {
        CdmaCellLocation cdmaCellLocation;
        vNetworkInfo vVar = new vNetworkInfo();
        if (aPermissionCheck("android.permission.ACCESS_COARSE_LOCATION") || aPermissionCheck("android.permission.ACCESS_FINE_LOCATION")) {
            try {
                CellLocation cellLocation = ((TelephonyManager) aContext.getSystemService(Context.TELEPHONY_SERVICE)).getCellLocation();
                if (cellLocation instanceof GsmCellLocation) {
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                    if (gsmCellLocation != null) {
                        vVar.dCellOrStation = gsmCellLocation.getCid();
                        vVar.eLocationOrNetwork = gsmCellLocation.getLac();
                        vVar.cGsmOrCdma = "gsm";
                        if (Build.VERSION.SDK_INT >= 9) {
                            vVar.cGsmOrCdma += abUtilities.aGetPrimaryScramblingCode(gsmCellLocation);
                        }
                    }
                } else if ((cellLocation instanceof CdmaCellLocation) && (cdmaCellLocation = (CdmaCellLocation) cellLocation) != null) {
                    vVar.dCellOrStation = cdmaCellLocation.getBaseStationId();
                    vVar.eLocationOrNetwork = cdmaCellLocation.getNetworkId();
                    vVar.cGsmOrCdma = "cdma:" + cdmaCellLocation.getSystemId() + ':' + cdmaCellLocation.getBaseStationLatitude() + ':' + cdmaCellLocation.getBaseStationLongitude();
                }
            } catch (Exception e2) {
            }
        }
        return vVar;
    }

    static List<Location> rGetLastKnownLocationsAlsoRegisterAndUnregisterUpdates() {
        ArrayList<Location> arrayList = new ArrayList<>();
        try {
            if (aPermissionCheck("android.permission.ACCESS_FINE_LOCATION") || aPermissionCheck("android.permission.ACCESS_COARSE_LOCATION")) {
                LocationManager locationManager = (LocationManager) aContext.getSystemService(Context.LOCATION_SERVICE);
                for (String enabledProvider : locationManager.getProviders(true)) {
                    Location lastKnownLocation = locationManager.getLastKnownLocation(enabledProvider);
                    if (lastKnownLocation != null) {
                        arrayList.add(lastKnownLocation);
                    }
                    try {
                        PendingIntent broadcast = PendingIntent.getBroadcast(aContext, 0, new Intent(), 0);
                        locationManager.requestLocationUpdates(enabledProvider, 300000, 0.0f, broadcast);
                        locationManager.removeUpdates(broadcast);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return arrayList;
    }

    static String sGetPackageVersionCode() {
        try {
            return String.valueOf(aContext.getPackageManager().getPackageInfo(aContext.getPackageName(), 0).versionCode);
        } catch (Exception e2) {
            return "unknown";
        }
    }

    static String tGetPackageVersionName() {
        try {
            return aContext.getPackageManager().getPackageInfo(aContext.getPackageName(), 0).versionName;
        } catch (Exception e2) {
            return "unknown";
        }
    }

    static Long[][] uGetAllApplicationsHashNames() {
        List<ActivityManager.RecentTaskInfo> recentTasks;

        ActivityManager activityManager = (ActivityManager) aContext.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = aContext.getPackageManager();

        HashSet<String> packageNames = new HashSet<>();
        packageNames.add(aContext.getPackageName());

        HashSet<Long> packageHashNames = new HashSet<>();

        if (aPermissionCheck("android.permission.GET_TASKS") && (recentTasks = activityManager.getRecentTasks(10, 1)) != null) {
            for (ActivityManager.RecentTaskInfo recentTaskInfo : recentTasks) {
                ComponentName component = recentTaskInfo.baseIntent.getComponent();
                if (component != null) {
                    String packageName = component.getPackageName();
                    if (packageNames.add(packageName)) {
                        packageHashNames.add(kHash(packageName));
                    }
                }
            }
        }

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        HashSet<Long> processHashNames = new HashSet<>();
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                String processName = runningAppProcessInfo.processName;
                if (packageManager.getLaunchIntentForPackage(processName) != null && !packageNames.contains(processName)) {
                    processHashNames.add(kHash(processName));
                }
            }
        }

        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
        HashSet<Long> installedApplicationsPackageHashNames = new HashSet<>();
        if (installedApplications != null) {
            for (ApplicationInfo applicationInfo : installedApplications) {
                if ((applicationInfo.flags & 1) <= 0 && !packageNames.contains(applicationInfo.packageName)) {
                    installedApplicationsPackageHashNames.add(kHash(applicationInfo.packageName));
                }
            }
        }

        Long[][] lArr = new Long[3][];

        lArr[0] = new Long[packageHashNames.size()];
        lArr[0] = packageHashNames.toArray(lArr[0]);

        lArr[1] = new Long[processHashNames.size()];
        lArr[1] = processHashNames.toArray(lArr[1]);

        lArr[2] = new Long[installedApplicationsPackageHashNames.size()];
        lArr[2] = installedApplicationsPackageHashNames.toArray(lArr[2]);

        return lArr;
    }

    static String vGetPackageName() {
        return aContext.getPackageName();
    }

    static long wGetPackageFirstInstallTime() {
        try {
            if (Build.VERSION.SDK_INT < 9) {
                return -1;
            }
            return abUtilities.aGetPackageFirstInstallTime(aContext);
        } catch (Exception e2) {
            return -1;
        }
    }

    static long xGetPackageLastUpdateTime() {
        try {
            if (Build.VERSION.SDK_INT < 9) {
                return -1;
            }

            return abUtilities.bGetPackageLastUpdateTime(aContext);
        } catch (Exception e2) {
            return -1;
        }
    }

    static long yGetPackageSourceLength() {
        try {
            return new File(aContext.getPackageManager().getApplicationInfo(aContext.getPackageName(), 0).sourceDir).length();
        } catch (Exception e2) {
            return -1;
        }
    }

    static String zGetFirstSignature() {
        try {
            return aContext.getPackageManager().getPackageInfo(aContext.getPackageName(), PackageManager.GET_SIGNATURES).signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e2) {
            return "";
        }
    }
}