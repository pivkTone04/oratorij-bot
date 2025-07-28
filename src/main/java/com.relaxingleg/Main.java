package com.relaxingleg;

import com.relaxingleg.commands.Ekipe;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault(Token.TOKEN)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        jda.addEventListener(new Listeners());

        CommandManager manager = new CommandManager();
        manager.add(new Ekipe());
        jda.addEventListener(manager);
    }
}
