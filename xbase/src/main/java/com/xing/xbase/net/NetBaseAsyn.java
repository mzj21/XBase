package com.xing.xbase.net;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mzj on 2017/1/23.
 * 异步
 */
public class NetBaseAsyn extends NetBase {
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

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netCallBack.onFailure(call, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    NetData<T> netData = new NetData<>();
                    netData.setCode(response.code());
                    netData.setMsg(response.message());
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
                    netCallBack.onFailure(call, response);
                }
            }
        });
    }

    /**
     * 下载
     *
     * @param method       请求方式
     * @param url          地址
     * @param params       请求参数
     * @param saveFile     保存目录
     * @param progressCall 监听
     */
    public static <T> void download(Method method, final String url, Map<String, String> params, final String saveFile, final ProgressCall<T> progressCall) {
        Type type = progressCall.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        final Class<T> clazz = (Class<T>) (parameterizedType.getActualTypeArguments()[0]);

        Request request;
        switch (method) {
            case POST:
                FormBody.Builder builder = new FormBody.Builder();
                if (params != null) {
                    for (String key : params.keySet()) {
                        builder.add(key, params.get(key));
                    }
                }
                RequestBody requestBody = builder.build();
                request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", DEFAULT_UA)
                        .post(new ProgressRequestBody(requestBody, progressCall))
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

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressCall.onFileFailure();
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    String result = null;
                    try {
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    T single = gson.fromJson(result, clazz);
                    progressCall.onSuccess(single);
                } else {
                    progressCall.onFileFailure();
                }
            }
        });
    }

    /**
     * 上传
     *
     * @param url          地址
     * @param params       参数
     * @param file_key     文件上传对应参数
     * @param file         文件
     * @param mediaType    文件类型
     * @param progressCall 监听
     */
    public static <T> void upload(String url, Map<String, String> params, String file_key, File file, MediaType mediaType, final ProgressCall<T> progressCall) {
        Type type = progressCall.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        final Class<T> clazz = (Class<T>) (parameterizedType.getActualTypeArguments()[0]);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
        builder.addFormDataPart(file_key, file.getName(), RequestBody.create(mediaType, file));
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", DEFAULT_UA)
                .post(new ProgressRequestBody(requestBody, progressCall))
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressCall.onFileFailure();
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    String result = null;
                    try {
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    T single = gson.fromJson(result, clazz);
                    progressCall.onSuccess(single);
                } else {
                    progressCall.onFileFailure();
                }
            }
        });
    }
}
