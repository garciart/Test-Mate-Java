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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * TestMate model class for test objects
 * @author Rob Garcia at rgarcia@rgprogramming.com
 */
public class Test {
    private final ArrayList<TestQuestion> testQuestions = new ArrayList<>();

    /**
     * Class constructor
     * @param testFileName the name of the test data file
     * @throws IOException When file cannot be opened
     */
    public Test(String testFileName) throws IOException {
        Settings s = new Settings();
        try {
            s.getSettingsFromFile();
        }
        catch(IOException ex) {
            System.out.println("Unable to read settings file: " + ex.toString());
            System.out.println("Applying default settings...");
            s.saveSettingsToFile(Constants.QuestionOrder.DEFAULT, Constants.TermDisplay.TERMISQUESTION, Constants.ProvideFeedback.YES);
        }
        try {
            ArrayList<TestData> testData = readFile(testFileName);
            ArrayList<Integer> ktIndex = new ArrayList<>();
            for(int x = 0; x < testData.size(); x++) {
                if(testData.get(x).getQuestionType() == Constants.QuestionType.K) {
                    ktIndex.add(x);
                }
            }
            int ktCount = 0;
            for(int x = 0; x < testData.size(); x++) {
                Constants.QuestionType qt = testData.get(x).getQuestionType();
                RandomNumbers rn;
                switch(qt) {
                    case K:
                        KeyTerm kt = (KeyTerm)testData.get(x);
                        ArrayList<String> ktTempChoices = new ArrayList<>();
                        rn = new RandomNumbers((ktIndex.size() - 1), ktCount, 3);
                        boolean displayTermAsQuestion = true;
                        switch(s.getTermDisplaySetting()) {
                            case DEFISQUESTION:
                                displayTermAsQuestion = false;
                                break;
                            case MIXEDQUESTION:
                                displayTermAsQuestion = new Random().nextBoolean();
                                break;
                            case TERMISQUESTION:
                            default:
                                break;
                        }
                        if(displayTermAsQuestion) {
                            for(int y = 0; y <= 3; y++) {
                                ktTempChoices.add(((KeyTerm)testData.get(ktIndex.get(rn.getUniqueArray()[y]))).getKTDefinition());
                            }
                            testQuestions.add(new TestQuestion(qt, kt.getKeyTerm(), kt.getMediaType(), kt.getMediaFileName(), 3, ktTempChoices, rn.getIndexLocation(), kt.getExplanation()));
                        }
                        else {
                            for(int y = 0; y <= 3; y++) {
                                ktTempChoices.add(((KeyTerm)testData.get(ktIndex.get(rn.getUniqueArray()[y]))).getKeyTerm());
                            }
                            testQuestions.add(new TestQuestion(qt, kt.getKTDefinition(), kt.getMediaType(), kt.getMediaFileName(), 3, ktTempChoices, rn.getIndexLocation(), kt.getExplanation()));
                        }
                        ktCount++;
                        break;
                    case M:
                        MultipleChoice mc = (MultipleChoice)testData.get(x);
                        ArrayList<String> mcTempChoices = new ArrayList<>();
                        rn = new RandomNumbers(3, 0, 3);
                        for(int i = 0; i <= mc.getMCNumberOfChoices(); i++) {
                            mcTempChoices.add(mc.getMCChoices().get(rn.getUniqueArray()[i]));
                        }
                        testQuestions.add(new TestQuestion(qt, mc.getMCQuestion(), mc.getMediaType(), mc.getMediaFileName(), mc.getMCNumberOfChoices(), mcTempChoices, rn.getIndexLocation(), mc.getExplanation()));
                        break;
                    case T:
                        TrueFalse tf = (TrueFalse)testData.get(x);
                        ArrayList<String> tfTempChoices = new ArrayList<>();
                        tfTempChoices.add("true");
                        tfTempChoices.add("false");
                        testQuestions.add(new TestQuestion(qt, tf.getTFQuestion(), tf.getMediaType(), tf.getMediaFileName(), 1, tfTempChoices, (tf.getTFAnswer() ? 0 : 1) , tf.getExplanation()));
                        break;
                    default:
                        throw new IllegalArgumentException("Corrupt data. Check structure and values.");
                }
            }
            if(s.getQuestionOrderSetting() == Constants.QuestionOrder.RANDOM) {
                RandomNumbers qoArray = new RandomNumbers(testQuestions.size() - 1);
                for(int x = 0; x < testQuestions.size(); x++) {
                    TestQuestion temp = testQuestions.get(x);
                    testQuestions.set(x, testQuestions.get(qoArray.getUniqueArray()[x]));
                    testQuestions.set(qoArray.getUniqueArray()[x], temp);
                }
            }
        }
        catch (IOException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.toString());
        }
    }

    /**
     * Get the number of questions (1-based)
     * @return the size of the testQuestion arraylist
     */
    public final int getListSize() {
        return testQuestions.size();
    }

    /**
     * 
     * @param index the index of the testQuestion
     * @return the testQuestion object
     */
    public final TestQuestion getTestQuestionByIndex(int index) {
        if(index >= testQuestions.size()) {
            return null;
        }
        else {
            return testQuestions.get(index);
        }
    }
    
    /**
     * Method to read file data and create an ArrayList of Animal objects
     * @return The Animal list
     * @throws InvalidAnimalException When an invalid name is encountered
     * @throws FileNotFoundException When file cannot be found
     * @throws IOException When file cannot be opened
     */    
    private ArrayList<TestData> readFile(String testFileName) throws FileNotFoundException, IOException {
        // Due to MultipleChoice's fluctuating size, we will use getters and setters instead of a constructor for all question types
        ArrayList<TestData> testData = new ArrayList<>();
        FileReader fileReader = new FileReader(testFileName);
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String firstLine;
            while(!Utilities.isNullOrEmpty(firstLine = bufferedReader.readLine())) {
                if(firstLine.equals(Constants.QuestionType.K.toString())) {
                    KeyTerm k = new KeyTerm();
                    k.setKeyTerm(Utilities.fixEscapeCharacters(bufferedReader.readLine()));
                    Constants.MediaType tempMT = Constants.MediaType.valueOf(bufferedReader.readLine());
                    String tempMF = bufferedReader.readLine();
                    k.validateAndSetMedia(tempMT, tempMF);
                    k.setKTDefinition(Utilities.fixEscapeCharacters(bufferedReader.readLine()));
                    k.setExplanation(k.getKeyTerm() + ": " + k.getKTDefinition());
                    testData.add(k);
                }
                else if(firstLine.equals(Constants.QuestionType.M.toString())) {
                    MultipleChoice m = new MultipleChoice();
                    m.setMCQuestion(Utilities.fixEscapeCharacters(bufferedReader.readLine()));
                    Constants.MediaType tempMT = Constants.MediaType.valueOf(bufferedReader.readLine());
                    String tempMF = bufferedReader.readLine();
                    m.validateAndSetMedia(tempMT, tempMF);
                    m.setMCNumberOfChoices(Integer.parseInt(bufferedReader.readLine()));
                    for(int x = 0; x <= m.getMCNumberOfChoices(); x++) {
                        m.getMCChoices().add(Utilities.fixEscapeCharacters(bufferedReader.readLine()));
                    }
                    String tempExplanation  = Utilities.fixEscapeCharacters(bufferedReader.readLine());
                    if(tempExplanation.toLowerCase().equals("null") || Utilities.isNullOrEmpty(tempExplanation)) {
                        m.setExplanation("The answer is: " + m.getMCChoices().get(0));
                    }
                    else {
                        m.setExplanation(tempExplanation);
                    }
                    testData.add(m);
                }
                else if(firstLine.equals(Constants.QuestionType.T.toString())) {
                    TrueFalse t = new TrueFalse();
                    t.setTFQuestion(Utilities.fixEscapeCharacters(bufferedReader.readLine()));
                    Constants.MediaType tempMT = Constants.MediaType.valueOf(bufferedReader.readLine());
                    String tempMF = bufferedReader.readLine();
                    t.validateAndSetMedia(tempMT, tempMF);
                    t.setTFAnswer(Boolean.valueOf(bufferedReader.readLine()));
                    String tempExplanation  = Utilities.fixEscapeCharacters(bufferedReader.readLine());
                    if(tempExplanation.toLowerCase().equals("null") || Utilities.isNullOrEmpty(tempExplanation)) {
                        t.setExplanation("The answer is: " + t.getTFAnswer());
                    }
                    else {
                        t.setExplanation(tempExplanation);
                    }
                    testData.add(t);
                }
                else {
                    throw new IllegalArgumentException("Corrupt data file. Check structure and values.");
                }
            }
        }
        return testData;
    }
}