package com.project.EMRChain.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil
{
    private static Gson gson;

    private static Gson getGson()
    {
        if(gson == null) {
            gson = new GsonBuilder().create();
        }

        return gson;
    }

    public static String createJson(Object object) {
        return getGson().toJson(object);
    }
}