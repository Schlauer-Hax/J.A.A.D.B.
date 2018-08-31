package de.hax.jaadb.core.listener;

import de.hax.jaadb.commands.ICommand;
import de.hax.jaadb.core.Bot;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class onMessage extends ListenerAdapter {

    Bot bot;

    public onMessage(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String prefix = "test!";
        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            String label = event.getMessage().getContentRaw().replaceFirst(prefix, "").split(" ")[0];
            String[] args = event.getMessage().getContentRaw().replaceFirst(prefix, "")
                    .replaceFirst(label, "").replaceFirst(" ", "").split(" ");
            for (ICommand command : bot.getCommands()) {
                for (String templabel : command.labels()) {
                    if (templabel.equals(label)) {
                        if ()
                        command.action(event, args);
                        break;
                    }
                }
            }
        }
    }

    public boolean haspermission(User user, ICommand iCommand) {

    }
}
