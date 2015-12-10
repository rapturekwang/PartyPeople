//package com.partypeople.www.partypeople.utils;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.BufferedHttpEntity;
//
//import android.content.Context;
//
//import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
//import com.partypeople.www.partypeople.manager.PropertyManager;
//
///**
// * Implementation of ImageDownloader which uses {@link HttpClient} for image stream retrieving.
// *
// * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
// * @since 1.4.1
// */
//public class HttpClientImageDownloaderWithHeader extends BaseImageDownloader {
//
//    private HttpClient httpClient;
//
//    public HttpClientImageDownloaderWithHeader(Context context, HttpClient httpClient) {
//        super(context);
//        this.httpClient = httpClient;
//    }
//
//    @Override
//    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
//        HttpGet httpGet = new HttpGet(imageUri);
//        httpGet.addHeader("authorization" , "Bearer " + PropertyManager.getInstance().getToken());
//        HttpResponse response = httpClient.execute(httpGet);
//        HttpEntity entity = response.getEntity();
//        BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
//        return bufHttpEntity.getContent();
//    }
//}
