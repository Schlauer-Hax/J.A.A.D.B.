package de.hax.jaadb.commands.botowner;

import de.hax.jaadb.commands.ICommand;
import de.hax.jaadb.core.Bot;
import de.hax.jaadb.core.waiter.Reaction;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class Test implements ICommand {

    Bot bot;

    public Test(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void action(GuildMessageReceivedEvent event, String[] args) {
        Reaction.yesnoMenu(bot, event.getChannel(), new EmbedBuilder().setDescription("Are u cool?").setTitle("1Test"),
                msg -> {
                    msg.editMessage(new EmbedBuilder().setDescription("Are u cool?\n\nYes you are!").setTitle("1Test").build()).queue();
                },
                msg -> {
                    msg.editMessage(new EmbedBuilder().setDescription("Are u cool?\n\nNo you aren't :(").setTitle("1Test").build()).queue();
                }
                );
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String[] labels() {
        return new String[]{"test"};
    }
}
