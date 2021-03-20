package me.mineapi.darthliloassistant.commands;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.cdimascio.dotenv.Dotenv;
import me.mineapi.darthliloassistant.DarthliloAssistant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class close extends DiscordCommand {
    @Override
    public String name() {
        return "close";
    }

    @Override
    public String description() {
        return "Close a ticket";
    }

    @Override
    public void perform(Message message) {
        final User user = message.getAuthor();
        message.getGuild().retrieveMember(user).queue();
        final Member member = message.getGuild().getMember(user);
        final MessageChannel channel = message.getChannel();

        if (member.hasPermission(Permission.MANAGE_CHANNEL)) {
            String filteredName = "";
            if (message.getChannel().getName().length() > 6) {
                filteredName = message.getChannel().getName().substring(0, 6);
            }
            if (filteredName.equals("ticket")) {
                EventWaiter waiter = DarthliloAssistant.getWaiter();
                channel.sendMessage("Are you sure you want to delete this channel?").queue();
                waiter.waitForEvent(MessageReceivedEvent.class, e -> e.getChannel().equals(channel) && e.getAuthor().equals(user) && e.getMessage().getContentRaw().equals("yes"), e -> {
                    Dotenv dotenv = Dotenv.load();
                    Long ticketChannelID = Long.valueOf(dotenv.get("TICKET_CHANNEL"));
                    e.getGuild().getTextChannelById(channel.getId()).delete().queue();

                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("Ticket Closed")
                            .addField("ID", message.getChannel().getName(), false)
                            .addField("Closed by", message.getAuthor().getAsMention(), false)
                            .setTimestamp(new Date().toInstant())
                            .setColor(Color.PINK)
                            .setFooter("Made by MineAPI", DarthliloAssistant.get().getSelfUser().getAvatarUrl());
                    message.getGuild().getTextChannelById(ticketChannelID).sendMessage(embed.build()).queue();
                }, 10, TimeUnit.MINUTES, () -> System.out.println("Waited too long"));
            } else {
                channel.sendMessage("In order to protect the server, you may not delete this channel!");
            }
        } else {
            channel.sendMessage("You do not have permission to run this command!").queue();
        }
    }
}
