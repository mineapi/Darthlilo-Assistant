package me.mineapi.darthliloassistant.commands;

import net.dv8tion.jda.api.entities.Message;

import java.io.IOException;

public abstract class DiscordCommand {
    abstract public String name();
    abstract public String description();

    abstract public void perform(Message message) throws IOException;
}
