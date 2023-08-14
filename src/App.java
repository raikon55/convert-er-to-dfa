import automata.NonDeterministicAutomata;
import regexp.RegularExpression;
import utils.JFlapFile;

public class App {
    public static void main(String[] args) throws Exception {

        JFlapFile jflap = new JFlapFile();
        NonDeterministicAutomata nda = new NonDeterministicAutomata();
        jflap.readRegularExpression("./test/er.jff");
        RegularExpression regularExpression = new RegularExpression(jflap.getRegularExpression());
        nda.createAutomataFromRegularExpression(regularExpression);
        jflap.writeAutomata(nda.getStates());
    }
}
