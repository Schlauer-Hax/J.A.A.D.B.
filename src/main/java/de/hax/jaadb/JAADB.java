package de.hax.jaadb;

import de.hax.jaadb.commands.ICommand;
import de.hax.jaadb.commands.botowner.Test;
import de.hax.jaadb.core.Bot;
import de.hax.jaadb.core.BotBuilder;
import de.hax.jaadb.core.Config;
import de.hax.jaadb.core.Database;
import de.hax.jaadb.core.caching.Caching;

import java.util.ArrayList;
import java.util.List;

public class JAADB {

    public static void main(String[] args) {
        new JAADB().jaadb();
    }

    public void jaadb() {
        ArrayList<ICommand> commands = new ArrayList<>() {{
            addAll(List.of(
               new Test()
            ));
        }};
        Config config = new Config().read("config.json");
        Database database = new Database(config);
        database.connect();
        Caching caching = new Caching(database);


        Bot bot = new BotBuilder(config).addEventListener().start();
        bot.setCommands(commands);
        bot.setCaching(caching);
    }

}
