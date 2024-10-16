package com.abliveira.whatsstarter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText phoneNumberInput;
    private TextView whatsappLinkOutput;

    // Static variable to ensure clipboard is only checked on app launch
    private static boolean isFirstLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        phoneNumberInput = findViewById(R.id.phone_number_input);
        whatsappLinkOutput = findViewById(R.id.whatsapp_link_output);

        // Set OnClickListener for the generate link button
        Button generateLinkButton = findViewById(R.id.generate_link_button);
        generateLinkButton.setOnClickListener(view -> onGenerateButtonCLick());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Check if this is the first launch and the window has focus
        if (hasFocus && isFirstLaunch) {
            Log.d("MainActivity", "Window gained focus, checking clipboard for phone number.");
            checkClipboardForPhoneNumber();

            // Set isFirstLaunch to false so we don't check clipboard again on future focus changes
            isFirstLaunch = false;
        }
    }

    private void checkClipboardForPhoneNumber() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboardManager == null) {
            Log.d("MainActivity", "Unable to access ClipboardManager.");
            return;
        }

        if (clipboardManager.hasPrimaryClip()) {
            ClipData clipData = clipboardManager.getPrimaryClip();

            if (clipData != null && clipData.getItemCount() > 0) {
                CharSequence clipboardText = clipData.getItemAt(0).getText();

                if (clipboardText != null && clipboardText.length() > 0) {
                    Log.d("MainActivity", "Phone number from clipboard set to input field: " + clipboardText);
                    String formattedPhone = convertToPhoneNumber((String) clipboardText);
                    if (isPhoneNumber(formattedPhone)) {
                        phoneNumberInput.setText(clipboardText);
                        generateWhatsAppLink(formattedPhone);
                    }
                }
            }
        }
    }

    public void onGenerateButtonCLick() {
        String phoneNumber = phoneNumberInput.getText().toString();
        String formattedPhone = convertToPhoneNumber(phoneNumber);
        if (isPhoneNumber(formattedPhone)) {
            generateWhatsAppLink(formattedPhone);
        }
    }

    private boolean isPhoneNumber(String text) {
        // Remove all non-numeric characters
        String phoneNumber = text.replaceAll("[^0-9]", "");

        // Check if the number has the correct length
        return phoneNumber.matches("\\d{10,15}");
    }

    private String convertToPhoneNumber(String phoneNumber) {
        // Remove all non-numeric characters
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");

        // Check if the phone number has enough characters
        if (phoneNumber.length() < 10) {
            Log.d("MainActivity", "Invalid phone number. Not enough digits.");
            return "";
        }

        try {
            // Extract first 2 digits and next 2 digits
            phoneNumber = phoneNumber.substring(0, 2) + phoneNumber.substring(2, 4) + phoneNumber.substring(4);
            return phoneNumber;
        } catch (StringIndexOutOfBoundsException e) {
            Log.d("MainActivity", "Error processing phone number: " + e.getMessage());
            return "";
        }
    }

    private void generateWhatsAppLink(String phoneNumber) {

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
