package com.tendcloud.tenddata;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class dSessionTable implements BaseColumns {
    public static final String aSESSION_ID = "session_id";
    public static final String bSTART_TIME = "start_time";
    public static final String cDURATION = "duration";
    public static final String dIS_LAUNCH = "is_launch";
    public static final String eINTERVAL = "interval";
    public static final String fIS_CONNECTED = "is_connected";
    public static final String gSESSION = "session";
    public static final String[] hCOLUMNS = {"_id", "session_id", "start_time", "duration", dIS_LAUNCH, eINTERVAL, fIS_CONNECTED};

    dSessionTable() {
    }

    public static void aCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE session (_id INTEGER PRIMARY KEY autoincrement,session_id TEXT,start_time LONG,duration INTEGER,is_launch INTEGER,interval LONG, is_connected INTEGER)");
    }

    public static void bDrop(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS session");
    }
}
