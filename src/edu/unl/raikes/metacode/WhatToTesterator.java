package edu.unl.raikes.metacode;

import java.util.ArrayList;
import java.util.Scanner;

public class WhatToTesterator {
    public static Scanner scanner;
    public static ArrayList<String> tests;

    static {
        scanner = new Scanner(System.in);
        tests = new ArrayList<String>();
    }

    public static String prompt(String promptString, int indentLevel) {
        System.out.print(makeSpaces(indentLevel) + promptString + " ");
        return scanner.nextLine();
    }

    public static void println(String toPrint, int indentLevel) {
        System.out.println(makeSpaces(indentLevel) + toPrint);
    }

    public static void newline() {
        System.out.println();
    }

    public static String makeSpaces(int indentLevel) {
        String spaces = "";
        for (int i = 0; i < indentLevel; i++) {
            spaces += "  ";
        }
        return spaces;
    }

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
            println("Simulate the circumstances that reproduce the bug, then fix the bug, ensure test passes. Keep your test in the codebase.",
                    0);
            return;
        }

        newline();
        println("Great. I'll help you organize all of those thoughts. I think you should test at least the following: ",
                0);

        for (String test : tests) {
            println(test, 2);
        }

    }

    private static void promptAboutAFeature(int indentLevel) {
        String[] behaviors = prompt(
                "What does your feature enable the user to do (if more than one thing, separate by commas)?",
                indentLevel).split(", ");
        for (String behavior : behaviors) {
            newline();
            promptAboutABehavior("user", behavior, indentLevel + 1);
        }
    }

    public static void promptAboutAClass(int indentLevel) {
        String className = prompt("What's the name of that class?", indentLevel);

        String generalBehavior = prompt("Okay. From the perspective of a user, what does a " + className + " do?",
                indentLevel);

        String[] behaviors = prompt("Let's break that up into parts. Here's what you said \"" + generalBehavior
                + "\". Will you write those behaviors in a comma separated list?", indentLevel).split(", ");

        for (String behavior : behaviors) {
            newline();
            promptAboutABehavior(className, behavior, indentLevel + 1);
        }

    }

    public static void promptAboutABehavior(String doerOfTheBehavior, String theBehavior, int indentLevel) {
        println("Got it. Let's talk more about \"" + theBehavior + "\".", indentLevel);

        indentLevel++;
        prompt("How exactly does a " + doerOfTheBehavior + " perform this behavior?", indentLevel);

        String successMetric = prompt("How do you know that " + doerOfTheBehavior
                + " successfully performs the behavior \"" + theBehavior + "\"?", indentLevel);

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
            tests.add(doerOfTheBehavior + " " + theBehavior + ": " + test);
        }
    }

}
