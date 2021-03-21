package me.mineapi.darthliloassistant.commands;

import io.github.cdimascio.dotenv.Dotenv;
import me.mineapi.darthliloassistant.DarthliloAssistant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Date;

public class help extends DiscordCommand {
    public help() {
        super(PermissionType.MEMBER);
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String description() {
        return "Get a list of commands for this bot";
    }

    @Override
    public void perform(Message message, String[] args) throws IOException {
        Dotenv dotenv = Dotenv.load();
        String ownerid = dotenv.get("OWNER_ID");
        message.getGuild().retrieveMember(message.getAuthor()).complete();
        Member member = message.getMember();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Help")
                .setColor(Color.PINK)
                .setTimestamp(new Date().toInstant())
                .setFooter("Made by MineAPI", DarthliloAssistant.get().getSelfUser().getAvatarUrl());
        if (message.getAuthor().getId().equals(ownerid)) {
            for (DiscordCommand command : DarthliloAssistant.messageCreate.ownerCommands) {
                embed.addField(command.name(), command.description(), false);
            }
        }
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            for (DiscordCommand command : DarthliloAssistant.messageCreate.adminCommands) {
                embed.addField(command.name(), command.description(), false);
            }
        }
        for (DiscordCommand command : DarthliloAssistant.messageCreate.defaultCommands) {
            embed.addField(command.name(), command.description(), false);
        }
        message.getChannel().sendMessage(embed.build()).queue();
    }
}
