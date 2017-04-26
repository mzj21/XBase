package com.xing.xbase.net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 上传
 * Created by mzj on 2017/3/13.
 */
public class ProgressRequestBody extends RequestBody {
    private RequestBody requestBody;
    private NetBase.ProgressCall progressCall;
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody, NetBase.ProgressCall progressCall) {
        this.requestBody = requestBody;
        this.progressCall = progressCall;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    /**
     * 写入，回调进度接口
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                progressCall.onProgress(bytesWritten, contentLength, (100 * bytesWritten) / contentLength, bytesWritten == contentLength);
            }
        };
    }
}
