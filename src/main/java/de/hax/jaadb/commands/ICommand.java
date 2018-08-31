package de.hax.jaadb.commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public interface ICommand {

    public void action(GuildMessageReceivedEvent event, String[] args);

    public String description();

    public String[] labels();
}
