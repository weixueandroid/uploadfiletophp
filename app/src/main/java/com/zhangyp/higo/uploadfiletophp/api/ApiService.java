package com.zhangyp.higo.uploadfiletophp.api;


import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import rx.Observable;

/**
 * Created by zhangyipeng on 16/2/18.
 */
public interface ApiService {
//   上传一个文件/图片
    @Multipart
    @POST("/UploadFileDemo/android_upload_file/upload.php")
    Observable<Response> uploadFile(@Part("uploadfile") TypedFile file);

//  上传多张图片
    @POST("/UploadFileDemo/android_upload_file/uploads.php")
    Observable<Response> uploadFiles(@Body MultipartTypedOutput multipartTypedOutput);

}
