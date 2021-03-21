package me.mineapi.darthliloassistant.events;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberJoin extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        event.getGuild().retrieveMember(event.getUser());
        Dotenv dotenv = Dotenv.load();
        Guild guild = event.getGuild();
        Long welcomeChannelID = Long.valueOf(dotenv.get("WELCOME_CHANNEL"));
        Long gifChannelID = Long.valueOf(dotenv.get("GIF_CHANNEL"));
        MessageChannel welcomeChannel = guild.getTextChannelById(welcomeChannelID);
        MessageChannel gifChannel = guild.getTextChannelById(gifChannelID);

        welcomeChannel.sendMessage("**" + event.getMember().getAsMention() + "** just joined the server! :slight_smile:").queue();
        gifChannel.sendMessage("Hello there " + event.getMember().getAsMention() + ", welcome to **" + guild.getName() + "** Take a gif present! https://i.imgur.com/aBPomxC.gif").queue();
    }
}
