package de.hax.jaadb.core;

import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class BotBuilder {
    private DefaultShardManagerBuilder shardManagerBuilder = new DefaultShardManagerBuilder();

    public BotBuilder(Config config) {
        shardManagerBuilder.setToken(config.getJsonObject().getString("TOKEN"));
        shardManagerBuilder.setGame(Game.of(Game.GameType.LISTENING, "To commands"));
        shardManagerBuilder.setStatus(OnlineStatus.ONLINE);
        shardManagerBuilder.setShardsTotal(config.getJsonObject().getInt("SHARDS"));
    }

    public BotBuilder addEventListener(Object... listener) {
        shardManagerBuilder.addEventListeners(listener);
        return this;
    }

    public DefaultShardManagerBuilder getBuilder() {
        return shardManagerBuilder;
    }

    public Bot start() {
        try {
            return new Bot(shardManagerBuilder.build());
        } catch (LoginException e) {
            e.printStackTrace();
        }
        return null;
    }
}
