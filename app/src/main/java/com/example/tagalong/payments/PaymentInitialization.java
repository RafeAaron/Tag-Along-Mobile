package com.example.tagalong.payments;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tagalong.NetworkConnection;
import com.example.tagalong.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentInitialization extends AppCompatActivity {

    Button initializeTransfer;

    EditText userName;
    String userInformation;
    JSONObject userInfo;
    int user_id;

    EditText reason;
    EditText amount;

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

        userName = (EditText) findViewById(R.id.userNameOfRecieverEditBox);
        reason = (EditText) findViewById(R.id.reasonForPayment);
        amount = (EditText) findViewById(R.id.amountToPay);
        //userInformation = getIntent().getStringExtra("User ID");
        user_id = getIntent().getIntExtra("userId", -1);

        initializeTransfer = (Button) findViewById(R.id.confirmPaymentButton);
        initializeTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        NetworkConnection conn = new NetworkConnection();
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("user_name", userName.getText().toString());

                            JSONObject res = conn.postData(obj, "getUserAccountFromUserName");
                            Log.i("Info", res.toString());
                            Log.i("Length", String.valueOf(res.getJSONArray("User").getJSONObject(0).has("id")));
                            if(!res.has("User"))
                            {

                                /*PaymentInitialization.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PaymentInitialization.this, "User doesn't exist in the system", Toast.LENGTH_SHORT).show();
                                    }
                                });*/
                                Log.e("User Error", "User Not Present");

                            }else
                            {
                                if(res.getJSONArray("User").length() == 0)
                                {

                                    PaymentInitialization.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PaymentInitialization.this, "User doesn't exist in the system", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{

                                    if(res.getJSONArray("User").getJSONObject(0).has("id"))
                                    {

                                        JSONObject reqObj = new JSONObject();
                                        try {
                                            reqObj.put("sender_user_id", getIntent().getIntExtra("userId", -100));
                                            reqObj.put("reciever_user_id", parseInt(res.getJSONArray("User").getJSONObject(0).get("id").toString()));
                                            reqObj.put("reason", reason.getText().toString());
                                            reqObj.put("amount", parseInt(amount.getText().toString()));
                                        } catch (JSONException e) {
                                            PaymentInitialization.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(PaymentInitialization.this, "Failed to parse user data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                        Thread newThread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {

                                                NetworkConnection conn = new NetworkConnection();
                                                JSONObject res = conn.postData(reqObj, "initialisePayment");

                                                Log.i("Response", res.toString());


                                                    if(res.has("errno"))
                                                    {
                                                        PaymentInitialization.this.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(PaymentInitialization.this, "Failed to initiate payment", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else
                                                    {
                                                        PaymentInitialization.this.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(PaymentInitialization.this, "Payment Completed", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                        goToPaymentSucessFromInitialization();
                                                    }


                                            }
                                        });

                                        newThread.start();
                                        try {
                                            newThread.join();
                                        } catch (InterruptedException e) {

                                            PaymentInitialization.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(PaymentInitialization.this, "Failed to initialise payment", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                    }

                                }
                            }

                        } catch (JSONException e) {
                            Log.e("Error", "Failed to parse JSON data");
                        }

                    }
                });

                newThread.start();
                try {
                    newThread.join();
                } catch (InterruptedException e) {
                    PaymentInitialization.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PaymentInitialization.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public void goToPaymentSucessFromInitialization()
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
        goToDepositOnWalletIntent.putExtra("user_id", user_id);
        startActivity(goToDepositOnWalletIntent);
        finish();

    }
}