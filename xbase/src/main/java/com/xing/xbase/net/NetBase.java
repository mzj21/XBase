package com.xing.xbase.net;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by mzj on 2017/3/16.
 */

public class NetBase {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*");
    public static String DEFAULT_UA = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)";
    protected static OkHttpClient okHttpClient;
    protected static Gson gson;

    public static abstract class NetCallBack<T> implements NetCallBackBase<T> {
    }

    public static abstract class ProgressCall<T> implements ProgressCallBase<T> {
    }

    public enum Method {
        GET, POST, PUT, DELETE
    }

    /**
     * 需要初始化
     */
    public static void init() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        if (gson == null) {
            gson = new Gson();
        }
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static void setOkHttpClient(OkHttpClient client) {
        okHttpClient = client;
    }

    public static OkHttpClient getProgressClient(final ProgressCall progressCall) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressCall))
                        .build();
            }
        };

        return new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();
    }

    public static class NetData<T> {
        private int code;
        private String msg;
        private T data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
