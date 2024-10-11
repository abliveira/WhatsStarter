package com.abliveira.whatsstarter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private
    EditText phoneNumberInput;
    private TextView whatsappLinkOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize views

        phoneNumberInput = findViewById(R.id.phone_number_input);
        whatsappLinkOutput = findViewById(R.id.whatsapp_link_output);

        // Set OnClickListener for the generate link button
        Button generateLinkButton = findViewById(R.id.generate_link_button);
        generateLinkButton.setOnClickListener(view -> generateWhatsAppLink());
    }

    private void generateWhatsAppLink() {
        String phoneNumber = phoneNumberInput.getText().toString();

        // Format the phone number (only when the button is clicked)
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        phoneNumber = phoneNumber.substring(0, 2) + phoneNumber.substring(2, 4) + phoneNumber.substring(4);

        // Create WhatsApp link
        String whatsappLink = getString(R.string.whats_url) + phoneNumber;

        // Set the WhatsApp link in the output TextView
        whatsappLinkOutput.setText(whatsappLink);
        whatsappLinkOutput.setVisibility(View.VISIBLE);

        // Make the WhatsApp link clickable
        whatsappLinkOutput.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(whatsappLink));
            startActivity(intent);
        });

        // Automatically open the WhatsApp link
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(whatsappLink));
        startActivity(intent);
    }
}
