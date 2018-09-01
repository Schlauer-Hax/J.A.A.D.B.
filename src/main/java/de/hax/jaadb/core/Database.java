package de.hax.jaadb.core;

import de.hax.jaadb.core.caching.Caching;
import de.hax.jaadb.core.caching.Discord_guild;
import de.hax.jaadb.core.caching.Discord_member;
import de.hax.jaadb.core.caching.Discord_user;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static de.hax.jaadb.JAADB.log;
import static java.lang.String.format;

public class Database {

    private Connection connection;
    private Config config;

    public Database(Config config) {
        this.config = config;
    }

    public void connect() {
        String sql = format("CREATE DATABASE IF NOT EXISTS %s;", config.getJsonObject().getString("DB_TABLE"));
        try (var connection = DriverManager.getConnection(format("jdbc:mysql://%s:%s/?serverTimezone=UTC", config.getJsonObject().getString("DB_HOST"),
                config.getJsonObject().getString("DB_PORT")), config.getJsonObject().getString("DB_USER"), config.getJsonObject().getString("DB_PW"));
             var preparedStatement = connection.prepareStatement(sql)) {
            log(this).info("Creating database (if not exists)...");
            preparedStatement.executeUpdate();
            log(this).info("Database created (or it already existed).");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://"+config.getJsonObject().getString("DB_HOST")+":"+config.getJsonObject().getString("DB_PORT")+"/"+
                    config.getJsonObject().getString("DB_TABLE")+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", config.getJsonObject().getString("DB_USER"), config.getJsonObject().getString("DB_PW"));
            this.createTablesIfNotExist();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String[] creationStatements = new String[] {
            "CREATE TABLE IF NOT EXISTS Discord_guild (guild_id BIGINT NOT NULL,PRIMARY KEY (guild_id));",
            "CREATE TABLE IF NOT EXISTS Discord_user (user_id BIGINT NOT NULL, PRIMARY KEY (user_id));",
            "CREATE TABLE IF NOT EXISTS Discord_member (guild_id BIGINT NOT NULL,user_id BIGINT NOT NULL,perm_lvl SMALLINT NOT NULL DEFAULT 1,UNIQUE (user_id, guild_id)," +
                    "FOREIGN KEY (guild_id) REFERENCES Discord_guild (guild_id) ON DELETE CASCADE,"
                  + "FOREIGN KEY (user_id) REFERENCES Discord_user (user_id));",
    };

    public void createTablesIfNotExist() {
        try {
            for (String statement : this.creationStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
            log(this).debug("Created Tables");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDB(Caching caching) {
        for (Discord_guild discord_guild : caching.getDiscord_guilds()) {
            try (var preparedstatement = connection.prepareStatement(format("UPDATE `discord_guild` SET `guild_id`='%s' WHERE `guild_id`='%s'", discord_guild.getId(), discord_guild.getId()))) {
                preparedstatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (Discord_member member : caching.getDiscord_members()) {
            try (var preparedstatement = connection.prepareStatement(

                    format("UPDATE `discord_member` SET `guild_id`='%s',`user_id`='%s',`perm_lvl`='%s' WHERE `guild_id`='%s' AND `user_id`='%s'",
                            member.getGuild_id(), member.getUser_id(), member.getPermlvl(), member.getGuild_id(), member.getUser_id()
                    ))) {
                preparedstatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (Discord_user user : caching.getDiscord_users()) {
            try (var preparedstatement = connection.prepareStatement(
                    format("UPDATE `discord_user` SET `user_id`=%s WHERE `user_id`='%s'",
                            user.getId(), user.getId()
                    ))) {
                preparedstatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void readCache(Caching caching, JDA jda) {
        for (Guild guild : jda.getGuilds()) {
            try (var preparedstatement = connection.prepareStatement("SELECT * FROM discord_guild WHERE guild_id=" + guild.getIdLong() + ";")) {
                var resultset = preparedstatement.executeQuery();
                if (resultset.next()) {
                    Discord_guild discord_guild = new Discord_guild(guild.getIdLong());
                    caching.getDiscord_guilds().add(discord_guild);

                    for (Member member : guild.getMembers()) {
                        try (var preparedstatement2 = connection.prepareStatement("SELECT * FROM `discord_member` WHERE `guild_id`=" + guild.getIdLong() + " AND `user_id`=" + member.getUser().getId())) {
                            var resultset2 = preparedstatement2.executeQuery();
                            if (resultset2.next())
                                caching.getDiscord_members().add(new Discord_member(resultset2.getLong("guild_id"), resultset2.getLong("user_id"), discord_guild, resultset2.getInt("perm_lvl")));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (User user:jda.getUsers()) {
            try (var preparedstatement = connection.prepareStatement("SELECT * FROM `discord_user` WHERE `user_id`="+user.getId())) {
                var resultset = preparedstatement.executeQuery();
                if (resultset.next())
                    caching.getDiscord_users().add(new Discord_user(user.getIdLong()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
