package me.mineapi.darthliloassistant.commands;

import me.mineapi.darthliloassistant.DarthliloAssistant;
import net.dv8tion.jda.api.entities.Message;

public class killswitch extends DiscordCommand {
    public killswitch() {
        super(PermissionType.BOTOWNER);
    }

    @Override
    public String name() {
        return "killswitch";
    }

    @Override
    public String description() {
        return "If a fatal error occurs, use this command to kill the bot";
    }

    @Override
    public void perform(Message message, String[] args) {
        if (message.getAuthor().getId().equals("291936058642268172") || message.getAuthor().getId().equals("497909398203531284")) {
            message.getChannel().sendMessage("Shutting down...").queue();
            DarthliloAssistant.get().shutdownNow();
        }
    }
}
