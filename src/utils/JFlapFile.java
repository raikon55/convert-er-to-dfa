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

import automata.DeterministicAutomata;

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
            writer.write(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Created with JFLAP 7.1.--><structure>\n");
            writer.append("\t<type>fa</type>\n");
            writer.append("\t<automaton>\n");

            NFtable.forEach((from, transitions) -> {
                String state = String.format("\t\t<state id=\"%s\" name=\"%s\">\n", from.replace("q", ""), from);
                String x_coord = String.format("\t\t\t<x>%d</x>\n", (int) (Math.random() * 100) % 1000);
                String y_coord = String.format("\t\t\t<y>%d</y>\n", (int) (Math.random() * 100) % 1000);

                if (from.equals("q0")) {
                    y_coord += "\t\t\t<initial/>\n\t\t</state>\n";
                } else {
                    y_coord += "\t\t</state>\n";
                }

                try {
                    writer.append(state + x_coord + y_coord);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                transitions.forEach((symbol, to) -> {
                    to.forEach(t -> {
                        String transition = "";
                        if (symbol.equals(SpecialSymbols.LAMBDA)) {
                            transition = String
                                    .format("\t\t<transition>\n\t\t\t<from>%d</from>\n\t\t\t<to>%d</to>\n\t\t\t<read/>\n\t\t</transition>\n",
                                            Integer.parseInt(from.replace("q", "")), t.getId(), symbol);
                        } else {
                            transition = String
                                    .format("\t\t<transition>\n\t\t\t<from>%d</from>\n\t\t\t<to>%d</to>\n\t\t\t<read>%s</read>\n\t\t</transition>\n",
                                            Integer.parseInt(from.replace("q", "")), t.getId(), symbol);
                        }

                        try {
                            writer.append(transition);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    });
                });
            });

            writer.append("\t</automaton>\n");
            writer.append("</structure>\n");
            writer.close();
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
    }
}
