package com.project.EMRChain.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil
{
    private Gson gson;

    private Gson getGson()
    {
        if(gson == null) {
            gson = new GsonBuilder().create();
        }

        return gson;
    }

    public String createJson(Object object) {
        return getGson().toJson(object);
    }
}
