import automata.NonDeterministicAutomata;
import regexp.RegularExpression;
import utils.JFlapFile;

public class App {
    public static void main(String[] args) throws Exception {
        JFlapFile jflap = new JFlapFile();
        NonDeterministicAutomata nda = new NonDeterministicAutomata();
        jflap.readRegularExpression("./test/sample_test.jff");
        RegularExpression regularExpression = new RegularExpression(jflap.getRegularExpression());
        nda.createAutomataFromRegularExpression(regularExpression);
        // System.out.println(nda);
    }
}
