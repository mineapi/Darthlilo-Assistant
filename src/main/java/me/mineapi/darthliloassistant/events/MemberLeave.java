package me.mineapi.darthliloassistant.events;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberLeave extends ListenerAdapter {
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Dotenv dotenv = Dotenv.load();
        Guild guild = event.getGuild();
        Long welcomeChannelID = Long.valueOf(dotenv.get("WELCOME_CHANNEL"));
        MessageChannel welcomeChannel = guild.getTextChannelById(welcomeChannelID);
        User user = event.getUser();

        welcomeChannel.sendMessage("**" + user.getAsTag() + "** just left the server. :slight_frown:").queue();
    }
}
