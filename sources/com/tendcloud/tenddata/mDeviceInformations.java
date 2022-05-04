package com.tendcloud.tenddata;

import java.io.IOException;

public class mDeviceInformations implements hSerializable, lMeasurable {
    public String ASimSerialNumber = "";
    public String BAndroidId = "";
    public String aProcessor = "";
    public int bCpuVariant = 0;
    public float cCpuMaxFreq = 0.0f;
    public String dHardware = "";
    public String e = "";
    public String f = "";
    public int gTotalMemory = 0;
    public int hAvailableMemory = 0;
    public int iEnvironmentTotalSize = 0;
    public int jEnvironmentFreeSize = 0;
    public int kExternalTotalSize = 0;
    public int lExternalFreeSize = 0;
    public int mBatteryCapacity = 0;
    public float nDisplayWidth = 0.0f;
    public float oDisplayHeight = 0.0f;
    public int pDensityDpi = 0;
    public String qDisplayId = "";
    public String rGsmVersionBaseband = "";
    public String sDeviceId = "";
    public String tWifiConnectionMacAddress = "";
    public String uApn = "";
    public String vMcc = "";
    public String wMnc = "";
    public boolean xProxyExists = false;
    public String ySubscriberId = "";
    public String z = "";

    @Override
    public int aCalculateLength() {
        return pSerializationUtils.cTypeLength(27)
                + pSerializationUtils.cTypeLength(this.aProcessor)
                + pSerializationUtils.cTypeLength(this.bCpuVariant)
                + pSerializationUtils.bTypeLength(this.cCpuMaxFreq)
                + pSerializationUtils.cTypeLength(this.dHardware)
                + pSerializationUtils.cTypeLength(this.e)
                + pSerializationUtils.cTypeLength(this.f)
                + pSerializationUtils.cTypeLength(this.gTotalMemory)
                + pSerializationUtils.cTypeLength(this.hAvailableMemory)
                + pSerializationUtils.cTypeLength(this.iEnvironmentTotalSize)
                + pSerializationUtils.cTypeLength(this.jEnvironmentFreeSize)
                + pSerializationUtils.cTypeLength(this.kExternalTotalSize)
                + pSerializationUtils.cTypeLength(this.lExternalFreeSize)
                + pSerializationUtils.cTypeLength(this.mBatteryCapacity)
                + pSerializationUtils.bTypeLength(this.nDisplayWidth)
                + pSerializationUtils.bTypeLength(this.oDisplayHeight)
                + pSerializationUtils.cTypeLength(this.pDensityDpi)
                + pSerializationUtils.cTypeLength(this.qDisplayId)
                + pSerializationUtils.cTypeLength(this.rGsmVersionBaseband)
                + pSerializationUtils.cTypeLength(this.sDeviceId)
                + pSerializationUtils.cTypeLength(this.tWifiConnectionMacAddress)
                + pSerializationUtils.cTypeLength(this.uApn)
                + pSerializationUtils.cTypeLength(this.vMcc)
                + pSerializationUtils.cTypeLength(this.wMnc)
                + pSerializationUtils.bTypeLength(this.xProxyExists)
                + pSerializationUtils.cTypeLength(this.ySubscriberId)
                + pSerializationUtils.cTypeLength(this.z)
                + pSerializationUtils.cTypeLength(this.ASimSerialNumber);
    }

    @Override
    public void aWrite(pSerializationUtils pVar) throws IOException {
        pVar.bWrite(28);
        pVar.aWrite(this.aProcessor);
        pVar.aWrite(this.bCpuVariant);
        pVar.aWrite(this.cCpuMaxFreq);
        pVar.aWrite(this.dHardware);
        pVar.aWrite(this.e);
        pVar.aWrite(this.f);
        pVar.aWrite(this.gTotalMemory);
        pVar.aWrite(this.hAvailableMemory);
        pVar.aWrite(this.iEnvironmentTotalSize);
        pVar.aWrite(this.jEnvironmentFreeSize);
        pVar.aWrite(this.kExternalTotalSize);
        pVar.aWrite(this.lExternalFreeSize);
        pVar.aWrite(this.mBatteryCapacity);
        pVar.aWrite(this.nDisplayWidth);
        pVar.aWrite(this.oDisplayHeight);
        pVar.aWrite(this.pDensityDpi);
        pVar.aWrite(this.qDisplayId);
        pVar.aWrite(this.rGsmVersionBaseband);
        pVar.aWrite(this.sDeviceId);
        pVar.aWrite(this.tWifiConnectionMacAddress);
        pVar.aWrite(this.uApn);
        pVar.aWrite(this.vMcc);
        pVar.aWrite(this.wMnc);
        pVar.aWrite(this.xProxyExists);
        pVar.aWrite(this.ySubscriberId);
        pVar.aWrite(this.z);
        pVar.aWrite(this.ASimSerialNumber);
        pVar.aWrite(this.BAndroidId);
    }
}
