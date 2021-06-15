package no.fint.xledger;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        log.info("OkHttp", String.format("--> Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));


        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        log.info("OkHttp", String.format("<-- Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        MediaType contentType = response.body().contentType();
        String content = response.body().string();
        log.info("OkHttp", content);

        ResponseBody wrappedBody = ResponseBody.create(contentType, content);
        return response.newBuilder().body(wrappedBody).build();
    }
}