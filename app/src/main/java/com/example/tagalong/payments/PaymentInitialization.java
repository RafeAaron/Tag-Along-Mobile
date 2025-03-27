package com.example.tagalong.payments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.R;

public class PaymentInitialization extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_initialization);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToPaymentSucessFromInitialization(View view)
    {
        Intent goToSuccess = new Intent(this, paymentConfirmationSuccess.class);
        startActivity(goToSuccess);
        finish();
    }

    public void goBackToMainScreen(View view)
    {
        //Action to be performed when back button is pressed.
        finish();
    }

    public void goToDepositOnWallet(View view)
    {
        //Action to be taken when the deposit on wallet text is clicked
        Intent goToDepositOnWalletIntent = new Intent(this, WalletDeposit.class);
        startActivity(goToDepositOnWalletIntent);

    }
}