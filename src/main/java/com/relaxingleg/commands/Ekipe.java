package com.relaxingleg.commands;

import com.relaxingleg.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.*;
import java.util.*;

public class Ekipe implements ICommand {
    private static final int MIN_TEAM_SIZE = 2;
    private static final int MAX_TEAM_SIZE = 14;
    String outputFileName = "src/main/resources/ekipe.txt";

    private class EkipaClass {
        String[][] ekipe;

        public EkipaClass(SkupinaClass sc, int velikostEkipe) {
            int stEkip = (int) Math.ceil(sc.getSteviloUdelezencev() / velikostEkipe);
            ekipe = new String[stEkip][velikostEkipe];

            int x = 0;
            int y = 0;
            for (ArrayList<String> skupina : sc) {
                for (String s : skupina) {
                    if (x >= stEkip) {
                        x = 0;
                        y++;
                    }

                    if (y >= velikostEkipe) {
                        return;
                    }

                    ekipe[x][y] = s;
                    x++;
                }
            }
        }

        public void izpisiEkipeVodoravno() {
            try (FileWriter fw = new FileWriter(outputFileName); PrintWriter pw = new PrintWriter(fw)) {
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

        public void izpisiEkipeNavzdol() {
            try (FileWriter fw = new FileWriter(outputFileName); PrintWriter pw = new PrintWriter(fw)) {
                for (int i = 0; i < ekipe[0].length; i++) {
                    pw.println((i + 1) + " skupina\n");
                    for (String s : ekipe[i]) {
                        pw.println("  " + s);
                    }
                    pw.println();
                }
            } catch (IOException e) {
                System.err.println("ERR: Napaka pri branju/pisanju datoteke: " + e);
            }
        }
    }

    private class SkupinaClass implements Iterable<ArrayList<String>> {
        public ArrayList<ArrayList<String>> skupine;

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
            skupine = new ArrayList<ArrayList<String>>();

            for (int i = 1; i < 10; i++) {
                ArrayList<String> skupina = preberiSkupino("src/main/resources/" + i + "skupina.txt");
                Collections.shuffle(skupina);
                skupine.add(skupina);
            }
        }

        public int getSteviloUdelezencev() {
            int velikost = 0;
            for (ArrayList<String> al : skupine) {
                velikost += al.size();
            }
            return velikost;
        }

        @Override
        public Iterator<ArrayList<String>> iterator() {
            return new Iterator<ArrayList<String>>() {
                private int index = 0;

                public boolean hasNext() {
                    return index < skupine.size();
                }

                public ArrayList<String> next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }

                    return skupine.get(index++);
                }
            };
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

        new EkipaClass(new SkupinaClass(), velikostEkip).izpisiEkipeNavzdol();

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
}
