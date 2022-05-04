package com.tendcloud.tenddata;

import java.io.IOException;
import java.util.Map;

public class afProbablyEventEntity implements hSerializable, lMeasurable {
    public String aString = "";
    public String bString = "";
    public int cInt = 0;
    public long dInt;
    public Map<String, Object> eStringObjectMap;

    @Override
    public int aCalculateLength() {
        return pSerializationUtils.cTypeLength(3)
                + pSerializationUtils.cTypeLength(this.aString)
                + pSerializationUtils.cTypeLength(this.bString)
                + pSerializationUtils.cTypeLength(this.cInt);
    }

    @Override
    public void aWrite(pSerializationUtils pVar) throws IOException {
        pVar.bWrite(5);
        pVar.aWrite(this.aString);
        pVar.aWrite(this.bString);
        pVar.aWrite(this.cInt);
        pVar.aWrite(this.dInt);
        pVar.aWrite(this.eStringObjectMap);
    }
}
