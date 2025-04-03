package com.kankangames.shadowofroles.networking.jsonutils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.networking.jsonutils.adapters.PlayerTypeAdapter;
import com.kankangames.shadowofroles.networking.jsonutils.adapters.RoleTypeAdapter;

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
