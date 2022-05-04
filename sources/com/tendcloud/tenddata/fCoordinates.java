package com.tendcloud.tenddata;

import java.io.IOException;

public class fCoordinates implements hSerializable, lMeasurable {
    public double aLongitude = 0.0d;
    public double bLatitude = 0.0d;

    @Override
    public int aCalculateLength() {
        return pSerializationUtils.cTypeLength(2)
                + pSerializationUtils.bTypeLength(this.aLongitude)
                + pSerializationUtils.bTypeLength(this.bLatitude);
    }

    @Override
    public void aWrite(pSerializationUtils pVar) throws IOException {
        pVar.bWrite(2);
        pVar.aWrite(this.aLongitude);
        pVar.aWrite(this.bLatitude);
    }
}
