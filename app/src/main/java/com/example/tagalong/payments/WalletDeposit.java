package com.example.tagalong.payments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.R;
import com.google.android.material.snackbar.Snackbar;

public class WalletDeposit extends AppCompatActivity {

    Context myContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wallet_deposit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goBackToPaymentInitialization(View view)
    {
        //Action when back arrow of the wallet deposit class is clicked
        Intent goBackToPaymentInitializationIntent = new Intent(this, PaymentInitialization.class);
        startActivity(goBackToPaymentInitializationIntent);
        finish();
    }

    public void goToDepositIntoWalletConfirmation(View view)
    {
        //Action when initiate Deposit into wallet is clicked
        Snackbar mysnackBar = Snackbar.make(view, "Processing Payment", 3000);
        mysnackBar.setAction("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Action when user cancels deposit request
                AlertDialog.Builder myAlertDialogBuilder = new AlertDialog.Builder(myContext);

                myAlertDialogBuilder.setMessage("Are you sure you want to cancel the payment request");
                myAlertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(myContext, "Cancelling Deposit Request", Toast.LENGTH_SHORT).show();
                    }
                });

                myAlertDialogBuilder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Deal with when the user cancels the cancellation of payment request
                        //Toast.makeText(myContext, "Cancelling Deposit Request", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog myAlertDialog = myAlertDialogBuilder.create();
                myAlertDialog.show();
            }
        });
        mysnackBar.show();

        //Code To Wait for confirmation of deposit success or failure
        Handler goToDepositconfirmation = new Handler();
        goToDepositconfirmation.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToSuccessPage();
            }
        }, 3000);
    }

    public void goToSuccessPage()
    {
        Intent goToWalletDepositSuccess = new Intent(this, PaymentsConfirmation.class);
        startActivity(goToWalletDepositSuccess);
        finish();
    }
}