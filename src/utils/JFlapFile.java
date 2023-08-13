package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
}
