package fr.leabar.iutbot.commands;

import fr.leabar.iutbot.config.ConfigManager;
import fr.leabar.iutbot.config.schedule.ScheduleWrapper;
import fr.leabar.iutbot.modules.ModuleManager;
import fr.leabar.iutbot.modules.discord.BotModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    private final ConcurrentHashMap<String, CommandInfo> commands = new ConcurrentHashMap<String, CommandInfo>();

    public void registerCommands(Object commandClass) {
        for(Method method : commandClass.getClass().getMethods()){
            SlashCommand annotation = method.getAnnotation(SlashCommand.class);
            if(annotation != null){
                registerCommand(commandClass, method, annotation);
            }
        }
    }

    private void registerCommand(Object commandClass, Method method, SlashCommand slashCommand) {
        SlashCommandData data = Commands.slash(slashCommand.name(),
                slashCommand.description());
        ScheduleWrapper scheduleWrapper = ConfigManager.getInstance().getScheduleWrapper();
        for(String options : slashCommand.options()){
            String[] parts = options.split(":");
            if (parts.length >= 3) {
                switch (parts[0].toUpperCase()) {
                    case "DATE":
                        OptionData optionData = new OptionData(OptionType.STRING, parts[1], parts[2], parts.length > 3 && Boolean.parseBoolean(parts[3]));
                        optionData.addChoice("Lundi", "Lundi");
                        optionData.addChoice("Mardi", "Mardi");
                        optionData.addChoice("Mercredi", "Mercredi");
                        optionData.addChoice("Jeudi", "Jeudi");
                        optionData.addChoice("Vendredi", "Vendredi");
                        optionData.addChoice("Samedi", "Samedi");
                        data.addOptions(optionData);
                        break;
                    case "CLASS":
                        OptionData optionData2 = new OptionData(OptionType.STRING, parts[1], parts[2], parts.length > 3 && Boolean.parseBoolean(parts[3]));
                        scheduleWrapper.getScheduleConfigs().forEach(config -> {
                            optionData2.addChoice(config.getSchoolClass(), config.getSchoolClass());
                        });
                        data.addOptions(optionData2);
                        break;
                    default:
                        data.addOption(
                                OptionType.valueOf(parts[0].toUpperCase()),
                                parts[1],
                                parts[2],
                                parts.length > 3 && Boolean.parseBoolean(parts[3])
                        );
                        break;
                }
            }
        }
        commands.put(slashCommand.name(), new CommandInfo(commandClass, method, slashCommand));
        ModuleManager.getModule(BotModule.class).ifPresent(module -> {
            JDA jda = module.getJdaInstance();
            if(slashCommand.guildOnly()){
                for(Guild guild : jda.getGuilds()){
                    guild.upsertCommand(data).queue();
                }
            }else{
                jda.upsertCommand(data).queue();
            }
        });
    }

    public CommandInfo getCommand(String commandName) {
        return commands.get(commandName);
    }

    @AllArgsConstructor
    @Getter
    public static class CommandInfo{
        private Object instance;
        private Method method;
        private SlashCommand slashCommand;
    }
}
