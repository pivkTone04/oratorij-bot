package com.relaxingleg;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listeners extends ListenerAdapter {
    // Uncomment and use these methods if needed

    // @Override
    // public void onReady(@NotNull ReadyEvent event) {
    //     Guild guild = event.getJDA().getGuildById("1255462270331392040");
    //     if (guild == null) {
    //         System.err.println("Guild with ID '1255502943822544988' not found or bot does not have access.");
    //         return;
    //     }
    //     guild.upsertCommand("ekipe", "Zgenerira ekipe za velike igre").addOptions(
    //             new OptionData(OptionType.INTEGER, "velikostekip", "Koliko velike skupine želimo", true)
    //                     .setMinValue(2)
    //                     .setMaxValue(14)
    //     ).queue();
    // }

    // @Override
    // public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    //     if (event.getAuthor().isBot()) return; // Ignore messages from bots
    //     MessageChannel channel = event.getChannel();
    //     channel.sendMessage(event.getMessage().getContentRaw()).queue();
    // }
}
