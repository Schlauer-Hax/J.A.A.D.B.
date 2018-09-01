package de.hax.jaadb.commands.botowner;

import de.hax.jaadb.commands.ICommand;
import de.hax.jaadb.core.Bot;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CachingCommand implements ICommand {

    Bot bot;

    public CachingCommand(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void action(GuildMessageReceivedEvent event, String[] args) {
        switch (args[0].toLowerCase()) {
            case "read":
                for (JDA jda:bot.getShardManager().getShards()) {
                    bot.getCaching().read(jda);
                }
                break;
            case "update":
                bot.getCaching().update();
                break;
        }
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String[] labels() {
        return new String[]{"cache"};
    }
}
