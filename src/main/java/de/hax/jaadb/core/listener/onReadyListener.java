package de.hax.jaadb.core.listener;

import de.hax.jaadb.core.Bot;
import de.hax.jaadb.core.caching.Caching;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class onReady extends ListenerAdapter {

    private Bot bot;

    public onReady(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(ReadyEvent event) {
        new Thread(() -> {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bot.getCaching().update();
                }
            }, 600000);
        }).start();
    }
}
