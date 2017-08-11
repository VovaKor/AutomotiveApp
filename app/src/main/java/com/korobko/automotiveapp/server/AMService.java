/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.server;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.korobko.automotiveapp.R;
import com.korobko.automotiveapp.utils.Constants;
import com.koushikdutta.async.http.AsyncHttpPut;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import static com.korobko.automotiveapp.utils.Constants.PORT;

public class AMService extends Service {

    private AsyncHttpServer mServer;
    //ExecutorService es;
    Object someRes;

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

      //es = Executors.newFixedThreadPool(1);
        someRes = new Object();
    }

    public void onDestroy() {
        super.onDestroy();
        // Tell the user we stopped.
        Toast.makeText(this, R.string.server_service_stopped, Toast.LENGTH_SHORT).show();
        mServer = null;

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        startServer();
       // int time = intent.getIntExtra("time", 1);
      //  MyRun mr = new MyRun(time, startId);
      //  es.execute(mr);
        return START_STICKY;
    }

    private void startServer() {

        mServer.get("\\/api\\/v1\\/drivers\\?id=\\w+@\\w+\\.[a-z]+", (request, response) -> {
            String driverId = request.getQuery().toString();
            response.send("Driver id = "+driverId);
        });

        mServer.get("/api/v1/drivers", (request, response) -> {
            response.send("All drivers retrieved!!!");
          //  response.send(json);
        });


        mServer.post("/api/v1/drivers/add", (request, response) -> {
            request.getBody().get();
        });

        mServer.post("/api/v1/drivers/update", (request, response) -> {
            request.getBody().get();
        });

        mServer.get("/api/v1/drivers/delete/\\w+@\\w+\\.[a-z]+", (request, response) -> {
            String driverId = request.getPath();
            response.send("Driver deleted id = "+driverId);
        });

        mServer.listen(PORT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
