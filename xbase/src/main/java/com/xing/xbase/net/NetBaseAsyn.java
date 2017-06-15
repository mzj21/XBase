package com.xing.xbase.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
                netCallBack.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (response.isSuccessful()) {
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
                    netCallBack.onFailure(call, result);
                }
            }
        });
    }

    /**
     * 下载
     *
     * @param url          地址
     * @param startsPoint  开始下载的点
     * @param saveFile     保存的目录
     * @param saveName     保存的文件名
     * @param progressCall 监听
     */
    public static void download(final String url, final long startsPoint, final String saveFile, final String saveName, final ProgressCall progressCall) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", DEFAULT_UA)
                .get()
                .build();

        getProgressClient(progressCall).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressCall.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody body = response.body();
                InputStream in = body.byteStream();
                FileChannel channelOut = null;
                // 随机访问文件，可以指定断点续传的起始位置
                RandomAccessFile randomAccessFile = null;
                File file = new File(saveFile + "/" + saveName);
                try {
                    randomAccessFile = new RandomAccessFile(file, "rwd");
                    //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
                    channelOut = randomAccessFile.getChannel();
                    // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
                    MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, body.contentLength());
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        mappedBuffer.put(buffer, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                        if (channelOut != null) {
                            channelOut.close();
                        }
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                progressCall.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response.isSuccessful()) {
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
                    progressCall.onSuccess(response.code(), netData);
                } else {
                    progressCall.onFailure(call, result);
                }
            }
        });
    }
}
