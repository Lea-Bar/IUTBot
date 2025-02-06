package fr.leabar.iutbot.config.discord;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DiscordConfig {
    private String token = "your token";
}
