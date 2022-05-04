package com.tendcloud.tenddata;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class eActivityTable implements BaseColumns {
    public static final String aNAME = "name";
    public static final String bSTART_TIME = "start_time";
    public static final String cDURATION = "duration";
    public static final String dSESSION_ID = "session_id";
    public static final String eREFER = "refer";
    public static final String fREALTIME = "realtime";
    public static final String gACTIVITY = "activity";
    public static final String[] hCOLUMNS = {"_id", aNAME, "start_time", "duration", "session_id", eREFER, fREALTIME};

    eActivityTable() {
    }

    public static void aCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE activity (_id INTEGER PRIMARY KEY autoincrement,name TEXT,start_time LONG,duration INTEGER,session_id TEXT,refer TEXT,realtime LONG)");
    }

    public static void bDrop(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS activity");
    }
}
