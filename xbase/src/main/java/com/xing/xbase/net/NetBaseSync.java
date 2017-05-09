package com.xing.xbase.net;

import com.google.gson.Gson;
import com.xing.xbase.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mzj on 2017/3/9.
 * 同步
 */
public class NetBaseSync extends NetBase {
    /**
     * 网络请求
     *
     * @param method      请求方式
     * @param url         地址
     * @param requestBody 请求参数
     * @param netCallBack 监听
     * @param <T>         解析类型
     */
    public static <T> void execute(Method method, String url, RequestBody requestBody, final NetCallBack<T> netCallBack) {
        Type type = netCallBack.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        final Class<T> clazz = (Class<T>) (parameterizedType.getActualTypeArguments()[0]);

        Request request;
        switch (method) {
            case POST:
                request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", DEFAULT_UA)
                        .post(requestBody)
                        .build();
                break;
            case GET:
                request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", DEFAULT_UA)
                        .get()
                        .build();
                break;
            default:
                return;
        }

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            if (response.isSuccessful()) {
                NetData<T> netData = new NetData<>();
                netData.setCode(response.code());
                netData.setMsg(response.message());
                if (clazz.isAssignableFrom(Void.class)) {
                    return;
                }
                Gson gson = new Gson();
                if (clazz.isAssignableFrom(Void.class)) {
                    return;
                }
                T single;
                if (clazz.isAssignableFrom(String.class)) {
                    single = (T) result;
                } else {
                    single = gson.fromJson(result, clazz);
                }
                netData.setData(single);
                netCallBack.onSuccess(response.code(), netData);
            } else {
                netCallBack.onFailure(call, result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void fromUpLoad(File file, String url, String policy, String signature, ProgressCall<T> progressCall) {
        Type type = progressCall.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        final Class<T> clazz = (Class<T>) (parameterizedType.getActualTypeArguments()[0]);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(null, file))
                .addFormDataPart("policy", policy)
                .addFormDataPart("signature", signature)
                .build();
        Request request = new Request.Builder()
                .addHeader("x-upyun-api-version", "2")
                .url(url)
                .post(new ProgressRequestBody(requestBody, progressCall))
                .build();

        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            String result = response.body().string();
            if (clazz.isAssignableFrom(Void.class)) {
                return;
            }
            Gson gson = new Gson();
            T single = gson.fromJson(result, clazz);
            progressCall.onSuccess(single);
        } catch (IOException e) {
            e.printStackTrace();
            progressCall.onFileFailure();
        }
    }
}
