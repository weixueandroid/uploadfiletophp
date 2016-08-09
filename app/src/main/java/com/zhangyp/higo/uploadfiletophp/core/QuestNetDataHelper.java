package com.zhangyp.higo.uploadfiletophp.core;


import android.util.Log;

import com.squareup.otto.Bus;
import com.zhangyp.higo.uploadfiletophp.api.ApiService;
import com.zhangyp.higo.uploadfiletophp.api.Client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import rx.schedulers.Schedulers;

/**
 * Created by zhangyipeng on 16/2/18.
 */
public class QuestNetDataHelper extends CancelQueue {

    private ApiService mApi;
    private Bus mBus;

    public QuestNetDataHelper() {
        mApi = Client.getServiceClient();
        mBus = BusProvider.getBus();
    }

    public void uploadFile(String path) {
        TypedFile typedFile = new TypedFile("", new File(path));
        mApi.uploadFile(typedFile)
                .subscribeOn(Schedulers.computation())
                .subscribe(response -> handleUploadFile(response), error -> handleFailure(error));
    }
    public void uploadFiles(List<String> paths) {
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        for (String imgPath : paths){
            multipartTypedOutput.addPart("uploadfile[]", new TypedFile("", new File(imgPath)));
        }
        mApi.uploadFiles(multipartTypedOutput)
                .subscribeOn(Schedulers.computation())
                .subscribe(response -> handleUploadFile(response), error -> handleFailure(error));
    }



    private void handleUploadFile(Response response) {

        System.out.println(response.getUrl());
        try {
            InputStream in = response.getBody().in();
            String responseString = inputStream2String(in);
            Log.i("AAA", responseString);
            //解析json数据

            //otto事件传递
            mBus.post(responseString);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static String inputStream2String(InputStream in) {
        String string = "";
        try {
            byte[] buf = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int i; (i = in.read(buf)) != -1; ) {
                baos.write(buf, 0, i);
            }
            string = baos.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }


    private void handleFailure(Throwable error) {
        mBus.post("获取列表失败");
    }


}
