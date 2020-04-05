package ro.iteahome.shop.ui.popups;

import java.util.Scanner;

public class InputFrame {

    public static String getInput() {
        return new Scanner(System.in).nextLine();
    }

    public static String getInputFromPrompt(String PROMPT) {
        OutputFrame.printInfo(PROMPT);
        return new Scanner(System.in).nextLine();
    }

    public static String getInputFromAlert(String ALERT) {
        OutputFrame.printAlert(ALERT);
        return new Scanner(System.in).nextLine();
    }

    public static String getInputFromSubtitle(String SUBTITLE) {
        OutputFrame.printSubtitle(SUBTITLE);
        return new Scanner(System.in).nextLine();
    }
}