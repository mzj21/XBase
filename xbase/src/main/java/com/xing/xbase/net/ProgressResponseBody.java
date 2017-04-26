package com.xing.xbase.net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 下载
 * Created by mzj on 2017/3/13.
 */
public class ProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private NetBase.ProgressCall progressCall;
    private BufferedSource bufferedSource;

    /**
     * 下载拦截器构造
     */
    public ProgressResponseBody(ResponseBody responseBody, NetBase.ProgressCall progressCall) {
        this.responseBody = responseBody;
        this.progressCall = progressCall;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 读取，回调进度接口
     */
    private Source source(Source source) {
        return new ForwardingSource(source) {
            //当前读取字节数
            long totalBytesRead = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                //回调，如果contentLength()不知道长度，会返回-1
                progressCall.onProgress(totalBytesRead, contentLength, (100 * totalBytesRead) / contentLength, bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
