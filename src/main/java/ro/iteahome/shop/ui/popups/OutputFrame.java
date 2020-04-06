package ro.iteahome.shop.ui.popups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OutputFrame {

    public static void printUnformatted(String TEXT) {
        System.out.println(TEXT);
    }

    public static void printTitle(String TITLE) {
        System.out.println("\n");
        System.out.println("****************************************************************************************************");
        System.out.println(TITLE);
        System.out.println("****************************************************************************************************");
    }

    public static void printSubtitle(String SUBTITLE) {
        System.out.println(SUBTITLE);
        System.out.println("....................................................................................................");
    }

    public static void printInfo(String INFO) {
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println(INFO);
        System.out.println("----------------------------------------------------------------------------------------------------");
    }

    public static void printConfirmation(String CONFIRMATION) {
        System.out.println("....................................................................................................");
        System.out.println(CONFIRMATION);
        System.out.println("....................................................................................................");
    }

    public static void printAlert(String ALERT) {
        System.out.println("                                                                                                    ");
        System.out.println("[WARNING] " + ALERT);
        System.out.println("                                                                                                    ");
    }

    public static void printList(ArrayList<String> stringList) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for (String string : stringList) {
            System.out.println(string);
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public static void printBulletList(ArrayList<String> listItems) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for (String item : listItems) System.out.println("- " + item);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public static void printMapPairs(HashMap<?, ?> map) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for (Map.Entry<?, ?> pair : map.entrySet()) {
            System.out.println(pair.getKey() + " : " + pair.getValue());
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}