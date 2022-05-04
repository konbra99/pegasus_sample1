package com.tendcloud.tenddata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class bDataClass implements hSerializable, lMeasurable {
    public static final int dONE = 1;
    public static final int eTWO = 2;
    public static final int fTHREE = 3;
    public String aSessionActivityEventId = "";
    public long bLong = 0;
    public int cInt = 0;
    public int gInt = 0;
    public List<zProbablyAppActivityEntity> hActivityList = new ArrayList<>();
    public List<afProbablyEventEntity> iEventList = new ArrayList<>();
    public int jInt = 0;
    public int kInt = 0;

    @Override // com.tendcloud.tenddata.l
    public int aCalculateLength() {
        int length = pSerializationUtils.cTypeLength(7)
                + pSerializationUtils.cTypeLength(this.aSessionActivityEventId)
                + pSerializationUtils.cTypeLength(this.bLong)
                + pSerializationUtils.cTypeLength(this.cInt)
                + pSerializationUtils.cTypeLength(this.gInt)
                + pSerializationUtils.cTypeLength(this.kInt)
                + pSerializationUtils.cTypeLength(this.hActivityList.size());

        for (zProbablyAppActivityEntity zProbablyAppActivityEntity : this.hActivityList) {
            length += zProbablyAppActivityEntity.aCalculateLength();
        }

        length += pSerializationUtils.cTypeLength(this.iEventList.size());
        for (afProbablyEventEntity afVar : this.iEventList) {
            length += afVar.aCalculateLength();
        }

        return length;
    }

    @Override // com.tendcloud.tenddata.h
    public void aWrite(pSerializationUtils pVar) throws IOException {
        pVar.bWrite(7);
        pVar.aWrite(this.aSessionActivityEventId);
        pVar.aWrite(this.bLong);
        pVar.aWrite(this.cInt);
        pVar.aWrite(this.gInt);
        pVar.bWrite(this.hActivityList.size());
        for (zProbablyAppActivityEntity zProbablyAppActivityEntity : this.hActivityList) {
            pVar.aWrite(zProbablyAppActivityEntity);
        }

        pVar.bWrite(this.iEventList.size());
        for (afProbablyEventEntity afVar : this.iEventList) {
            pVar.aWrite(afVar);
        }

        pVar.aWrite(this.kInt);
    }
}
