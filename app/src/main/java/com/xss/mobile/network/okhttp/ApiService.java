package com.xss.mobile.network.okhttp;

import com.xss.mobile.activity.okhttp.AuthRequest;
import com.xss.mobile.entity.MovieEntity;

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
//import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by xss on 2017/3/28.
 */

public interface ApiService {

    @GET("/users/{userId}/login")
    Call<String> login(@Path("userId") String userId);

    // 只使用 Retrofit 返回的是一个 Call
    @GET("top250")
    Call<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);

    // 使用 RxJava 返回的是一个 Observable
    @GET("top250")
    Observable<MovieEntity> getTopMovie2(@Query("start") int start, @Query("count") int count);


    /**
     * HTTP - 方法注解：可以替代其他方法的任意一种
     * method - 表示请求的方法，不区分大小写
     * path - 表示路径
     * hasBody - 表示是否有请求体
     * @param user
     * @return
     */
    @HTTP(method = "get", path = "users/{user}", hasBody = false)
    Call<String> getFirstBlog(@Path("user") String user);

    /**
     * Streaming - 标记注解：用于下载大文件
     * Url 注解，需要使用全路径复写baseUrl，适用于非统一 baseUrl的场景
     *
     * 返回结果：
     * long fileSize = response.body().contentLength();
     * InputStream is = body.byteStream();
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);

    /**
     * Multipart - 标记注解：用于POST文件上传
     * 其中 @Part MultipartBody.Part 代表文件，@Part("key") RequestBody 代表参数，
     * 需要添加 @Multipart 表示支持文件上传的表单，Content-Type: multipart/form-data
     * @param desc
     * @param file
     * @return
     */
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadFile(@Part("description") RequestBody desc, @Part MultipartBody.Part file);


    @POST("NeedleWebProject/RedirectServlet")
    Call<ResponseBody> auth();  // @Body AuthRequest request
}
