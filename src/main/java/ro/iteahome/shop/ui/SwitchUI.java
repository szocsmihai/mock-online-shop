package ro.iteahome.shop.ui;

import ro.iteahome.shop.ui.popups.OutputFrame;

/**
 * Switch UIs will be used for navigational purposes: they print out their menu title, present the user with a set of
 * options (navigation to other UIs or singular actions) and execute the corresponding action based on the user's input.
 * In doing so, the user "temporarily leaves" the current UI until said option's action is finished. When returning to a
 * Switch UI, the title is always reprinted, to let the user know their position within the program. The only way to
 * close a Switch UI is by inputting "0" when presented with the UI's options.
 */
public abstract class SwitchUI extends UI {

    /**
     * Switch UIs depend on a list of actions that are possible within their context. The actual, inheriting, UIs'
     * constructors will define 3 aspects: the UI's menu title, the list of {@code uiOptions} and the
     * {@code instructionBar} by triggering the {@code setInstructionsBar()} method.
     */

    @Override
    public void start() {
        do {
            OutputFrame.printTitle(menuTitle);
            showSelectAndExecuteOption();
        } while (!selectedOption.equals("0"));
    }
}