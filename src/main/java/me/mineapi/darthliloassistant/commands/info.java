package me.mineapi.darthliloassistant.commands;

import me.mineapi.darthliloassistant.DarthliloAssistant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class info extends DiscordCommand {
    public info() {
        super(PermissionType.MEMBER);
    }

    @Override
    public String name() {
        return "info";
    }

    @Override
    public String description() {
        return "General Bot Information";
    }

    @Override
    public void perform(Message message, String[] args) throws IOException {
        final Properties properties = new Properties();
        properties.load(DarthliloAssistant.class.getClassLoader().getResourceAsStream(".properties"));
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Info")
                .addField("Developer", "MineAPI", false)
                .addField("Library", "JDA (Java Discord API)", false)
                .addField("Version", properties.getProperty("botVersion"), false)
                .addField("Public Repo", "https://github.com/mineapi/Darthlilo-Assistant", false)
                .setColor(Color.PINK)
                .setTimestamp(new Date().toInstant())
                .setFooter("Made with ❤️", DarthliloAssistant.get().getSelfUser().getAvatarUrl());
        message.getChannel().sendMessage(embed.build()).queue();
    }
}
