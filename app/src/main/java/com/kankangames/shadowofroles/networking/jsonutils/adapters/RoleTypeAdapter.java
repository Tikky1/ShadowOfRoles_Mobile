package com.kankangames.shadowofroles.networking.jsonutils.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.analyst.DarkRevealer;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.analyst.Darkseer;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.killing.Psycho;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.support.Blinder;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.support.Disguiser;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.support.Interrupter;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.support.LastJoke;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.analyst.Detective;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.analyst.Observer;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.analyst.Stalker;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.protector.FolkHero;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.protector.Soulbinder;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.support.SealMaster;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.chaos.ChillGuy;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.chaos.Clown;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.unique.Entrepreneur;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.good.Lorekeeper;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.killing.Assassin;


import java.lang.reflect.Type;

public class RoleTypeAdapter implements JsonDeserializer<RoleTemplate> {

    @Override
    public RoleTemplate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        RoleID roleID = RoleID.valueOf(jsonObject.get("id").getAsString());

        switch (roleID) {
            case DETECTIVE:
                return context.deserialize(jsonObject, Detective.class);
            case OBSERVER:
                return context.deserialize(jsonObject, Observer.class);
            case PSYCHO:
                return context.deserialize(jsonObject, Psycho.class);
            case SOUL_BINDER:
                return context.deserialize(jsonObject, Soulbinder.class);
            case STALKER:
                return context.deserialize(jsonObject, Stalker.class);
            case DARK_REVEALER:
                return context.deserialize(jsonObject, DarkRevealer.class);
            case INTERRUPTER:
                return context.deserialize(jsonObject, Interrupter.class);
            case SEAL_MASTER:
                return context.deserialize(jsonObject, SealMaster.class);
            case ASSASSIN:
                return context.deserialize(jsonObject, Assassin.class);
            case CHILL_GUY:
                return context.deserialize(jsonObject, ChillGuy.class);
            case LAST_JOKE:
                return context.deserialize(jsonObject, LastJoke.class);
            case CLOWN:
                return context.deserialize(jsonObject, Clown.class);
            case DISGUISER:
                return context.deserialize(jsonObject, Disguiser.class);
            case DARK_SEER:
                return context.deserialize(jsonObject, Darkseer.class);
            case FOLK_HERO:
                return context.deserialize(jsonObject, FolkHero.class);
            case ENTREPRENEUR:
                return context.deserialize(jsonObject, Entrepreneur.class);
            case LORE_KEEPER:
                return context.deserialize(jsonObject, Lorekeeper.class);
            case BLINDER:
                return context.deserialize(jsonObject, Blinder.class);
            default:
                throw new JsonParseException("Unknown role type: " + roleID);
        }
    }



}
