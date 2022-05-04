package com.tendcloud.tenddata;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class nAppEventTable implements BaseColumns {
    public static final String aEVENT_ID = "event_id";
    public static final String bEVENT_LABEL = "event_label";
    public static final String cSESSION_ID = "session_id";
    public static final String dOCCURTIME = "occurtime";
    public static final String ePARAMAP = "paramap";
    public static final String fAPP_EVENT = "app_event";
    public static final String[] gCOLUMNS = {"_id", aEVENT_ID, bEVENT_LABEL, "session_id", dOCCURTIME, ePARAMAP};

    nAppEventTable() {
    }

    public static void aCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE app_event (_id INTEGER PRIMARY KEY autoincrement,event_id TEXT,event_label TEXT,session_id TEXT,occurtime LONG,paramap BLOB)");
    }

    public static void bDrop(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS app_event");
    }
}
