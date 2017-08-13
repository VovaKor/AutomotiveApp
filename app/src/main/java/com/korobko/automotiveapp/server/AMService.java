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
import com.korobko.automotiveapp.restapi.RegistrationCard;
import com.korobko.automotiveapp.server.repository.DataSource;
import com.korobko.automotiveapp.server.repository.LocalDataSource;
import com.korobko.automotiveapp.utils.Constants;
import com.koushikdutta.async.http.body.JSONObjectBody;
import com.koushikdutta.async.http.server.AsyncHttpServer;

import java.util.List;

import static com.korobko.automotiveapp.utils.Constants.HTTP_CODE_CREATED;
import static com.korobko.automotiveapp.utils.Constants.HTTP_CODE_INTERNAL_SERVER_ERROR;
import static com.korobko.automotiveapp.utils.Constants.HTTP_CODE_NOT_FOUND;
import static com.korobko.automotiveapp.utils.Constants.HTTP_CODE_OK;
import static com.korobko.automotiveapp.utils.Constants.REGEXP_CARD_ID;
import static com.korobko.automotiveapp.utils.Constants.PORT;
import static com.korobko.automotiveapp.utils.Constants.SLASH;
import static com.korobko.automotiveapp.utils.Constants.URL_ADD_REGISTRATION_CARD;
import static com.korobko.automotiveapp.utils.Constants.URL_GET_ALL_REGISTRATION_CARDS;
import static com.korobko.automotiveapp.utils.Constants.URL_DELETE_REGISTRATION_CARD;
import static com.korobko.automotiveapp.utils.Constants.URL_UPDATE_REGISTRATION_CARD;

public class AMService extends Service {

    private AsyncHttpServer mServer;
    private LocalDataSource mLocalDataSource;

    public void onCreate() {
        super.onCreate();
        mServer = new AsyncHttpServer();
        mLocalDataSource = LocalDataSource.getInstance(getApplicationContext());
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

        mServer.get(URL_GET_ALL_REGISTRATION_CARDS+SLASH+ REGEXP_CARD_ID, (request, response) -> {
            String cardId = request.getPath().replace(URL_GET_ALL_REGISTRATION_CARDS+SLASH,"");
            mLocalDataSource.getRegistrationCard(cardId, new DataSource.GetCardCallback() {
                @Override
                public void onCardLoaded(RegistrationCard registrationCard) {
                    String json = AutomotiveApp.getGson().toJson(registrationCard);
                    response.send(json);
                }

                @Override
                public void onDataNotAvailable() {
                    response.code(HTTP_CODE_NOT_FOUND).end();
                }
            });

        });

        mServer.get(URL_GET_ALL_REGISTRATION_CARDS, (request, response) -> {
            mLocalDataSource.getRegistrationCards(new DataSource.LoadCardsCallback() {
                @Override
                public void onCardsLoaded(List<RegistrationCard> registrationCards) {
                    String json = AutomotiveApp.getGson().toJson(registrationCards);
                    response.send(json);
                }

                @Override
                public void onDataNotAvailable() {
                    response.code(HTTP_CODE_NOT_FOUND).end();
                }
            });

        });


        mServer.post(URL_ADD_REGISTRATION_CARD, (request, response) -> {
            final JSONObjectBody body = (JSONObjectBody) request.getBody();
            String json = body.get().toString();
            RegistrationCard card = AutomotiveApp.getGson().fromJson(json, RegistrationCard.class);
            mLocalDataSource.createRegistrationCard(card, new DataSource.SaveCardCallback() {
                @Override
                public void onSuccess() {
                    response.code(HTTP_CODE_CREATED).end();
                }

                @Override
                public void onError() {
                    response.code(HTTP_CODE_INTERNAL_SERVER_ERROR).end();
                }
            });
        });

        mServer.post(URL_UPDATE_REGISTRATION_CARD, (request, response) -> {
            final JSONObjectBody body = (JSONObjectBody) request.getBody();
            String json = body.get().toString();
            RegistrationCard card = AutomotiveApp.getGson().fromJson(json, RegistrationCard.class);
            mLocalDataSource.saveRegistrationCard(card, new DataSource.GetCardCallback() {
                @Override
                public void onCardLoaded(RegistrationCard registrationCard) {

                    //There must be some logic on the client to check if update was successful
                    String json = AutomotiveApp.getGson().toJson(registrationCard);
                    response.send(json);
                }

                @Override
                public void onDataNotAvailable() {
                    response.code(HTTP_CODE_INTERNAL_SERVER_ERROR).end();
                }
            });
        });

        mServer.get(URL_DELETE_REGISTRATION_CARD+REGEXP_CARD_ID, (request, response) -> {
            String cardId = request.getPath().replace(URL_DELETE_REGISTRATION_CARD,"");
            mLocalDataSource.deleteRegistrationCard(cardId, new DataSource.DeleteCardCallback() {
                @Override
                public void onSuccess() {
                    response.code(HTTP_CODE_OK).end();
                }

                @Override
                public void onError() {
                    response.code(HTTP_CODE_NOT_FOUND).end();
                }
            });
        });

        mServer.listen(PORT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
