package com.relaxingleg.commands;


import com.relaxingleg.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Ekipe implements ICommand {
    private static final int MIN_TEAM_SIZE = 2;
    private static final int MAX_TEAM_SIZE = 14;

    private class SkupinaClass {
        private ArrayList<ArrayList<String>> skupine;

        private static ArrayList<String> preberiSkupino(String imeDatoteke) {
            ArrayList<String> skupina = new ArrayList<>();

            try {
                Scanner sc = new Scanner(new File(imeDatoteke));
                while (sc.hasNextLine()) {
                    skupina.add(sc.nextLine());
                }
            } catch (FileNotFoundException e) {
                System.err.println("ERR: Ne najdem datoteke z imenom " + imeDatoteke);
            }

            return skupina;
        }

        public SkupinaClass() {
            skupine = new ArrayList<>();

            for (int i = 1; i < 10; i++) {
                ArrayList<String> skupina = preberiSkupino("src/main/resources/" + i + "skupina.txt");
                Collections.shuffle(skupina);
                skupine.add(skupina);
            }
        }

        public int getSteviloUdelezencev() {
            int velikost = 0;
            for (ArrayList a : skupine) {
                velikost += a.size();
            }
            return velikost;
        }

        public String[][] ustvariEkipe(int velikostEkipe) {
            int stEkip = (int) Math.ceil(this.getSteviloUdelezencev() / velikostEkipe);
            String[][] ekipe = new String[stEkip][velikostEkipe];

            int x = 0;
            int y = 0;
            for (ArrayList<String> skupina : skupine) {
                for (String s : skupina) {
                    if (x >= stEkip) {
                        x = 0;
                        y++;
                    }

                    if (y >= velikostEkipe) {
                        return ekipe;
                    }

                    ekipe[x][y] = s;
                    x++;
                }
            }

            return ekipe;
        }
    }

    @Override
    public String getName() {
        return "ekipe";
    }

    @Override
    public String getDescription() {
        return "Skupine razdeli posteno po ekipah";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.INTEGER, "velikostekip", "Koliko velike skupine želimo", true)
                .setMinValue(MIN_TEAM_SIZE)
                .setMaxValue(MAX_TEAM_SIZE));
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        int velikostEkip = event.getOption("velikostekip").getAsInt();

        SkupinaClass skupine = new SkupinaClass();
        String[][] ekipe = skupine.ustvariEkipe(velikostEkip);
        izpisEkipeNavzdol(ekipe);

        String sklanjatev = switch (velikostEkip) {
            case 1 -> "skupino";
            case 2 -> "skupini";
            case 3, 4 -> "skupine";
            default -> "skupin";
        };
        event.reply("Ekipe razdeljene po " + velikostEkip + " " + sklanjatev).queue();

        File file = new File("src/main/resources/ekipe.txt");
        event.getChannel().sendFiles(FileUpload.fromData(file)).queue();
    }

    private static void izpisiEkipeTxt(String[][] ekipe) {
        String fileName = "src/main/resources/ekipe.txt";

        try (FileWriter fw = new FileWriter(fileName); PrintWriter pw = new PrintWriter(fw)) {
            for (int i = 0; i < ekipe[0].length; i++) {
                pw.printf("%20s", (i + 1) + " skupina");
            }

            pw.println();

            for (String[] ekipa : ekipe) {
                for (String s : ekipa) {
                    pw.printf("%20s", s);
                }
                pw.println();
            }
        } catch (IOException e) {
            System.err.println("ERR: Napaka pri branju/pisanju datoteke: " + e);
        }
    }

    private static void izpisEkipeNavzdol(String[][] ekipe){
        String fileName = "src/main/resources/ekipe.txt";
        try (FileWriter fw = new FileWriter(fileName); PrintWriter pw = new PrintWriter(fw)) {
            for (int i = 0; i < ekipe[0].length; i++) {
                pw.printf("%20s", (i + 1) + " skupina\n");
                for (String s : ekipe[i]) {
                    pw.printf("%20s\n", s);
                }
                pw.println();
            }
        } catch (IOException e) {
            System.err.println("ERR: Napaka pri branju/pisanju datoteke: " + e);
        }

    }
}
