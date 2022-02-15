package edu.unl.raikes.metacode;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The WhatToTesterator is a chatbot-like tool that helps students know what needs to be tested in their code.
 *
 * @author Dr. Stephanie Valentine
 *
 */
public class WhatToTesterator {
    public static Scanner scanner;
    public static ArrayList<String> tests;

    static {
        scanner = new Scanner(System.in);
        tests = new ArrayList<>();
    }

    /**
     * This function handles the prompting functionality related to the addition of a feature to a codebase.
     *
     * @param indentLevel How many spaces indented should the text be? (If unknown, supply 0)
     */
    private static void promptAboutAFeature(int indentLevel) {
        String[] behaviors = prompt("What does your feature enable the consumer of your code to do "
                + "(if more than one thing, separate by commas)?", indentLevel).split(", ");
        for (String behavior : behaviors) {
            newline();
            promptAboutABehavior("the code", behavior, indentLevel + 1);
        }
    }

    /**
     * This function handles the prompting functionality related to the addition of a feature to a codebase.
     *
     * @param indentLevel How many spaces indented should the text be? (If unknown, supply 0)
     */
    private static void promptAboutAClass(int indentLevel) {
        String className = prompt("What's the name of that class?", indentLevel);

        String generalBehavior = prompt(
                "Okay. From the perspective of a consumer of this class, what does a " + className + " do?",
                indentLevel);

        String[] behaviors = prompt("Let's break that up into parts. Here's what you said \"" + generalBehavior
                + "\". Will you write those behaviors in a comma separated list?", indentLevel).split(", ");

        for (String behavior : behaviors) {
            newline();
            promptAboutABehavior(className, behavior, indentLevel + 1);
        }

        newline();
        String[] initRequirements = prompt("This has been really helpful. One last thing. What does a " + className
                + " need to know in order to be successful? What needs to be initialized in a " + className
                + " before use (comma-separated list)?", indentLevel).split(", ");

        for (String requirement : initRequirements) {
            tests.add(className + " initialization includes: " + requirement);
        }
    }

    /**
     * This function handles the prompting functionality related to what needs to be tested for a single behavior.
     *
     * @param indentLevel How many spaces indented should the text be? (If unknown, supply 0)
     */
    private static void promptAboutABehavior(String doerOfTheBehavior, String theBehavior, int indentLevel) {
        println("Got it. Let's talk more about \"" + theBehavior + "\".", indentLevel);

        indentLevel++;

        String happyPath = prompt("On the happy path, how do you know that " + doerOfTheBehavior
                + " successfully performs the behavior \"" + theBehavior + "\"?", indentLevel) + " (happy path)";
        tests.add(happyPath);

        String possibleTests = prompt("What are some circumstances/contexts when you would expect a "
                + doerOfTheBehavior + " to successfully \"" + theBehavior + "\" (comma-separated list, please)?",
                indentLevel);

        possibleTests += ", " + prompt("What are some constraints of the " + doerOfTheBehavior
                + " in terms of their behavior \"" + theBehavior + "\" (comma-separated list, please)?", indentLevel);

        possibleTests += ", " + prompt("Please share any other weird or extreme edge cases related to \"" + theBehavior
                + "\" (comma-separated list, please).", indentLevel);

        possibleTests += ", " + prompt(
                "What are the side-effects related to the " + doerOfTheBehavior + " behavior \"" + theBehavior + "\"?",
                indentLevel);

        String[] suggestedTests = possibleTests.split(", ");

        for (String test : suggestedTests) {
            if (!test.equals("")) {
                tests.add(doerOfTheBehavior + " " + theBehavior + ": " + test);
            }
        }
    }

    /**
     * This function prompts the user for a string value, then returns the user- value to the caller.
     *
     * @param promptString The string that asks a console user for a specific need
     * @param indentLevel How many spaces indented should the text be? (If unknown, supply 0)
     * @return the user-supplied response to the prompt
     */
    private static String prompt(String promptString, int indentLevel) {
        System.out.print(makeSpaces(indentLevel) + promptString + " ");
        return scanner.nextLine();
    }

    /**
     * This function prints a string to the console, with a preceding number of spaces.
     *
     * @param toPrint the string to be printed to the console
     * @param indentLevel How many spaces indented should the text be? (If unknown, supply 0)
     */
    private static void println(String toPrint, int indentLevel) {
        System.out.println(makeSpaces(indentLevel) + toPrint);
    }

    /**
     * This function prints a newline to the console.
     *
     */
    private static void newline() {
        System.out.println();
    }

    /**
     * This function generates a string consisting of a provided number of spaces.
     *
     * @param indentLevel the number of spaces requested
     * @return a string consisting of indentLevel spaces
     */
    private static String makeSpaces(int indentLevel) {
        String spaces = "";
        for (int i = 0; i < indentLevel; i++) {
            spaces += "  ";
        }
        return spaces;
    }

    /**
     * This function provides the chatbot functionality. What needs to be tested?
     *
     * @param args no args are used in this method
     */
    public static void main(String[] args) {
        println("So you wrote/need to write some code to do a thing. Great!\n"
                + "Let's figure out what you need to test.", 0);
        newline();

        String choice = prompt("Did you write a new \"class\", a new \"feature\", or a new \"bug\" fix?", 0);

        newline();

        if (choice.equals("class")) {
            promptAboutAClass(1);
        } else if (choice.equals("feature")) {
            promptAboutAFeature(1);
        } else if (choice.equals("bug")) {
            println("Simulate the circumstances that reproduce the bug, then fix the bug, ensure test passes. "
                    + "Keep your test in the codebase.", 0);
            return;
        }

        newline();
        println("Great. I'll help you organize all of those thoughts. I think you should test at least the following: ",
                0);

        for (String test : tests) {
            println(test, 2);
        }

        scanner.close();
    }

}
