package me.mineapi.darthliloassistant.commands;

import me.mineapi.darthliloassistant.DarthliloAssistant;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

public class eval extends DiscordCommand {
    @Override
    public String name() {
        return "eval";
    }

    @Override
    public String description() {
        return "Run code from the server.";
    }

    @Override
    public void perform(Message message) {
        message.getGuild().retrieveMember(message.getAuthor());
        DarthliloAssistant.get().getEventManager().handle(new GuildMemberRemoveEvent(DarthliloAssistant.get(), 1, message.getGuild(), message.getAuthor(), message.getMember()));
    }
}
