package me.mineapi.darthliloassistant;


import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.cdimascio.dotenv.Dotenv;
import me.mineapi.darthliloassistant.events.MemberJoin;
import me.mineapi.darthliloassistant.events.MemberLeave;
import me.mineapi.darthliloassistant.events.MessageCreate;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public final class DarthliloAssistant {
    private static EventWaiter waiter;
    private static JDABuilder builder;
    private static JDA api;
    public static MessageCreate messageCreate = new MessageCreate();
    public static void main(String[] args) throws LoginException {
        Dotenv dotenv = Dotenv.load();
        final String token = dotenv.get("BOT_TOKEN");
        builder = JDABuilder.createDefault(token);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        api = builder.build();
        waiter = new EventWaiter();

        api.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing("Make a ticket with !!ticket"));
        api.addEventListener(waiter);
        api.addEventListener(new MemberJoin());
        api.addEventListener(new MemberLeave());
        api.addEventListener(messageCreate);
    }
    public static EventWaiter getWaiter() { return waiter; }
    public static JDA get() { return api; }
}
