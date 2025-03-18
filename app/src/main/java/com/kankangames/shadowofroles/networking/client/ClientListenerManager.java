package com.kankangames.shadowofroles.networking.client;

import com.kankangames.shadowofroles.networking.listeners.clientlistener.ClientListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class ClientListenerManager {
    private final Map<Class<? extends ClientListener>, ClientListener> listeners = new HashMap<>();


    public <T extends ClientListener> void addListener(Class<T> clazz, T listener){
        listeners.put(clazz, listener);
    }

    public <T extends ClientListener> void callListener(Class<T> eventType, Consumer<T> action){
        T listener = eventType.cast(listeners.get(eventType));
        if(listener!=null && action!=null){
            action.accept(listener);
        }
    }

     void resetListeners(){
        listeners.clear();
    }
}
