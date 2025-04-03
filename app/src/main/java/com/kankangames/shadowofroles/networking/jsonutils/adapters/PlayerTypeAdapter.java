package com.kankangames.shadowofroles.utils.jsonutils.adapters;

import com.google.gson.*;
import com.kankangames.shadowofroles.game.models.player.AIPlayer;
import com.kankangames.shadowofroles.game.models.player.HumanPlayer;
import com.kankangames.shadowofroles.game.models.player.Player;

import java.lang.reflect.Type;

public class PlayerTypeAdapter implements JsonDeserializer<Player> {

    @Override
    public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        boolean isPlayerAI = jsonObject.get("isAI").getAsBoolean();

        if (isPlayerAI) {
            return context.deserialize(jsonObject, AIPlayer.class);
        } else {
            return context.deserialize(jsonObject, HumanPlayer.class);
        }
    }
}
