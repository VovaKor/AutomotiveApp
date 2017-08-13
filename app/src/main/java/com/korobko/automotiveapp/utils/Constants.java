/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.utils;

/**
 * Created by vova on 09.08.17.
 */

public class Constants {

    //Server side constants
    public static final String DATABASE_NAME = "automotive.db";
    public static final int NOTIFICATION_ID = 1;
    public static final int PORT = 5000;
    public static final int HTTP_CODE_OK = 200;
    public static final int HTTP_CODE_NOT_FOUND = 404;
    public static final int HTTP_CODE_CREATED = 201;
    public static final int HTTP_CODE_INTERNAL_SERVER_ERROR = 500;
    public static final String URL_BASE = "http://localhost:"+PORT;
    public static final String URL_GET_ALL_REGISTRATION_CARDS = "/api/v1/drivers";
    public static final String REGEXP_CARD_ID = "[0-9a-f]{8}-[0-9a-f]{4}-[34][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";
    public static final String SLASH = "/";
    public static final String URL_ADD_REGISTRATION_CARD = "/api/v1/drivers/add";
    public static final String URL_UPDATE_REGISTRATION_CARD = "/api/v1/drivers/update";
    public static final String URL_DELETE_REGISTRATION_CARD = "/api/v1/drivers/delete/";
    //Client side constants
    public static final int REQUEST_CODE_ADD_DRIVER = 1;
    public static final String EXTRA_DRIVER_ID = "DRIVER_ID";
    public static final String ARGUMENT_DRIVER_ID = "DRIVER_ID";
    public static final int REQUEST_CODE_EDIT_DRIVER = 1;
    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";
    public static final String EXTRA_EDIT_DRIVER_ID = "EDIT_DRIVER_ID";
    public static final String REGEXP_EMAIL = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

}
