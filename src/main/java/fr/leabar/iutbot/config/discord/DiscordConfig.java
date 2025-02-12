package fr.leabar.iutbot.config.discord;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DiscordConfig {
    private String token = "your token";
    @SerializedName("isSemester2Enabled")
    private boolean semester2Enabled = false;
}
