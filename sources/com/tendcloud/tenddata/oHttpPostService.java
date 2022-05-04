package com.tendcloud.tenddata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

public final class oHttpPostService {
    private static final String aUrl = "http://tdcv3.talkingdata.net";
    private static final String bEndpoint = "/g/d";
    private static final int cTimeout = 60000;
    private static final boolean dStatusOk = true;

    static DefaultHttpClient aCreateHttpClient() {
        HttpHost d2;
        int timeout = cTimeout;
        boolean shorterTimeout = uDeviceUtils.bIsWifiConnected();
        new SchemeRegistry().register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, shorterTimeout ? cTimeout : 120000);

        if (!shorterTimeout) {
            timeout = 120000;
        }

        HttpConnectionParams.setSoTimeout(basicHttpParams, timeout);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
        if (!shorterTimeout && uDeviceUtils.cDoesProxyExist() && (d2 = uDeviceUtils.dGetDefaultHttpHost()) != null) {
            defaultHttpClient.getParams().setParameter("http.route.default-proxy", d2);
        }

        return defaultHttpClient;
    }

    static boolean aPostArchived(ahDeviceAndPackageInfo ahDeviceAndPackageInfo) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        new pSerializationUtils(gZIPOutputStream).aWrite(ahDeviceAndPackageInfo);
        gZIPOutputStream.finish();
        gZIPOutputStream.flush();
        return aPostBytes(bEndpoint, byteArrayOutputStream.toByteArray(), dStatusOk);
    }

    static boolean aPostBytes(String str, byte[] bArr, boolean z) {
        DefaultHttpClient a2 = aCreateHttpClient();
        try {
            HttpPost httpPost = new HttpPost(aUrl + str);
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bArr);
            byteArrayEntity.setContentType("application/unpack_chinar");
            httpPost.setEntity(byteArrayEntity);

            if (a2.execute(httpPost).getStatusLine().getStatusCode() == 200) {
                return dStatusOk;
            }

            return false;
        } catch (Exception ignored) {
        }
    }
}
