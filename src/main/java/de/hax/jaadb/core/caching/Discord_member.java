package de.hax.jaadb.core.caching;

public class Discord_member {

    Long guild_id;
    Long user_id;
    Discord_guild guild;
    Integer permlvl;

    public Discord_member(Long guild_id, Long user_id, Discord_guild guild, Integer permlvl) {
        this.guild_id = guild_id;
        this.user_id = user_id;
        this.guild = guild;
        this.permlvl = permlvl;
    }

    public Long getGuild_id() {
        return guild_id;
    }

    public void setGuild_id(Long guild_id) {
        this.guild_id = guild_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Discord_guild getGuild() {
        return guild;
    }

    public void setGuild(Discord_guild guild) {
        this.guild = guild;
    }

    public Integer getPermlvl() {
        return permlvl;
    }

    public void setPermlvl(Integer permlvl) {
        this.permlvl = permlvl;
    }
}
