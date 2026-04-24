package com.deepseek.helper.service.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonProvider {

    private static final Gson INSTANCE = new GsonBuilder().create();

    private GsonProvider() {
    }

    public static Gson get() {
        return INSTANCE;
    }
}
