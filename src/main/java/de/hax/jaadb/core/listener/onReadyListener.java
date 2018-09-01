package de.hax.jaadb.core.listener;

import de.hax.jaadb.core.Bot;
import de.hax.jaadb.core.caching.Caching;
import de.hax.jaadb.core.caching.Discord_guild;
import de.hax.jaadb.core.caching.Discord_member;
import de.hax.jaadb.core.caching.Discord_user;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class onReadyListener extends ListenerAdapter {

    private Bot bot;

    public onReadyListener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(ReadyEvent event) {
        bot.getCaching().read(event.getJDA());
        new Thread(() -> {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.getCaching().update();
                }
            }, 600000);
        }).start();
        ArrayList<Long> guildids = new ArrayList<>();
        for (Discord_guild guild : bot.getCaching().getDiscord_guilds()) {
            guildids.add(guild.getId());
        }
        for (Guild guild : event.getJDA().getGuilds()) {
            if (!guildids.contains(guild.getIdLong())) {
                bot.getCaching().getDiscord_guilds().add(new Discord_guild(
                        guild.getIdLong()
                ));
            }
            ArrayList<String> memberids = new ArrayList<>();
            for (Discord_member member : bot.getCaching().getDiscord_members()) {
                memberids.add(member.getUser_id()+"|"+member.getGuild_id());
            }
            for (Member member :guild.getMembers()) {
                if (!memberids.contains(member.getUser().getIdLong()+"|"+member.getGuild().getIdLong())) {
                    bot.getCaching().getDiscord_members().add(new Discord_member(
                            member.getGuild().getIdLong(), member.getUser().getIdLong(),
                            bot.getCaching().getDiscordGuild(member.getGuild().getIdLong()), 1
                    ));
                }
            }
        }
        ArrayList<Long> userids = new ArrayList<>();
        for (Discord_user user : bot.getCaching().getDiscord_users()) {
            userids.add(user.getId());
        }
        for (User user : event.getJDA().getUsers()) {
            if (!userids.contains(user.getIdLong())) {
                bot.getCaching().getDiscord_users().add(new Discord_user(user.getIdLong()));
            }
        }

    }
}
