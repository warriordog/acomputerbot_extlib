package net.acomputerdog.ircbot.ext.command;

import com.sorcix.sirc.listener.MessageListener;
import com.sorcix.sirc.main.IrcConnection;
import com.sorcix.sirc.structure.Channel;
import com.sorcix.sirc.structure.User;
import com.sorcix.sirc.util.Chattable;
import net.acomputerdog.ircbot.command.Command;
import net.acomputerdog.ircbot.command.util.CommandLine;
import net.acomputerdog.ircbot.config.Config;
import net.acomputerdog.ircbot.main.IrcBot;

import java.util.HashMap;
import java.util.Map;

public class CommandSpyOn extends Command {
    private final Map<User, UserSpy> spyMap = new HashMap<>();

    public CommandSpyOn(IrcBot bot) {
        super(bot, "SpyOn", "spyon", "spy-on", "spy_on");
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public boolean requiresAdmin() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Monitors conversation by a particular person.";
    }

    @Override
    public String getHelpString() {
        return Config.COMMAND_PREFIX + "spyon <target>";
    }

    @Override
    public boolean processCommand(Channel channel, User sender, Chattable target, CommandLine command) {
        if (command.args.equals("*")) {
            spyMap.keySet().forEach(u -> spyMap.get(u).stop());
            target.send("All spy sessions ended.");
            return true;
        } else {
            User user = bot.getConnection().createUser(command.args.toLowerCase().trim());
            UserSpy spy = spyMap.get(user);
            if (spy != null) {
                spy.stop();
                spyMap.remove(user);
            } else {
                spy = new UserSpy(bot, user, target);
                spyMap.put(user, spy);
                spy.start();
            }
            return true;
        }
    }

    private static class UserSpy implements MessageListener {

        private final IrcBot bot;
        private final User spyTarget;
        private final Chattable spySender;
        private final String name;

        private UserSpy(IrcBot bot, User spyTarget, Chattable spySender) {
            this.bot = bot;
            this.spyTarget = spyTarget;
            this.spySender = spySender;
            String targetNick = spyTarget.getNick();
            this.name = "[SPY][" + targetNick.substring(0, targetNick.length() - 1).concat("□") + "]";
        }

        @Override
        public void onMessage(IrcConnection irc, User sender, Channel target, String message) {
            if (sender.equals(spyTarget) && irc.getState().hasChannel(spySender.getName())) {
                spySender.send(name + "[CHAT][" + target.getName() + "] " + message);
            }
        }

        private void start() {
            spySender.send(name + "[INFO] Spying started.  Use \"" + Config.COMMAND_PREFIX + "spyin *\" to stop.");
            bot.getConnection().addMessageListener(this);
        }

        private void stop() {
            bot.getConnection().removeMessageListener(this);
            spySender.send(name + "[INFO] Spying stopped.");
        }

        @Override
        public void onAction(IrcConnection irc, User sender, Channel target, String action) {}
        @Override
        public void onAction(IrcConnection irc, User sender, String action) {}
        @Override
        public void onCtcpReply(IrcConnection irc, User sender, String command, String message) {}
        @Override
        public void onNotice(IrcConnection irc, User sender, Channel target, String message) {}
        @Override
        public void onNotice(IrcConnection irc, User sender, String message) {}
        @Override
        public void onPrivateMessage(IrcConnection irc, User sender, String message) {}
    }
}
