package com.example.unitconverterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Spinner sourceUnitSpinner, destinationUnitSpinner;
    EditText valueEditText;
    Button convertButton;
    TextView convertedValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        sourceUnitSpinner = findViewById(R.id.sourceUnitSpinner);
        destinationUnitSpinner = findViewById(R.id.destinationUnitSpinner);
        valueEditText = findViewById(R.id.valueEditText);
        convertButton = findViewById(R.id.convertButton);
        convertedValueTextView = findViewById(R.id.convertedValueTextView);

        ArrayAdapter<CharSequence> sourceAdapter = ArrayAdapter.createFromResource(this,
                R.array.source_units, android.R.layout.simple_spinner_item);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceUnitSpinner.setAdapter(sourceAdapter);

        ArrayAdapter<CharSequence> destinationAdapter = ArrayAdapter.createFromResource(this,
                R.array.destination_units, android.R.layout.simple_spinner_item);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationUnitSpinner.setAdapter(destinationAdapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertUnits();
            }
        });
    }

    private void convertUnits() {
        String sourceUnit = sourceUnitSpinner.getSelectedItem().toString();
        String destinationUnit = destinationUnitSpinner.getSelectedItem().toString();
        String valueText = valueEditText.getText().toString();

        // Check if the source and destination units are the same
        if (sourceUnit.equals(destinationUnit)) {
            Toast.makeText(this, "Source and destination units are the same. No conversion needed.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the value is empty
        if (valueText.isEmpty()) {
            valueEditText.setError("Please enter a value");
            return;
        }

        try {
            double value = Double.parseDouble(valueText);

            double convertedValue = performConversion(sourceUnit, destinationUnit, value);

            // If conversion is invalid, return early
            if (convertedValue == -1) {
                return;
            }

            convertedValueTextView.setText(String.format("%.2f %s", convertedValue, destinationUnit));

            View resultCardView = findViewById(R.id.resultCardView);
            resultCardView.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            valueEditText.setError("Invalid input. Please enter a valid number.");
        }
    }


    private double performConversion(String sourceUnit, String destinationUnit, double value) {
        double convertedValue;

        // Unit categories
        String[] lengthUnits = {"Inch", "Foot", "Yard", "Mile", "Centimeter", "Kilometer"};
        String[] weightUnits = {"Pound", "Ounce", "Ton", "Gram", "Kilogram"};
        String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};

        // Check if source and destination units are in the same category
        if ((isUnitInCategory(sourceUnit, lengthUnits) && isUnitInCategory(destinationUnit, lengthUnits)) ||
                (isUnitInCategory(sourceUnit, weightUnits) && isUnitInCategory(destinationUnit, weightUnits)) ||
                (isUnitInCategory(sourceUnit, temperatureUnits) && isUnitInCategory(destinationUnit, temperatureUnits))) {

            // Length Conversions
            if (sourceUnit.equals("Inch") && destinationUnit.equals("Centimeter")) {
                convertedValue = value * 2.54;
            } else if (sourceUnit.equals("Foot") && destinationUnit.equals("Centimeter")) {
                convertedValue = value * 30.48;
            } else if (sourceUnit.equals("Yard") && destinationUnit.equals("Centimeter")) {
                convertedValue = value * 91.44;
            } else if (sourceUnit.equals("Mile") && destinationUnit.equals("Kilometer")) {
                convertedValue = value * 1.60934;
            }

            // Weight Conversions
            else if (sourceUnit.equals("Pound") && destinationUnit.equals("Kilogram")) {
                convertedValue = value * 0.453592;
            } else if (sourceUnit.equals("Ounce") && destinationUnit.equals("Gram")) {
                convertedValue = value * 28.3495;
            } else if (sourceUnit.equals("Ton") && destinationUnit.equals("Kilogram")) {
                convertedValue = value * 907.185;
            }

            // Temperature Conversions
            else if (sourceUnit.equals("Celsius") && destinationUnit.equals("Fahrenheit")) {
                convertedValue = (value * 1.8) + 32;
            } else if (sourceUnit.equals("Fahrenheit") && destinationUnit.equals("Celsius")) {
                convertedValue = (value - 32) / 1.8;
            } else if (sourceUnit.equals("Celsius") && destinationUnit.equals("Kelvin")) {
                convertedValue = value + 273.15;
            } else if (sourceUnit.equals("Kelvin") && destinationUnit.equals("Celsius")) {
                convertedValue = value - 273.15;
            } else {
                convertedValue = value; //
            }

        } else {
            // error and return -1 for invalid conversion
            Toast.makeText(this, "Invalid conversion! Units must belong to the same category.", Toast.LENGTH_SHORT).show();
            return -1;
        }

        return convertedValue;
    }

    // Helper function to check if a unit belongs to a specific category
    private boolean isUnitInCategory(String unit, String[] category) {
        for (String u : category) {
            if (u.equals(unit)) {
                return true;
            }
        }
        return false;
    }
}