package me.mineapi.darthliloassistant.commands;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.cdimascio.dotenv.Dotenv;
import me.mineapi.darthliloassistant.DarthliloAssistant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ticket extends DiscordCommand {
    public ticket() {
        super(PermissionType.MEMBER);
    }

    @Override
    public String name() {
        return "ticket";
    }

    @Override
    public String description() {
        return "Create a new ticket.";
    }

    public static String bug = "null";
    public static String recreation = "null";
    public static String memberTag;
    public static int id;
    @Override
    public void perform(Message message, String[] args) {
        id = generateID(100);
        EventWaiter waiter = DarthliloAssistant.getWaiter();
        User author = message.getAuthor();
        Dotenv dotenv = Dotenv.load();
        Long botChannel = Long.valueOf(dotenv.get("BOT_CHANNEL"));

        if (message.getChannel().equals(message.getGuild().getTextChannelById(botChannel))) {
            TextChannel ticketChannel = message.getGuild().createTextChannel("Ticket-"+id).complete();
            ticketChannel.createPermissionOverride(message.getMember()).setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY).complete();
            ticketChannel.createPermissionOverride(message.getGuild().getPublicRole()).setDeny(Permission.ALL_PERMISSIONS).complete();

            message.getChannel().sendMessage("You've opened a ticket, you were mentioned in the channel.").queue();

            ticketChannel.sendMessage("<@" + message.getAuthor().getId() + "> This is your ticket, please answer carefully").complete();
            question1(waiter, ticketChannel, message, author);
        } else {
            message.getChannel().sendMessage("You may only run this in " + message.getGuild().getTextChannelById(botChannel).getAsMention() + "!").queue();
        }
    }

    void question1(EventWaiter waiter, TextChannel ticketChannel, Message message, User author) {
        ticketChannel.sendMessage("What is the bug? Please explain how it affects the rig, do not explain how to recreate it. Keep this in 1 message.").queue();
        waiter.waitForEvent(MessageReceivedEvent.class,
                e -> {
                    if (e.getAuthor().equals(author) && e.getChannel().equals(ticketChannel) && !e.getMessage().getContentRaw().equals(message.getContentRaw())) {
                        return true;
                    } else {
                        return false;
                    }
                },
                e -> {
                    bug = e.getMessage().getContentRaw();
                    question2(waiter, ticketChannel, message, author);
                },
                10, TimeUnit.MINUTES,
                () -> ticketChannel.delete().complete());
    }

    void question2(EventWaiter waiter, TextChannel ticketChannel, Message message, User author) {
        ticketChannel.sendMessage("How do you recreate this bug? Please explain thoroughly. Keep this in 1 message.").complete();
        waiter.waitForEvent(MessageReceivedEvent.class,
                e -> {
                    if (e.getAuthor().equals(author) && e.getChannel().equals(ticketChannel) && !e.getMessage().getContentRaw().equals(message.getContentRaw())) {
                        return true;
                    } else {
                        return false;
                    }
                },
                e -> {
                    recreation = e.getMessage().getContentRaw();
                    confirm(waiter, ticketChannel, message, author);
                },
                10, TimeUnit.MINUTES,
                () -> ticketChannel.delete().complete());
    }

    void confirm(EventWaiter waiter, TextChannel ticketChannel, Message message, User author) {
        EmbedBuilder confirmEmbed = new EmbedBuilder();
        confirmEmbed.setTitle("Confirm");
        confirmEmbed.setDescription("Is this information correct, if so, reply with \'yes\', if not, your ticket will be closed, reply with \'no\'. If Lilo requires further information, he will be able to contact you here.");
        confirmEmbed.addField("Bug", bug, false);
        confirmEmbed.addField("Recreation", recreation, false);
        confirmEmbed.setColor(Color.PINK);
        confirmEmbed.setTimestamp(new Date().toInstant());
        confirmEmbed.setFooter("Made by MineAPI", DarthliloAssistant.get().getSelfUser().getAvatarUrl());

        ticketChannel.sendMessage(confirmEmbed.build()).complete();
        send(waiter, ticketChannel, message, author);
    }

    void send(EventWaiter waiter, TextChannel ticketChannel, Message message, User author) {
        waiter.waitForEvent(MessageReceivedEvent.class, e -> e.getMessage().getAuthor().equals(author) && e.getChannel().equals(ticketChannel), e -> {
            if (e.getMessage().getContentRaw().equals("yes")) {
                Dotenv dotenv = Dotenv.load();
                Long ticketChannelID = Long.valueOf(dotenv.get("TICKET_CHANNEL"));
                EmbedBuilder ticket = new EmbedBuilder();
                ticket.setTitle("Ticket");
                ticket.addField("ID", Integer.toString(id), false);
                ticket.addField("Sent by", author.getAsMention(), false);
                ticket.addField("Bug", bug, false);
                ticket.addField("Recreation", recreation, false);
                ticket.setColor(Color.PINK);
                ticket.setTimestamp(new Date().toInstant());
                ticket.setFooter("Made by MineAPI", DarthliloAssistant.get().getSelfUser().getAvatarUrl());

                message.getGuild().getTextChannelById(ticketChannelID).sendMessage(ticket.build()).queue();
                ticketChannel.sendMessage("✈️ Your ticket was sent!").queue();
            } else {
                ticketChannel.delete().queue();
            }
        }, 1, TimeUnit.MINUTES, () -> ticketChannel.delete().queue());
    }

    public int generateID(long seed) {
        Random rand = new Random();
        return rand.nextInt(65536);
    }
}
