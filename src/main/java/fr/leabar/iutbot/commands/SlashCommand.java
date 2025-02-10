package fr.leabar.iutbot.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SlashCommand {
    String name();
    String description();
    boolean guildOnly() default true;
    String[] options() default {};
}
