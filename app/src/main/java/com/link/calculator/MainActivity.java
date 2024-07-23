package com.link.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView calcDisplay;
    String[] operations = {"+", "-", "*", "/"};


    //method for getting the last char on Calc display
    public String getLastChar() {

        String lastChar = "";

        if (calcDisplay != null) {
            lastChar = (calcDisplay.getText().length() == 0) ? "" : calcDisplay.getText().subSequence(calcDisplay.getText().length() - 1, calcDisplay.getText().length()).toString();
        }

        return lastChar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get Calc display element reference
        calcDisplay = (TextView) findViewById(R.id.resultTextView);

    }

    //method for getting the index of last arithmetic char (+, -, *, /)
    public int getLastArithmeticIndex() {

        // -1 means there is no arithmetic
        int indexOfLastArithmetic = -1;

        //convert calc display text to string
        String displayText = calcDisplay.getText().toString();

        //if there is no input on calc display, there is no arithmetic signs so return -1
        if (displayText.length() == 0)
            return indexOfLastArithmetic;

        //inverted for loop to determine the index of last arithmetic sign
        for (int i = displayText.length() - 1; i >= 0; i--) {
            //if arithmetic sign is found, write his index to indexOfLastArithmetic
            if (displayText.charAt(i) == '+' || displayText.charAt(i) == '-' || displayText.charAt(i) == '*' || displayText.charAt(i) == '/') {
                indexOfLastArithmetic = i;
                break;
            }
        }

        // return the index of last arithmetic sign
        //return value will be sign index or -1, if there is no arithmetic sign
        return indexOfLastArithmetic;
    }


    public void handleNumericButtonClick(View v) {

        //30 character are max input for whole formula
        if (calcDisplay.length() > 30)
            return;

        //max chars for every number is 7
        if ((calcDisplay.getText().length() - getLastArithmeticIndex() + 1) > 8) {
            return;
        }

        //depending on button pressed, write digit on screen
        //digits 1-9 are trouble free, because they can be present at any location inside number
        //digit 0 can be at the beginning only if followed by dot char
        //the same way, dot must come after some digits
        switch (v.getId()) {
            case R.id.oneButton:
                calcDisplay.append("1");
                break;
            case R.id.twoButton:
                calcDisplay.append("2");
                break;
            case R.id.threeButton:
                calcDisplay.append("3");
                break;
            case R.id.fourButton:
                calcDisplay.append("4");
                break;
            case R.id.fiveButton:
                calcDisplay.append("5");
                break;
            case R.id.sixButton:
                calcDisplay.append("6");
                break;
            case R.id.sevenButton:
                calcDisplay.append("7");
                break;
            case R.id.eightButton:
                calcDisplay.append("8");
                break;
            case R.id.nineButton:
                calcDisplay.append("9");
                break;
            case R.id.zerroButton:

                //first check if some digits are entered
                if (calcDisplay.length() > 0) {

                    //get all individual numbers
                    String[] digits = calcDisplay.getText().toString().split("[-+*/]");

                    //check last number
                    //no more than one zero in the beginning of number can exists
                    if (digits.length > 0 && digits[digits.length - 1].length() == 1) {
                        if (digits[digits.length - 1].charAt(0) != '0') {
                            calcDisplay.append("0");
                        }
                    } else {
                        calcDisplay.append("0");
                    }
                    //if there is no digits entered, just write zero
                } else {
                    calcDisplay.append("0");
                }
                break;

            case R.id.dotButton:

                //first check if some chars are entered
                //if there is no chars, dot can not be printed
                if (calcDisplay.getText().length() > 0) {

                    //get all entered numbers
                    String[] digits = calcDisplay.getText().toString().split("[-+*/]");

                    //check in the current number, if dot exists
                    //if dot exists in the current number, dot can not be entered more than once
                    if (digits.length > 0 && !digits[digits.length - 1].contains(".")) {
                        if (!getLastChar().equals(".")) {
                            calcDisplay.append(".");
                        }
                    }
                }
                break;
        }
    }

    public void handleArithmeticButtonClick(View v) {

        //30 character are max input for whole formula
        if (calcDisplay.length() > 30)
            return;

        // no more than one arithmetic operator in row
        if (Arrays.asList(operations).contains(getLastChar())) {
            return;
        }

        //depending on button pressed, write arithmetic sign on screen
        switch (v.getId()) {
            case R.id.plusButton:
                calcDisplay.append("+");
                break;
            case R.id.minusButton:
                calcDisplay.append("-");
                break;
            case R.id.devideButton:
                //divide can be at the beginning
                if(calcDisplay.getText().length() > 0 ) {
                    calcDisplay.append("/");
                }
                break;
            case R.id.multiplyButton:
                //multiply can be at the beginning
                if(calcDisplay.getText().length() > 0 ) {
                    calcDisplay.append("*");
                }
                break;
        }
    }

    public  void handleEqualsButtonClicked(View v){

        if(calcDisplay.getText().length() == 0 || Arrays.asList(operations).contains(getLastChar()))
            return;

        String result = evaluateExpression(calcDisplay.getText().toString());
        calcDisplay.setText(result);
    }

    public void handleClearButtonClicked(View v) {

        CharSequence existingText = calcDisplay.getText();

        if (existingText.length() == 0)
            return;

        calcDisplay.setText(existingText.subSequence(0, existingText.length() - 1));
    }


    public String evaluateExpression(String expression) {

        //get all entered numbers in string format
        //if expression starts with + or -, just add one zero at the beginning
        if(expression.charAt(0) == '+' || expression.charAt(0) == '-'){
            expression = 0 + expression;
        }
        //split expression by arithmetic operation to get all digits
        String[] numbers = expression.split("[-+*/=]");



        //parse all operations
        List<String> operationList = new ArrayList<>();
        for (int i = 0; i < expression.length() - 1; i++) {
            if(expression.charAt(i) == '+' || expression.charAt(i) == '-' || expression.charAt(i) == '*' || expression.charAt(i) == '/' ) {
                operationList.add(String.valueOf(expression.charAt(i)));
            }
        }

        //parse all string numbers to float ones
        List<Float> numberList = new ArrayList<>();

        for (int i = 0; i < numbers.length; i++) {

            if(numbers[i].equals("-Infinity")) {
                numberList.add(Float.NEGATIVE_INFINITY);
            } else if (numbers[i].equals("Infinity")) {
                numberList.add(Float.POSITIVE_INFINITY);
            } else {

                try {
                    numberList.add(Float.parseFloat(numbers[i]));
                } catch (Exception exc) {
                    return "ERROR";
                }
            }
        }

        calculateAll(numberList, operationList);
        String textResult = Float.toString(finalResult);
        return textResult;
    }

    float finalResult;

    public void calculateAll( List<Float> numbers, List<String> operations) {

        if(numbers.size() == 1){
            finalResult = numbers.get(0);
            return;
        }

        float result = 0;

        int indexMultiply = operations.indexOf("*");
        int indexDivide = operations.indexOf("/");

        if(indexMultiply != -1 && indexDivide != -1) {
            if(indexMultiply < indexDivide){
                result += numbers.get(indexMultiply) * numbers.get(indexMultiply+1);

                numbers.set(indexMultiply, result);
                numbers.remove(indexMultiply + 1);

                operations.remove(indexMultiply);

                calculateAll(numbers, operations);
                return;
            } else {
                result += numbers.get(indexDivide) / numbers.get(indexDivide+1);

                numbers.set(indexDivide, result);
                numbers.remove(indexDivide + 1);

                operations.remove(indexDivide);

                calculateAll(numbers, operations);
                return;
            }
        }

        if(indexMultiply != -1) {
            result += numbers.get(indexMultiply) * numbers.get(indexMultiply+1);

            numbers.set(indexMultiply, result);
            numbers.remove(indexMultiply + 1);

            operations.remove(indexMultiply);

            calculateAll(numbers, operations);
            return;
        }

        if(indexDivide != -1) {
            result += numbers.get(indexDivide) / numbers.get(indexDivide+1);

            numbers.set(indexDivide, result);
            numbers.remove(indexDivide + 1);

            operations.remove(indexDivide);

            calculateAll(numbers, operations);
            return;
        }

        int indexPlus = operations.indexOf("+");
        int indexMinus = operations.indexOf("-");

        if(indexMinus != -1 && indexPlus != -1) {
            if(indexPlus < indexMinus){
                result += numbers.get(indexPlus) + numbers.get(indexPlus+1);

                numbers.set(indexPlus, result);
                numbers.remove(indexPlus + 1);

                operations.remove(indexPlus);

                calculateAll(numbers, operations);
                return;
            } else {
                result += numbers.get(indexMinus) - numbers.get(indexMinus+1);

                numbers.set(indexMinus, result);
                numbers.remove(indexMinus + 1);

                operations.remove(indexMinus);

                calculateAll(numbers, operations);
                return;
            }

        }

        if(indexPlus != -1) {
            result += numbers.get(indexPlus) + numbers.get(indexPlus+1);

            numbers.set(indexPlus, result);
            numbers.remove(indexPlus + 1);

            operations.remove(indexPlus);

            calculateAll(numbers, operations);
            return;
        }

        if(indexMinus != -1) {
            result += numbers.get(indexMinus) - numbers.get(indexMinus+1);

            numbers.set(indexMinus, result);
            numbers.remove(indexMinus + 1);

            operations.remove(indexMinus);

            calculateAll(numbers, operations);
            return;
        }

    }

}