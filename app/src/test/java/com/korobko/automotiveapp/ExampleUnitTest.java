/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.korobko.automotiveapp.models.Driver;
import com.korobko.automotiveapp.models.RegistrationCard;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void jsonTest() throws Exception {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        int i = 1;
        RegistrationCard card = new RegistrationCard();
        card.setRegistrationNumber("driver"+i+"@driver.com");
        Driver driver = new Driver(UUID.randomUUID().toString(),
                "Test first name "+i, "Test last name "+i, "555-55-5"+i,"ADSF3456"+i);
        card.setDriver(driver);
        card.setCars(new ArrayList<>());
        String json = gson.toJson(card);
        System.out.println(json);
        RegistrationCard card1 = gson.fromJson(json, RegistrationCard.class);
        System.out.println(card1.toString());

    }
}