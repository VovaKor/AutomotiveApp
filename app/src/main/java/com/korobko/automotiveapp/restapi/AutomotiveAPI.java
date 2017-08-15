/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.restapi;

import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;
import com.korobko.automotiveapp.utils.Constants;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by vova on 11.08.17.
 */

public interface AutomotiveAPI {

    @GET(Constants.URL_GET_ALL_REGISTRATION_CARDS +Constants.SLASH+"{id}")
    Single<RegistrationCard> getRegistrationCardById(@Path("id") String registrationCardId);

    @GET(Constants.URL_DELETE_REGISTRATION_CARD +Constants.SLASH+"{id}")
    Single<RegistrationCard> deleteRegistrationCardById(@Path("id") String registrationCardId);

    @GET(Constants.URL_GET_ALL_REGISTRATION_CARDS)
    Single<List<RegistrationCard>> getRegistrationCards();

    @POST(Constants.URL_ADD_REGISTRATION_CARD)
    Single<ResponseBody> createRegistrationCard(@Body RegistrationCard registrationCard);

    @POST(Constants.URL_UPDATE_REGISTRATION_CARD)
    Single<RegistrationCard> updateRegistrationCard(@Body RegistrationCard registrationCard);

}
