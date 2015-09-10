package com.renyu.nj_tran.common;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by renyu on 15/9/10.
 */
public class HttpUtils {

    static OkHttpClient client=null;

    public static OkHttpClient getInstance() {
        synchronized (HttpUtils.class) {
            if (client==null) {
                synchronized (HttpUtils.class) {
                    client=new OkHttpClient();
                    client.setConnectTimeout(10, TimeUnit.SECONDS);
                    client.setReadTimeout(10, TimeUnit.SECONDS);
                    client.setWriteTimeout(10, TimeUnit.SECONDS);
                }
            }
        }
        return client;
    }


    public interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }

    public static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override public long contentLength() throws IOException {
            return responseBody.contentLength();
        }

        @Override public BufferedSource source() throws IOException {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;
                @Override public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

}
