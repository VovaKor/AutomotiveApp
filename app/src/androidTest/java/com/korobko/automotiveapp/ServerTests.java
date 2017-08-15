/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.util.Log;

import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;
import com.korobko.automotiveapp.utils.Constants;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.body.StringBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * Created by vova on 12.08.17.
 */
@RunWith(AndroidJUnit4.class)
public class ServerTests extends AndroidTestCase {
    AsyncHttpServer httpServer;
    private String json;
    private RegistrationCard card;
    private String responseJson;
    private String jsonResult;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        httpServer = new AsyncHttpServer();
        int i = 1;
        Driver driver = new Driver(UUID.randomUUID().toString(),
                "Test first name "+i, "Test last name "+i, "555-55-5"+i,"ADSF3456"+i);
        card = new RegistrationCard();
        card.setRegistrationNumber("driver"+i+"@driver.com");
        card.setDriver(driver);
        json = AutomotiveApp.getGson().toJson(card);


        httpServer.post(Constants.URL_ADD_REGISTRATION_CARD, new HttpServerRequestCallback() {

            @Override
            public void onRequest(final AsyncHttpServerRequest request, final AsyncHttpServerResponse response) {
                Log.d(this.toString(), "server post onRequest: "+request.getBody().toString());
                final JSONObjectBody body = (JSONObjectBody) request.getBody();
                assertEquals(json, body.get().toString());
                request.setEndCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        response.send(body.toString());
                       // response.send(request.getBody().toString());
                    }
                });
            }
        });
        httpServer.get(Constants.URL_GET_ALL_REGISTRATION_CARDS,(request, response) -> response.send("test"));
        httpServer.listen(Constants.PORT);
    }

    @Test
    public void testNull() throws Exception {
        assertNull(httpServer);

    }

    @Test
    public void testGet() throws Exception {
        if (httpServer == null) {
            setUp();
        }
        AsyncHttpGet request = new AsyncHttpGet(Constants.URL_BASE+Constants.URL_GET_ALL_REGISTRATION_CARDS);
        AsyncHttpClient.getDefaultInstance().executeString(request, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                Log.d(this.toString(), "onCompleted: "+result);
                assertEquals(result, "test");
                Log.d(this.toString(), "2 onCompleted: "+source);
               // assertEquals(source.message(), "test");
            }
        });
//
    }

    @Test
    public void testUpload() throws Exception {
        if (httpServer == null) {
            setUp();
        }
//
//        final String FIELD_VAL = "bar";

        AsyncHttpPost post = new AsyncHttpPost(Constants.URL_BASE+Constants.URL_ADD_REGISTRATION_CARD);

        //        JSONObject jsonObject = new JSONObject(json);
//        JSONObjectBody jsonObjectBody = new JSONObjectBody(jsonObject);
        StringBody stringBody = new StringBody(json);
//        MultipartFormDataBody body = new MultipartFormDataBody();
//        body.addStringPart("foo", FIELD_VAL);
//        body.addStringPart("baz", FIELD_VAL);
        post.setBody(stringBody);
        Future<String> future = AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                //jsonResult = result;
                Log.d(this.toString(), "testUpload onCompleted post : "+result);

            }
        });
        String res = future.get(10000, TimeUnit.MILLISECONDS);
        assertEquals(json, res);
        assertEquals(json, jsonResult);

    }
    @Test
    public void testRetrofit() throws Exception {
        if (httpServer == null) {
            setUp();
        }

        new CompositeDisposable().add(AutomotiveApp.getAutomotiveAPI()
                .createRegistrationCard(card)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        try {
                            responseJson = value.string();
                            Log.d(this.toString(), "retrofit post : "+value.toString());
                            assertEquals("Must be equals",json,responseJson);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }
                })
        );

    }
}
