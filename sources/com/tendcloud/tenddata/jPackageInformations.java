package com.tendcloud.tenddata;

import java.io.IOException;

public class jPackageInformations implements hSerializable, lMeasurable {
    public String aPackageName = "";
    public String bPackageVersionName = "";
    public String cPackageVersionCode = "";
    public long dTime = 0;
    public String eString = "";
    public String fString = "";
    public boolean gBoolean = false;
    public long hPackageFirstInstallTime = 0;
    public long iPackageLastUpdateTime = 0;

    @Override
    public int aCalculateLength() {
        return pSerializationUtils.cTypeLength(9)
                + pSerializationUtils.cTypeLength(this.aPackageName)
                + pSerializationUtils.cTypeLength(this.bPackageVersionName)
                + pSerializationUtils.cTypeLength(this.cPackageVersionCode)
                + pSerializationUtils.cTypeLength(this.dTime)
                + pSerializationUtils.cTypeLength(this.eString)
                + pSerializationUtils.cTypeLength(this.fString)
                + pSerializationUtils.bTypeLength(this.gBoolean)
                + pSerializationUtils.cTypeLength(this.hPackageFirstInstallTime)
                + pSerializationUtils.cTypeLength(this.iPackageLastUpdateTime);
    }

    @Override
    public void aWrite(pSerializationUtils pVar) throws IOException {
        pVar.bWrite(9);
        pVar.aWrite(this.aPackageName);
        pVar.aWrite(this.bPackageVersionName);
        pVar.aWrite(this.cPackageVersionCode);
        pVar.aWrite(this.dTime);
        pVar.aWrite(this.eString);
        pVar.aWrite(this.fString);
        pVar.aWrite(this.gBoolean);
        pVar.aWrite(this.hPackageFirstInstallTime);
        pVar.aWrite(this.iPackageLastUpdateTime);
    }
}
