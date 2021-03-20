package me.mineapi.darthliloassistant.events;

import me.mineapi.darthliloassistant.DarthliloAssistant;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;

public class EventHandler {
    ArrayList<EventListener> events = new ArrayList<>();
    public EventHandler() {
        events.add(new MemberJoin());
        events.add(new MemberLeave());
        events.add(new MessageCreate());
        loadEvents();
    }

    void loadEvents() {
        for (EventListener event : events) {
            DarthliloAssistant.get().addEventListener(event);
        }
    }
}
