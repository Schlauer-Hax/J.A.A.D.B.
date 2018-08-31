package de.hax.jaadb.commands.botowner;

import de.hax.jaadb.commands.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CheckPermsCommand implements ICommand {
    @Override
    public void action(GuildMessageReceivedEvent event, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Permission permission : Permission.values()) {
            stringBuilder.append((event.getGuild().getSelfMember().hasPermission(permission)) ? "✅" : "❌");
            stringBuilder.append(permission.getName());
            stringBuilder.append("\n");
        }
        event.getChannel().sendMessage(stringBuilder.toString()).queue();
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String[] labels() {
        return new String[0];
    }
}
