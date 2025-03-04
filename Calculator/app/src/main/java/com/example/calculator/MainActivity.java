package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNum1, editTextNum2;
    private Button buttonAdd, buttonSub, buttonMul, buttonDiv;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNum1 = findViewById(R.id.editTextNumber1);
        editTextNum2 = findViewById(R.id.editTextNumber2);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSub = findViewById(R.id.buttonSub);
        buttonMul = findViewById(R.id.buttonMul);
        buttonDiv = findViewById(R.id.buttonDiv);
        textViewResult = findViewById(R.id.textViewResult);

        buttonAdd.setOnClickListener(v -> calculate('+'));
        buttonSub.setOnClickListener(v -> calculate('-'));
        buttonMul.setOnClickListener(v -> calculate('*'));
        buttonDiv.setOnClickListener(v -> calculate('/'));
    }
    private void calculate(char operator) {
        String num1Str = editTextNum1.getText().toString();
        String num2Str = editTextNum2.getText().toString();

        if (num1Str.isEmpty() || num2Str.isEmpty()) {
            textViewResult.setText("Introduceti ambele numere");
            return;
        }

        double num1 = Double.parseDouble(num1Str);
        double num2 = Double.parseDouble(num2Str);
        double result = 0;

        switch (operator) {
            case '+': result = num1 + num2; break;
            case '-': result = num1 - num2; break;
            case '*': result = num1 * num2; break;
            case '/':
                if (num2 == 0) {
                    textViewResult.setText("Eroare: Impartire la zero");
                    return;
                }
                result = num1 / num2;
                break;
        }

        textViewResult.setText("Rezultat: " + result);
    }
}
