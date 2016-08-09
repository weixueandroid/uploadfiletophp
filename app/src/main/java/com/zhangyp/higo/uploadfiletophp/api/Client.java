package com.zhangyp.higo.uploadfiletophp.api;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by ihgoo on 2015/5/18.
 */
public class Client {

    public static String API_URL = Constant.API_URL;

    public static ExecutorService mExecutorService;

    private static ApiService instance;

    public static ApiService getServiceClient() {
        if (instance == null) {
            synchronized (Client.class) {
                if (instance == null) {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    okHttpClient.setCookieHandler(new MyCookieManager());
                    okHttpClient.setReadTimeout(100, TimeUnit.SECONDS);
                    okHttpClient.setConnectTimeout(100, TimeUnit.SECONDS);

                    RestAdapter.Builder restAdapter = new RestAdapter.Builder();

                    restAdapter.setRequestInterceptor(new ApiHeaders());
                    restAdapter.setEndpoint(API_URL);
                    restAdapter.setClient(new OkClient(okHttpClient));
                    restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

                    mExecutorService = Executors.newCachedThreadPool();
                    instance = restAdapter.build().create(ApiService.class);
                }
            }
        }
        return instance;
    }

    public static void stopAll() {
        List<Runnable> pendingAndOngoing = mExecutorService.shutdownNow();
    }

    static class MyCookieManager extends CookieManager {

        //保存到首选项
        @Override
        public void put(URI uri, Map<String, List<String>> stringListMap) throws IOException {
            super.put(uri, stringListMap);
            if (stringListMap != null && stringListMap.get("Set-Cookie") != null)
                for (String string : stringListMap.get("Set-Cookie")) {
                    String cookieValue = string.substring(0, string.indexOf("\"; ") + 1);
                    if (string.contains("A2=\"")) {

                    }
                    if (string.contains("PB3_SESSION=\"")) {

                    }
                }
        }
    }

    static class ApiHeaders implements RequestInterceptor {

        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.1.1; zh-cn; HTC One X - 4.1.1 - API 16 - 720x1280 Build/JRO03S) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
            request.addHeader("Accept", "*/*");
            request.addHeader("Accept-Language", "zh-cn,zh");
        }
    }

}
