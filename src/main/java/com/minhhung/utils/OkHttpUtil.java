package com.minhhung.utils;

import com.squareup.okhttp.*;

import java.io.IOException;

public class OkHttpUtil {

    private OkHttpClient client = new OkHttpClient();

    // code request code here
    public String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    // post request code here
    public String doPostRequest(String url, String json) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
