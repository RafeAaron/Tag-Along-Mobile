package com.example.tagalong.payments;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class WalletDeposit extends AppCompatActivity {

    RadioGroup paymentMethod;
    TextView description;
    EditText editTextPaymentDetail;

    Button btnInitiate;

    EditText amountToAdd;

    int user_id;

    boolean detailsVisible = false;

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
        description = (TextView) findViewById(R.id.additionalPaymentDetailsHeading);
        amountToAdd = (EditText) findViewById(R.id.amountToAdd);
        editTextPaymentDetail = (EditText) findViewById(R.id.paymentOptionsAdditionalDetailOne);
        btnInitiate = (Button) findViewById(R.id.depositInitiate);
        user_id = getIntent().getIntExtra("user_id", -200);

        description.setVisibility(INVISIBLE);
        editTextPaymentDetail.setVisibility(INVISIBLE);

        description.setText("Enter MTN Mobile Number");

        paymentMethod = (RadioGroup) findViewById(R.id.paymentMethodOptions);

        btnInitiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(parseInt(amountToAdd.getText().toString()) < 3000)
                {
                    Toast.makeText(WalletDeposit.this, "Amount being deposited is too little. Minimum is 3000", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(editTextPaymentDetail.getText().length() < 10)
                {
                    Toast.makeText(WalletDeposit.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(user_id == -200)
                {
                    Toast.makeText(WalletDeposit.this, "User ID not parsed", Toast.LENGTH_SHORT).show();
                }else
                {
                    startDeposit(user_id, parseInt(amountToAdd.getText().toString()));
                }

            }
        });

        paymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                TextView description = (TextView) findViewById(R.id.additionalPaymentDetailsHeading);
                if(checkedId == R.id.mtnMobileMoneyOption)
                {

                    description.setText("Enter MTN Mobile Number");
                    editTextPaymentDetail.getText().clear();
                    editTextPaymentDetail.setHint("0774568398");


                    if(!detailsVisible)
                    {
                        editTextPaymentDetail.setVisibility(VISIBLE);
                        description.setVisibility(VISIBLE);

                        detailsVisible = true;
                    }
                }
                else if(checkedId == R.id.airtelMoneyOption)
                {

                    description.setText("Enter Airtel Mobile Number");
                    editTextPaymentDetail.getText().clear();
                    editTextPaymentDetail.setHint("0704567896");

                    if(!detailsVisible)
                    {
                        editTextPaymentDetail.setVisibility(VISIBLE);
                        description.setVisibility(VISIBLE);
                        detailsVisible = true;
                    }
                    /*
                    Handle Inputs to airtel money transaction
                     */

                }
            }
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

    public void startDeposit(int userID, int amount)
    {

        Thread mythread = new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject obj = new JSONObject();
                try {
                    obj.put("user_id", userID);
                    obj.put("amount", amount);

                    NetworkConnection networkConnection = new NetworkConnection();
                    JSONObject res = networkConnection.postData(obj, "updateUserAmount");

                    if(res.has("error")){
                        WalletDeposit.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WalletDeposit.this, "Failed to deposit funds to wallet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        goToSuccessPage();
                    }

                } catch (JSONException e) {
                    Log.e("Error", "Failed to work with JSON: " + e.toString());
                }


            }
        });

        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            Log.e("Error", "Failed to join main thread");
        }
    }
}