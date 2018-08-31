package de.hax.jaadb.commands.botowner;

import de.hax.jaadb.commands.ICommand;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class Test implements ICommand {
    @Override
    public void action(GuildMessageReceivedEvent event, String[] args) {

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
