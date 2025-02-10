package fr.leabar.iutbot.commands;

import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class TestCommand {
    @SlashCommand(
            name = "test",
            description = "Test",
            guildOnly = true
    )
    public void onTestCommand(SlashCommandInteraction commandInteraction){
        commandInteraction.reply("Test").queue();
    }
}
