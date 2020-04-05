package ro.iteahome.shop.ui;

import ro.iteahome.shop.ui.options.UIOption;
import ro.iteahome.shop.ui.popups.InputFrame;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;

/**
 * UIs handle the interaction between the user and the program. They print predefined information on screen and execute
 * actions based on the user's input. All UIs share a set of common traits/features:
 * <p>
 * - Every UI has a title that can be printed on screen;
 * - Every UI has a predefined set of actions it can trigger.
 * <p>
 * UIs function as follows:
 * <p>
 * - Inform the user about the available actions and how to trigger them;
 * - Get a valid input from the user;
 * - Trigger the action corresponding to the input.
 */
public abstract class UI {

    /**
     * Inheriting UIs' constructors must define (via "{@code this}"):
     * <p>
     * - The {@code menuTitle} via its setter method;
     * - The {@code UIOptions} ArrayList content by adding objects by adding {@code UIOption} objects to it;
     * - The {@code instructionsBar} via its setter method. This cannot be done until the options list is populated,
     * otherwise a {@code StringIndexOutOfBoundsException} will occur;
     * <p>
     * Inheriting UIs must also:
     * - Override the {@code start()} method to define the UI's behavior regarding its title, options and exit strategy.
     */

    String menuTitle;
    ArrayList<UIOption> uiOptions = new ArrayList<>();
    String instructionsBar;
    String selectedOption;

    void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    void setInstructionsBar() {
        this.instructionsBar = instructionsBarBuilder();
    }

    void start() {
    }

    String instructionsBarBuilder() {
        StringBuilder instructionsBar = new StringBuilder();
        for (UIOption option : uiOptions) {
            instructionsBar.append(option.getOPTION_NUMBER()).append(" : ").append(option.getOPTION_DESCRIPTION()).append(" | ");
        }
        return instructionsBar.substring(0, instructionsBar.length() - 3);
    }

    void showSelectAndExecuteOption() {
        selectedOption = InputFrame.getInputFromSubtitle(instructionsBar);
        boolean optionFound = false;
        for (UIOption option : uiOptions) {
            if (selectedOption.equals(option.getOPTION_NUMBER())) {
                option.getACTION().execute();
                optionFound = true;
                break;
            }
        }
        if (!optionFound) {
            OutputFrame.printAlert("INVALID INPUT.");
        }
    }
}