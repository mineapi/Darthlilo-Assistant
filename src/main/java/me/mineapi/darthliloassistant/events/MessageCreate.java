package me.mineapi.darthliloassistant.events;

import io.github.cdimascio.dotenv.Dotenv;
import me.mineapi.darthliloassistant.commands.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class MessageCreate extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    public ArrayList<DiscordCommand> commands = new ArrayList<>();
    public ArrayList<DiscordCommand> ownerCommands = new ArrayList<>();
    public ArrayList<DiscordCommand> adminCommands = new ArrayList<>();
    public ArrayList<DiscordCommand> defaultCommands = new ArrayList<>();
    String prefix = dotenv.get("PREFIX");
    String ownerid = dotenv.get("OWNER_ID");

    public MessageCreate() {
        initCommands();
        for (DiscordCommand command : commands) {
            if (command.getType() == DiscordCommand.PermissionType.BOTOWNER) {
                ownerCommands.add(command);
            }
            if (command.getType() == DiscordCommand.PermissionType.ADMINISTRATOR) {
                adminCommands.add(command);
            }
            if (command.getType() == DiscordCommand.PermissionType.MEMBER) {
                defaultCommands.add(command);
            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();

        String[] args = event.getMessage().getContentRaw().split(" ");
        if (event.getMember().getId().equals(ownerid)) {
            for (DiscordCommand command : ownerCommands) {
                if (content.equals(prefix + command.name())) {
                    try {
                        command.perform(message, args);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            for (DiscordCommand command : adminCommands) {
                if (content.equals(prefix + command.name())) {
                    try {
                        command.perform(message, args);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (DiscordCommand command : defaultCommands) {
            if (content.equals(prefix + command.name())) {
                try {
                    command.perform(message, args);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void initCommands() {
        commands.add(new help());
        commands.add(new info());
        commands.add(new ticket());
        commands.add(new close());
        commands.add(new killswitch());
    }
}
