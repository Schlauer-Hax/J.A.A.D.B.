package de.hax.jaadb.core.caching;

import de.hax.jaadb.core.Database;
import net.dv8tion.jda.core.JDA;

import java.util.ArrayList;

import static de.hax.jaadb.JAADB.log;
import static java.lang.String.format;

public class Caching {

    private ArrayList<Discord_member> discord_members;
    private ArrayList<Discord_user> discord_users;
    private ArrayList<Discord_guild> discord_guilds;

    private Database database;

    public Caching(Database database) {
        this.discord_members = new ArrayList<>();
        this.discord_users = new ArrayList<>();
        this.discord_guilds = new ArrayList<>();

        this.database = database;
    }

    public ArrayList<Discord_member> getDiscord_members() {
        return discord_members;
    }

    public ArrayList<Discord_user> getDiscord_users() {
        return discord_users;
    }

    public ArrayList<Discord_guild> getDiscord_guilds() {
        return discord_guilds;
    }

    public Discord_member getDiscordMember(Long userid, Long guildid) {
        for (Discord_member member : discord_members) {
            if (member.getGuild_id() == guildid && member.getUser_id() == userid) {
                return member;
            }
        }
        return null;
    }

    public Discord_guild getDiscordGuild(Long guildid) {
        for (Discord_guild guild : discord_guilds) {
            if (guild.getId() == guildid) {
                return guild;
            }
        }
        return null;
    }

    public Discord_user getDiscordUser(Long userid) {
        for (Discord_user user : discord_users) {
            if (user.getId() == userid) {
                return user;
            }
        }
        return null;
    }

    public Caching update() {
        log(this).info("Updating DB...");
        database.updateDB(this);
        log(this).info("Updated DB!");
        return this;
    }

    public Caching read(JDA jda) {
        log(this).info(format("Reading Cache... Shard ID: %s", jda.getShardInfo().getShardId()));
        database.readCache(this, jda);
        log(this).info(format("Read Cache! Shard ID: %s", jda.getShardInfo().getShardId()));
        return this;
    }

    public Database getDatabase() {
        return database;
    }
}
