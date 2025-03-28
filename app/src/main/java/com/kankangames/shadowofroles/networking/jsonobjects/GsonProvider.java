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

public final class GsonProvider {
    private GsonProvider(){}
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(Player.class, new PlayerTypeAdapter())
                .registerTypeAdapter(RoleTemplate.class, new RoleTypeAdapter())
                .enableComplexMapKeySerialization()
                .create();
    }

    public static Gson getGson() {
        return gson;
    }

}
