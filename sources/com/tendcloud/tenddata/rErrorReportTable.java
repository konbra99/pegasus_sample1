package com.tendcloud.tenddata;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class rErrorReportTable implements BaseColumns {
    public static final String aERROR_TIME = "error_time";
    public static final String bMESSAGE = "message";
    public static final String cREPEAT = "repeat";
    public static final String dSHORTHASHCODE = "shorthashcode";
    public static final String eERROR_REPORT = "error_report";
    public static final String[] fCOLUMNS = {"_id", aERROR_TIME, bMESSAGE, cREPEAT, dSHORTHASHCODE};

    rErrorReportTable() {
    }

    public static void aCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE error_report (_id INTEGER PRIMARY KEY autoincrement,error_time LONG,message BLOB,repeat INTERGER,shorthashcode TEXT)");
    }

    public static void bDrop(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS error_report");
    }
}
