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
package testmatejava;

import java.util.ArrayList;

/**
 *
 * @author Rob Garcia at rgarcia@rgprogramming.com
 */
public final class MultipleChoice extends TestData {
    private String mcQuestion;
    private int mcNumberOfChoices = 0;
    private ArrayList<String> mcAnswer;
    private String mcExplanation;
    
    public final String getMCQuestion() {
        return mcQuestion;
    }

    public final int getMCNumberOfChoices() {
        return mcNumberOfChoices;
    }
    
    public final ArrayList<String> getMCAnswer() {
        return mcAnswer;
    }
    
    public final String getMCExplanation() {
        return mcExplanation;
    }
    
    public final void setMCQuestion(String mcQuestion) {
        if(Utility.isNullOrEmpty(mcQuestion)) throw new NullPointerException("Multiple choice questions cannot be null or empty.");
        this.mcQuestion = mcQuestion;
    }
    
    public final void setMCNumberOfChoices(int mcNumberOfChoices) {
        if(mcNumberOfChoices <= 0) throw new IllegalArgumentException("The number of multiple choice answers cannot be null or zero.");
        this.mcNumberOfChoices = mcNumberOfChoices;
    }
    
    public final void setMCAnswer(ArrayList<String> mcAnswer) {
        if(mcAnswer == null) throw new NullPointerException("Multiple choice questions must have at least one answer.");
        this.mcAnswer = mcAnswer;
    }

    public final void setMCExplanation(String mcExplanation) {
        this.mcExplanation = mcExplanation;
    }
}