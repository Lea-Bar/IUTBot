package fr.leabar.iutbot.events;

import fr.leabar.iutbot.commands.CommandManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;

public class CommandEvents extends ListenerAdapter {
    private CommandManager commandManager;

    public CommandEvents(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String name = event.getName();
        CommandManager.CommandInfo info = commandManager.getCommand(name);
        if(info != null){
            try {
                info.getMethod().invoke(info.getInstance(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            };
        }
    }
}
