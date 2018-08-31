package de.hax.jaadb.core;

import de.hax.jaadb.commands.ICommand;
import de.hax.jaadb.core.caching.Caching;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.audio.factory.IAudioSendFactory;
import net.dv8tion.jda.core.audio.factory.IAudioSendSystem;
import net.dv8tion.jda.core.audio.factory.IPacketProvider;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class Bot {

    private ShardManager shardManager;
    private ArrayList<ICommand> commands;
    private Caching caching;

    public Bot(ShardManager shardManager) {
        this.shardManager = shardManager;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public void setCommands(ArrayList<ICommand> commands) {
        this.commands = commands;
    }

    public ArrayList<ICommand> getCommands() {
        return commands;
    }

    public Caching getCaching() {
        return caching;
    }

    public void setCaching(Caching caching) {
        this.caching = caching;
    }
}
