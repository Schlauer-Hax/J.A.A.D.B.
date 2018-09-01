package de.hax.jaadb.core.listener;

import de.hax.jaadb.commands.ICommand;
import de.hax.jaadb.core.Bot;
import de.hax.jaadb.core.caching.Discord_member;
import de.hax.jaadb.core.caching.Discord_user;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class onMessage extends ListenerAdapter {

    Bot bot;

    public onMessage(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        register(event);
        handleCommand(event);
    }

    public void register(GuildMessageReceivedEvent event) {

    }

    public void handleCommand(GuildMessageReceivedEvent event) {
        String prefix = "test!";
        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            String label = event.getMessage().getContentRaw().replaceFirst(prefix, "").split(" ")[0];
            String[] args = event.getMessage().getContentRaw().replaceFirst(prefix, "")
                    .replaceFirst(label, "").replaceFirst(" ", "").split(" ");
            for (ICommand command : bot.getCommands()) {
                for (String templabel : command.labels()) {
                    if (templabel.equals(label)) {
                        String packagename = command.getClass().getPackageName().replaceFirst("de.hax.jaadb.commands.", "");
                        Discord_member discord_member = bot.getCaching().getDiscordMember(event.getAuthor().getIdLong(), event.getGuild().getIdLong());
                        switch (packagename) {
                            case "botowner":
                                if (!bot.getConfig().getJsonObject().getJSONArray("BOTOWNER").toList().contains(event.getAuthor().getId())) {
                                    return;
                                }
                                break;
                            case "guildadmin":
                                if (discord_member.getPermlvl()!=3 || event.getGuild().getOwner().getUser().getIdLong() != event.getAuthor().getIdLong()) return;
                                break;
                            case "mod":
                                if (discord_member.getPermlvl()!=2) return;
                                break;
                            case "user":
                                if (discord_member.getPermlvl()!=1) return;
                                break;
                        }
                        // TODO: PERMISSION WITH PACKAGE NAME
                        command.action(event, args);
                        break;
                    }
                }
            }
        }
    }
}
