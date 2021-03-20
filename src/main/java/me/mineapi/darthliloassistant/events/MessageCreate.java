package me.mineapi.darthliloassistant.events;

import io.github.cdimascio.dotenv.Dotenv;
import me.mineapi.darthliloassistant.commands.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class MessageCreate extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    ArrayList<DiscordCommand> commands = new ArrayList<>();
    String prefix = dotenv.get("PREFIX");

    public MessageCreate() {
        initCommands();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();

        for (DiscordCommand command : commands) {
            if (content.equals(prefix + command.name())) {
                try {
                    command.perform(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void initCommands() {
        commands.add(new ticket());
        commands.add(new close());
        commands.add(new info());
        commands.add(new eval());
        commands.add(new killswitch());
    }
}
