package com.tendcloud.tenddata;

import android.content.Context;
import android.telephony.gsm.GsmCellLocation;

import java.io.File;

public class abUtilities {
    private abUtilities() {
    }

    static long aGetPackageFirstInstallTime(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 64).firstInstallTime;
        } catch (Exception e) {
            return -1;
        }
    }

    static long aGetPrimaryScramblingCode(GsmCellLocation gsmCellLocation) {
        try {
            return gsmCellLocation.getPsc();
        } catch (Exception e) {
            return -1;
        }
    }

    static void aSetFileReadable(File file) {
        file.setReadable(true, false);
    }

    static long bGetPackageLastUpdateTime(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 64).lastUpdateTime;
        } catch (Exception e) {
            return -1;
        }
    }
}
