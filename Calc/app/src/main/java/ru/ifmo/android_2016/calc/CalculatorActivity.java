package ru.ifmo.android_2016.calc;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by alexey.nikitin on 13.09.16.
 * Modified by ivan.trofimov on 04.11.16.
 */

public final class CalculatorActivity extends Activity {

    private final static String KEY_HAVE_DOT = "dot";
    private final static String KEY_IS_ERROR = "isError";
    private final static String KEY_IS_CLEAR = "isCLear";
    private final static String KEY_HIGH_NUM = "highNum";
    private final static String KEY_LOW_NUM = "lowNum";
    private final static String KEY_HIGH_OP = "highOperand";
    private final static String KEY_LOW_OP = "lowOperand";
    private final static String KEY_PREV_OP = "prevOperand";
    private final static String KEY_MID_OP = "midOperand";
    private final static String KEY_RESULT = "result";

    TextView result;
    int ids[];

    //need to save
    boolean haveDot;
    boolean isError;
    boolean isClear = true;
    double highNumber = 0;
    double lowNumber = 0;
    int previousHighOperationId;
    int previousLowOperationId;
    int previousOperation;
    int lowHighOperation = R.id.add;

    private int getId(View v) {
        int curId = -1;
        for (int i = 0; i < ids.length; ++i)
            if (ids[i] == v.getId()) curId = i;
        return curId;
    }

