package me.mineapi.darthliloassistant.commands;

import net.dv8tion.jda.api.entities.Message;

import java.io.IOException;

public abstract class DiscordCommand {
    public enum PermissionType { BOTOWNER, ADMINISTRATOR, MEMBER }

    private PermissionType permissionType;

    public DiscordCommand(PermissionType type) {
        this.permissionType = type;
    }

    abstract public String name();
    abstract public String description();

    abstract public void perform(Message message, String[] args) throws IOException;

    public PermissionType getType() { return this.permissionType; }
}
