package com.tendcloud.tenddata;

import java.io.IOException;

public class yNetworkAndLocalisationInfo implements hSerializable, lMeasurable {
    public String aDeviceDescription = "";
    public String bSdkVersion = "";
    public fCoordinates cCoordinates = new fCoordinates();
    public String dCpuAbi = "";
    public String eResolutionEquation = "";
    public String fCountry = "";
    public String gNetworkOperatorName = "";
    public String hLanguage = "";
    public int i = 8;
    public String jAndroidVersion = "";
    public int kIsWifiConnected = -1;
    public String lNetworkType = "";
    public boolean m = false;
    public String nSimOperator = "";
    public String oNetworkOperator = "";
    public String pLastKnownLocations = "";
    public String q = "";
    public long r = 0;
    public String s = "";
    public String tWifiBssidRssiAndAccessPointsBssids = "";
    public String uGsmOrCdma = "";
    public int vCellOrStation;
    public int wLocationOrNetwork;

    @Override // com.tendcloud.tenddata.l
    public int aCalculateLength() {
        return pSerializationUtils.cTypeLength(18)
                + pSerializationUtils.cTypeLength(this.aDeviceDescription)
                + pSerializationUtils.cTypeLength(this.bSdkVersion)
                + this.cCoordinates.aCalculateLength()
                + pSerializationUtils.cTypeLength(this.dCpuAbi)
                + pSerializationUtils.cTypeLength(this.eResolutionEquation)
                + pSerializationUtils.cTypeLength(this.fCountry)
                + pSerializationUtils.cTypeLength(this.gNetworkOperatorName)
                + pSerializationUtils.cTypeLength(this.hLanguage)
                + pSerializationUtils.cTypeLength(this.i)
                + pSerializationUtils.cTypeLength(this.jAndroidVersion)
                + pSerializationUtils.cTypeLength(this.kIsWifiConnected)
                + pSerializationUtils.cTypeLength(this.lNetworkType)
                + pSerializationUtils.bTypeLength(this.m)
                + pSerializationUtils.cTypeLength(this.nSimOperator)
                + pSerializationUtils.cTypeLength(this.oNetworkOperator)
                + pSerializationUtils.cTypeLength(this.pLastKnownLocations)
                + pSerializationUtils.cTypeLength(this.q)
                + pSerializationUtils.cTypeLength(this.r)
                + pSerializationUtils.cTypeLength(this.tWifiBssidRssiAndAccessPointsBssids);
    }

    @Override // com.tendcloud.tenddata.h
    public void aWrite(pSerializationUtils pVar) throws IOException {
        pVar.bWrite(23);
        pVar.aWrite(this.aDeviceDescription);
        pVar.aWrite(this.bSdkVersion);
        pVar.aWrite(this.cCoordinates);
        pVar.aWrite(this.dCpuAbi);
        pVar.aWrite(this.eResolutionEquation);
        pVar.aWrite(this.fCountry);
        pVar.aWrite(this.gNetworkOperatorName);
        pVar.aWrite(this.hLanguage);
        pVar.aWrite(this.i);
        pVar.aWrite(this.jAndroidVersion);
        pVar.aWrite(this.kIsWifiConnected);
        pVar.aWrite(this.lNetworkType);
        pVar.aWrite(this.m);
        pVar.aWrite(this.nSimOperator);
        pVar.aWrite(this.oNetworkOperator);
        pVar.aWrite(this.pLastKnownLocations);
        pVar.aWrite(this.q);
        pVar.aWrite(this.r).aWrite(this.s).aWrite(this.tWifiBssidRssiAndAccessPointsBssids).aWrite(this.uGsmOrCdma).aWrite(this.vCellOrStation).aWrite(this.wLocationOrNetwork);
    }
}
