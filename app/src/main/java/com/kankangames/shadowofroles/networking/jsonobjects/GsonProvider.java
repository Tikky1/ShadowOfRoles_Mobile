package com.kankangames.shadowofroles.networking.jsonobjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.networking.jsonobjects.adapters.PlayerTypeAdapter;
import com.kankangames.shadowofroles.networking.jsonobjects.adapters.RoleTypeAdapter;

import java.lang.reflect.Type;
import java.util.List;

public class GsonProvider {
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(Player.class, new PlayerTypeAdapter())
                .registerTypeAdapter(RoleTemplate.class, new RoleTypeAdapter())
                .create();
    }

    public static Gson getGson() {
        return gson;
    }
    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        Type listType = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, listType);
    }


}
