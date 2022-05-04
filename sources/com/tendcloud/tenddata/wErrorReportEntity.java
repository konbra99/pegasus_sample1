package com.tendcloud.tenddata;

import java.io.IOException;

public class wErrorReportEntity implements hSerializable, lMeasurable {
    public long aErrorTime = 0;
    public int bRepeat = 1;
    public String cPackageVersionCode = "";
    public byte[] dMessage = new byte[0];
    public String eShortHashCode = "";

    @Override
    public int aCalculateLength() {
        return pSerializationUtils.cTypeLength(5)
                + pSerializationUtils.cTypeLength(this.aErrorTime)
                + pSerializationUtils.cTypeLength(this.bRepeat)
                + pSerializationUtils.cTypeLength(this.cPackageVersionCode)
                + pSerializationUtils.bTypeLength(this.dMessage)
                + pSerializationUtils.cTypeLength(this.eShortHashCode);
    }

    @Override // com.tendcloud.tenddata.h
    public void aWrite(pSerializationUtils pVar) throws IOException {
        pVar.aWrite(5);
        pVar.aWrite(this.aErrorTime);
        pVar.aWrite(this.bRepeat);
        pVar.aWrite(this.cPackageVersionCode);
        pVar.aWrite(this.dMessage);
        pVar.aWrite(this.eShortHashCode);
    }
}
