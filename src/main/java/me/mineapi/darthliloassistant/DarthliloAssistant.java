package me.mineapi.darthliloassistant;


import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.cdimascio.dotenv.Dotenv;
import me.mineapi.darthliloassistant.events.EventHandler;
import me.mineapi.darthliloassistant.events.MemberJoin;
import me.mineapi.darthliloassistant.events.MessageCreate;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.Random;

public final class DarthliloAssistant {
    private static EventWaiter waiter;
    private static JDA api;
    public static void main(String[] args) throws LoginException {
        Dotenv dotenv = Dotenv.load();
        final String token = dotenv.get("BOT_TOKEN");
        api = JDABuilder.createDefault(token).build();
        waiter = new EventWaiter();

        api.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing("Make a ticket with !!ticket"));
        api.addEventListener(waiter);
        new EventHandler();
    }

    public static EventWaiter getWaiter() { return waiter; }
    public static JDA get() { return api; }
}
