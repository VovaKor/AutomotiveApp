/*
 * Copyright (c) 2017.  | Volodymyr Korobko | volodymyr.korobko@gmail.com
 */

package com.korobko.automotiveapp.client.repository.shared.modules;

import android.content.SharedPreferences;

import com.korobko.automotiveapp.client.repository.shared.JsonPreference;
import com.korobko.automotiveapp.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vova on 14.08.17.
 */
@Module(includes = InjectionModule.class)
public class PrefsModule {

    @Provides
    @Singleton
    public JsonPreference provideJsonPreference(SharedPreferences prefs) {
        return new JsonPreference(prefs, Constants.KEY_JSON_STRING, "");
    }
}
