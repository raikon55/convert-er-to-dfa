package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import automata.State;

public class JFlapFile {
    private String regularExpression = "";
    private String filename = "";

    public void createAutomata(String automata) {
        try {
            String str = "automata";
            FileWriter writer = new FileWriter(this.filename);
            BufferedWriter bufferWriter = new BufferedWriter(writer);

            bufferWriter.write(str);

            bufferWriter.close();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            return;
        }
    }

    public String getRegularExpression() {
        return regularExpression;
    }

    public void readRegularExpression(String filename) {
        if (!filename.endsWith(".jff")) {
            return;
        }

        this.filename = filename;
        try {
            File regExpFile = new File(filename);
            Scanner scanner = new Scanner(regExpFile);

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                if (data.contains("<expression>")) {
                    int startOfExpression = data.indexOf(">") + 1;
                    int endOfExpression = data.lastIndexOf("<");
                    this.regularExpression = data.substring(startOfExpression, endOfExpression);
                }
            }

            scanner.close();
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
            return;
        }
    }

    public void writeAutomata(Map<String, Map<String, Set<State>>> NFtable) {
        String filename = "test/afd.jff";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.append(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Created with JFLAP 6.4.--><structure>\n");
            writer.append("\t<type>fa</type>\n");
            writer.append("\t<automaton>\n");

            NFtable.forEach((from, transitions) -> {
                String state = String.format("\t\t<state id=\"%s\" name=\"%s\">\n", from.replace("q", ""), from);
                String x_coord = String.format("\t\t\t<x>%f</x>\n", Math.random() % 1000);
                String y_coord = String.format("\t\t\t<y>%f</y>\n\t\t</state>\n", Math.random() % 1000);

                try {
                    writer.append(state + x_coord + y_coord);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                transitions.forEach((symbol, to) -> {
                    System.out.println("\tSymbol=" + symbol + ":");
                    to.forEach(t -> {
                        String transition = String
                                .format("\t\t<transition>\n\t\t\t<from>%d</from>\n\t\t\t<to>%d</to>\n\t\t\t<read>%s<read/>\n\t\t</transition>\n",
                                        Integer.parseInt(from.replace("q", "")), t.getId(), symbol);
                        try {
                            writer.append(transition);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("\t\t" + t);
                    });
                });
            });

            writer.append("\t</automaton>\n");
            writer.append("\t</structure>\n");
            writer.close();
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
            exc.printStackTrace();
        }
    }
}
