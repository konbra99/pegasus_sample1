package com.tendcloud.tenddata;

import android.util.Log;

import java.util.Date;

public class sLogger {
    static void aLog(String... strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("+++++++++++++++++++++++ ").append(new Date());
        for (String str : strArr) {
            sb.append("\t");
            sb.append(str);
        }
        sb.append("\r\n---------------------- end");
        Log.d("TalkingData", sb.toString());
    }
}
