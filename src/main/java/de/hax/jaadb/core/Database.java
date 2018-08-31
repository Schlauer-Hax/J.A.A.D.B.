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

public class Database {

    private Connection connection;
    private Config config;

    public Database(Config config) {
        this.config = config;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://"+config.getJsonObject().getString("DB_HOST")+":"+config.getJsonObject().getString("DB_PORT")+"/"+
                    config.getJsonObject().getString("DB_TABLE")+"?useSSL=false", config.getJsonObject().getString("DB_USER"), config.getJsonObject().getString("DB_PW"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String[] creationStatements = new String[] {
        "CREATE TABLE IF NOT EXISTS Discord_guild (guild_id BIGINT NOT NULL,PRIMARY KEY (guild_id));",
                "CREATE TABLE IF NOT EXISTS Discord_user (user_id BIGINT NOT NULL, PRIMARY KEY (user_id));",
                "CREATE TABLE IF NOT EXISTS Discord_member (guild_id BIGINT NOT NULL,user_id BIGINT NOT NULL,UNIQUE (user_id, guild_id)," +
                        "FOREIGN KEY (guild_id) REFERENCES Discord_guild (guild_id) ON DELETE CASCADE,"
                        + "FOREIGN KEY (user_id) REFERENCES Discord_user (user_id), PRIMARY KEY (member_id));",
    };

    public void createTablesIfNotExist() {
        try {
            for (String statement : this.creationStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
            System.out.println("Created Tables");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readCache(Caching caching, JDA jda) {
        for (Guild guild : jda.getGuilds()) {
            try (var preparedstatement = connection.prepareStatement("SELECT * FROM Discord_guild WHERE guild_id=" + guild.getIdLong() + ";")) {
                var resultset = preparedstatement.executeQuery();
                resultset.next();
                Discord_guild discord_guild = new Discord_guild(guild.getIdLong());
                caching.getDiscord_guilds().add(discord_guild);

                for (Member member : guild.getMembers()) {
                    try (var preparedstatement2 = connection.prepareStatement("SELECT * FROM `Discord_member` WHERE `guild_id`="+guild.getIdLong()+" AND `user_id`="+member.getUser().getId())) {
                        var resultset2 = preparedstatement2.executeQuery();
                        resultset2.next();
                        caching.getDiscord_members().add(new Discord_member(member.getGuild().getIdLong(), member.getUser().getIdLong(), discord_guild));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (User user:jda.getUsers()) {
            try (var preparedstatement = connection.prepareStatement("SELECT * FROM `Discord_user` WHERE `user_id`="+user.getId())) {
                var resultset = preparedstatement.executeQuery();
                resultset.next();
                caching.getDiscord_users().add(new Discord_user(user.getIdLong()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