    private View.OnClickListener buttonLister = new View.OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.eqv || id == R.id.clear) programButtonPress(v);
            else if (id == R.id.add || id == R.id.sub) binaryLowOperationPress(v);
            else if (id == R.id.mul || id == R.id.div) binaryHighOperationPress(v);
            else if (id == R.id.per || id == R.id.sign) unaryOperationPress(v);
            else numberPress(v);
        }
    };

    private void numberPress(View v) {
        if (isError) return;
        if (v.getId() == R.id.dot) {
            if (haveDot) {
                return;
            }
            haveDot = true;
            setResult(".");
        } else {
            setResult(Integer.toString(getId(v)));
        }
    }

    private void unaryOperationPress(View v) {
        if (isError) return;
        double currentNumber = Double.parseDouble(result.getText().toString());
        if (v.getId() == R.id.per) {
            currentNumber /= 100;
        } else {
            currentNumber = -currentNumber;
        }
        setResult(currentNumber);
    }

    private void binaryHighOperationPress(View v) {
        if (isError) return;
        applyOperation(v);
        previousHighOperationId = v.getId();
        previousOperation = v.getId();
    }

    private void binaryLowOperationPress(View v) {
        if (isError) return;
        applyOperation(v);
        previousLowOperationId = v.getId();
        previousOperation = v.getId();
    }

    private void programButtonPress(View v) {
        if (v.getId() == R.id.eqv) {
            if (isError) return;
            applyOperation(v);
            previousOperation = v.getId();
        }
        if (v.getId() == R.id.clear) {
            if (isClear) {
                isError = false;
                haveDot = false;
                isClear = true;
                lowNumber = 0;
                highNumber = 0;
                previousOperation = 0;
                result.setText("0");
            } else {
                isClear = true;
                haveDot = false;
                result.setText("0");
            }
        }
    }

    private void applyOperation(View v) {
        double currentNumber = Double.parseDouble(result.getText().toString());
        if (previousOperation == R.id.add || previousOperation == R.id.sub) {
            if (lowHighOperation == R.id.add) {
                lowNumber += highNumber;
            } else {
                lowNumber -= highNumber;
            }
            highNumber = currentNumber;
            lowHighOperation = previousOperation;

        } else if (previousOperation == R.id.mul) {
            highNumber *= currentNumber;
        } else if (previousOperation == R.id.div) {
            highNumber /= currentNumber;
        } else if (previousOperation == R.id.eqv) {

        } else {
            highNumber = currentNumber;
        }
        if (v.getId() == R.id.eqv) {
            if (lowHighOperation == R.id.add) {
                highNumber = lowNumber + highNumber;
            } else {
                highNumber = lowNumber - highNumber;
            }
            lowHighOperation = R.id.add;
            lowNumber = 0;
        }
        if (v.getId() == R.id.div || v.getId() == R.id.mul) {
            System.out.println("set from */: low =" + lowNumber + " | high = " + highNumber);
            setResult(highNumber);
        } else {
            System.out.println("set from +-: low =" + lowNumber + " | high = " + highNumber);
            setResult((lowNumber + highNumber));
        }
        System.out.println("low: " + lowNumber + " | high: " + highNumber);
        isClear = true;
        haveDot = false;
    }

    public void setResult(Double value) {
        String newDoubleString;
        boolean needToClean;
        if
                (value < 0.000001 && value > -0.000001) newDoubleString = "0";
        else {
            newDoubleString = new BigDecimal(value + (value > 0 ? 0.00000001 : -0.00000001)).setScale(7, RoundingMode.DOWN).toString();
            needToClean = newDoubleString.contains(".");
            if (needToClean) {
                while (newDoubleString.charAt(newDoubleString.length() - 1) == '0') {
                    newDoubleString = newDoubleString.substring(0, newDoubleString.length() - 1);
                }
                if (newDoubleString.charAt(newDoubleString.length() - 1) == '.') {
                    newDoubleString = newDoubleString.substring(0, newDoubleString.length() - 1);
                    haveDot = false;
                }
            }
        }
        if (isClear) {
            if (haveDot)
                result.setText("0.");
            else result.setText(newDoubleString);
        }
        if (value == 0) {
            isClear = true;
            haveDot = false;
            result.setText("0");
        }
        if (!isClear) result.setText(newDoubleString);
    }

    public void setResult(String additional) {
        if (isClear) {
            if (haveDot)
                result.setText("0.");
            else
                result.setText(additional);
        } else
            result.setText(result.getText().toString().concat(additional));
        if (!(isClear && additional.equals("0"))) isClear = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        {
            int[] ids2 = {R.id.d0, R.id.d1, R.id.d2, R.id.d3, R.id.d4, R.id.d5, R.id.d6, R.id.d7, R.id.d8, R.id.d9, R.id.dot,
                    R.id.sign, R.id.per,
                    R.id.div, R.id.mul,
                    R.id.sub, R.id.add,
                    R.id.clear, R.id.eqv
            };
            ids = ids2;
            for (int id : ids) {
                findViewById(id).setOnClickListener(buttonLister);
            }
        }
        result = (TextView) findViewById(R.id.result);
        if (savedInstanceState != null) {
            haveDot = savedInstanceState.getBoolean(KEY_HAVE_DOT);
            isError = savedInstanceState.getBoolean(KEY_IS_ERROR);
            isClear = savedInstanceState.getBoolean(KEY_IS_CLEAR);
            highNumber = savedInstanceState.getDouble(KEY_HIGH_NUM);
            lowNumber = savedInstanceState.getDouble(KEY_LOW_NUM);
            previousHighOperationId = savedInstanceState.getInt(KEY_HIGH_OP);
            previousLowOperationId = savedInstanceState.getInt(KEY_LOW_OP);
            previousOperation = savedInstanceState.getInt(KEY_PREV_OP);
            lowHighOperation = savedInstanceState.getInt(KEY_MID_OP);
            result.setText(savedInstanceState.getString(KEY_RESULT));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_HAVE_DOT, haveDot);
        outState.putBoolean(KEY_IS_ERROR, isError);
        outState.putBoolean(KEY_IS_CLEAR, isClear);
        outState.putDouble(KEY_HIGH_NUM, highNumber);
        outState.putDouble(KEY_LOW_NUM, lowNumber);
        outState.putInt(KEY_HIGH_OP, previousHighOperationId);
        outState.putInt(KEY_LOW_OP, previousLowOperationId);
        outState.putInt(KEY_PREV_OP, previousOperation);
        outState.putInt(KEY_MID_OP, lowHighOperation);
        outState.putString(KEY_RESULT, result.getText().toString());
    }
}