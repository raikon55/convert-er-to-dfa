import java.lang.String;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import automata.DeterministicAutomata;

public class SimDFA {

    public static void TestEntry(String filePath, DeterministicAutomata dfa){
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter("pub.out"));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if(dfa.sampleTest(line))
                    writer.write("Sample " + line + " accept");
                else
                    writer.write("Sample " + line + " Reject");

                writer.newLine();
            }

            // Write data to the file


            // Close the BufferedWriter after writing
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[]args){
        // Scanner scanner = new Scanner(System.in);
        // String file = scanner.nextLine();
        String file = "test/teste-dfa.jff";
        DeterministicAutomata dfa = new DeterministicAutomata(file);
        // WriteAutomata()
        TestEntry("pub.in", dfa);    
        System.out.println(dfa);
    }
}

