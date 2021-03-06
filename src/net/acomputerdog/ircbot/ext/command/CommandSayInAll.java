package net.acomputerdog.ircbot.ext.command;

import com.sorcix.sirc.structure.Channel;
import com.sorcix.sirc.structure.User;
import com.sorcix.sirc.util.Chattable;
import net.acomputerdog.ircbot.command.Command;
import net.acomputerdog.ircbot.command.util.CommandLine;
import net.acomputerdog.ircbot.config.Config;
import net.acomputerdog.ircbot.main.IrcBot;

import java.util.Map;

public class CommandSayInAll extends Command {
    public CommandSayInAll(IrcBot bot) {
        super(bot, "SayInAll", "sayinall", "say-in-all", "say_in_all", "sayall", "say-all", "say_all");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public boolean requiresAdmin() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Tells AcomputerBot to say something in all connected channels.";
    }

    @Override
    public String getHelpString() {
        return Config.COMMAND_PREFIX + "sayinall <message>";
    }

    @Override
    public boolean processCommand(Channel channel, User sender, Chattable target, CommandLine command) {
        String filtered = bot.getStringCheck().filterString(command.args);
        if (filtered == null) {
            target.send(colorRed("The string was blocked, probably due to cascaded commands!"));
            return false;
        }
        Map<String, Channel> channelMap = bot.getConnection().getState().getChannelMap();
        for (String chanName : channelMap.keySet()) {
            channelMap.get(chanName).send(filtered);
        }
        return true;
    }
}
