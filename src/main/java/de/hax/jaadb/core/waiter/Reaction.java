package de.hax.jaadb.core.waiter;

import de.hax.jaadb.core.Bot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.function.Consumer;

public class Reaction {


    public static void yesnoMenu(Bot bot, TextChannel channel, EmbedBuilder embedBuilder, Consumer<Message> yes, Consumer<Message> no) {

        channel.sendMessage(embedBuilder.build()).queue(
                msg -> {
                    msg.addReaction("✅").queue();
                    msg.addReaction("❌").queue();
                    ListenerAdapter listenerAdapter = new ListenerAdapter() {
                        @Override
                        public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
                            if (event.getMember().getUser().getIdLong() != event.getJDA().getSelfUser().getIdLong()) {
                                if (msg.getIdLong() == event.getMessageIdLong()) {
                                    if (event.getReactionEmote().getName().equals("✅"))
                                        yes.accept(msg);
                                    else if (event.getReactionEmote().getName().equals("❌"))
                                        no.accept(msg);
                                    bot.getShardManager().removeEventListener(this);
                                }
                            }
                        }
                    };
                    bot.getShardManager().addEventListener(
                        listenerAdapter
                    );
                }
        );
    }

}
