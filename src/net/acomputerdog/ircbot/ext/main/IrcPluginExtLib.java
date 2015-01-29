package net.acomputerdog.ircbot.ext.main;

import net.acomputerdog.ircbot.command.CommandManager;
import net.acomputerdog.ircbot.ext.command.*;
import net.acomputerdog.ircbot.main.IrcBot;
import net.acomputerdog.ircbot.plugin.IrcPlugin;

public class IrcPluginExtLib implements IrcPlugin {

    private IrcBot bot;

    @Override
    public IrcBot getIrcBot() {
        return bot;
    }

    @Override
    public String getID() {
        return "extlib";
    }

    @Override
    public String getName() {
        return "Extra Lib";
    }

    @Override
    public void onLoad(IrcBot ircBot) {
        this.bot = ircBot;
        try {
            CommandManager manager = bot.getCommandManager();
            manager.registerCommand(new CommandSay(bot));
            manager.registerCommand(new CommandSayIn(bot));
            manager.registerCommand(new CommandSayInAll(bot));
            manager.registerCommand(new CommandMe(bot));
            manager.registerCommand(new CommandMeIn(bot));
            manager.registerCommand(new CommandMeInAll(bot));
            manager.registerCommand(new CommandGithub(bot));
            manager.registerCommand(new CommandSpyOn(bot));
            manager.registerCommand(new CommandSpyIn(bot));
            manager.registerCommand(new CommandSmiley(bot));
            manager.registerCommand(new CommandChar(bot));
            manager.registerCommand(new CommandSudo(bot));
            manager.registerCommand(new CommandSudoPrivate(bot));
            manager.registerCommand(new CommandPrivateMessage(bot));
            manager.registerCommand(new CommandWhoAmI(bot));
            manager.registerCommand(new CommandJavaScript(bot));
            manager.registerCommand(new CommandPipe(bot));
            manager.registerCommand(new CommandJavaScriptConsole(bot));
            manager.registerCommand(new CommandNote(bot));
            manager.registerCommand(new CommandClearNotes(bot));
            } catch (Exception e) {
            getLogger().logError("Exception registering extra commands!", e);
        }
        getLogger().logInfo("Loaded extra commands.");
    }

    @Override
    public void onUnload() {
        getLogger().logInfo("Unloaded extra commands.");
    }
}
