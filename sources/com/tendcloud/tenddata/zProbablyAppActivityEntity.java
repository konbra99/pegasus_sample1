package com.tendcloud.tenddata;

import java.io.IOException;

public class zProbablyAppActivityEntity implements hSerializable, lMeasurable {
    public String a = "";
    public long b = 0;
    public int c = 0;
    public String d = "";

    @Override // com.tendcloud.tenddata.l
    public int aCalculateLength() {
        return pSerializationUtils.cTypeLength(4)
                + pSerializationUtils.cTypeLength(this.a)
                + pSerializationUtils.cTypeLength(this.b)
                + pSerializationUtils.cTypeLength(this.c)
                + pSerializationUtils.cTypeLength(this.d);
    }

    @Override // com.tendcloud.tenddata.h
    public void aWrite(pSerializationUtils pVar) throws IOException {
        pVar.bWrite(4);
        pVar.aWrite(this.a);
        pVar.aWrite(this.b);
        pVar.aWrite(this.c);
        pVar.aWrite(this.d);
    }
}
