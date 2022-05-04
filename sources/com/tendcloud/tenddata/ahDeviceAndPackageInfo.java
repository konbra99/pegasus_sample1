package com.tendcloud.tenddata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ahDeviceAndPackageInfo implements hSerializable, tMeasurable {
    public String aDeviceId = "";
    public String b = "";
    public jPackageInformations cPackageInformations = new jPackageInformations();
    public yNetworkAndLocalisationInfo dNetworkAndLocalistaionInfo = new yNetworkAndLocalisationInfo();
    public List<agMessageContainer> eMessages = new ArrayList<>();
    public long fMaxActivityId = 0;
    public long gMaxAppEventId = 0;
    public long hMadErrorReportId = 0;
    public Long[][] iAllApplicationsHashNames;

    @Override
    public int aCalculateLength() {
        return pSerializationUtils.cTypeLength(5)
                + pSerializationUtils.cTypeLength(this.aDeviceId)
                + pSerializationUtils.cTypeLength(this.b)
                + this.cPackageInformations.aCalculateLength()
                + this.dNetworkAndLocalistaionInfo.aCalculateLength();
    }

    @Override
    public void aWrite(pSerializationUtils pVar) throws IOException {
        pVar.bWrite(6);
        pVar.aWrite(this.aDeviceId);
        pVar.aWrite(this.b);
        pVar.aWrite(this.cPackageInformations);
        pVar.aWrite(this.dNetworkAndLocalistaionInfo);
        pVar.bWrite(this.eMessages.size());
        for (agMessageContainer agVar : this.eMessages) {
            pVar.aWrite(agVar);
        }
        if (this.iAllApplicationsHashNames == null) {
            pVar.bWriteNull();
            return;
        }

        pVar.bWrite(this.iAllApplicationsHashNames.length);
        for (Long[] lArr : this.iAllApplicationsHashNames) {
            pVar.aWrite(lArr);
        }
    }
}
