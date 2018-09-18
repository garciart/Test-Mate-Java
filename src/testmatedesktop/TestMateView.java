/*
 * The MIT License
 *
 * Copyright 2018 Rob Garcia at rgarcia@rgprogramming.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package testmatedesktop;

import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author Rob Garcia at rgarcia@rgprogramming.com
 */
public class TestMateView {
    public void introduction() {
        System.out.println("Welcome to TestMate!");
    }
    
    /**
     * Display main menu and sends choice to controller
     * @return The user's choice
     */
    public int mainMenuView() {
        System.out.println("Please select one of the following choices...");
        System.out.println("[1] Take a Test...");
        System.out.println("[2] Continue a Test...");
        System.out.println("[3] Change Settings...");
        System.out.println("[4] About TestMate...");
        System.out.println("[5] Exit Testmate");
        System.out.print("Enter your choice here: ");
        return getAndValidateChoice(5, false);
    }
    
    public int askQuestionView(int questionNumber, TestQuestion tq) {
        System.out.println((questionNumber + 1) + ". " + tq.getQuestion());
        for(int y = 0; y <= tq.getNumberOfChoices(); y++) {
            System.out.println(Constants.LETTERS[y] + ". " + tq.getChoices().get(y) + (y == tq.getCorrectAnswerIndex() ? " - HERE!" : ""));
        }
        System.out.print("Enter your choice or [0] to exit the test: ");
        // numberOfChoices is zero-based and getAndValidateChoice is one-based
        // Add one to use getAndValidateChoice and subtract 1 to return the correct index
        return (getAndValidateChoice(tq.getNumberOfChoices() + 1, true) - 1);
    }

    public void feedbackView(boolean result, String explanation) {
        System.out.print(result ? "Correct. " : "Incorrect. ");
        System.out.println(explanation);
        System.out.println();
    }
    
    public int settingsMenuView(String qo, String td, String pf) {
        System.out.println("Please select which setting to change...");
        System.out.println("[1] Question Order: " + qo);
        System.out.println("[2] Term Display: " + td);
        System.out.println("[3] Provide Feedback: " + pf);
        System.out.print("Enter your choice here or [0] to return to Settings Menu: ");
        return getAndValidateChoice(3, false);
    }
    
    public int questionOrderSettingView(String qo) {
        System.out.println("Your current question order setting is " + qo);
        System.out.println("Please select a new setting...");
        System.out.println("[1] Display questions as read from the file (Default)");
        System.out.println("[2] Randomize the order");
        System.out.print("Enter your choice here or [0] to return to Settings Menu: ");
        return getAndValidateChoice(2, false);
    }
    
    public int termDisplaySettingView(String td) {
        System.out.println("Your current term display setting is " + td);
        System.out.println("Please select which setting to change...");
        System.out.println("[1] Display terms as question (Default)");
        System.out.println("[2] Display definitions as question");
        System.out.println("[3] Mix it up");
        System.out.print("Enter your choice here or [0] to return to Settings Menu: ");
        return getAndValidateChoice(3, false);
    }
    
    public int provideFeedbackSettingView(String pf) {
        System.out.println("Your current provide feedback setting is " + pf);
        System.out.println("Please select which setting to change...");
        System.out.println("[1] Provide feedback after each answer (Default)");
        System.out.println("[2] Wait until the end of the test");
        System.out.print("Enter your choice here or [0] to return to Main Menu: ");
        return getAndValidateChoice(2, false);
    }
    
    public void aboutView() {
        System.out.println("Test Mate");
        System.out.println("Copyright 1993-" + Calendar.getInstance().get(Calendar.YEAR) + " Robert Garcia");
        System.out.println("Test Mate is a mobile self-study system, designed to assist you in achieving your educational and professional goals by allowing you to study what you want, when and where you want. Why study alone?");
        System.out.println();
    }
    
    public boolean exitView() {
        System.out.println("Are you sure you want to leave?");
        System.out.print("Enter [1] for Yes or [2] for No: ");
        boolean result = (getAndValidateChoice(2, false) == 1);
        if(result) System.out.println("Goodbye!");
        return result;
    }
    
    public int errorView(String message) {
        System.out.println("Oops! Something went wrong!");
        System.out.println(message);
        System.out.println("We've been notified and will start fixing the problem right away!\n");
        return -1;
    }
    
    /**
     * Get and validate the user's choice
     * @param numberOfChoices One-based maximum number of choices
     * @param hexFlag Only accept values between A through F
     * @return the validated choice
     */
    private int getAndValidateChoice(int numberOfChoices, boolean hexFlag) {
        boolean validChoice = false;
        int choice = 0;
        while (!validChoice) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt(16)) {
                choice = scanner.nextInt(16);
                // Convert hex letter value to one-based number unless choice is 0 (used to exit) 
                if (hexFlag && choice != 0) choice -= 9;
                if (choice < 0 || choice > numberOfChoices) {
                    System.out.print("That choice is not available. Please try again: ");
                }
                else {
                    validChoice = true;
                }
            }
            else {
                System.out.print("Invalid input. Please try again: ");
            }
        }
        System.out.println();
        return choice;
    }
}
