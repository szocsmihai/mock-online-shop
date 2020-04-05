package ro.iteahome.shop.ui;

import ro.iteahome.shop.ui.popups.OutputFrame;

/**
 * Loop UIs will be used for continuously looping processes within the same menu (e.g. browsing products): they print
 * out their menu title, present the user with an initial set of information (e.g. available product categories) and a
 * set of available options (e.g. filtering products by category), then executes a series of actions based on the user's
 * initial chosen option. The entire Loop UI serves a singular purpose, who's end is reachable through said series of
 * actions. Upon completion of a loop, the UI reprints the available information and options (but not the menu title).
 * The only way to close a Loop UI is by inputting "0" when presented with the UI's options.
 */
public abstract class LoopUI extends UI {

    /**
     * Loop UIs usually depend on a set of local variables that are called upon by the different class methods. These
     * local variables are defined outside of the {@code start()} method as null or empty, receive values within the
     * main logic and are reset (via the {@code resetLocalVariables()} method) before subsequent loops start.
     */

    @Override
    public void start() {
        OutputFrame.printTitle(menuTitle);
        do {
            resetLocalVariables();
            showInitialInfo();
            showSelectAndExecuteOption();
        } while (!selectedOption.equals("0"));
    }

    void resetLocalVariables() {
    }

    void showInitialInfo() {
    }
}