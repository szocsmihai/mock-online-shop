package ro.iteahome.shop;

import ro.iteahome.shop.ui.WelcomeUI;

public class Application {

    public static void main(String[] args) {

        System.out.println(
                "\n" +
                        "----------------------------------------------------------------------------------------------------\n" +
                        "----------------------------------------------------------------------------------------------------\n" +
                        "                                                                                                    \n" +
                        "                   W E L C O M E   T O   T H E   M O C K   O N L I N E   S H O P.                   \n" +
                        "                                   SELECT AN OPTION BY ITS NUMBER                                   \n" +
                        "                                                                                                    \n" +
                        "----------------------------------------------------------------------------------------------------\n" +
                        "----------------------------------------------------------------------------------------------------");

        new WelcomeUI().start();

        //TODO: Review exception management. Catch them earlier where possible.
        //TODO: Change collection manipulation to streams.
        //TODO: Optimize the argument types. "UIs spit Strings" doesn't mean they don't know any other type. UIs should indeed read String, but send something more specific to the services.

        //TODO: Clean up entire program code. Make it sparkle.
        //TODO: Remove unused code at the very end of the exercise.
    }
}