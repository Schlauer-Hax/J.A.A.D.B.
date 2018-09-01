package de.hax.jaadb;

import de.hax.jaadb.commands.ICommand;
import de.hax.jaadb.commands.botowner.CheckPermsCommand;
import de.hax.jaadb.commands.botowner.Test;
import de.hax.jaadb.core.Bot;
import de.hax.jaadb.core.BotBuilder;
import de.hax.jaadb.core.Config;
import de.hax.jaadb.core.Database;
import de.hax.jaadb.core.caching.Caching;
import de.hax.jaadb.core.listener.onMessage;
import de.hax.jaadb.core.listener.onReadyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JAADB {

    public static Logger log(Object object) {
        return LoggerFactory.getLogger(object.getClass());
    }

    public static void main(String[] args) {
        new JAADB().jaadb();
    }

    public void jaadb() {
        ArrayList<ICommand> commands = new ArrayList<>() {{
            addAll(List.of(
               new Test(),
                    new CheckPermsCommand()
            ));
        }};
        Config config = new Config().read("config.json");
        Database database = new Database(config);
        database.connect();
        Caching caching = new Caching(database);

        Bot bot = new BotBuilder(config).start();
        bot.getShardManager().addEventListener(
                new onReadyListener(bot),
                new onMessage(bot)
        );
        bot.setCommands(commands);
        bot.setCaching(caching);
        bot.setConfig(config);
    }

}
