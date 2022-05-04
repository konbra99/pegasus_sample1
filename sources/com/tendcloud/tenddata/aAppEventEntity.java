package com.tendcloud.tenddata;

import java.util.Map;

class aAppEventEntity {
    String aEventId;
    String bEventLabel;
    long cTime;
    String dMessage;
    String eStackTrace;
    long fOccurrenceTime = System.currentTimeMillis();
    Map<String, Object> gParameterMap = null;
}
