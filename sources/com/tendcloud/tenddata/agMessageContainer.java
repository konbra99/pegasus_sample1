package com.tendcloud.tenddata;

import java.io.IOException;

public class agMessageContainer implements hSerializable {
    public int aDeviceInformationsOrSmthOrErrorMessage = -1;
    public bDataClass b;
    public mDeviceInformations cDeviceInformations;
    public wErrorReportEntity dErrorReportEntity;

    @Override
    public void aWrite(pSerializationUtils pVar) throws IOException {
        pVar.bWrite(2);
        pVar.aWrite(this.aDeviceInformationsOrSmthOrErrorMessage);
        switch (this.aDeviceInformationsOrSmthOrErrorMessage) {
            case 1:
                pVar.aWrite(this.cDeviceInformations);
                return;
            case 2:
                pVar.aWrite(this.b);
                return;
            case 3:
                pVar.aWrite(this.dErrorReportEntity);
                return;
            default:
                throw new IOException("unknown TMessageType");
        }
    }
}
