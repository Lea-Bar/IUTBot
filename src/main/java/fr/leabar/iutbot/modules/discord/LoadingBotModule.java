package fr.leabar.iutbot.modules.discord;

import fr.leabar.iutbot.config.ConfigManager;
import fr.leabar.iutbot.modules.IModule;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.concurrent.CompletableFuture;

public class LoadingBotModule implements IModule {
    @Getter
    private JDA jdaInstance;

    @Override
    public CompletableFuture<Boolean> start() {
        return CompletableFuture.supplyAsync(() -> {
            String token = ConfigManager.getInstance().getDiscordConfig().getToken();

            this.jdaInstance = JDABuilder
                    .createDefault(token)
                    .enableIntents(
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MEMBERS
                    )
                    .disableCache(
                            CacheFlag.EMOJI,
                            CacheFlag.VOICE_STATE,
                            CacheFlag.SCHEDULED_EVENTS,
                            CacheFlag.STICKER,
                            CacheFlag.ACTIVITY
                    )
                    .setActivity(Activity.of(Activity.ActivityType.CUSTOM_STATUS, "\uD83C\uDF93 Navigue sur Moodle"))
                    .build();
            try {
                this.jdaInstance.awaitReady();
                return true;
            } catch (InterruptedException e) {
                return false;
            }
        });
    }

    @Override
    public void stop() {
        if(this.jdaInstance != null && this.jdaInstance.getStatus().isInit()){
            try {
                this.jdaInstance.awaitShutdown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
