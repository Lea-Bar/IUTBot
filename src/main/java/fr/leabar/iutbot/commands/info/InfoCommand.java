package fr.leabar.iutbot.commands.info;

import fr.leabar.iutbot.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.requests.RestConfig;

import java.awt.*;
import java.lang.management.ManagementFactory;

public class InfoCommand {
    @SlashCommand(
            name="info",
            description = "Voir les informations concernant le bot",
            guildOnly = true
    )
    public void info(SlashCommandInteraction event) {
        JDA jda = event.getJDA();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Information sur le bot");
        embed.setDescription("Ces informations permettent de debug le bot au cas où");
        embed.addField("Bot UP Time", ((int) (ManagementFactory.getRuntimeMXBean().getUptime()/1000))+" sec", false);
        embed.addField("Discord API URL", RestConfig.DEFAULT_BASE_URL, false);
        embed.addField("JDA Version", JDAInfo.VERSION, false);
        embed.addField("Nombre de guilds", jda.getGuilds().size()+" guilds", false);
        embed.setColor(new Color(0xe74c3c));
        event.replyEmbeds(embed.build()).queue();
    }
}
