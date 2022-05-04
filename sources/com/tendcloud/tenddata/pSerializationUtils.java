package com.tendcloud.tenddata;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pSerializationUtils {
    private final int byteHeader = -48;
    private final int nullHeader = -64;
    protected byte[] aByteArray = new byte[9];
    protected OutputStream bOutputStream;

    public pSerializationUtils(OutputStream outputStream) {
        this.bOutputStream = outputStream;
    }

    public static int bTypeLength(double d) {
        return 9;
    }

    public static int bTypeLength(float f) {
        return 5;
    }

    public static int bTypeLength(boolean z) {
        return 1;
    }

    public static int bTypeLength(byte[] bArr) {
        if (bArr == null) {
            return 1;
        }

        return cTypeLength(bArr.length) + bArr.length;
    }

    public static int cTypeLength(int i) {
        if (i < 16) {
            return 1;
        }
        return i < 65536 ? 3 : 5;
    }

    public static int cTypeLength(long j) {
        if (j < -32) {
            return j < -32768 ? j < -2147483648L ? 9 : 5 : j < -128 ? 3 : 2;
        }
        if (j < 128) {
            return 1;
        }
        return j < 65536 ? j < 256 ? 2 : 3 : j < 4294967296L ? 5 : 9;
    }

    public static int cTypeLength(String str) {
        try {
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            return bytes.length + cTypeLength(bytes.length);
        } catch (Exception e) {
            return 0;
        }
    }

    public pSerializationUtils aWriteFalse() throws IOException {
        this.bOutputStream.write(-62);
        return this;
    }

    public pSerializationUtils aWrite(byte b2) throws IOException {
        return bWrite(b2);
    }

    public pSerializationUtils aWrite(double d) throws IOException {
        return cWrite(d);
    }

    public pSerializationUtils aWrite(float f) throws IOException {
        return cWrite(f);
    }

    public pSerializationUtils aWrite(int i) throws IOException {
        return dWrite(i);
    }

    public pSerializationUtils aWrite(long j) throws IOException {
        return bWrite(j);
    }

    public pSerializationUtils aWrite(hSerializable hSerializable) throws IOException {
        if (hSerializable == null) {
            return bWriteNull();
        }
        hSerializable.aWrite(this);
        return this;
    }

    public pSerializationUtils aWrite(Boolean bool) throws IOException {
        return bool == null ? bWriteNull() : bool ? cWriteTrue() : aWriteFalse();
    }

    public pSerializationUtils aWrite(Byte b2) throws IOException {
        return b2 == null ? bWriteNull() : bWrite(b2);
    }

    public pSerializationUtils aWrite(Double d) throws IOException {
        return d == null ? bWriteNull() : cWrite(d);
    }

    public pSerializationUtils aWrite(Float f) throws IOException {
        return f == null ? bWriteNull() : cWrite(f);
    }

    public pSerializationUtils aWrite(Integer num) throws IOException {
        return num == null ? bWriteNull() : dWrite(num);
    }

    public pSerializationUtils aWrite(Long l) throws IOException {
        return l == null ? bWriteNull() : bWrite(l);
    }

    public pSerializationUtils aWrite(Short sh) throws IOException {
        return sh == null ? bWriteNull() : bWrite(sh);
    }

    public pSerializationUtils aWrite(String str) throws IOException {
        return str == null ? bWrite("") : bWrite(str);
    }

    public pSerializationUtils aWrite(BigInteger bigInteger) throws IOException {
        return bigInteger == null ? bWriteNull() : bWrite(bigInteger);
    }

    public pSerializationUtils aWrite(ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer == null) {
            return bWriteNull();
        }
        fWriteWithAHeader(byteBuffer.remaining());
        return bWriteArray(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
    }

    public pSerializationUtils aWrite(List<Object> list) throws IOException {
        if (list == null) {
            return bWriteNull();
        }
        bWrite(list.size());
        for (Object o : list) {
            aWrite((String) o);
        }
        return this;
    }

    public pSerializationUtils aWrite(Map<String, Object> map) throws IOException {
        if (map == null) {
            return bWriteNull();
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        for (String str : map.keySet()) {
            Object obj = map.get(str);
            if (obj instanceof String) {
                hashMap.put(str, obj.toString());
            } else if (obj instanceof Number) {
                hashMap.put(str, ((Number) obj).doubleValue());
            }
        }
        eWriteWith8Header(hashMap.size());
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            aWrite(entry.getKey());
            Object value = entry.getValue();
            if (value instanceof Number) {
                aWrite(((Number) value).doubleValue());
            } else if (value instanceof String) {
                aWrite(value.toString());
            }
        }
        return this;
    }

    public pSerializationUtils aWrite(short s) throws IOException {
        return bWrite(s);
    }

    public pSerializationUtils aWrite(boolean z) throws IOException {
        return z ? cWriteTrue() : aWriteFalse();
    }

    public pSerializationUtils aWrite(byte[] bArr) throws IOException {
        if (bArr == null) {
            return bWriteNull();
        }
        fWriteWithAHeader(bArr.length);
        return dWriteArray(bArr);
    }

    public pSerializationUtils aWriteArray(byte[] bArr, int i, int i2) throws IOException {
        fWriteWithAHeader(i2);
        return bWriteArray(bArr, i, i2);
    }

    public pSerializationUtils aWrite(Long[] lArr) throws IOException {
        if (lArr == null || lArr.length == 0) {
            return bWriteNull();
        }
        bWrite(lArr.length);
        for (Long l : lArr) {
            aWrite(l.longValue());
        }
        return this;
    }

    public pSerializationUtils bWriteNull() throws IOException {
        this.bOutputStream.write(nullHeader);
        return this;
    }

    public pSerializationUtils bWrite(byte b2) throws IOException {
        if (b2 < -32) {
            this.aByteArray[0] = byteHeader;
            this.aByteArray[1] = b2;
            this.bOutputStream.write(this.aByteArray, 0, 2);
        } else {
            this.bOutputStream.write(b2);
        }
        return this;
    }

    public pSerializationUtils bWrite(int i) throws IOException {
        if (i < 16) {
            this.bOutputStream.write((byte) (i | 144));
        } else if (i < 65536) {
            this.aByteArray[0] = -36;
            this.aByteArray[1] = (byte) (i >> 8);
            this.aByteArray[2] = (byte) (i);
            this.bOutputStream.write(this.aByteArray, 0, 3);
        } else {
            this.aByteArray[0] = -35;
            this.aByteArray[1] = (byte) (i >> 24);
            this.aByteArray[2] = (byte) (i >> 16);
            this.aByteArray[3] = (byte) (i >> 8);
            this.aByteArray[4] = (byte) (i);
            this.bOutputStream.write(this.aByteArray, 0, 5);
        }
        return this;
    }

    public pSerializationUtils bWrite(long j) throws IOException {
        if (j < -32) {
            if (j < -32768) {
                if (j < -2147483648L) {
                    this.aByteArray[0] = -45;
                    this.aByteArray[1] = (byte) ((int) (j >> 56));
                    this.aByteArray[2] = (byte) ((int) (j >> 48));
                    this.aByteArray[3] = (byte) ((int) (j >> 40));
                    this.aByteArray[4] = (byte) ((int) (j >> 32));
                    this.aByteArray[5] = (byte) ((int) (j >> 24));
                    this.aByteArray[6] = (byte) ((int) (j >> 16));
                    this.aByteArray[7] = (byte) ((int) (j >> 8));
                    this.aByteArray[8] = (byte) ((int) (j));
                    this.bOutputStream.write(this.aByteArray, 0, 9);
                } else {
                    this.aByteArray[0] = -46;
                    this.aByteArray[1] = (byte) ((int) (j >> 24));
                    this.aByteArray[2] = (byte) ((int) (j >> 16));
                    this.aByteArray[3] = (byte) ((int) (j >> 8));
                    this.aByteArray[4] = (byte) ((int) (j));
                    this.bOutputStream.write(this.aByteArray, 0, 5);
                }
            } else if (j < -128) {
                this.aByteArray[0] = -47;
                this.aByteArray[1] = (byte) ((int) (j >> 8));
                this.aByteArray[2] = (byte) ((int) (j));
                this.bOutputStream.write(this.aByteArray, 0, 3);
            } else {
                this.aByteArray[0] = byteHeader;
                this.aByteArray[1] = (byte) ((int) j);
                this.bOutputStream.write(this.aByteArray, 0, 2);
            }
        } else if (j < 128) {
            this.bOutputStream.write((byte) ((int) j));
        } else if (j < 65536) {
            if (j < 256) {
                this.aByteArray[0] = -52;
                this.aByteArray[1] = (byte) ((int) j);
                this.bOutputStream.write(this.aByteArray, 0, 2);
            } else {
                this.aByteArray[0] = -51;
                this.aByteArray[1] = (byte) ((int) ((65280 & j) >> 8));
                this.aByteArray[2] = (byte) ((int) ((255 & j)));
                this.bOutputStream.write(this.aByteArray, 0, 3);
            }
        } else if (j < 4294967296L) {
            this.aByteArray[0] = -50;
            this.aByteArray[1] = (byte) ((int) ((-16777216 & j) >> 24));
            this.aByteArray[2] = (byte) ((int) ((16711680 & j) >> 16));
            this.aByteArray[3] = (byte) ((int) ((65280 & j) >> 8));
            this.aByteArray[4] = (byte) ((int) ((255 & j)));
            this.bOutputStream.write(this.aByteArray, 0, 5);
        } else {
            this.aByteArray[0] = -49;
            this.aByteArray[1] = (byte) ((int) (j >> 56));
            this.aByteArray[2] = (byte) ((int) (j >> 48));
            this.aByteArray[3] = (byte) ((int) (j >> 40));
            this.aByteArray[4] = (byte) ((int) (j >> 32));
            this.aByteArray[5] = (byte) ((int) (j >> 24));
            this.aByteArray[6] = (byte) ((int) (j >> 16));
            this.aByteArray[7] = (byte) ((int) (j >> 8));
            this.aByteArray[8] = (byte) ((int) (j));
            this.bOutputStream.write(this.aByteArray, 0, 9);
        }
        return this;
    }

    public pSerializationUtils bWrite(String str) throws IOException {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        fWriteWithAHeader(bytes.length);
        return dWriteArray(bytes);
    }

    public pSerializationUtils bWrite(BigInteger bigInteger) throws IOException {
        if (bigInteger.bitLength() <= 63) {
            return bWrite(bigInteger.longValue());
        }
        if (bigInteger.bitLength() > 64 || bigInteger.signum() < 0) {
            throw new IOException("can't pack BigInteger larger than 0xffffffffffffffff");
        }
        this.aByteArray[0] = -49;
        byte[] byteArray = bigInteger.toByteArray();
        this.aByteArray[1] = byteArray[byteArray.length - 8];
        this.aByteArray[2] = byteArray[byteArray.length - 7];
        this.aByteArray[3] = byteArray[byteArray.length - 6];
        this.aByteArray[4] = byteArray[byteArray.length - 5];
        this.aByteArray[5] = byteArray[byteArray.length - 4];
        this.aByteArray[6] = byteArray[byteArray.length - 3];
        this.aByteArray[7] = byteArray[byteArray.length - 2];
        this.aByteArray[8] = byteArray[byteArray.length - 1];
        this.bOutputStream.write(this.aByteArray, 0, 9);
        return this;
    }

    public pSerializationUtils bWrite(ByteBuffer byteBuffer) throws IOException {
        fWriteWithAHeader(byteBuffer.remaining());
        return bWriteArray(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
    }

    public pSerializationUtils bWrite(short s) throws IOException {
        if (s < -32) {
            if (s < -128) {
                this.aByteArray[0] = -47;
                this.aByteArray[1] = (byte) (s >> 8);
                this.aByteArray[2] = (byte) (s);
                this.bOutputStream.write(this.aByteArray, 0, 3);
            } else {
                this.aByteArray[0] = byteHeader;
                this.aByteArray[1] = (byte) s;
                this.bOutputStream.write(this.aByteArray, 0, 2);
            }
        } else if (s < 128) {
            this.bOutputStream.write((byte) s);
        } else if (s < 256) {
            this.aByteArray[0] = -52;
            this.aByteArray[1] = (byte) s;
            this.bOutputStream.write(this.aByteArray, 0, 2);
        } else {
            this.aByteArray[0] = -51;
            this.aByteArray[1] = (byte) (s >> 8);
            this.aByteArray[2] = (byte) (s);
            this.bOutputStream.write(this.aByteArray, 0, 3);
        }
        return this;
    }

    public pSerializationUtils bWriteArray(byte[] bArr, int i, int i2) throws IOException {
        this.bOutputStream.write(bArr, i, i2);
        return this;
    }

    public pSerializationUtils cWriteTrue() throws IOException {
        this.bOutputStream.write(-61);
        return this;
    }

    public pSerializationUtils cWrite(double d) throws IOException {
        this.aByteArray[0] = -53;
        long doubleToRawLongBits = Double.doubleToRawLongBits(d);
        this.aByteArray[1] = (byte) ((int) (doubleToRawLongBits >> 56));
        this.aByteArray[2] = (byte) ((int) (doubleToRawLongBits >> 48));
        this.aByteArray[3] = (byte) ((int) (doubleToRawLongBits >> 40));
        this.aByteArray[4] = (byte) ((int) (doubleToRawLongBits >> 32));
        this.aByteArray[5] = (byte) ((int) (doubleToRawLongBits >> 24));
        this.aByteArray[6] = (byte) ((int) (doubleToRawLongBits >> 16));
        this.aByteArray[7] = (byte) ((int) (doubleToRawLongBits >> 8));
        this.aByteArray[8] = (byte) ((int) (doubleToRawLongBits));
        this.bOutputStream.write(this.aByteArray, 0, 9);
        return this;
    }

    public pSerializationUtils cWrite(float f) throws IOException {
        this.aByteArray[0] = -54;
        int floatToRawIntBits = Float.floatToRawIntBits(f);
        this.aByteArray[1] = (byte) (floatToRawIntBits >> 24);
        this.aByteArray[2] = (byte) (floatToRawIntBits >> 16);
        this.aByteArray[3] = (byte) (floatToRawIntBits >> 8);
        this.aByteArray[4] = (byte) (floatToRawIntBits);
        this.bOutputStream.write(this.aByteArray, 0, 5);
        return this;
    }

    public pSerializationUtils cWrite(boolean z) throws IOException {
        return z ? cWriteTrue() : aWriteFalse();
    }

    public pSerializationUtils cWrite(byte[] bArr) throws IOException {
        fWriteWithAHeader(bArr.length);
        return bWriteArray(bArr, 0, bArr.length);
    }

    public pSerializationUtils dWrite(int i) throws IOException {
        if (i < -32) {
            if (i < -32768) {
                this.aByteArray[0] = -46;
                this.aByteArray[1] = (byte) (i >> 24);
                this.aByteArray[2] = (byte) (i >> 16);
                this.aByteArray[3] = (byte) (i >> 8);
                this.aByteArray[4] = (byte) (i);
                this.bOutputStream.write(this.aByteArray, 0, 5);
            } else if (i < -128) {
                this.aByteArray[0] = -47;
                this.aByteArray[1] = (byte) (i >> 8);
                this.aByteArray[2] = (byte) (i);
                this.bOutputStream.write(this.aByteArray, 0, 3);
            } else {
                this.aByteArray[0] = byteHeader;
                this.aByteArray[1] = (byte) i;
                this.bOutputStream.write(this.aByteArray, 0, 2);
            }
        } else if (i < 128) {
            this.bOutputStream.write((byte) i);
        } else if (i < 256) {
            this.aByteArray[0] = -52;
            this.aByteArray[1] = (byte) i;
            this.bOutputStream.write(this.aByteArray, 0, 2);
        } else if (i < 65536) {
            this.aByteArray[0] = -51;
            this.aByteArray[1] = (byte) (i >> 8);
            this.aByteArray[2] = (byte) (i);
            this.bOutputStream.write(this.aByteArray, 0, 3);
        } else {
            this.aByteArray[0] = -50;
            this.aByteArray[1] = (byte) (i >> 24);
            this.aByteArray[2] = (byte) (i >> 16);
            this.aByteArray[3] = (byte) (i >> 8);
            this.aByteArray[4] = (byte) (i);
            this.bOutputStream.write(this.aByteArray, 0, 5);
        }
        return this;
    }

    public pSerializationUtils dWriteArray(byte[] bArr) throws IOException {
        this.bOutputStream.write(bArr);
        return this;
    }

    public pSerializationUtils eWriteWith8Header(int i) throws IOException {
        if (i < 16) {
            this.bOutputStream.write((byte) (i | 128));
        } else if (i < 65536) {
            this.aByteArray[0] = -34;
            this.aByteArray[1] = (byte) (i >> 8);
            this.aByteArray[2] = (byte) (i);
            this.bOutputStream.write(this.aByteArray, 0, 3);
        } else {
            this.aByteArray[0] = -33;
            this.aByteArray[1] = (byte) (i >> 24);
            this.aByteArray[2] = (byte) (i >> 16);
            this.aByteArray[3] = (byte) (i >> 8);
            this.aByteArray[4] = (byte) (i);
            this.bOutputStream.write(this.aByteArray, 0, 5);
        }
        return this;
    }

    public pSerializationUtils fWriteWithAHeader(int i) throws IOException {
        if (i < 32) {
            this.bOutputStream.write((byte) (i | 160));
        } else if (i < 65536) {
            this.aByteArray[0] = -38;
            this.aByteArray[1] = (byte) (i >> 8);
            this.aByteArray[2] = (byte) (i);
            this.bOutputStream.write(this.aByteArray, 0, 3);
        } else {
            this.aByteArray[0] = -37;
            this.aByteArray[1] = (byte) (i >> 24);
            this.aByteArray[2] = (byte) (i >> 16);
            this.aByteArray[3] = (byte) (i >> 8);
            this.aByteArray[4] = (byte) (i);
            this.bOutputStream.write(this.aByteArray, 0, 5);
        }
        return this;
    }
}
