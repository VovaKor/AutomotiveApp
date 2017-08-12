/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.server;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.korobko.automotiveapp.AutomotiveApp;
import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.restapi.Driver;
import com.korobko.automotiveapp.utils.Constants;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.korobko.automotiveapp.utils.Constants.ID_REGEXP;
import static com.korobko.automotiveapp.utils.Constants.PORT;
import static com.korobko.automotiveapp.utils.Constants.SLASH;
import static com.korobko.automotiveapp.utils.Constants.URL_DRIVERS_ADD;
import static com.korobko.automotiveapp.utils.Constants.URL_DRIVERS;
import static com.korobko.automotiveapp.utils.Constants.URL_DRIVERS_DELETE;
import static com.korobko.automotiveapp.utils.Constants.URL_DRIVERS_UPDATE;

public class AMService extends Service {

    private AsyncHttpServer mServer;


    public void onCreate() {
        super.onCreate();
        mServer = new AsyncHttpServer();

        CharSequence text = getText(R.string.server_service_started);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.app_name))
                .setContentText(text)
                .build();

        startForeground(Constants.NOTIFICATION_ID, notification);

    }

    public void onDestroy() {
        super.onDestroy();
        mServer.stop();
        // Tell the user we stopped.
        Toast.makeText(this, R.string.server_service_stopped, Toast.LENGTH_SHORT).show();


    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        startServer();
        return START_STICKY;
    }

    private void startServer() {

        mServer.get(URL_DRIVERS+SLASH+ID_REGEXP, (request, response) -> {
            String driverId = request.getPath().replace(URL_DRIVERS+SLASH,"");
//            String json = AutomotiveApp.getGson().toJson(new Driver("","","","",""));
//            try {
//                JSONObject jsonObject = new JSONObject("");
//                response.send(jsonObject);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            Log.d(this.getPackageName(),"Driver id from get request "+driverId);
        });

        mServer.get(URL_DRIVERS, (request, response) -> {
            List<Driver> drivers = new ArrayList<Driver>();
            for (int i = 0; i < 100 ; i++) {
                Driver driver = new Driver("driver"+i+"@driver.com",
                        "Test first name "+i, "Test last name "+i, "555-55-5"+i,"ADSF3456"+i);
                drivers.add(driver);
            }

            String json = AutomotiveApp.getGson().toJson(drivers);
            response.send(json);
            Log.d(this.getPackageName(),json);
        });


        mServer.post(URL_DRIVERS_ADD, (request, response) -> {
            Log.d(this.toString(), "server post onRequest: "+request.getBody().toString());
            final JSONObjectBody body = (JSONObjectBody) request.getBody();

        });

        mServer.post(URL_DRIVERS_UPDATE, (request, response) -> {
            String json = (String) request.getBody().get();
            Driver driver = AutomotiveApp.getGson().fromJson(json, Driver.class);
            Log.d(this.getPackageName(),URL_DRIVERS_UPDATE+driver.toString());
        });

        mServer.get(URL_DRIVERS_DELETE+ID_REGEXP, (request, response) -> {
            String driverId = request.getPath().replace(URL_DRIVERS_DELETE,"");
            Log.d(this.getPackageName(),"Driver id from delete request "+driverId);
        });

        mServer.listen(PORT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
